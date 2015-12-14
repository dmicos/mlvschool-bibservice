using FirstFloor.ModernUI.Windows.Controls;
using FirstFloor.ModernUI.Windows.Navigation;
using GalaSoft.MvvmLight.Messaging;
using MLVApplication.Content;
using MLVApplication.service;
using MLVApplication.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace MLVApplication.pages
{
    /// <summary>
    /// Interaction logic for Home.xaml
    /// </summary>
    public partial class Home : UserControl
    {
        private BookListTransition transionPanel = new BookListTransition();
        private PageLoader loader;
        private BookList view;
        public Home()
        {
            InitializeComponent();
            menu.ContentLoader =loader= new PageLoader(transionPanel);
            Messenger.Default.Register<ActionEvent>(this, handleEvent);
        }

        private void handleEvent(ActionEvent obj)
        {
            if(obj == ActionEvent.BagChanged)
            {
                transition.ShowPage(new BookSelected());
            }
        }

        private void menu_SelectedSourceChanged(object sender, SourceEventArgs e)
        {

            if (loader != null)
            {

                int index = e.Source.ToString().IndexOf("#");
                Service.getRubrique().SelectedRubrique = e.Source.ToString().Substring(index + 1);
                string cat = Service.getRubrique().SelectedRubrique;
                if (e.Source.ToString().Trim().Contains(loader.LastLoad?.ToString().Trim()))
                {
                    BookListTransition tr = new BookListTransition();
                    // loader.Transition.move(tr);
                    view = new BookList();
                    loader.Transition.move(view);
                }
              
            }



        }

        private async void loadAsync()
        {
           var data =  await Task<List<BookVM>>.Factory.StartNew(() => Service.getBooksFrom(Service.getRubrique().SelectedRubrique));
      
        }

        private void b1_Click(object sender, RoutedEventArgs e)
        {
            if (view == null)
                return;

            view.search(search.Text);
        }
    }
}
