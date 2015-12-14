using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MLVApplication
{
    public static class Extentions
    {
        //Extention to ObservableCollection to allow add a source collection to a destination collection
        public static void AddRange<T>(this ObservableCollection<T> destination,
                                       IEnumerable<T> source)
        {
            if (source == null) { throw new ArgumentException("source"); }
            foreach (var item in source)
            {
                destination.Add(item);
            }
        }


        public static bool IsEmpty(this string value)
        {
            return value.Trim().Count() == 0;
        }
        public static bool IsHexa(this string test)
        {
            return System.Text.RegularExpressions.Regex.IsMatch(test, @"\A\b[0-9a-fA-F]+\b\Z");
        }
    }
}

