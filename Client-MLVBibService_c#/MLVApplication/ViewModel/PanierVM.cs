using GalaSoft.MvvmLight;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MLVApplication.ViewModel
{
    public class PanierVM: ViewModelBase
    {
        public ObservableCollection<BookVM> Books { get; set; }
        private bool _isLoagin;
        public bool IsLoading
        {
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
        public PanierVM()
        {

            init();

        }

        public double getTotal()
        {
            return Books.Select(b => b.Price).Sum();
        }
        private async void init()
        {

            IsLoading = true;
            Random r = new Random();
            Books = new ObservableCollection<BookVM>();
        }
    }
}
