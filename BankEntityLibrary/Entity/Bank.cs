using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.Entity
{
   public class Bank
    {
        public Bank()
        {

        }
        public Bank(string bankName, string bankID, double rTGSForSameBank, double iMPSForSameBank, double rTGSForOtherBank, double iMPSForOtherBank, List<Dictionary<string, double>> acceptedCurrency, List<Dictionary<string, string>> bankStaffs)
        {
            BankName = bankName;
            BankID = bankID;
            RTGSForSameBank = rTGSForSameBank;
            IMPSForSameBank = iMPSForSameBank;
            RTGSForOtherBank = rTGSForOtherBank;
            IMPSForOtherBank = iMPSForOtherBank;
            AcceptedCurrency = acceptedCurrency;
            BankStaffs = bankStaffs;
        }

        public string BankName { get; set; }
        public string BankID { get; set; }
        public double RTGSForSameBank { get; set; }
        public double IMPSForSameBank { get; set; }
        public double RTGSForOtherBank { get; set; }
        public double IMPSForOtherBank { get; set; }
        public List<Dictionary<string,double>> AcceptedCurrency { get; set; }

        public List<Dictionary<string, string>> BankStaffs { get; set; }

    }
}
