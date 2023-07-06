package com.example.comupnaguilarchavez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.comupnaguilarchavez.BD.AppDatabase;
import com.example.comupnaguilarchavez.Entities.Duelista;
import com.example.comupnaguilarchavez.Repositories.DuelistaRepository;
import com.example.comupnaguilarchavez.Utilities.RetrofitU;

import retrofit2.Retrofit;

public class RegistroDuelistaActivity extends AppCompatActivity {

    Retrofit retrofitP;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_duelista);

        EditText regDNom = findViewById(R.id.etDuelistaD);
        Button btnRegistro = findViewById(R.id.btttnRegistro);
        Button btnGoingBack = findViewById(R.id.bttnGoingBack2);

        retrofitP = RetrofitU.build(); //settear mockapi

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreD = regDNom.getText().toString();

                //Crear un service del repository
                AppDatabase database = AppDatabase.getInstance(context);
                DuelistaRepository dueliRepository = database.duelistaRepository();

                // Obtener el último ID registrado en la base de datos para crear uno nuevo
                int lastId = dueliRepository.getLastId();

                Duelista duel = new Duelista();
                duel.setNombreDuelista(nombreD);
                duel.setSyncro(false);

                dueliRepository.create(duel); //enviando a la BD

                Intent intent =  new Intent(RegistroDuelistaActivity.this, ListaDuelistasActivity.class);
                startActivity(intent);
                finish();


            }
        });



    }
}