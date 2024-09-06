package com.example.atendimentosloja.adapters;

import android.graphics.Color;
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
        holder.tvMediaTempo.setText(String.format("%.2f", vendedoraData.getMediaTempo())); // Ajuste o formato conforme necessário
        holder.itemView.setOnClickListener(v -> listener.onItemClick(vendedoraData));

        // Configura o PieChart com valores estáticos
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(40f, "Conversão"));
        pieEntries.add(new PieEntry(60f, "Não"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        // Ajusta os valores dentro das fatias para sem casa decimal e com o simbolo %
        dataSet.setValueFormatter(new PercentFormatter());
        // Ajuste o tamanho do texto dos valores
        dataSet.setValueTextSize(15f);
        // Ajuste da cor dos valores no grafico pizza
        dataSet.setValueTextColor(Color.BLUE);
        // nao sei
        dataSet.setDrawValues(true);
        // nao sei
        dataSet.setDrawIcons(true);
        // ajusta as cores do grafico pizza
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        // nao sei
        dataSet.setDrawValues(true);
        // seta as configurações no grafico pizza
        PieData pieData = new PieData(dataSet);

        // Configurando a legenda
        Legend legend = holder.pieChart.getLegend();
        legend.setEnabled(true);  // Habilita a exibição da legenda
        legend.setTextSize(18f);  // Define o tamanho do texto da legenda
        legend.setForm(Legend.LegendForm.SQUARE);  // Define o formato dos ícones (pode ser SQUARE, CIRCLE, ou LINE)
        legend.setFormSize(12f);  // Aumenta o tamanho dos ícones (quadradinhos)
        legend.setXEntrySpace(5f);  // Define o espaço entre as entradas da legenda
        legend.setYEntrySpace(5f);  // Define o espaço vertical entre as entradas
        legend.setWordWrapEnabled(true);  // Permite que a legenda seja quebrada se não couber na tela
        legend.setTextColor(Color.WHITE);

        // Passa os valores para o meu pieChart
        holder.pieChart.setData(pieData);
        // Atualiza o gráfico
        holder.pieChart.invalidate();
        // Ajusta o grafico pizza para ser solido. Sem "buraco" no meio
        holder.pieChart.setDrawHoleEnabled(false);
        // Não sei
        holder.pieChart.setDrawEntryLabels(false);
        holder.pieChart.getDescription().setEnabled(true);


    }

    @Override
    public int getItemCount() {
        return vendedoraDataList.size();
    }

    public static class ChartVendedoraViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeVendedora;
        TextView tvMediaTempo;
        PieChart pieChart;



        public ChartVendedoraViewHolder(View itemView) {
            super(itemView);
            tvNomeVendedora = itemView.findViewById(R.id.tvNomeVendedora);
            tvMediaTempo = itemView.findViewById(R.id.tvMediaTempo);
            pieChart = itemView.findViewById(R.id.pieChart);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(VendedoraData vendedoraData);
    }
}
