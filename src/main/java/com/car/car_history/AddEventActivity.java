package com.car.car_history;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;


public class AddEventActivity extends AppCompatActivity implements ServerConnect.OnServerResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_event), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        ImageButton btnEventBack2 = findViewById(R.id.btnEventBack2);
        btnEventBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCurrentPage();
            }
        });

        EditText txtVin = findViewById(R.id.txtVinCode2);
        EditText txtEventType = findViewById(R.id.txtEventType);
        EditText txtDate = findViewById(R.id.txtDate);
        EditText txtDescription = findViewById(R.id.txtDesription);

        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vin = txtVin.getText().toString().trim();
                String eventType = txtEventType.getText().toString().trim();
                String date = txtDate.getText().toString().trim();
                String description = txtDescription.getText().toString().trim();

                if (vin.length() != 17) {
                    Toast.makeText(AddEventActivity.this, "VIN should be 17 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (eventType.isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Event Type should be completed first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (date.isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Event Date should be completed first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (description.isEmpty()) {
                    Toast.makeText(AddEventActivity.this, "Description should be completed first!", Toast.LENGTH_SHORT).show();
                    return;
                }



                new AlertDialog.Builder(AddEventActivity.this)
                        .setTitle("Confirm registration")
                        .setMessage("Are you sure you want to register this event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String savedVin = vin;
                                String savedEventType = eventType;
                                String savedDate = date;
                                String savedDescription = description;

                                String msg = "data-"+savedVin+"-"+savedEventType+"-"+savedDate+"-"+savedDescription;

                                connectToServer(msg);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();            }
        });



    }

    public void connectToServer(String message) {
        new ServerConnect(message,this).execute();
    }

    public void closeCurrentPage(){
        finish();
    }


    @Override
    public void onServerResponse(String response) {
        if (response.equals("success")) {
            Toast.makeText(this, "Event registered successfully!", Toast.LENGTH_SHORT).show();
            closeCurrentPage();
        } else {
            Toast.makeText(this, "Failed to register event: " + response, Toast.LENGTH_SHORT).show();
        }
    }
}

