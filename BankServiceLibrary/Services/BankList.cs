using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.Entity
{
  public  class BankList
    {
        
        private static List<Bank> _banks = new List<Bank>();
        

       static List<Dictionary<string, double>> acceptedCurrency = new List<Dictionary<string, double>>()
        {
            { new Dictionary<string, double>{{"INR",1} } },
            { new Dictionary<string, double>{{"RUB",0.92} } }
       };
       static List<Dictionary<string, string>> bankstaff = new List<Dictionary<string, string>>()
        {
            { new Dictionary<string, string>{{"abc@123","abc"} } },
            { new Dictionary<string, string>{{"xyz@234","xyz" } } }
       };
        public static List<Bank> getBankList()
        {
            return _banks;
        }
       
        public static void init()
        {
            _banks.Add(new Bank("ICICI", "ICI10102011", 0,5,2,6, acceptedCurrency, bankstaff));
            _banks.Add(new Bank("SBI", "SBI10102011", 0, 5, 2, 6, acceptedCurrency, bankstaff));
        }

    }
}
