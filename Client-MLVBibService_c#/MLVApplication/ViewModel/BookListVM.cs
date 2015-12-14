using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.CommandWpf;
using GalaSoft.MvvmLight.Messaging;
using MLVApplication.service;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Threading;

namespace MLVApplication.ViewModel
{
    public class BookListVM:ViewModelBase
    {
        private List<BookVM> _allBook = new List<BookVM>();
        private List<BookVM> _books;
        private BackgroundWorker worker = new BackgroundWorker();
        public List<BookVM> Books { get
            {
                if (_books == null)
                    return _books = new List<BookVM>();

                return _books;
            }
            set
            {
                _books = value;
                RaisePropertyChanged("Books");
            }
        }

        internal void update(string text)
        {
            handleEvent(ActionEvent.ReloadBooks,text);
        }

        public RelayCommand<string> AddToBag { get; set; }


        private bool _isLoagin;
        private List<BookVM> page;

        public bool IsLoading {
            get
            {
                return _isLoagin;
            }
            set
            {
                _isLoagin = value;
                RaisePropertyChanged("IsLoading");
            }
        }
        public BookListVM()
        {
            Messenger.Default.Register<ActionEvent>(this, handleEvent);
            worker.DoWork += Worker_DoWork;
            worker.ProgressChanged += Worker_ProgressChanged;
            worker.RunWorkerCompleted += Worker_RunWorkerCompleted;
            worker.WorkerReportsProgress = true;
            worker.WorkerSupportsCancellation = true;
            init();
           
        }

        private void handleEvent(ActionEvent obj)
        {
            handleEvent(obj,string.Empty);
        }

        private void Worker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {

            if (!(bool)e.Result)
                Books = page;
    
            IsLoading = false;
            adjustPages();
            //RaisePropertyChanged(nameof(Books));

        }

        private void adjustPages()
        {

            RubriqueVM vm = Service.getRubrique();
            int count = _allBook.Count();
            int pageSize = vm.PageSize;
            int totalPage = count / pageSize;
            totalPage = (totalPage == 0) ? 1 : totalPage;
            vm.Pages.Clear();
            vm.pagesChanged();
            Enumerable.Range(0, totalPage).ToList().ForEach(i => vm.Pages.Add("Page " + (i + 1) + "/" + totalPage));
            vm.SelectedPage = vm.Pages.FirstOrDefault();
        }

        private  void handleEvent(ActionEvent obj,string param="")
        {

           

            switch (obj)
            {
                case ActionEvent.SwitchPage:
                    IsLoading = true;
                    Books = null;
                    BackgroundWorker bg = new BackgroundWorker();
                    bg.WorkerReportsProgress = true;
                    bg.DoWork += search_DoWork;
                    bg.RunWorkerCompleted += Current_RunWorkerCompleted;
                    bg.RunWorkerAsync();
                    break;

                case ActionEvent.ReloadBooks:

                    List<BookVM> result = new List<BookVM>();
                    BackgroundWorker bg2 = new BackgroundWorker();
                    bg2.WorkerReportsProgress = true;
                    bg2.DoWork += (a, b) =>
                     {
                         var worker = (BackgroundWorker)a;

                         IsLoading = true;
                         List<BookVM> values = Service.Search(param);
                         _allBook.Clear();
                         for(int i = 0; i< values.Count(); i++)
                         {
                             _allBook.Add(values[i]);
                             if (i < Service.getRubrique().PageSize)
                             {
                                 result.Add(values[i]);
                             }
                             else if (i == Service.getRubrique().PageSize)
                                 worker.ReportProgress(i);
                         }

                     };
                    bg2.ProgressChanged += (a, b) =>
                    {
                        Books = result;
                        IsLoading = false;
                        RaisePropertyChanged(nameof(Books));

                    };
                    bg2.RunWorkerCompleted += (a, b) =>
                    {

                        if (result.Count() < Service.getRubrique().PageSize)
                        {
                            Books = result;
                            IsLoading = false;
                        }
                        else
                        {
                            adjustPages();

                        }

                    };
                    bg2.RunWorkerAsync();
                    break;

            }
        }

        private void Current_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            Books = page;
            IsLoading = false;
        }


        private void search_DoWork(object sender, DoWorkEventArgs e)
        {
            //var data = Service.GetAllBooksAsync(Service.getRubrique().SelectedRubrique).Result;
            int count = _allBook.Count();
            RubriqueVM vm = Service.getRubrique();
            int offset = (vm.Pages.IndexOf(vm.SelectedPage)) * vm.PageSize;
            int limit = offset + vm.PageSize;
            int top = (limit < count) ? vm.PageSize : count - offset;
            top = (top < 0) ? 0 : top;
            List<BookVM> page = new List<BookVM>();
            _allBook.ToList().GetRange(offset, top).ForEach(b => page.Add(b));
            this.page = page;
            e.Result = page;
        }

        public async void reloadFrom(String cat)
        {
            Books.Clear();
            await Task.Factory.StartNew(() =>
            {
                Service.getBooksFrom(cat).ForEach(b => Books.Add(b));
            });
        }

        public  async void init()
        {

            IsLoading = true;
            await Task.Delay(2000);
            //IsLoading = true;

            worker.RunWorkerAsync();
            Service.getRubrique().SelectedPage = Service.getRubrique().Pages.FirstOrDefault();

        }

        private void Worker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {

            //Books = _allBook;
            Books = page;
            //RaisePropertyChanged(nameof(Books));


        }

        private void Worker_DoWork(object sender, DoWorkEventArgs e)
        {
            var data = Service.GetAllBooksAsync(Service.getRubrique().SelectedRubrique).Result;
            int count = data.Count();
            int step = 0;
            RubriqueVM vm = Service.getRubrique();
            _allBook.Clear();
            page = new List<BookVM>();

            for (int i = 0 ;i <count;i++)
            {
                if (i >= count)
                    break;

                _allBook.Add( data[i]);

                if (step == vm.PageSize) {
                    worker.ReportProgress(i);
                }
                else
                {
                    page.Add(data[i]);
                    step++;
                }

            }

            e.Result = step == vm.PageSize ;

        }

        private bool comparator(BookVM arg)
        {
            var filter = Service.getRubrique().Pattern;

            if (!string.IsNullOrEmpty(filter))
                return arg.Name.ToLower().Contains(filter?.ToLower());

          
            return true;
        }

        internal void InitWith(List<BookVM> tmp)
        {
            Books.Clear();
            tmp.ForEach(b => Books.Add(b));
            RaisePropertyChanged("Books");
        }
    }
}
