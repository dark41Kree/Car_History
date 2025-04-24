package com.car.car_history;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewActivity extends AppCompatActivity implements ServerConnect.OnServerResponseListener {

    private EditText txtSendVin;
    private ListView responseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_view_activity);

        txtSendVin = findViewById(R.id.lblVinCode);
        responseListView = findViewById(R.id.listViewHistory);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.event_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnEventBack = findViewById(R.id.btnEventBack);
        btnEventBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCurrentPage();
            }
        });

        Button btnSearchByVin = findViewById(R.id.btnEventSearch);
        btnSearchByVin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String savedVin = txtSendVin.getText().toString().trim();
                if (savedVin.length() != 17) {
                    Toast.makeText(HistoryViewActivity.this, "VIN should be 17 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = "receive-" + savedVin;

                new ServerConnect(msg, HistoryViewActivity.this).execute();
            }
        });
    }

    public void closeCurrentPage() {
        finish();
    }

    @Override
    public void onServerResponse(String response) {

        if (response.isEmpty() || response.equals("No records found.")) {
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] records = response.split("\n");

        List<CarHistory> carHistoryList = new ArrayList<>();

        for (String record : records) {
            if (!record.trim().isEmpty()) {
                String[] parts = record.split(" \\| ");
                if (parts.length == 3) {
                    String type = parts[0].trim();
                    String date = parts[1].trim();
                    String description = parts[2].trim();
                    carHistoryList.add(new CarHistory(type, date, description));
                }
            }
        }


        CarHistoryAdapter adapter = new CarHistoryAdapter(this, carHistoryList);


        responseListView.setAdapter(adapter);
    }

}