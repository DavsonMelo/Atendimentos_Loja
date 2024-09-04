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
import com.example.atendimentosloja.database.MyDatabase;
import com.example.atendimentosloja.entity.Atendimento;
import com.example.atendimentosloja.entity.Vendedora;
import com.example.atendimentosloja.utils.SpacingItemDecoration;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndicadoresFragment extends Fragment {
    private RecyclerView recyclerViewChart;
    private TextView tvNomeVendedora, tvMediaTempo;
    private PieChart pieChart;
    private ChartVendedoraAdapter adapter;
    private MyDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indicadores, container, false);

        // Inicialização do banco de dados
        db = MyDatabase.getDatabase(requireContext());

        // Configuração da UI
        configurarUI(view);

        // Observando mudanças na lista de vendedoras
        observarVendedoras();

        calculaMediaEConversao();

        return view;
    }

    private void observarVendedoras() {
        db.vendedoraDao().getAll().observe(getViewLifecycleOwner(), (List<Vendedora> vendedoras) -> {
            adapter.updateData(vendedoras);
        });
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

        adapter = new ChartVendedoraAdapter(new ArrayList<>(), vendedora -> {
            tvNomeVendedora.setText(vendedora.getNome());
        });
        recyclerViewChart.setAdapter(adapter);

    }

    private void calculaMediaEConversao() {
        db.vendedoraDao().getAll().observe(getViewLifecycleOwner(), vendedoras -> {
            List<String> nomes = new ArrayList<>();
            for (Vendedora vendedora : vendedoras) {
                nomes.add(vendedora.getNome());
            }

            // Agora que você tem a lista de nomes, faça chamadas para obter dados
            for (String nome : nomes) {
                // Obtém total de atendimentos para um nome específico
                db.atendimentoDao().getCountAtendimentosForNomes(Collections.singletonList(nome))
                        .observe(getViewLifecycleOwner(), totalAtendimento -> {
                            Log.d("LogTeste", "Nome: " + nome + ", Total de atendimentos: " + totalAtendimento);
                        });

                // Obtém o total de tempo de atendimento para um nome específico
                db.atendimentoDao().getTotalTempoAtendimentoForNomes(Collections.singletonList(nome))
                        .observe(getViewLifecycleOwner(), totalTempo -> {
                            Log.d("LogTeste", "Nome: " + nome + ", Tempo Total: " + totalTempo);
                        });

                // Obtém o número de conversões para um nome específico
                db.atendimentoDao().getCountConversaoPorVendedora(nome)
                        .observe(getViewLifecycleOwner(), totalConversoes -> {
                            Log.d("LogTeste", "Nome: " + nome + ", Conversões: " + totalConversoes);
                        });
            }
        });
    }
}