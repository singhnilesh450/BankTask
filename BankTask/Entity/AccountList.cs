using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.Entity
{
    class AccountList
    {
        private static List<Account> _accounts = new List<Account>();

       static  List<string> trans = new List<string>()
        {
            { "TXNPNS10102012ANS1010201220120229"+" "+"+123" },{"TXNKNU10102012NSJ1010201222121223"+" "+"-233"}

        };
        public static void init()
        {
            _accounts.Add(new Account("Nilesh", "123@abc", "123", "SBI","SBI10102011", "NIL10102011", 1281.10,trans));
            _accounts.Add(new Account("Singh", "111@abc", "111", "SBI", "SBI10102011", "SIN10102011", 1828.10, trans));
        }
        public static void AddAccount(Account ac)
        {
            foreach(var a in ac.UserName)
            {
                Console.WriteLine(a);
            }
            _accounts.Add(ac);
        }

        public static void DeleteAccount(int idx)
        {
             _accounts.RemoveAt(idx);
        }
        public static void UpdateAccount(int idx,Account ac)
        {
            _accounts[idx] = ac;
        }

        public static void deposit(string curr, double amt,string username)
        {
            int idx = -1;
            Account ac = new Account();
            foreach (var item in _accounts)
            {
                if (item.UserName == username)
                {
                    idx = _accounts.IndexOf(item);
                    ac = item;
                    break;
                }
            }
            double excRate = BankList.getExchangeRate(ac.BankId, curr);
            if (excRate == -1)
            {
                Console.WriteLine("Currency is Not Valid Or Accepted");
                return;
            }
            
            _accounts[idx].Balance = _accounts[idx].Balance + amt*excRate;
            _accounts[idx].TransactionHistory.Add("TXN"+ac.BankId+ac.AccountId+DateTime.Now.ToString("ddMMyyyy")+" "+"+"+ amt*excRate);
        }

        internal static void Transfer(string tar_acc_id, double amt, string username)
        {
            int idx = -1;
            Account ac = new Account();
            foreach (var item in _accounts)
            {
                if (item.UserName == username)
                {
                    idx = _accounts.IndexOf(item);
                    ac = item;
                    break;
                }
            }
            if (ac.Balance < amt)
            {
                Console.WriteLine("Amount Insufficient");
                return;
            }
            Account ac1 = new Account();
            foreach (var item in _accounts)
            {
                if (item.AccountId == tar_acc_id)
                {
                    idx = _accounts.IndexOf(item);
                    ac1 = item;
                    break;
                }
            }
            if (ac1 == null)
            {
                Console.WriteLine("Target Account Id Incorrect");
                return;
            }
            ac.Balance = ac.Balance - amt;
            ac1.Balance = ac1.Balance + amt;
            ac.TransactionHistory.Add("TXN" + ac.BankId + ac.AccountId + DateTime.Now.ToString("ddMMyyyy") +" " +"transfer" +" "+ amt );
            ac1.TransactionHistory.Add("TXN" + ac.BankId + ac.AccountId + DateTime.Now.ToString("ddMMyyyy") +" " +"received"+" " + amt);

        }

        public static void Withdraw(double amt,string username)
        {
            int idx = -1;
            Account ac = new Account();
            foreach (var item in _accounts)
            {
                if (item.UserName == username)
                {
                    idx = _accounts.IndexOf(item);
                    ac = item;
                    break;
                }
            }
            if (ac.Balance < amt)
            {
                Console.WriteLine("Balance is not sufficient");
                return;
            }
            _accounts[idx].Balance = _accounts[idx].Balance - amt;
            _accounts[idx].TransactionHistory.Add("TXN" + ac.BankId + ac.AccountId + DateTime.Now.ToString("ddMMyyyy")+" " + "-" + amt );
        }

        internal static bool CheckUserPss(string username, string pass)
        {
            var user = from accholder in _accounts
                       where accholder.UserName.Equals(username) && accholder.Password.Equals(pass)
                       select accholder;
            if (user.Count() == 0)
            {
                return false;
            }
            else
            {

                return true;
            }
        }

      

        public static int getIndexByAccId(string acc_id)
        {
            Console.WriteLine("hi");
            Console.WriteLine(acc_id);
            int n = -1;
            List<int> ll = new List<int>()
            {
                22,44,22
            };
            foreach(var i in ll)
            {
                Console.WriteLine(i);
            }
            
            foreach (var item in _accounts)
            {
                if (item.AccountId ==acc_id)
                {
                    n = _accounts.IndexOf(item);
                    break;
                }
            }
            return n;
        }

        internal static bool transactRevert(string acc_id, string tras_id)
        {
            Account temp=new Account();
            foreach (var item in _accounts)
            {
                if (item.AccountId == acc_id)
              {
                    temp = item;
                    break;
                }
           }
            if (temp.AccountId == null)
            {
                return false;
            }
            int idx = -1;
            foreach(var item in _accounts[_accounts.IndexOf(temp)].TransactionHistory)
            {
                if (item.Substring(0,33) == tras_id)
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

        public static List<string> getMatchedTransactionHistory(string acc_id)
        {
            List<string> li = new List<string>();
            foreach (Account item in _accounts)
            {
               if (item.AccountId == acc_id)
               {
                   foreach(var i in item.TransactionHistory)
                    {
                        li.Add(i);
                    }
                }
            }
            return li;
        }

        internal static Account getAccByAccId(string acc_id)
        {
            foreach (Account item in _accounts)
            {
                if (item.AccountId == acc_id)
                {

                    return item;
                }
            }
            return null;
        }

        public static void displayAcc()
        {
            Console.WriteLine("sd");
            var acc = from ast in _accounts
                      select ast.UserName;
            var acc1 = from ast in _accounts
                      select ast.BankName;
            var acc2 = from ast in _accounts
                       select ast.Balance;

            Console.WriteLine(acc.Count());
            foreach(var s in acc)
            {
                Console.WriteLine(s);
            }
            foreach (var s in acc1)
            {
                Console.WriteLine(s);
            }
            foreach (var s in acc2)
            {
                Console.WriteLine(s);
            }
        }

        public static void TransferFunds(int idxs,int idxd, double amt)
        {
            _accounts[idxs].Balance = _accounts[idxs].Balance - amt;
            _accounts[idxs].TransactionHistory.Add("-" + amt);
            _accounts[idxd].Balance = _accounts[idxd].Balance + amt;
            _accounts[idxd].TransactionHistory.Add("+" + amt);

        }
    }
}
