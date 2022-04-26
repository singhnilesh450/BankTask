using BankTask.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.App
{
    class AccHolder
    {
        internal static bool AccHolderFunctionalities(int v,string username)
        {
            switch (v)
            {
                case 1:
                    depositMoney(username);
                    return false;
                case 2:
                    withdrawMoney(username);
                    return false;
                case 3:
                    transferFunds(username);
                    return false;
                case 4:
                    printTransactHistory();
                    return false;
                case 5:
                    AccountList.saveToJson();
                    AccountList.LoadFromJson();
                    return true;
                default:
                    return false;
            }
        }

        private static void transferFunds(string username)
        {
            Console.WriteLine("Enter Target Account Id");
            string tar_acc_id = Console.ReadLine();
            Console.WriteLine("Enter Amount in INR only");
            double amt = double.Parse(Console.ReadLine());
            AccountList.Transfer(tar_acc_id, amt, username);
        }

        private static void withdrawMoney(string username)
        {
            
            Console.WriteLine("Enter Amount in INR only");
            double amt = double.Parse(Console.ReadLine());
            AccountList.Withdraw( amt, username);
        }

        private static void printTransactHistory()
        {
            Console.WriteLine("Enter Account ID");
            var acc_id = Console.ReadLine();
            List<String> li = AccountList.getMatchedTransactionHistory(acc_id);
            if (li.Count() == 0)
            {
                Console.WriteLine("No such acc_id exist");
                return;
            }
            foreach (var i in li)
            {
                Console.WriteLine(i);
            }
        }
        private static void depositMoney(string username)
        {
            Console.WriteLine("Enter Currency");
            string curr = Console.ReadLine();
            Console.WriteLine("Enter Amount");
            double amt = double.Parse(Console.ReadLine());
            AccountList.deposit(curr, amt, username);
        }
    }
    }

