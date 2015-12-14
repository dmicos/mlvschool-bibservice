using FirstFloor.ModernUI.Presentation;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Messaging;
using MLVApplication;
using MLVApplication.service;
using MLVApplication.ViewModel;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FirstFloor.ModernUI.App
{
    public class SampleFormViewModel
        : ViewModelBase, IDataErrorInfo
    {
        private string firstName;
        private string lastName;
        private string cardNumber;
        private string secret;
        private string devise;
        private string price;
        private bool requesting;
        public PanierVM Panier { get; set; }
        public int NbItems => Panier.Books.Count();
        public RelayCommand Save { get; set; }

        public string CardNumber
        {
            get { return this.cardNumber; }
            set
            {
                if (this.cardNumber != value)
                {
                    this.cardNumber = value;
                    RaisePropertyChanged("CardNumber");
                    RaisePropertyChanged(nameof(save));
                }
            }
        }

        public string Price
        {
            get { return (price.Contains("...") ? price : formatedPrice()); }
            set
            {
                if (this.price != value)
                {
                    this.price = value;
                    RaisePropertyChanged("Price");
                    RaisePropertyChanged(nameof(save));

                }
            }
        }

        public string SecretCode
        {
            get { return this.secret; }
            set
            {
                if (this.secret != value)
                {
                    this.secret = value;
                    RaisePropertyChanged("SecretCode");
                    RaisePropertyChanged(nameof(save));

                }
            }
        }

        public string FirstName
        {
            get { return this.firstName; }
            set
            {
                if (this.firstName != value)
                {
                    this.firstName = value;
                    RaisePropertyChanged("FirstName");
                    RaisePropertyChanged(nameof(save));

                }
            }
        }
        public bool Requesting { get { return requesting; } set
            {
                requesting = value;
                RaisePropertyChanged("Requesting");
            }
        }

        public string Devise
        {
            get { return this.devise; }
            set
            {
                if (this.devise != value)
                {
                    this.devise = value;
                    calculatePrice();
                    RaisePropertyChanged("Devise");
                }
            }
        }

        private async void calculatePrice()
        {
           
            Requesting = true;
            Price = "...";

            var value =devise.Equals("EUR")? Service.getPanier().getTotal(): await Service.calculateChange(Service.getPanier().getTotal(), "EUR", Devise);
            Requesting = false;
            Price = value.ToString();
        }

        public List<string> AllDevise { get; set; }

        public SampleFormViewModel()
        {
            devise = "EUR";
            calculatePrice();
            Save = new RelayCommand((a) => save() , validate);
            AllDevise = Service.getAllDevises();
            devise = AllDevise.IndexOf(devise) != -1 ? devise : AllDevise.FirstOrDefault();
            Requesting = false;
            Panier = Service.getPanier();

            Messenger.Default.Register<ActionEvent>(this, (e) =>
            {
                if (e == ActionEvent.BagChanged)
                {
                    calculatePrice();
                    RaisePropertyChanged(nameof(NbItems));
                }
             });

        }

        private bool validate(object arg)
        {
            if (requesting)
                return false;

            if (Service.getPanier().getTotal() == 0)
                return false;

            return !string.IsNullOrEmpty(FirstName)
                 && !string.IsNullOrEmpty(LastName)
                 && !string.IsNullOrEmpty(CardNumber)
                 && !string.IsNullOrEmpty(SecretCode);
        }

        private async void save()
        {
            Requesting = true;
            var value = await Service.initiatePaiement(lastName, firstName, SecretCode, cardNumber, Devise,Panier.getTotal());

            if(value == ActionEvent.Pay_Success)
            {
                Panier.Books.Clear();
                FirstName = string.Empty;
                LastName = string.Empty;
                CardNumber = string.Empty;
                SecretCode = string.Empty;
                Messenger.Default.Send(ActionEvent.BagChanged);
            }

            Requesting = false;
            Messenger.Default.Send(value);
        }

        private string formatedPrice()
        {
            string symb = null;
            try {
                symb = GetCurrencySymbol(Devise);
            }catch(Exception e)
            {
                symb = Devise;
            }
            return (Double.Parse(price)).ToString("n2") + " " + symb;
        }

        public static string GetCurrencySymbol(string code)
        {
            System.Globalization.RegionInfo regionInfo = (from culture in System.Globalization.CultureInfo.GetCultures(System.Globalization.CultureTypes.InstalledWin32Cultures)
                                                          where culture.Name.Length > 0 && !culture.IsNeutralCulture
                                                          let region = new System.Globalization.RegionInfo(culture.LCID)
                                                          where String.Equals(region.ISOCurrencySymbol, code, StringComparison.InvariantCultureIgnoreCase)
                                                          select region).First();

            return regionInfo.CurrencySymbol;
        }
        public string LastName
        {
            get { return this.lastName; }
            set
            {
                if (this.lastName != value) {
                    this.lastName = value;
                    RaisePropertyChanged("LastName");
                }
            }
        }

        public string Error
        {
            get { return null; }
        }

        public string this[string columnName]
        {
            get
            {
                if (columnName == "FirstName") {
                    return string.IsNullOrEmpty(this.firstName) ? "Obligatoire" : null;
                }
                if (columnName == "LastName") {
                    return string.IsNullOrEmpty(this.lastName) ? "Obligatoire" : null;
                }

                if (columnName == "CardNumber")
                {
                    return string.IsNullOrEmpty(this.CardNumber) ? "Obligatoire" : null;
                }
                if (columnName == "SecretCode")
                {
                    return string.IsNullOrEmpty(this.SecretCode) ? "Obligatoire" : null;
                }

                if (columnName == "Devise")
                {
                    return string.IsNullOrEmpty(this.SecretCode) ? "Obligatoire" : null;
                }
                return null;
            }
        }
    }
}
