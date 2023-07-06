package com.example.comupnaguilarchavez;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.comupnaguilarchavez.BD.AppDatabase;
import com.example.comupnaguilarchavez.Entities.Cartas;
import com.example.comupnaguilarchavez.Entities.ImageResponse;
import com.example.comupnaguilarchavez.Entities.ImageToSave;
import com.example.comupnaguilarchavez.Repositories.CartasRepository;
import com.example.comupnaguilarchavez.Services.CartasService;
import com.example.comupnaguilarchavez.Utilities.RetrofitU;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroCartasActivity extends AppCompatActivity {

    private static final String urlFotoApi= "https://demo-upn.bit2bittest.com/";
    private static final int OPEN_GALLERY_REQUEST = 1002;

    Retrofit retrofitP;
    Context context = this;

    private ImageView ivAvatar;
    private String urlCamara; //settear camara
    private LocationManager mlocationManager; //para mapas
    double latitud = 0;
    double longitud = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cartas);

        EditText regCNom = findViewById(R.id.etNombreC);
        EditText regCAtaq = findViewById(R.id.etpuntosAtaqueC);
        EditText regCDef = findViewById(R.id.etpuntosDefensaC);

        ivAvatar = findViewById(R.id.ivImagenC);

        Button btnGallery = findViewById(R.id.btnGaleria);
        Button btnRegistro = findViewById(R.id.btttnRegistro);
        Button btnGoingBack = findViewById(R.id.bttnGoingBack4);

        retrofitP = RetrofitU.build(); //settear mockapi

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                    obtenerCoordenadas(); //para cargar coodenadas
                }
                else {
                    String[] permissions = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, 2000);
                }
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre = regCNom.getText().toString();
                int ataque = Integer.parseInt(regCAtaq.getText().toString());
                int defensa = Integer.parseInt(regCDef.getText().toString());

                //Crear un service del repository
                AppDatabase database = AppDatabase.getInstance(context);
                CartasRepository pokeRepository = database.cartitasRepository();

                // Obtener el último ID registrado en la base de datos para crear uno nuevo
                int lastId = pokeRepository.getLastId();

                //Crear un nuevo pokemon
                Cartas cartas = new Cartas();
                //Llenar los datos
                cartas.setIdCartas(lastId + 1);
                cartas.setNombre(nombre);
                cartas.setPuntosAtaque(ataque);
                cartas.setPuntosDefensa(defensa);
                cartas.setFoto(urlCamara);
                cartas.setLatitud(latitud+"");
                cartas.setLongitud(longitud+"");
                cartas.setSynced(false);

                pokeRepository.create(cartas); //enviando pokemon a la BD

                //Ir al mostrar luego de registrar
                Intent intent =  new Intent(RegistroCartasActivity.this, ListaCartasActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnGoingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(RegistroCartasActivity.this, ListaDuelistasActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == OPEN_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close(); // close cursor

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ivAvatar.setImageBitmap(bitmap);
            //Convertir a base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String imgBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            ImageToSave imgB64 = new ImageToSave(imgBase64);
            enviarImagen(imgB64);
        }

    }
    private void enviarImagen (ImageToSave imgB64){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlFotoApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartasService service = retrofit.create(CartasService.class);
        Call<ImageResponse> call = service.subirImagen(imgB64);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if(response.isSuccessful()){
                    Log.i("MAIN_APP", "Si se subió");
                    Log.i("MAIN_APP", urlFotoApi  + response.body().getUrl());
                    urlCamara = urlFotoApi + response.body().getUrl();
                }
                else{
                    Log.i("MAIN_APP", "No se subió");
                }
            }
            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST);
    }

    // ALMACENAR COORDENADAS
    void obtenerCoordenadas(){
        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    Log.i("MAIN_APP", "Latitud" + latitud);
                    Log.i("MAIN_APP", "Longitud" + longitud);
                    mlocationManager.removeUpdates(this);
                }
            };
            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
        }
        else{
            String[] permissions = new String[] {Manifest.permission.ACCESS_FINE_LOCATION};
            Log.i("MAIN_APP", "No hay permisos pa esta webada");
            requestPermissions(permissions, 1000);
        }
    }
}