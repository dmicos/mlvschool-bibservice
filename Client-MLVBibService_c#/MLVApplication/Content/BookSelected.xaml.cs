using MLVApplication.service;
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
    /// Interaction logic for BookSelected.xaml
    /// </summary>
    public partial class BookSelected : UserControl
    {
        public BookSelected()
        {
            InitializeComponent();
            nbBooks.Text = Service.getPanier().Books.Count().ToString();
            total.Text = Service.getPanier().getTotal().ToString("c2");

        }
    }
}
