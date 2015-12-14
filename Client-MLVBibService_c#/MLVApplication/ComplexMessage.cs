using MLVApplication.Content;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MLVApplication
{
    public class ComplexMessage<T>
    {
        public T Message { get; set; }
        public ActionEvent MyEvent { get; set;}
        public object Sender { get; set; }
        public BookListTransition Dest { get; set; }

    }
}
