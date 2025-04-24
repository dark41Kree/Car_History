using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.SQLite; //Install-Package System.Data.SQLite


namespace ServerExemplu
{
    internal class DBHandler
    {

        public static SQLiteConnection SQLiteConnect()
        {
            SQLiteConnection conn;
            conn = new SQLiteConnection("Data Source=db.sqlite; Version=3;");
            try
            {
                conn.Open();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                conn.Close();
            }
            return conn;
        }

        public static void InsertData(SQLiteConnection conn, string Vin, string type, string date, string description)
        {
            using (SQLiteCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "INSERT INTO Car_History (Vin, Type, Date, Description) VALUES (@Vin, @Type, @Date, @Description)";
                cmd.Parameters.AddWithValue("@Vin", Vin);
                cmd.Parameters.AddWithValue("@Type", type);
                cmd.Parameters.AddWithValue("@Date", date);
                cmd.Parameters.AddWithValue("@Description", description);

                try
                {
                    cmd.ExecuteNonQuery();
                    Console.WriteLine("Adaugare cu succes!");
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Eroare la adaugare: " + ex.Message);
                }
            }
        }

        public static void SQLiteDisconnect(SQLiteConnection conn)
        {
            if (conn != null)
            {
                conn.Close();
            }
        }



        public static string GetInfo(SQLiteConnection conn, string Vin)
        {
            // Curatam valoarea Vin de caractere invizibile
            Vin = Vin.Trim(); // Eliminam spatiile
            Vin = Vin.Replace("\n", "").Replace("\r", ""); // Eliminam newline și carriage return

            List<CarHistory> allInfo = new List<CarHistory>();
            StringBuilder result = new StringBuilder();

            using (SQLiteCommand cmd = conn.CreateCommand())
            {
                cmd.CommandText = "SELECT Type, Date, Description FROM Car_History WHERE Vin = @Vin";
                cmd.Parameters.AddWithValue("@Vin", Vin);

                using (SQLiteDataReader reader = cmd.ExecuteReader())
                {
                    bool found = false;
                    while (reader.Read())
                    {
                        CarHistory car = new CarHistory
                        {
                            Type = reader.GetString(0),
                            Date = reader.GetString(1),
                            Description = reader.GetString(2)
                        };

                        allInfo.Add(car);
                        result.AppendLine($"{car.Type} | {car.Date} | {car.Description}");
                        found = true;
                    }

                    if (!found)
                    {
                        result.AppendLine("No records found.");
                    }
                }
            }

            return result.ToString().Trim();
        }

    }
}
