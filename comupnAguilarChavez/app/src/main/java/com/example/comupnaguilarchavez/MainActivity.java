package com.example.comupnaguilarchavez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registrarY = findViewById(R.id.btnRegistrarD);
        Button showY = findViewById(R.id.btnMisDuelistas);

        registrarY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistroDuelistaActivity.class);
                startActivity(intent);
            }
        });

        showY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaDuelistasActivity.class);
                startActivity(intent);
            }
        });
    }
}