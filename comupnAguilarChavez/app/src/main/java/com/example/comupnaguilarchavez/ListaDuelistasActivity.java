package com.example.comupnaguilarchavez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.comupnaguilarchavez.Adapters.DuelistaAdapter;
import com.example.comupnaguilarchavez.BD.AppDatabase;
import com.example.comupnaguilarchavez.Entities.Duelista;
import com.example.comupnaguilarchavez.Repositories.DuelistaRepository;
import com.example.comupnaguilarchavez.Services.DuelistaService;
import com.example.comupnaguilarchavez.Utilities.RetrofitU;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListaDuelistasActivity extends AppCompatActivity {

    RecyclerView mRvLista;
    boolean mIsLoading = false;
    int mPage = 1;
    List<Duelista> mdata = new ArrayList<>();
    DuelistaAdapter mAdapter = new DuelistaAdapter(mdata, this);
    Retrofit mRetrofit;
    Context context = this;
    String currentFilter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_duelistas);

        mRetrofit = RetrofitU.build();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvLista =  findViewById(R.id.rvListaSimple2);
        mRvLista.setLayoutManager(layoutManager);
        mRvLista.setAdapter(mAdapter);

        Button btnActualizar = findViewById(R.id.btnActualizar2);
        Button btnGoBack = findViewById(R.id.btnGoBack2);
        Button btnSyncro = findViewById(R.id.btnSyncro);

        mRvLista.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        DuelistaRepository repository = db.duelistaRepository();
        List<Duelista> duel = repository.getAllUser();
        Log.i("MAIN_APP: DB", new Gson().toJson(duel));
        mAdapter.setDuelista(duel);
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
                Intent intent =  new Intent(ListaDuelistasActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSyncro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<duel.size(); i++){
                    if (!duel.get(i).isSyncro()){

                        Duelista aux = new Duelista();
                        aux.setNombreDuelista(duel.get(i).getNombreDuelista());
                        aux.setSyncro(true);

                        mRetrofit = RetrofitU.build();
                        DuelistaService service = mRetrofit.create(DuelistaService.class);
                        Call<Duelista> call = service.create(aux);

                        call.enqueue(new Callback<Duelista>() {
                            @Override
                            public void onResponse(Call<Duelista> call, Response<Duelista> response) {
                                Intent intent =  new Intent(ListaDuelistasActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Duelista> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });
        mRvLista.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void uploadToWebService(String filter, int nextPage) {

        AppDatabase db = AppDatabase.getInstance(context);
        db.clearAllTables();

        DuelistaService service = mRetrofit.create(DuelistaService.class);
        service.getAllUser(20, nextPage).enqueue(new Callback<List<Duelista>>() {
            @Override
            public void onResponse(Call<List<Duelista>> call, Response<List<Duelista>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Inserta los datos en la base de datos
                    AppDatabase db = AppDatabase.getInstance(ListaDuelistasActivity.this);
                    DuelistaRepository repository = db.duelistaRepository();
                    repository.insertAll(response.body());

                    // Actualiza los datos en el adaptador y notifica los cambios
                    List<Duelista> newData = repository.getAllUser();
                    mAdapter.setDuelista(newData);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Duelista>> call, Throwable t) {
                // Maneja el error de la llamada al servicio MockAPI
            }
        });
    }


    private void loadMore(int nextPage) {
        mIsLoading = true;

        mdata.add(null);
        mAdapter.notifyItemInserted(mdata.size() - 1);

        DuelistaService service = mRetrofit.create(DuelistaService.class);
        Log.i("MAIN_APP  Page:", String.valueOf(nextPage));
        service.getAllUser(100, nextPage).enqueue(new Callback<List<Duelista>>() { // Cambia el número de registros por página según tus necesidades
            @Override
            public void onResponse(Call<List<Duelista>> call, Response<List<Duelista>> response) {
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
            public void onFailure(Call<List<Duelista>> call, Throwable t) {
                // Manejar error de la llamada al servicio MockAPI
            }
        });
    }
}