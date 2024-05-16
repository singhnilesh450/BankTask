using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.Entity
{
    class AccountUtil
    {
        static List<Account> _accounts = AccountList.getAccountList();

        public static void AddAccount(Account ac)
        {
            _accounts.Add(ac);
        }
        public static void DeleteAccount(int idx)
        {
            _accounts.RemoveAt(idx);
        }
        public static void UpdateAccount(int idx, Account ac)
        {
            _accounts[idx] = ac;
        }
        internal static bool CheckUserPss(string username, string pass)
        {
            if (username.Length == 0 || pass.Length == 0)
                return false;
            var user = from accholder in _accounts
                       where accholder.UserName.Equals(username) && accholder.Password.Equals(pass)
                       select accholder;
            if (user.Count() == 0)
                return false;
            else
                return true;
        }
        public static List<string> getMatchedTransactionHistory(string acc_id)
        {
            List<string> li = new List<string>();
            foreach (Account item in _accounts)
            {
                if (item.AccountId == acc_id)
                {
                    foreach (var i in item.TransactionHistory)
                    {
                        li.Add(i);
                    }
                }
            }
            return li;
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
            var acc3 = from ast in _accounts
                       select ast.AccountId;
            Console.WriteLine(acc.Count());
            foreach (var s in acc)
                Console.WriteLine(s);
            foreach (var s in acc1)
                Console.WriteLine(s);
            foreach (var s in acc2)
                Console.WriteLine(s);
            foreach (var s in acc3)
                Console.WriteLine(s);
        }
    }
}
