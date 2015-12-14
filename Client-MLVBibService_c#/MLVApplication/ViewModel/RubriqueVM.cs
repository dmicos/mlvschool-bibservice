using FirstFloor.ModernUI.Presentation;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Messaging;
using MLVApplication.service;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MLVApplication.ViewModel
{
    public class RubriqueVM : ViewModelBase
    {
        private Uri main = new Uri("/Content/BookList.xaml", UriKind.RelativeOrAbsolute);
        private Uri transition = new Uri("/Content/BookListTransition.xaml", UriKind.RelativeOrAbsolute);
        private Uri current;
        public bool IsSwitching { get; set; }
        public List<string> Pages { get; set; } 
        public RelayCommand<string> Search { get; set; }

        private string selectedPage  = 1.ToString();
        public string SelectedPage
        {
            get
            {
                return selectedPage;
            }
            set
            {

                selectedPage = value;
                RaisePropertyChanged(nameof(SelectedPage));

                if(value!=null)
                MessengerInstance.Send(ActionEvent.SwitchPage);
            }
        }


        internal void loadCatFrom(IList<string> categories)
        {
            int i = 0;
            categories.ToList().ForEach(cat =>
            {
                _links.Add(new Link { DisplayName = cat, Source = new Uri("/Content/BookListTransition.xaml#" +cat, UriKind.RelativeOrAbsolute) });

            });
            SelectedRubrique = categories.FirstOrDefault();

RaisePropertyChanged("Links");
        }

        private LinkCollection _links;
        private string _selectedRubrique;

  

        public string SelectedRubrique { get; set; }
        public LinkCollection Links
        {
            get
            {
                return _links;
            }
            set
            {
                _links = value;
                RaisePropertyChanged("Links");
            }
        }

        public string Pattern { get; internal set; }
        public int PageSize { get; internal set; }

        public RubriqueVM()
        {
            current = main;
            _links = new LinkCollection();
            Pages = new List<string>();
            PageSize = 20;
            Search = new RelayCommand<string>((a) =>
           {
               Service.getRubrique().Pattern = a.Trim();
               Messenger.Default.Send(ActionEvent.ReloadBooks);
           });

        }

        internal void pagesChanged()
        {
            RaisePropertyChanged("Pages");
        }

        private async void loadCategorie()
        {
            Links.Clear();
            Links.Add(new Link { DisplayName = "Chargement ..." });
            await Task.Factory.StartNew(() => Service.LoadCategories());
        }
    }
}
