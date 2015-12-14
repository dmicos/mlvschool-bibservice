using FirstFloor.ModernUI.Windows;
using MLVApplication.Content;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WpfPageTransitions;

namespace MLVApplication
{
    public class PageLoader: DefaultContentLoader
    {
        public BookListTransition Transition { get; set; }
        public Uri LastLoad { get; set; }

        public PageLoader(BookListTransition tr)
        {
            Transition = tr;
        }
        /// <summary>
        /// Loads the content from specified uri.
        /// </summary>
        /// <param name="uri">The content uri</param>
        /// <returns>The loaded content.</returns>
        protected override object LoadContent(Uri uri)
        {
            BookListTransition tr = new BookListTransition();
            LastLoad = uri;
            Transition.move(new BookList());
            //loadData(tr);
            return Transition;
        }

        private async void loadData(BookListTransition tr)
        {
            //tr.loader.IsActive = true;

            await Task.Delay(10).ContinueWith(_ =>
            {
                tr.move(new BookList());
               // tr.loader.IsActive = false;
                Transition = tr;
            });
        }

        public void reload(BookListTransition tr)
        {
            loadData(tr);
        }

    }
}
