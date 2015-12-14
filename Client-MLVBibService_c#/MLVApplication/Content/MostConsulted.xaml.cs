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

namespace MLVApplication.Content
{
    /// <summary>
    /// Interaction logic for MostConsulted.xaml
    /// </summary>
    public partial class MostConsulted : UserControl
    {
        private BookListTransition transionPanel = new BookListTransition();
        private PageLoader loader;

        public MostConsulted()
        {
            InitializeComponent();
            menu.ContentLoader = loader = new PageLoader(transionPanel);
        }

        private void menu_SelectedSourceChanged(object sender, FirstFloor.ModernUI.Windows.Controls.SourceEventArgs e)
        {
            if (loader == null)
                return;

            if (e.Source.ToString().Trim().Contains(loader.LastLoad?.ToString().Trim()))
            {
                BookListTransition tr = new BookListTransition();
                //loader.Transition.move(tr);
                tr.loadAsync(loader.Transition);

            }
        }
    }
}
