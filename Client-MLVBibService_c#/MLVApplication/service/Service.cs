using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Messaging;
using MLVApplication.ViewModel;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;


namespace MLVApplication.service
{
    public class Service : ViewModelBase
    {
        private static List<string> categories = new List<string>();
        private static List<BookVM> booksCache = new List<BookVM>();
        private static LibService.MLVLibServiceClient RemoteService = new LibService.MLVLibServiceClient();
        private static SellingService.SellingServiceClient payService = new SellingService.SellingServiceClient();
        private static int TIME_OUT = 2000;
        public static bool logged = false;

        public static PanierVM getPanier()
        {

            return (App.Current.Resources["Locator"] as ViewModelLocator).Panier;

        }

        public static RubriqueVM getRubrique()
        {
            return (App.Current.Resources["Locator"] as ViewModelLocator).Rubrique;

        }

        public static BookListVM getBooks()
        {
            return (App.Current.Resources["Locator"] as ViewModelLocator).BookList;

        }
        public static void LoadCategories()
        {
            var rubriques = (App.Current.Resources["Locator"] as ViewModelLocator).Rubrique;
            var connVM = (App.Current.Resources["Locator"] as ViewModelLocator).ConnectVM;
            connVM.State = "Chargement des categories en cours...";
            // rubriques.loadCatFrom(getCategories());            

            try
            {
                rubriques.loadCatFrom(RemoteService.getCategories());
            }catch(Exception ex) { 
            connVM.State = "Le Chargement des catégories a échoué... ";
                Console.WriteLine(ex.Message);
                return;
            }

            LoadBooks();


            logged = true;

        }

        internal static void Reconnect()
        {
            
            LoadCategories();
        }

        public static void LoadBooks(int max=130)
        {
            var books = (App.Current.Resources["Locator"] as ViewModelLocator).BookList;
            var connVM = (App.Current.Resources["Locator"] as ViewModelLocator).ConnectVM;
            connVM.State = "Chargement des livres en cours...";

            try
            {

                var data = Service.decode(RemoteService.getFormattedBooks(max));
               
                var tmp = booksCache = data;

            }catch (Exception ex)
            {
                connVM.State = "Le Chargement des livres a échoué... ";
                Console.WriteLine(ex.Message);
                return;
            }

            logged = true;
        }

        public static void sendError()
        {
            Messenger.Default.Send(ActionEvent.Unreachable);
        }

        public static async Task<ActionEvent> initiatePaiement(string lastName, string firstName, string cardNumber, string secretCode, string devise,double price)
        {
            try
            {
                await payService.sellBookAsync(long.Parse(cardNumber), secretCode, price, devise);
            }catch(Exception e)
            {
                return ActionEvent.PaymentRefused;
            }
            return ActionEvent.Pay_Success;
        }

        public static List<string> getAllDevises()
        {

            return new List<string> { "AFA", "ALL", "DZD", "ARS", "AWG", "AUD", "BSD", "BHD", "BDT", "BBD", "BZD", "BMD", "BTN", "BOB", "BWP", "BRL", "GBP", "BND", "BIF", "XOF", "XAF", "KHR", "CAD", "CVE", "KYD", "CLP", "CNY", "COP", "KMF", "CRC", "HRK", "CUP", "CYP", "CZK", "DKK", "DJF", "DOP", "XCD", "EGP", "SVC", "EEK", "ETB", "EUR", "FKP", "GMD", "GHC", "GIP", "XAU", "GTQ", "GNF", "GYD", "HTG", "HNL", "HKD", "HUF", "ISK", "INR", "IDR", "IQD", "ILS", "JMD", "JPY", "JOD", "KZT", "KES", "KRW", "KWD", "LAK", "LVL", "LBP", "LSL", "LRD", "LYD", "LTL", "MOP", "MKD", "MGF", "MWK", "MYR", "MVR", "MTL", "MRO", "MUR", "MXN", "MDL", "MNT", "MAD", "MZM", "MMK", "NAD", "NPR", "ANG", "NZD", "NIO", "NGN", "KPW", "NOK", "OMR", "XPF", "PKR", "XPD", "PAB", "PGK", "PYG", "PEN", "PHP", "XPT", "PLN", "QAR", "ROL", "RUB", "WST", "STD", "SAR", "SCR", "SLL", "XAG", "SGD", "SKK", "SIT", "SBD", "SOS", "ZAR", "LKR", "SHP", "SDD", "SRG", "SZL", "SEK", "TRY", "CHF", "SYP", "TWD", "TZS", "THB", "TOP", "TTD", "TND", "TRL", "USD", "AED", "UGX", "UAH", "UYU", "VUV", "VEB", "VND", "YER", "YUM", "ZMK", "ZWD" };
        }

        internal static async Task<double> calculateChange(double v1, string from, string to)
        {
            try
            {
                return await Task.Factory.StartNew(() => payService.change(from, to, v1));

            }catch(Exception e)
            {
                return -1;
            }
        }


        public static void loadBooks(ICollection<BookVM> Books)
        {

            Random r = new Random();

            for (int i = 0; i < 500; i++)
            {
                BookVM b = new BookVM { Name = "Book N° " + (i + 1), CanAddToBag = true, Rate=r.Next(6), Price = r.NextDouble(), Genre = "Categorie " + (r.Next(11)), ISBN = r.Next(), Auhtors = "Unknown" };
                Books.Add(b);
            }
        }

        public static  async Task<List<BookVM>> GetAllBooksAsync(String cat)
        {

            return await Task<List<BookVM>>.Factory.StartNew(() => booksCache.Where( b =>
            {
                return b.Genre.Trim().ToLower().Equals(cat.ToLower());
            }).ToList());
        }


        public static List<BookVM> getBooksFrom(string cat)
        {

            var tmp =  booksCache.Where(b => b.Genre.Equals(cat)).ToList();
            return tmp;
        }

        public static List<BookVM> Search(string pattern)
        {
            pattern = pattern.ToLower();
            return booksCache.Where(b => b.Name.ToLower().Contains(pattern) || b.Genre.ToLower().Contains(pattern)).ToList();
        }

        private static List<BookVM> decode(string[] values)
        {
            List<BookVM> data = new List<BookVM>();



            for (int i = 0; (i+5) < values.Length; i+=6)
            {
                string p = values[i + 4].Trim();
                double d = GetDouble(p, 0);
                string rank = values[i + 5];
                data.Add(new BookVM
                {
                    ISBN = int.Parse(values[i]),
                    Name = values[i + 1],
                    Genre = values[i + 2],
                    Auhtors = values[i + 3],
                    Price = d,
                    Rate = int.Parse(values[i + 5].ToString()),
                    CanAddToBag = true
                    //image
                });
            }

        return data;
        }

        public static double GetDouble(string value, double defaultValue)
        {
            double result;

            //Try parsing in the current culture
            if (!double.TryParse(value, System.Globalization.NumberStyles.Any, CultureInfo.CurrentCulture, out result) &&
                //Then try in US english
                !double.TryParse(value, System.Globalization.NumberStyles.Any, CultureInfo.GetCultureInfo("en-US"), out result) &&
                //Then in neutral language
                !double.TryParse(value, System.Globalization.NumberStyles.Any, CultureInfo.InvariantCulture, out result))
            {
                result = defaultValue;
            }

            return result;
        }

        
        public static IList<string> getCategories()
        {

            List<string> list = new List<string>();
            for (int i = 0; i < 10; i++)
            {
                list.Add("Categorie " + (i + 1));
                categories.Add("Categorie " + (i + 1));
            }

            return list;
        }
    }
}
