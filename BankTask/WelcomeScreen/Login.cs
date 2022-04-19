using BankTask.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.WelcomeScreen
{
    class Login
    {
        public static void BankStaffLogin()
        {
            Console.WriteLine("Enter Username");
            string username = Console.ReadLine();
            Console.WriteLine("Enter Password");
            string pass = Console.ReadLine();
            bool isOk = BankList.CheckUserPss(username, pass);
            if (isOk)
            {
                Utility.BankStaffops();
            }
            else
            {
                Console.WriteLine("Incorrect Credentials, Try Again");
                return;
            }
        }
        public static void AccHolderLogin()
        {
            
            Console.WriteLine("Enter Username");
            string username = Console.ReadLine();
            Console.WriteLine("Enter Password");
            string pass = Console.ReadLine();
            bool isOk = AccountList.CheckUserPss(username, pass);
            if (isOk)
            {
                Utility.AccHolderops(username);
            }
            else
            {
                Console.WriteLine("Incorrect Credentials, Try Again");
                return;
            }
        }
    }
}
