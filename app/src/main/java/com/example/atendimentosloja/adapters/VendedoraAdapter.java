package com.example.atendimentosloja.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atendimentosloja.R;
import com.example.atendimentosloja.entity.Vendedora;
import java.util.List;

public class VendedoraAdapter extends RecyclerView.Adapter<VendedoraAdapter.VendedoraViewHolder> {

    private List<Vendedora> vendedoraList;
    private OnItemClickListener listener;

    // Interface para o clique no item
    public interface OnItemClickListener {
        void onItemClick(Vendedora vendedora);
    }
    // Construtor
    public VendedoraAdapter(List<Vendedora> vendedoraList, OnItemClickListener listener) {
        this.vendedoraList = vendedoraList;
        this.listener = listener;
    }

    // Construtor
    public VendedoraAdapter(List<Vendedora> vendedoraList) {
        this.vendedoraList = vendedoraList;
    }

    @Override
    public VendedoraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);
        return new VendedoraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendedoraViewHolder holder, int position) {
        Vendedora vendedora = vendedoraList.get(position);
        holder.text_name.setText(vendedora.getNome());

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

    public static class VendedoraViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;

        public VendedoraViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
        }
    }
}

