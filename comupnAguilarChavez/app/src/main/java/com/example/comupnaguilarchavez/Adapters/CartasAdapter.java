package com.example.comupnaguilarchavez.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comupnaguilarchavez.DetalleCartasActivity;
import com.example.comupnaguilarchavez.Entities.Cartas;
import com.example.comupnaguilarchavez.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartasAdapter extends RecyclerView.Adapter {

    private List<Cartas> items;
    Context context;

    public CartasAdapter(List<Cartas> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartasAdapter.NameViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == 1) {
            View view = inflater.inflate(R.layout.item_cartas, parent, false);
            viewHolder = new CartasAdapter.NameViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_progressbar, parent, false);
            viewHolder = new CartasAdapter.NameViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Cartas item = items.get(position);

        if(item == null) return;

        View view = holder.itemView;

        TextView tvNameC = view.findViewById(R.id.tvNameCard);
        TextView tvAtaqueC = view.findViewById(R.id.tvAtaqueCard);
        TextView tvDefensaC = view.findViewById(R.id.tvDefensaCard);
        ImageView imageView = view.findViewById(R.id.imageView);

        tvNameC.setText(item.nombre);
        tvAtaqueC.setText(item.puntosAtaque);
        tvDefensaC.setText(item.puntosDefensa);

        Picasso.get().load(item.getFoto())
                .resize(300, 400) //tamaño específico
                .into(imageView);

        //evita el uso de un boton "ver detalle"
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context, DetalleCartasActivity.class);
                intent.putExtra("position", item.getIdCartas());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public int getItemViewType(int position) {
        Cartas item = items.get(position);
        return item == null ? 0 : 1;
    }

    public class NameViewHolder extends RecyclerView.ViewHolder {
        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setCartitas(List<Cartas> cartitas) {
        this.items = cartitas;
    }
}
