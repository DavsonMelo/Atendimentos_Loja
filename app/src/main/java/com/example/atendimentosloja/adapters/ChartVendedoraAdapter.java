package com.example.atendimentosloja.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.atendimentosloja.R;
import com.example.atendimentosloja.entity.Vendedora;
import java.util.List;

public class ChartVendedoraAdapter extends RecyclerView.Adapter<ChartVendedoraAdapter.ChartVendedoraViewHolder> {

    private List<Vendedora> vendedoraList;
    private OnItemClickListener listener;

    // Interface para o clique no item
    public interface OnItemClickListener {
        void onItemClick(Vendedora vendedora);
    }

    // Construtor
    public ChartVendedoraAdapter(List<Vendedora> vendedoraList, OnItemClickListener listener) {
        this.vendedoraList = vendedoraList;
        this.listener = listener;
    }

    @Override
    public ChartVendedoraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chart_indicadores, parent, false); // Use o novo layout
        return new ChartVendedoraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChartVendedoraViewHolder holder, int position) {
        Vendedora vendedora = vendedoraList.get(position);
        holder.tvNomeVendedora.setText(vendedora.getNome());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(vendedora));
    }

    @Override
    public int getItemCount() {
        return vendedoraList.size();
    }

    public void updateData(List<Vendedora> newData) {
        this.vendedoraList = newData;
        notifyDataSetChanged();
    }

    public static class ChartVendedoraViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeVendedora;

        public ChartVendedoraViewHolder(View itemView) {
            super(itemView);
            tvNomeVendedora = itemView.findViewById(R.id.tvNomeVendedora); // Use o ID correspondente do novo layout
        }
    }
}
