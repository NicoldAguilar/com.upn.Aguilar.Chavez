package com.example.comupnaguilarchavez.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comupnaguilarchavez.Entities.Duelista;
import com.example.comupnaguilarchavez.R;

import java.util.List;

public class DuelistaAdapter extends RecyclerView.Adapter {

    private List<Duelista> items;
    Context context;
    public DuelistaAdapter(List<Duelista> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NameViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == 1) {
            View view = inflater.inflate(R.layout.item_duelistas, parent, false);
            viewHolder = new NameViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_progressbar, parent, false);
            viewHolder = new NameViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Duelista mov = new Duelista();
        mov = items.get(position);

        if(mov == null) return;

        View view = holder.itemView;

        Log.i("MAIN_APP", items.get(position).getNombreDuelista()+"B");
        TextView tvNombreDuelista = view.findViewById(R.id.tvDuelistas);
        tvNombreDuelista.setText(items.get(position).getNombreDuelista()+".");

        Button btnRegisCards = view.findViewById(R.id.btnRegistrarCartas);
        btnRegisCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(context, RegistroCartasActivity.class);
                intent.putExtra("position", item.getId());
                context.startActivity(intent);

            }
        });

        Button btnShowCards = view.findViewById(R.id.btnShowCards);
        btnShowCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Duelista item = items.get(position);
        return item == null ? 0 : 1;
    }

    public class NameViewHolder extends RecyclerView.ViewHolder {

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setDuelista(List<Duelista> duelista) {
        this.items = duelista;
    }
}
