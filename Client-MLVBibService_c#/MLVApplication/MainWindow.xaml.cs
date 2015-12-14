using FirstFloor.ModernUI.Presentation;
using FirstFloor.ModernUI.Windows.Controls;
using GalaSoft.MvvmLight.Messaging;
using MLVApplication.Content;
using MLVApplication.pages;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
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

namespace MLVApplication
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : ModernWindow
    {
        public MainWindow()
        {
            InitializeComponent();
            Application.Current.MainWindow.WindowState = WindowState.Maximized;

            Messenger.Default.Register<ActionEvent>(this, (a) =>
            {

                switch (a)
                {
                    case ActionEvent.ShowLinks:
                        showLinks();
                        break;
                }
            });
        }

        internal void showLinks()
        {

            LinkGroup bib = new LinkGroup { DisplayName = "Bibliothèque" };
            bib.Links.Add(new Link { DisplayName = "Nos livres par rubrique", Source = new Uri("/pages/home/home.xaml", UriKind.RelativeOrAbsolute)});
           // bib.Links.Add(new Link { DisplayName = "Nos livres les plus consultés", Source = new Uri("/Content/MostConsulted.xaml", UriKind.RelativeOrAbsolute) });

            LinkGroup achat = new LinkGroup { DisplayName = "Service Achat" };
            achat.Links.Add(new Link { DisplayName = "Bienvenue", Source = new Uri("/pages/achat/Achat.xaml", UriKind.RelativeOrAbsolute) });

            MenuLinkGroups.Add(bib);
            MenuLinkGroups.Add(achat);

            ContentSource = bib.Links.First().Source ;


        }
    }
}
