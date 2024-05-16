using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.Entity
{
   public class Account
    {
        public Account()
        {       }
        public Account(string name, string userName, string password, string bankName, string bankId, string accountId, double balance, List<string> transactionHistory)
        {
            Name = name;
            UserName = userName;
            Password = password;
            BankName = bankName;
            BankId = bankId;
            AccountId = accountId;
            Balance = balance;
            TransactionHistory = transactionHistory;
        }

        public string Name { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }
        public string BankName { get; set; }
        public string BankId { get; set; }
        public string AccountId { get; set; }
        public double Balance { get; set; }
  
        public List<string> TransactionHistory { get; set; }

    }
}
