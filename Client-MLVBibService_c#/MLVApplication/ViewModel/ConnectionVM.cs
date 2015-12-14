using GalaSoft.MvvmLight;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MLVApplication.ViewModel
{
    public class ConnectionVM:ViewModelBase
    {

        private string _state;
        public string State
        {
            get
            {
                return _state;
            }
            set
            {
                _state = value;
                RaisePropertyChanged("State");
            }
        }

        public ConnectionVM()
        {
            State = "Connexion au service MLV...";
        }


    }
}
