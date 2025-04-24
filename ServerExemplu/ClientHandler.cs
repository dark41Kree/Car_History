
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Data.SQLite;

namespace ServerExemplu
{
    class ClientHandler
    {
        private Socket _sk = null;
        private int _idx = -1;
        private Thread _th = null;
        private bool _shouldRun = true;
        private bool _isRunning = true;

        private SQLiteConnection conn = null;

        public ClientHandler(Socket sk, int id)
        {
            _sk = sk;
            _idx = id;
            conn = DBHandler.SQLiteConnect();
        }

        public void initClient()
        {
            if (null != _th)
                return;

            _th = new Thread(new ThreadStart(run));
            _th.Start();
        }

        public void stopClient()
        {
            if (_th == null)
                return;

            _sk.Close();
            _shouldRun = false;
        }

        public bool SocketConnected(Socket s)
        {
            bool part1 = s.Poll(10000, SelectMode.SelectRead);
            bool part2 = (s.Available == 0);
            return !(part1 && part2);
        }

        private void handleMsg(String msg)
        {
            char[] sep = { '-' };
            String[] arrMsg = msg.Split(sep);

            if (arrMsg[0].StartsWith("data")) // mesaj de tip "data-str1-str2-str3-str4"
            {
                handleReceivedData(arrMsg);
                string message = "success";
                byte[] data = Encoding.UTF8.GetBytes(message);

                try
                {
                    _sk.Send(data);
                    Console.WriteLine("Mesaj trimis: " + message);
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Eroare la trimiterea mesajului: " + ex.Message);
                }
            }
            else if (arrMsg[0].StartsWith("receive"))  //mesaj de tipul "receive-str1"
            {
                sendData(arrMsg);
            }
        }

        private void handleReceivedData(String[] arrMsg)
        {
            if (arrMsg.Length < 5)
            {
                Console.WriteLine("Eroare: mesaj invalid!");
                return;
            }

            string field1 = arrMsg[1];
            string field2 = arrMsg[2];
            string field3 = arrMsg[3];
            string field4 = arrMsg[4];

            Console.WriteLine($"Date primite: {field1}, {field2}, {field3}, {field4}");

            DBHandler.InsertData(conn,field1, field2, field3, field4); //Aici se introduc datele in db.sqlite
        }

        private void sendData(String[] arrMsg)
        {
            
            string field1 = arrMsg[1];

            string carList = DBHandler.GetInfo(conn, field1);  // Extragem datele din DB

            byte[] data = Encoding.UTF8.GetBytes(carList);  //o transformam in byte

            try
            {
                _sk.Send(data);
                Console.WriteLine("Mesaj trimis: " + carList);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Eroare la trimiterea mesajului: " + ex.Message);
            }

        }


        private void run()
        {
            while (_shouldRun)
            {
                byte[] rawMsg = new byte[256];
                try
                {
                    int bCount = _sk.Receive(rawMsg);
                    if (bCount > 0)
                    {
                        String msg = Encoding.UTF8.GetString(rawMsg, 0, bCount);
                        Console.WriteLine("New Client " + _idx + ": " + msg);
                        handleMsg(msg);
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Eroare la receptia mesajului(run): " + ex.Message);
                    return;
                }
                Thread.Sleep(1);
            }
            _isRunning = false;
        }

        public bool isAlive()
        {
            return _isRunning;
        }
    }


}
