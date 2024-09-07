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
import com.github.mikephil.charting.data.PieEntry;

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
        adapter.setPieChart(pieChart); // Define o PieChart no adaptador
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
                    List<PieEntry> pieEntries = getPieEntriesForVendedora(nome);
                    //Log.d("LogTeste", "Pientries: " + pieEntries);
                    vendedoraDataList.add(new VendedoraData(nome, media, pieEntries));
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

        return (atendimentos > 0) ? Math.round(media) : 0;
    }

    private List<PieEntry> getPieEntriesForVendedora(String nome) {
        List<PieEntry> pieEntries = new ArrayList<>();

        int totalAtendimentos = db.atendimentoDao().getCountAtendimentosForNomes(nome);
        int totalConversoes = db.atendimentoDao().getCountConversaoPorVendedora(nome);

        int totalNaoConvertido = totalAtendimentos - totalConversoes;
        int totalconvertido = totalConversoes;

        // calcular percentual nao convertido
        int percentNaoConvertido = (totalNaoConvertido * 100) / totalAtendimentos;
        // calcular percentual convertido
        int percentualConvertido = (totalconvertido * 100) / totalAtendimentos;


        int conversoes = percentualConvertido;
        int naoConversoes = percentNaoConvertido;


        pieEntries.add(new PieEntry(conversoes, "Conversões"));
        pieEntries.add(new PieEntry(naoConversoes, "Não"));

        return pieEntries;
    }


}