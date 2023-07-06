package com.example.comupnaguilarchavez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comupnaguilarchavez.BD.AppDatabase;
import com.example.comupnaguilarchavez.Entities.Cartas;
import com.example.comupnaguilarchavez.Repositories.CartasRepository;
import com.example.comupnaguilarchavez.Utilities.RetrofitU;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;

public class DetalleCartasActivity extends AppCompatActivity {

    int idCarta;
    Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cartas);

        TextView regDENomC = findViewById(R.id.tvNombreCarta);
        TextView regDEAtaq = findViewById(R.id.tvPuntosAtaque);
        TextView regDEDef = findViewById(R.id.tvPuntosDefensa);
        ImageView regDEImgC = findViewById(R.id.imFotoCarta);

        mRetrofit = RetrofitU.build();

        Button goBackP = findViewById(R.id.btnGoBack2);
        goBackP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalleCartasActivity.this, ListaCartasActivity.class);
                startActivity(intent);
            }
        });

        int position = getIntent().getIntExtra("position", 0);

        //Crear mapa en BD
        AppDatabase db = AppDatabase.getInstance(this);
        CartasRepository repository = db.cartitasRepository();
        Cartas cartita = repository.findCartaById(position);

        idCarta = position;
        regDENomC.setText(cartita.getNombre());
        regDEAtaq.setText(cartita.getPuntosAtaque()+"");
        regDEDef.setText(cartita.getPuntosDefensa()+"");
        Picasso.get().load(cartita.getFoto())
                .resize(300, 400) //tamaño específico
                .into(regDEImgC);

        Button goMaps = findViewById(R.id.btnGoMaps);
        goMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalleCartasActivity.this, MapsActivity.class);
                intent.putExtra("position", idCarta);
                startActivity(intent);
            }
        });
    }
}