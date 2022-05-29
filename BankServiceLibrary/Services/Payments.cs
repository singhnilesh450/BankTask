using BankTask.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.Entity
{
 public   class Payments
    {

        static List<Account> _accounts = AccountList.getAccountList();
        public static void deposit(string curr, double amt, string username)
        {
            int idx = AccountList.getIndexByUsername(username);
            Account ac = new Account();
            ac = AccountList.getAccByUsername(username);
            double excRate = BankUtil.getExchangeRate(ac.BankId, curr);
            if (excRate == -1)
            {
                Console.WriteLine("Currency is Not Valid Or Accepted");
                return;
            }

            _accounts[idx].Balance = _accounts[idx].Balance + amt * excRate;
            _accounts[idx].TransactionHistory.Add("TXN" + ac.BankId + ac.AccountId + DateTime.Now.ToString("ddMMyyyy") + " " + "+" + amt * excRate);
        }
        public static void Transfer(string tar_acc_id, double amt, string username)
        {
            Account ac = new Account();
            ac = AccountList.getAccByAccId(tar_acc_id);
            if (ac.Balance < amt)
            {
                Console.WriteLine("Amount Insufficient");
                return;
            }
            Account ac1 = new Account();
            ac1 = AccountList.getAccByAccId(tar_acc_id);
            if (ac1 == null)
            {
                Console.WriteLine("Target Account Id Incorrect");
                return;
            }
            ac.Balance = ac.Balance - amt;
            ac1.Balance = ac1.Balance + amt;
            ac.TransactionHistory.Add("TXN" + ac.BankId + ac.AccountId + DateTime.Now.ToString("ddMMyyyy") + " " + "transfer" + " " + amt);
            ac1.TransactionHistory.Add("TXN" + ac.BankId + ac.AccountId + DateTime.Now.ToString("ddMMyyyy") + " " + "received" + " " + amt);

        }
        public static void Withdraw(double amt, string username)
        {
            int idx = AccountList.getIndexByUsername(username);
            Account ac = new Account();
            ac = AccountList.getAccByUsername(username);
            if (ac.Balance < amt)
            {
                Console.WriteLine("Balance is not sufficient");
                return;
            }
            _accounts[idx].Balance = _accounts[idx].Balance - amt;
            _accounts[idx].TransactionHistory.Add("TXN" + ac.BankId + ac.AccountId + DateTime.Now.ToString("ddMMyyyy") + " " + "-" + amt);
        }
        public static void TransferFunds(int idxs, int idxd, double amt)
        {
            _accounts[idxs].Balance = _accounts[idxs].Balance - amt;
            _accounts[idxs].TransactionHistory.Add("-" + amt);
            _accounts[idxd].Balance = _accounts[idxd].Balance + amt;
            _accounts[idxd].TransactionHistory.Add("+" + amt);

        }
        public static bool transactRevert(string acc_id, string tras_id)
        {
            Account temp = new Account();
            temp=AccountList.getAccByAccId(acc_id);
            if (temp.AccountId == null)
            {
                return false;
            }
            int idx = -1;
            foreach (var item in _accounts[_accounts.IndexOf(temp)].TransactionHistory)
            {
                if (item.Substring(0, 33) == tras_id)
                {
                    idx = _accounts[_accounts.IndexOf(temp)].TransactionHistory.IndexOf(item);
                }
            }
            temp.Balance = temp.Balance - int.Parse(temp.TransactionHistory.ElementAt(idx).Substring(35));
            foreach (var item in temp.TransactionHistory)
            {
                if (item.Substring(0, 33) == tras_id)
                {
                    temp.TransactionHistory.Remove(item);
                    break;
                }
            }
            return true;
        }
    }
}
