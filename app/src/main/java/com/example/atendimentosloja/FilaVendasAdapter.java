package com.example.atendimentosloja;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilaVendasAdapter extends RecyclerView.Adapter<FilaVendasAdapter.ViewHolder> {

    private List<String> vendedoras;
    private AdapterView.OnItemClickListener onItemClickListener;

    public FilaVendasAdapter(List<String> vendedoras) {
        this.vendedoras = vendedoras;
    }

    public interface OnItemClickListener{
        void onItemClick(String nome);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item da RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendedora, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obter o nome da vendedora na posição atual e definir no TextView
        String vendedora = vendedoras.get(position);
        holder.nomeTextView.setText(vendedora);
    }

    @Override
    public int getItemCount() {
        // Retornar o número total de itens
        return vendedoras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontrar o TextView no layout do item
            nomeTextView = itemView.findViewById(R.id.text_name);
        }
    }
}
