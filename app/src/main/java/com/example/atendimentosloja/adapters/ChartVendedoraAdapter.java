package com.example.atendimentosloja.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atendimentosloja.R;
import com.example.atendimentosloja.formatters.PercentFormatter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartVendedoraAdapter extends RecyclerView.Adapter<ChartVendedoraAdapter.ChartVendedoraViewHolder> {

    private List<VendedoraData> vendedoraDataList;
    private PieChart pieChart;
    private OnItemClickListener onItemClickListener;

    public ChartVendedoraAdapter(List<VendedoraData> vendedoraDataList) {
        this.vendedoraDataList = vendedoraDataList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(VendedoraData vendedoraData);
    }

    public void setPieChart(PieChart pieChart) {
        this.pieChart = pieChart;
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
        holder.tvMediaTempo.setText(String.format("%.0f", vendedoraData.getMediaTempo())); // Ajuste o formato conforme necessário
        //holder.itemView.setOnClickListener(v -> listener.onItemClick(vendedoraData));
        // Configurando o clique no item
        List<PieEntry> pieEntries = vendedoraData.getPieEntries();
        configurePieChart(holder.pieChart, pieEntries);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null){
                onItemClickListener.onItemClick(vendedoraData  );
            }
        });

    }

    private void configurePieChart(PieChart pieChart, List<PieEntry> pieEntries) {

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setValueTextSize(16f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawValues(true);
        dataSet.setDrawIcons(true);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawValues(true);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);

        // Configurando a legenda
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(15f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(12f);
        legend.setXEntrySpace(5f);
        legend.setYEntrySpace(5f);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(Color.BLACK);

    }

    @Override
    public int getItemCount() {
        return vendedoraDataList.size();
    }

    public static class ChartVendedoraViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeVendedora;
        TextView tvMediaTempo;
        PieChart pieChart; // Adicione essa linha




        public ChartVendedoraViewHolder(View itemView) {
            super(itemView);
            tvNomeVendedora = itemView.findViewById(R.id.tvNomeVendedora);
            tvMediaTempo = itemView.findViewById(R.id.tvMediaTempo);
            pieChart = itemView.findViewById(R.id.pieChart); // Certifique-se de que o ID corresponde ao seu layout


        }
    }

}
