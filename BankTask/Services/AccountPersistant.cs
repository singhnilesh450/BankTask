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
    class AccountPersistant
    {

        static List<Account> _accounts = AccountList.getAccountList();

        public static void saveToJson()
        {
            string jsonString = JsonSerializer.Serialize(_accounts);
            String filename = "accData.json";
            String path = "D:/task_techno/Practice/BankTask/BankTask/IntactData" + "/" + filename;
            FileStream fileStream = new FileStream(path, FileMode.Create);
            using (StreamWriter writer = new StreamWriter(fileStream))
            {
                writer.Write(jsonString);
            }
            //  Console.WriteLine(jsonString);
        }
        public static void LoadFromJson()
        {
            String filename = "accData.json";
            string content = "";
            String path = "D:/task_techno/Practice/BankTask/BankTask/IntactData" + "/" + filename;
            if (File.Exists(path))
            {
                using (StreamReader reader = new StreamReader(path))
                {
                    content = reader.ReadToEnd();

                }
            }
            // var data = JsonConvert.DeserializeObject<Account>(content);
            //        var data =
            //JsonSerializer.Deserialize<Account>(path);
            //        foreach(var i in data.AccountId)
            //        Console.WriteLine(i);
            JavaScriptSerializer js = new JavaScriptSerializer();
            // Account<List> ac = js.Deserialize<List<Account>(content);
            // Console.WriteLine();
        }
    }
}
