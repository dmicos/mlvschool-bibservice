using FirstFloor.ModernUI.Windows.Controls;
using GalaSoft.MvvmLight.Messaging;
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
    /// Interaction logic for AchatForm.xaml
    /// </summary>
    public partial class AchatForm : UserControl
    {
        public AchatForm()
        {
            InitializeComponent();
            Messenger.Default.Register<ActionEvent>(this, handleEvent);
        }

        private void handleEvent(ActionEvent obj)
        {
            switch (obj)
            {
                case ActionEvent.No_Account_Found:
                    ModernDialog.ShowMessage("La carte de crédit n'a pas été reconnue", " Aucun Compte", MessageBoxButton.OK);
                    break;
                case ActionEvent.PaymentRefused:
                    ModernDialog.ShowMessage("Vous n'avez pas assez d'argent sur ce compte", "Paiement refusé", MessageBoxButton.OK);
                    break;
                case ActionEvent.Pay_Success:
                    ModernDialog.ShowMessage("Paiement effectué avec succès"," Paiement",MessageBoxButton.OK);
                    break;
            }
        }
    }
}
