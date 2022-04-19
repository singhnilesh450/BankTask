using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.Entity
{
    class BankList
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
        public static bool CheckUserPss(string username, string pass)
        {
           
            var user = from bank in _banks
                       from staff in bank.BankStaffs
                       where staff.Keys.Contains(username) && staff[username].Contains(pass)
                       select staff;
            if (user.Count()==0)
            {
                return false;
            }else
            {
                
                return true;
            }
        }
        public static void init()
        {
            _banks.Add(new Bank("ICICI", "SBI10102011", 0,5,2,6, acceptedCurrency, bankstaff));
  
        }

        internal static double getExchangeRate(string bankId, string curr)
        {
            int idx = -1;
            Bank bn = new Bank();
            foreach (var item in _banks)
            {
                if (item.BankID == bankId)
                {
                    idx = _banks.IndexOf(item);
                    bn = item;
                    break;
                }
            }
            double rate = -1;
            foreach (var item in  bn.AcceptedCurrency)
            {
                if (item.ContainsKey(curr))
                {
                    rate = item[curr];
                    break;
                }
            }
            return rate;

        }




        public static void AddBank(Bank bank)
        {
            _banks.Add(bank);
        }
       

        internal static void addCurrencyAndForex(Dictionary<string, double> dictionary)
        {
            _banks.ForEach(cs => cs.AcceptedCurrency.Add(dictionary));
        }

        internal static void updateServiceChargeForSameBank(string bank_id,double rtgs, double imps)
        {
            int n = -1;
            foreach (var item in _banks)
            {
                if (item.BankID == bank_id)
                {
                    n = _banks.IndexOf(item);
                    break;
                }
            }
            if (n == -1)
            {
                Console.WriteLine("Incorrect Bank id");
                return;
            }
            Console.WriteLine(_banks[n].IMPSForSameBank);
            _banks[n].RTGSForSameBank = rtgs;
            _banks[n].IMPSForSameBank = imps;
            Console.WriteLine(_banks[n].IMPSForSameBank);
        }

        internal static void updateServiceChargeForOtherBank(string bank_id, double rtgs, double imps)
        {
            int n = -1;
            foreach (var item in _banks)
            {
                if (item.BankID == bank_id)
                {
                    n = _banks.IndexOf(item);
                    break;
                }
            }
            if (n == -1)
            {
                Console.WriteLine("Incorrect Bank id");
                return;
            }
            Console.WriteLine(_banks[n].IMPSForOtherBank);
            _banks[n].RTGSForOtherBank = rtgs;
            _banks[n].IMPSForOtherBank = imps;
            Console.WriteLine(_banks[n].IMPSForOtherBank);
        }
 
    }
}
