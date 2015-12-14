using FirstFloor.ModernUI.Windows.Controls;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Messaging;
using MLVApplication.pages;
using MLVApplication.service;
using MLVApplication.ViewModel;
using System;
using System.Collections.Generic;
using System.ComponentModel;
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
    /// Interaction logic for Connection.xaml
    /// </summary>
    public partial class Connection : UserControl
    {
        BackgroundWorker worker;

        public Connection()
        {
            InitializeComponent();
            ShowHomePage();
        }

        private void ShowHomePage()
        {
             worker = new BackgroundWorker();
            worker.DoWork += Worker_DoWork;
            worker.RunWorkerCompleted += Worker_RunWorkerCompleted;
            worker.RunWorkerAsync();
        }

        private void Worker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e) {

            if (!Service.logged)
            {
                var connVM = (App.Current.Resources["Locator"] as ViewModelLocator).ConnectVM;
                connVM.State = "Reconnexion en cours...";
                worker.RunWorkerAsync();              
            }
            else
                Dispatcher.BeginInvoke(new Action(() => Messenger.Default.Send<ActionEvent, MainWindow>(ActionEvent.ShowLinks)));
        }

        private void Worker_DoWork(object sender, DoWorkEventArgs e)
        {
          
            
            Service.Reconnect();

        }
    }
}
