using BankTask.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.WelcomeScreen
{
    public static class WelcomeScreen
    {
        internal static void Welcome()
        {
            Console.Clear();
            Console.Title = "Bank Task";
            Console.ForegroundColor = ConsoleColor.White;
            Console.WriteLine("--------------------Welcome to the Bank App-----------------");

            BankList.init();
            AccountList.init();
            while (true)
            {
                Console.WriteLine("Enter your choice");
                Console.WriteLine("1. Bank Staff");
                Console.WriteLine("2. Account Holder");
                Console.WriteLine("3. Exit");
                try
                {
                    Utility.MainFunc(int.Parse(Console.ReadLine()));
                }
                catch { Console.WriteLine("Wronge Choice"); };
            }
           
        }

      
    }
}
