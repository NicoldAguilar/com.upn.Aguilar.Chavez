 package com.example.comupnaguilarchavez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.comupnaguilarchavez.Adapters.CartasAdapter;
import com.example.comupnaguilarchavez.BD.AppDatabase;
import com.example.comupnaguilarchavez.Entities.Cartas;
import com.example.comupnaguilarchavez.Repositories.CartasRepository;
import com.example.comupnaguilarchavez.Services.CartasService;
import com.example.comupnaguilarchavez.Utilities.RetrofitU;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

 public class ListaCartasActivity extends AppCompatActivity {

     RecyclerView mRvMLista;
     boolean mIsLoading = false;
     int mPage = 1;
     List<Cartas> mdata = new ArrayList<>();
     CartasAdapter mAdapter = new CartasAdapter(mdata, this);
     Retrofit mRetrofit;
     Context context = this;
     String currentFilter = "";
     int idDuelista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cartas);

        idDuelista = getIntent().getIntExtra("position", 0); //RECIVI EL POKEMON EXACTO
        mRetrofit = RetrofitU.build();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvMLista =  findViewById(R.id.rvListaSimple3);
        mRvMLista.setLayoutManager(layoutManager);
        mRvMLista.setAdapter(mAdapter);

        Button btnActualizar = findViewById(R.id.btnActualizar3);
        Button btnGoBack = findViewById(R.id.btnGoBack3);
        Button btnSyncro = findViewById(R.id.btnSyncro2);



        mRvMLista.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!mIsLoading) {

                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mdata.size() - 1) {
                        mPage++;
                        loadMore(mPage);
                    }
                }

            }
        });

        //Manda la lista desde base de datos para mostrarse
        AppDatabase db = AppDatabase.getInstance(context);
        CartasRepository repository = db.cartitasRepository();
        List<Cartas> cartas = repository.getAllCartas(); //mandamos la lista de los pokemones
        Log.i("MAIN_APP: DB", new Gson().toJson(cartas));
        Log.i("MAIN_APP", idDuelista+"");
        mAdapter.setCartitas(cartas);
        mAdapter.notifyDataSetChanged();

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToWebService(currentFilter, mPage);
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(ListaCartasActivity.this, ListaDuelistasActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSyncro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<cartas.size(); i++){
                    if (!cartas.get(i).isSynced()){

                        Cartas aux = new Cartas();
                        aux.setNombre(cartas.get(i).getNombre());

                        aux.setSynced(true);

                        mRetrofit = RetrofitU.build();
                        CartasService service = mRetrofit.create(CartasService.class);
                        Call<Cartas> call = service.create(aux);

                        call.enqueue(new Callback<Cartas>() {
                            @Override
                            public void onResponse(Call<Cartas> call, Response<Cartas> response) {
                                Intent intent =  new Intent(ListaCartasActivity.this, ListaCartasActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Cartas> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });

        mRvMLista.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!mIsLoading) {

                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mdata.size() - 1) {
                        mPage++;
                        loadMore(mPage);
                    }
                }

            }
        });

    }

     private void uploadToWebService(String filter, int nextPage) {

         AppDatabase db = AppDatabase.getInstance(context);
         db.clearAllTables();

         CartasService service = mRetrofit.create(CartasService.class);
         service.getAllCartas(20, nextPage).enqueue(new Callback<List<Cartas>>() {
             @Override
             public void onResponse(Call<List<Cartas>> call, Response<List<Cartas>> response) {
                 if (response.isSuccessful() && response.body() != null) {
                     // Inserta los datos en la base de datos
                     AppDatabase db = AppDatabase.getInstance(ListaCartasActivity.this);
                     CartasRepository repository = db.cartitasRepository();
                     repository.insertAll(response.body());

                     // Actualiza los datos en el adaptador y notifica los cambios
                     List<Cartas> newData = repository.getAllCartas();
                     mAdapter.setCartitas(newData);
                     mAdapter.notifyDataSetChanged();
                 }
             }
             @Override
             public void onFailure(Call<List<Cartas>> call, Throwable t) {
                 // Maneja el error de la llamada al servicio MockAPI
             }
         });
     }


     private void loadMore(int nextPage) {
         mIsLoading = true;

         mdata.add(null);
         mAdapter.notifyItemInserted(mdata.size() - 1);

         CartasService service = mRetrofit.create(CartasService.class);
         Log.i("MAIN_APP  Page:", String.valueOf(nextPage));
         service.getAllCartas(100, nextPage).enqueue(new Callback<List<Cartas>>() { // Cambia el número de registros por página según tus necesidades
             @Override
             public void onResponse(Call<List<Cartas>> call, Response<List<Cartas>> response) {
                 if (response.isSuccessful() && response.body() != null) {
                     mdata.remove(mdata.size() - 1);
                     mAdapter.notifyItemRemoved(mdata.size() - 1);

                     mdata.addAll(response.body());
                     mAdapter.notifyDataSetChanged();
                     mIsLoading = false;

                     // Si hay más registros disponibles, cargar la siguiente página
                     if (response.body().size() >= 100) { // Cambia el número de registros por página según tus necesidades
                         loadMore(nextPage + 1);
                     }
                 }
             }
             @Override
             public void onFailure(Call<List<Cartas>> call, Throwable t) {
                 // Manejar error de la llamada al servicio MockAPI
             }
         });
     }
}