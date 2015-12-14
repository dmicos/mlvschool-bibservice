/*
  In App.xaml:
  <Application.Resources>
      <vm:ViewModelLocator xmlns:vm="clr-namespace:MLVApplication"
                           x:Key="Locator" />
  </Application.Resources>
  
  In the View:
  DataContext="{Binding Source={StaticResource Locator}, Path=ViewModelName}"

  You can also use Blend to do all this with the tool's support.
  See http://www.galasoft.ch/mvvm
*/

using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Ioc;
using Microsoft.Practices.ServiceLocation;

namespace MLVApplication.ViewModel
{
    /// <summary>
    /// This class contains static references to all the view models in the
    /// application and provides an entry point for the bindings.
    /// </summary>
    public class ViewModelLocator
    {
        /// <summary>
        /// Initializes a new instance of the ViewModelLocator class.
        /// </summary>
        public ViewModelLocator()
        {
            ServiceLocator.SetLocatorProvider(() => SimpleIoc.Default);

            ////if (ViewModelBase.IsInDesignModeStatic)
            ////{
            ////    // Create design time view services and models
            ////    SimpleIoc.Default.Register<IDataService, DesignDataService>();
            ////}
            ////else
            ////{
            ////    // Create run time view services and models
            ////    SimpleIoc.Default.Register<IDataService, DataService>();
            ////}

            SimpleIoc.Default.Register<MainViewModel>(true);
            SimpleIoc.Default.Register<RubriqueVM>(true);
            SimpleIoc.Default.Register<BookListVM>(false);
            SimpleIoc.Default.Register<PanierVM>(true);
            SimpleIoc.Default.Register<ConnectionVM>(true);


        }

        public MainViewModel Main
        {
            get
            {
                return ServiceLocator.Current.GetInstance<MainViewModel>();
            }
        }
        public ConnectionVM ConnectVM
        {
            get
            {
                return ServiceLocator.Current.GetInstance<ConnectionVM>();
            }
        }

        public RubriqueVM Rubrique
        {
            get
            {
                return ServiceLocator.Current.GetInstance<RubriqueVM>();
            }
        }

        public BookListVM BookList
        {
            get
            {
                return ServiceLocator.Current.GetInstance<BookListVM>();
            }
        }

        public PanierVM Panier
        {
            get
            {
                return ServiceLocator.Current.GetInstance<PanierVM>();
            }
        }


        public static void Cleanup()
        {
            // TODO Clear the ViewModels
        }
    }
}