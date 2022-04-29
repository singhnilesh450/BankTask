
using Nancy.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.Json;
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
        public static List<Account> getAccountList()
        {
            return _accounts;
        }
        public static int getIndexByAccId(string acc_id)
        {
            int n = -1;         
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
        internal static Account getAccByAccId(string acc_id)
        {
            foreach (Account item in _accounts)
            {
                if (item.AccountId == acc_id)
                    return item;               
            }
            return null;
        }
        public static int getIndexByUsername(string user)
        {
            int n = -1;
            foreach (var item in _accounts)
            {
                if (item.UserName == user)
                {
                    n = _accounts.IndexOf(item);
                    break;
                }
            }
            return n;
        }
        internal static Account getAccByUsername(string user)
        {
            foreach (Account item in _accounts)
            {
                if (item.UserName == user)
                    return item;
            }
            return null;
        }
    }
}
