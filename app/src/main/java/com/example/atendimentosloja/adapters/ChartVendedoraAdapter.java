package com.example.atendimentosloja.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atendimentosloja.R;

import java.util.List;

public class ChartVendedoraAdapter extends RecyclerView.Adapter<ChartVendedoraAdapter.ChartVendedoraViewHolder> {

    private List<VendedoraData> vendedoraDataList;
    private OnItemClickListener listener;

    public ChartVendedoraAdapter(List<VendedoraData> vendedoraDataList) {
        this.vendedoraDataList = vendedoraDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChartVendedoraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chart_indicadores, parent, false);
        return new ChartVendedoraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartVendedoraViewHolder holder, int position) {
        VendedoraData vendedoraData = vendedoraDataList.get(position);

        holder.tvNomeVendedora.setText(vendedoraData.getNome());
        holder.tvMediaTempo.setText(String.format("Média tempo/atendimento: %.2f", vendedoraData.getMediaTempo())); // Ajuste o formato conforme necessário

        holder.itemView.setOnClickListener(v -> listener.onItemClick(vendedoraData));

    }

    @Override
    public int getItemCount() {
        return vendedoraDataList.size();
    }

    public static class ChartVendedoraViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeVendedora;
        TextView tvMediaTempo;


        public ChartVendedoraViewHolder(View itemView) {
            super(itemView);
            tvNomeVendedora = itemView.findViewById(R.id.tvNomeVendedora);
            tvMediaTempo = itemView.findViewById(R.id.tvMediaTempo);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(VendedoraData vendedoraData);
    }
}
