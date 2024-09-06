package com.example.atendimentosloja.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.atendimentosloja.R;
import com.example.atendimentosloja.adapters.ChartVendedoraAdapter;
import com.example.atendimentosloja.adapters.VendedoraAdapter;
import com.example.atendimentosloja.adapters.VendedoraData;
import com.example.atendimentosloja.database.MyDatabase;
import com.example.atendimentosloja.entity.Atendimento;
import com.example.atendimentosloja.entity.Vendedora;
import com.example.atendimentosloja.utils.SpacingItemDecoration;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IndicadoresFragment extends Fragment {
    private RecyclerView recyclerViewChart;
    private TextView tvNomeVendedora, tvMediaTempo;
    private PieChart pieChart;
    private ChartVendedoraAdapter adapter;
    private MyDatabase db;
    private List<VendedoraData> vendedoraDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indicadores, container, false);

        // Inicialização do banco de dados
        db = MyDatabase.getDatabase(requireContext());

        configurarUI(view);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        carregarDadosVendedoras();
    }

    private void configurarUI(View view) {
        recyclerViewChart = view.findViewById(R.id.recyclerViewChart);
        tvNomeVendedora = view.findViewById(R.id.tvNomeVendedora);
        tvMediaTempo = view.findViewById(R.id.tvMediaTempo);
        pieChart = view.findViewById(R.id.pieChart);

        // Configuração do RecyclerView
        recyclerViewChart.setLayoutManager(new LinearLayoutManager(getContext()));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerViewChart.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        carregarDadosVendedoras();

        adapter = new ChartVendedoraAdapter(vendedoraDataList);
        recyclerViewChart.setAdapter(adapter);

    }

    private void carregarDadosVendedoras() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            List<Vendedora> vendedoras = db.vendedoraDao().getAllVendedoras();
            vendedoraDataList.clear(); // Limpa a lista existente antes de adicionar os novos dados

                for (Vendedora vendedora : vendedoras){
                    String nome = vendedora.getNome();
                    float media = calculaMedia(nome);
                    vendedoraDataList.add(new VendedoraData(nome, media));
                }

                // Atualiza a lista na thread principal
                getActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
        });
    }

    private float calculaMedia(String nome) {
        Integer atendimentos = db.atendimentoDao().getCountAtendimentosForNomes(nome);
        Integer tempoAtendimentos = db.atendimentoDao().getTotalTempoAtendimentoForNomes(nome);
        tempoAtendimentos = (tempoAtendimentos != null) ? tempoAtendimentos : 0;

        float media = (atendimentos > 0) ? (float) tempoAtendimentos / atendimentos : 0;

        return (atendimentos > 0) ? (float) tempoAtendimentos / atendimentos : 0;    }


}