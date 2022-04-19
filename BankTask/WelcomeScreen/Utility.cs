using BankTask.App;
using BankTask.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.WelcomeScreen
{
   public static class Utility
    {

        internal static void MainFunc(int choice)
        {
            
                if (choice == 1)
                {
                Login.BankStaffLogin();
                
                }
                else if (choice == 2)
                {
                Login.AccHolderLogin();
                }
                else if (choice == 3)
                {
                    Environment.Exit(0);
                }
                else
                {
                    string msg = "invalid";
                    printErrorMessage(msg);
                    Console.WriteLine("Enter Valid Choice");
                }
            
        }

        public static void AccHolderops(string username)
        {
            bool isTrue = false;
            while (!isTrue)
            {
                Console.WriteLine("1. Deposit");
                Console.WriteLine("2. Withdraw");
                Console.WriteLine("3. Transfer");
                Console.WriteLine("4. View Transaction History");
                Console.WriteLine("5. Exit");
                isTrue=AccHolder.AccHolderFunctionalities(int.Parse(Console.ReadLine()),username);
            }
        }
        

        public static void BankStaffops()
        {
            bool isTrue = false;
        while (!isTrue)
        {
            Console.WriteLine("1. Create New Account");
            Console.WriteLine("2. Update Account");
            Console.WriteLine("3. Delete New Account");
            Console.WriteLine("4. Add Currency");
            Console.WriteLine("5. Add Service Charges for same bank");
            Console.WriteLine("6. Add Service Charges for other bank");
            Console.WriteLine("7. View Transcation History");
            Console.WriteLine("8. Revert Transaction");
            Console.WriteLine("9. Exit");
                Console.WriteLine("10. Display");
                isTrue =BankStaff.BankStaffFunctionalities(int.Parse(Console.ReadLine()));
        }
               
        }

        private static void printErrorMessage(string msg)
        {
            Console.WriteLine($"Choice enteres is {msg}");
        }
    }
}
