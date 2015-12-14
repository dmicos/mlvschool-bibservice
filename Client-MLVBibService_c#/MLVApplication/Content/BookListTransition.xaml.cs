using GalaSoft.MvvmLight.Messaging;
using MLVApplication.service;
using MLVApplication.ViewModel;
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
using WpfPageTransitions;

namespace MLVApplication.Content
{
    /// <summary>
    /// Interaction logic for BookList.xaml
    /// </summary>
    public partial class BookListTransition : UserControl
    {

        public BookListTransition(PageTransitionType type = PageTransitionType.SpinAndFade)
        {
            InitializeComponent();
            pageTransitionControl.TransitionType = PageTransitionType.Slide;
            activeListener();
        }

        public void activeListener()
        {
            Messenger.Default.Register<ComplexMessage<List<BookVM>>>(this, handleEvent);
        }
        public async void loadAsync(BookListTransition tr)
        {

            //await Task.Delay(0).ContinueWith(_ => Dispatcher.BeginInvoke(new Action(() => tr.move(new BookList()))));
        }

        public void handleEvent(ComplexMessage<List<BookVM>> message)
        {
            if (message.Sender == this || message.Dest != this)
                return;

            if(message.MyEvent == ActionEvent.ReadyToDisplay)
            {
                Service.getBooks().InitWith(message.Message);
                message.Dest.move(new BookList());
            }
        }

        public void move(UserControl control)
        {
            pageTransitionControl.ShowPage(control);
        }
    }
}
