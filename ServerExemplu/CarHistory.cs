using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServerExemplu
{
    [Table("Car_History")]
    public class CarHistory
    {

        public int Id { get; set; }

        public string Vin { get; set; }

        public string Type { get; set; }

        public string Date { get; set; }

        public string Description { get; set; }


    }

    //clasa MyDBContext se conecteaza la baza de date si va genera inserturile, selecturile
    public class carHistoryDBContext : DbContext
    {
        public carHistoryDBContext() : base("name=HistoryDBContext")
        {
        }

        public DbSet<CarHistory> AboutCar { get; set; }

    }
}
