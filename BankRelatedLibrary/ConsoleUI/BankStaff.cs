using BankTask.Entity;
using BankTask.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BankTask.App
{
   public class BankStaff
    {
        private static List<Bank> _banks = BankList.getBankList();
        internal static bool BankStaffFunctionalities(int choice)
        {
            switch (choice)
            {
                case 1:
                    createNewAcc();
                    return false;
                case 2:
                    updateAcc(); 
                    
                    return false;
                case 3:
                    deleteAcc();
                    return false;
                case 4:
                    addCurrAndForex();
                    return false;
                case 5:
                    addServiceChargeForSameBank();
                    return false;
                case 6:
                    addServiceChargeForOtherBank();
                    return false;
                case 7:
                    printTransactHistory();
                    return false;
                case 8:
                    revertTrans();
                    return false;
                case 9:
                    return true;
                case 10:
                    AccountUtil.displayAcc();
                    return false;
                default:
                    return false;
                  
            }
        }

        private static void addServiceChargeForOtherBank()
        {
            Console.WriteLine("Enter Bank ID");
            string bank_id = Console.ReadLine();
            Console.WriteLine("Enter RTGS rate");
            double rtgs = double.Parse(Console.ReadLine());
            Console.WriteLine("Enter IMPS rate");
            double imps = double.Parse(Console.ReadLine());
            BankUtil.updateServiceChargeForOtherBank(bank_id, rtgs, imps);
        }

        private static void addServiceChargeForSameBank()
        {
            Console.WriteLine("Enter Bank ID");
            string bank_id = Console.ReadLine();
            Console.WriteLine("Enter RTGS rate");
            double rtgs = double.Parse(Console.ReadLine());
            Console.WriteLine("Enter IMPS rate");
            double imps = double.Parse(Console.ReadLine());
            BankUtil.updateServiceChargeForSameBank(bank_id,rtgs, imps);
        }

        private static void revertTrans()
        {
            Console.WriteLine("Enter Account Id");
            string acc_id = Console.ReadLine();
            Console.WriteLine("Enter Transction ID to be reverted");
            string tras_id = Console.ReadLine();
           bool revert_status = Payments.transactRevert(acc_id,tras_id);

        }

        private static void printTransactHistory()
        {
            Console.WriteLine("Enter Account ID");
            var acc_id = Console.ReadLine();
            List<String> li = AccountUtil.getMatchedTransactionHistory(acc_id);
            if (li.Count()==0)
            {
                Console.WriteLine("No such acc_id exist");
                return;
            }
           foreach(var i in li)
            {
                Console.WriteLine(i);
            }
        }

        private static void addCurrAndForex()
        {
            Console.WriteLine("Enter Currency");
            var curr = Console.ReadLine();
            Console.WriteLine("Enter Exchange rate");
            var rate = double.Parse(Console.ReadLine());
            BankUtil.addCurrencyAndForex(new Dictionary<string, double>() {{ curr, rate }});
                }

        private static void deleteAcc()
        {
            Console.WriteLine("Enter Account ID to be deleted");
            string acc_id = Console.ReadLine();
            int idx = AccountList.getIndexByAccId(acc_id);
            Account acc = AccountList.getAccByAccId(acc_id);
            if (idx == -1)
            {
                Console.WriteLine("didn't find any ele");
            }
            else
            {
                AccountUtil.DeleteAccount(idx);
                Console.WriteLine("Account Succesfully deleted");
            }
        }

        private static void updateAcc()
        {
            Console.WriteLine("Enter Account ID to be updated");
            string acc_id = Console.ReadLine();
            int idx = AccountList.getIndexByAccId(acc_id);
            Account acc = AccountList.getAccByAccId(acc_id);
            if (idx == -1)
            {
                Console.WriteLine("didn't find any ele");
            }
            else
            {
                Console.WriteLine("Enter Your Name");
                string name = Console.ReadLine();
                Console.WriteLine("Enter Bank Name");
                string bank_name = Console.ReadLine();
                
                Console.WriteLine("Account Balance");
                double bal = double.Parse(Console.ReadLine());
                string oldDate = acc.BankId.Substring(3);
                acc.Name = name;
                acc.BankName = bank_name;
                acc.Balance = bal;
                acc.BankId = bank_name.Substring(0, 3) + oldDate;
                acc.AccountId = name.Substring(0, 3) + oldDate;
                AccountUtil.UpdateAccount(idx,acc);
                Console.WriteLine("Account successfully uodated");
            }
        }
        internal static Bank getBankListByBankName(string bankName)
        {
            foreach (Bank item in _banks)
            {
                if (item.BankName == bankName)
                    return item;
            }
            return null;
        }
        private static void createNewAcc()
        {
            Console.WriteLine("Enter Your Name");
            string name = Console.ReadLine();
            Console.WriteLine("Enter Bank Name");
            string bank_name = Console.ReadLine();

            Console.WriteLine("Give Username");
            string username = Console.ReadLine();
            Console.WriteLine("Give Password");
            string pass = Console.ReadLine();
            Console.WriteLine("Account Balance");
            double bal = double.Parse(Console.ReadLine());
            Bank bn = getBankListByBankName(bank_name);
            if (bn == null)
            {
                Console.WriteLine("Bank does not exist TryAgain");
                return;
            }
            AccountUtil.AddAccount(new Account(name, username, pass, bank_name, bn.BankID, name.Substring(0, 3).ToUpper() + DateTime.Now.ToString("ddMMyyyy"), bal, new List<string>()));
        }
    }
}