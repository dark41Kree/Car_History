package com.car.car_history;

import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnect extends AsyncTask<Void, Void, String> {

    private static final String SERVER_IP = "10.0.2.2";
    private static final int SERVER_PORT = 8000;
    private String message;
    private OnServerResponseListener listener; // Listener pentru a transmite rezultatul

    public ServerConnect(String message, OnServerResponseListener listener) {
        this.message = message;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Socket socket = new Socket(serverAddr, SERVER_PORT);

            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(message);

            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String response = new String(buffer, 0, bytesRead);

            socket.close();

            return response;
        } catch (Exception e) {
            Log.e("ClientSocket", "Eroare: " + e.getMessage());
            return "Eroare la conectare!";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("ClientSocket", "Raspunsul serverului: " + result);
        if (listener != null) {
            listener.onServerResponse(result);
        }
    }

    public interface OnServerResponseListener {
        void onServerResponse(String response);
    }
}