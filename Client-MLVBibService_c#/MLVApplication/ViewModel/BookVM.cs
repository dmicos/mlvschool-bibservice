using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.CommandWpf;
using MLVApplication.service;
using System.Collections.Generic;
using System.Linq;

namespace MLVApplication.ViewModel
{
    public class BookVM:ViewModelBase
    {
        public int ISBN { get; set; }
        public string Auhtors { get; set; }

        private string _name;
        public string Name { get
            {
                if (_name == null)
                    return "Untitled";

                return (_name.Length > 15) ?_name.Substring(0,7)+"...":_name;
            }
            set
            {
                _name = value;
                RaisePropertyChanged(nameof(Name));
            }
        }
        public double Price { get; set; }
        public string Summry { get; set; }
        public string Genre { get; set; }
        public int Rate { get; set; }
        public IEnumerable<int> Fill {
            get {
                return Enumerable.Range(0, Rate);
                }
        }

        public IEnumerable<int> Empty
        {
            get
            {
                return Enumerable.Range(0, 5 - Rate);
            }
        }


        private bool canAddToBag;
        public bool CanAddToBag
        {
            get
            {
                return canAddToBag;
            }
            set
            {
                canAddToBag = value;
                RaisePropertyChanged("CanAddToBag");
            }
        }

        public RelayCommand<string> AddToBag { get; private set; }
        public RelayCommand<string> RemoveFromBag{ get; private set; }
        public string Image { get; internal set; }

        public BookVM()
        {
            AddToBag = new RelayCommand<string>((a) =>
            {
                Service.getPanier().Books.Add(this);
                CanAddToBag = false;

                MessengerInstance.Send(ActionEvent.BagChanged);
            });

            RemoveFromBag = new RelayCommand<string>((a) =>
            {
                Service.getPanier().Books.Remove(this);
                CanAddToBag = true;

                MessengerInstance.Send(ActionEvent.BagChanged);
            });

        }

    }
}