package com.example.atendimentosloja.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.atendimentosloja.ButtonUtils;
import com.example.atendimentosloja.PendenciaUtils;
import com.example.atendimentosloja.R;
import com.example.atendimentosloja.Utils;
import com.example.atendimentosloja.dao.AtendimentoDao;
import com.example.atendimentosloja.database.MyDatabase;
import com.example.atendimentosloja.entity.Atendimento;
import com.example.atendimentosloja.entity.Vendedora;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;

public class FilaFragment extends Fragment {

    // Constantes
    private static final String PREFS_NAME = "VendedorasPrefs";
    private static final String FILA_KEY = "fila";
    private static final String PENDENTE_KEY = "pendente";
    private static final String PLACEHOLDER_TEXT = "Selecione seu nome aqui.";

    // Views
    private Spinner spinner;

    public FilaFragment() {
        // Construtor vazio necessário
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fila, container, false);

        // Configurações iniciais
        setupSpinner(view);
        criarBotoes(view);
        criarBotoesPendentes(view);
        //calculaTempoAtendimento();

        // Limpar fila e pendencia - manter comentado
        //funcaoLimpaTudo(view);

        return view;

    }

    private void calculaTempoAtendimento() {
        // Criação do banco de dados e DAO
        MyDatabase db = Room.databaseBuilder(getContext(), MyDatabase.class, "MeuBD").build();
        AtendimentoDao atendimentoDao = db.atendimentoDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Atendimento> atendimentos = atendimentoDao.getAtendimentosPendentes();
            // consertar primeiro como as datas sao armazenadas.

        });

    }

    private void funcaoLimpaTudo(View view) {
        LinearLayout mainView = view.findViewById(R.id.mainView);
        LinearLayout mainViewPendencias = view.findViewById(R.id.mainViewPendencias);
        mainView.removeAllViews();
        mainViewPendencias.removeAllViews();
        List<String> fila = carregarVendedoras();
        List<String> pendencia = carregarPendentes();
        fila.clear();
        pendencia.clear();
        salvarFila(fila);
        salvarPendente(pendencia);
        Log.d("LogTeste", "limpaTudo fila: "+fila);
        Log.d("LogTeste", "limpa tudo pendencia: "+pendencia);
    }

    private void setupSpinner(View view) {
        spinner = view.findViewById(R.id.spinner);

        // Configuração do banco de dados para carregar nomes de vendedoras
        MyDatabase db = MyDatabase.getDatabase(requireContext());
        db.vendedoraDao().getAll().observe(getViewLifecycleOwner(), vendedoras -> {
            List<String> nomes = new ArrayList<>();
            for (Vendedora vendedora : vendedoras) {
                nomes.add(vendedora.getNome());
            }
            nomes.add(0, PLACEHOLDER_TEXT);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.simple_spinner_item, nomes);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        spinner.setSelection(0);  // Voltar para o placeholder
                    } else {
                        String selectedVendedora = nomes.get(position);
                        adicionarVendedora(selectedVendedora);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                spinner.setSelection(0);
                            }
                        }, 3000);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Se nada foi selecionado
                }
            });
        });
    }

    private List<String> carregarVendedoras() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String filaJson = prefs.getString(FILA_KEY, "[]");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(filaJson, type);
    }

    private List<String> carregarPendentes() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String pendenteJson = prefs.getString(PENDENTE_KEY, "[]");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(pendenteJson, type);
    }

    private void salvarFila(List<String> fila) {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String filaJson = new Gson().toJson(fila);
        editor.putString(FILA_KEY, filaJson);
        editor.apply();
    }

    private void salvarPendente(List<String> pendente) {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String pendenteJson = new Gson().toJson(pendente);
        editor.putString(PENDENTE_KEY, pendenteJson);
        editor.apply();
    }

    private void adicionarVendedora(String nome) {
        List<String> fila = carregarVendedoras();
        if (!fila.contains(nome)) {
            fila.add(nome);
            salvarFila(fila);
            criarBotoes(requireView());
            Utils.showToast(this, "Vendedora adicionada à fila!");
        } else {
            Utils.showToast(this, "Esta vendedora já foi adicionada!");
        }
    }

    private void criarBotoes(View view) {
        LinearLayout mainView = view.findViewById(R.id.mainView);
        mainView.removeAllViews();

       List<String> fila = carregarVendedoras();
        if (fila != null && !fila.isEmpty()) {
            for (String nome : fila) {
                Button button = ButtonUtils.createCustomButton(getContext(), nome, fila);
                mainView.addView(button);

                button.setOnClickListener(view1 -> {
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.custom_dialog_clicked_vendedora);
                    Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.
                            WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);

                    Button btn_atender = dialog.findViewById(R.id.btnDialogAtender);
                    Button btn_sair = dialog.findViewById(R.id.btnDialogSair);
                    Button btn_concluir = dialog.findViewById(R.id.btnDialogConcluir);

                    btn_atender.setEnabled(false);
                    btn_atender.setAlpha(0.5f);
                    btn_concluir.setEnabled(false);
                    btn_concluir.setAlpha(0.5f);

                    if (fila.indexOf(nome) == 0) {
                        PendenciaUtils.checkPendencias(requireContext(), nome, hasPendencia -> {
                            if (hasPendencia) {
                                btn_concluir.setEnabled(true);
                                btn_concluir.setAlpha(1.0f);
                            } else {
                                btn_atender.setEnabled(true);
                                btn_atender.setAlpha(1.0f);
                            }
                        });
                    } else if (fila.indexOf(nome) != 0) {
                        PendenciaUtils.checkPendencias(requireContext(), nome, hasPendencia -> {
                            if (hasPendencia) {
                                btn_concluir.setEnabled(true);
                                btn_concluir.setAlpha(1.0f);
                            }
                        });
                    }

                    btn_sair.setOnClickListener(sairView -> {
                        fila.remove(nome);
                        salvarFila(fila);
                        carregarVendedoras();
                        criarBotoes(view);
                        dialog.dismiss();
                    });

                    btn_atender.setOnClickListener(atenderView -> {
                        // captura a data atual e coloca na string datetimeinicio
                            long currentTimeMillis = System.currentTimeMillis();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                            String datetimeinicio = dateFormat.format(new Date(currentTimeMillis));
                        // Pega o nome que clicou em atender e coloca na variavel string vendedora
                        String vendedora = fila.get(0);
                        // Cria uma nova instancia de Atendimento
                        Atendimento atendimento = new Atendimento();
                        // Seta os valores capturados ao clicar no botão atender
                        atendimento.setDateInicio(datetimeinicio);
                        atendimento.setDateFim(null);
                        atendimento.setNomeAtendimento(vendedora);
                        atendimento.setConversao(false);
                        atendimento.setFeed(null);
                        atendimento.setBoleta(null);
                        // instancia o Banco de dados
                        MyDatabase db = MyDatabase.getDatabase(requireContext());
                        AtendimentoDao atendimentoDao = db.atendimentoDao();
                        // insere um novo registro de aterndimento na tab_atendimentos
                        new Thread(() -> {
                            atendimentoDao.insert(atendimento);
                        }).start();
                        // fecha o dialogo e muda a vendedora para a lista pendencias
                        dialog.dismiss();
                        trocaPosicao(view);
                        criarBotoesPendentes(view);
                    });
                    btn_concluir.setOnClickListener(concluirView -> {

                        dialog.dismiss();

                        Dialog dialogConcluir = new Dialog(getContext());
                        dialogConcluir.setContentView(R.layout.custom_dialog_clicked_concluir);
                        Objects.requireNonNull(dialogConcluir.getWindow()).setLayout(ViewGroup.LayoutParams.
                                MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialogConcluir.setCancelable(true);

                        dialogConcluir.show();

                        Button btn_salvar = dialogConcluir.findViewById(R.id.btn_salvar_conclusao);
                        Button btn_cancelar = dialogConcluir.findViewById(R.id.btn_cancelar_conclusao);
                        CheckBox checkBox = dialogConcluir.findViewById(R.id.chk_conversao);
                        EditText edt_Boleta = dialogConcluir.findViewById(R.id.edt_boleta);
                        EditText edt_feedback = dialogConcluir.findViewById(R.id.edt_feedback);

                        btn_cancelar.setOnClickListener(cancelarConclusaoView -> {
                            dialogConcluir.dismiss();
                        });

                        btn_salvar.setOnClickListener(salvarConclusaoView -> {

                            long currentTimeMillis = System.currentTimeMillis();
                            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.getDefault());
                            String datetimefim = dateFormat.format(new java.util.Date(currentTimeMillis));
                            String nomeConclusao = nome;

                            boolean check = checkBox.isChecked();
                            String boleta = edt_Boleta.getText().toString().trim();
                            String feed = edt_feedback.getText().toString();

                            // Se check false e feed vazio - digitar feedback
                            if (!check && feed.isEmpty()) {
                                requireActivity().runOnUiThread(() -> {
                                    Utils.showToast(this, "Se não converteu, deixe um feedback, por favor!");
                                });
                                // Se check true e boleta vazio - digitar boleta
                            } else if (check && boleta.isEmpty()) {
                                requireActivity().runOnUiThread(() -> {
                                    Utils.showToast(this, "Se converteu, informe a BOLETA!");
                                });
                            } else if (!boleta.isEmpty() && !check) {
                                requireActivity().runOnUiThread(() -> {
                                    Utils.showToast(this, "Marque a caixa de CONVERTIDO!");
                                });

                            } else {
                                PendenciaUtils.checkPendencias(requireContext(), nomeConclusao, hasPendencia -> {
                                    if (hasPendencia) {

                                        MyDatabase db = Room.databaseBuilder(getContext(), MyDatabase.class, "MeuBD").build();
                                        AtendimentoDao atendimentoDao = db.atendimentoDao();

                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            Atendimento atendimentoExistente = atendimentoDao.findByName(nomeConclusao);

                                            if (atendimentoExistente != null) {
                                                atendimentoExistente.setDateFim(datetimefim);
                                                atendimentoExistente.setConversao(check);

                                                if (!boleta.isEmpty()) {
                                                    try {
                                                        atendimentoExistente.setBoleta(Integer.parseInt(boleta));
                                                    } catch (NumberFormatException e) {
                                                        atendimentoExistente.setBoleta(0);
                                                        Log.e("LogUpdate", "Erro na conversão do campo boleta: " + e.getMessage());
                                                    }
                                                } else {
                                                    atendimentoExistente.setBoleta(0);
                                                }

                                                atendimentoExistente.setFeed(feed);
                                                atendimentoDao.update(atendimentoExistente);

                                                requireActivity().runOnUiThread(() -> {
                                                    dialogConcluir.dismiss();
                                                    criarBotoes(view);
                                                });
                                            } else {
                                                // Lidar com o caso onde o atendimento não é encontrado
                                                requireActivity().runOnUiThread(() -> {
                                                    // Você pode adicionar um log ou mostrar uma mensagem ao usuário
                                                    Log.d("LogUpdate", "Atendimento não encontrado para o nome: " + nomeConclusao);
                                                    // Opcionalmente, você pode mostrar uma mensagem ao usuário aqui
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    });
                    dialog.show();
                });
            }
        } else {
            Utils.showToast(this, "Fila vazia!");
        }

    }

    private void trocaPosicao(View view) {
        // instancia as duas views
        LinearLayout mainView = view.findViewById(R.id.mainView);
        // limpa a a view
        mainView.removeAllViews();

        List<String> fila = carregarVendedoras();
        List<String> pendencia = carregarPendentes();

        String firstItem = fila.remove(0);
        pendencia.add(firstItem);
        salvarFila(fila);
        salvarPendente(pendencia);
        //criarBotoes(view);

        for (String nome : fila) {
            Button button = ButtonUtils.createCustomButton(requireContext(), nome, fila);
            mainView.addView(button);
            criarBotoes(view);
        }
    }

    private void criarBotoesPendentes(View view){
        LinearLayout mainViewPendencias = view.findViewById(R.id.mainViewPendencias);
        mainViewPendencias.removeAllViews();

        List<String> pendentes = carregarPendentes();
        if (pendentes != null && !pendentes.isEmpty()){
            for (String nome : pendentes){
                Button button = ButtonUtils.createCustomButton(getContext(), nome, pendentes);
                mainViewPendencias.addView(button);

                button.setOnClickListener(view1 -> {
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.custom_dialog_clicked_vendedora);
                    Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.
                            WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);

                    Button btn_atender = dialog.findViewById(R.id.btnDialogAtender);
                    Button btn_sair = dialog.findViewById(R.id.btnDialogSair);
                    Button btn_concluir = dialog.findViewById(R.id.btnDialogConcluir);

                    btn_atender.setEnabled(false);
                    btn_atender.setAlpha(0.5f);
                    btn_sair.setEnabled(false);
                    btn_sair.setAlpha(0.5f);

                    PendenciaUtils.checkPendencias(requireContext(), nome, hasPendencia -> {
                        if (!hasPendencia){
                            btn_concluir.setEnabled(false);
                            btn_concluir.setAlpha(0.5f);
                        }
                    });

                    btn_concluir.setOnClickListener(concluirView -> {
                        dialog.dismiss();

                        Dialog dialogConcluir = new Dialog(getContext());
                        dialogConcluir.setContentView(R.layout.custom_dialog_clicked_concluir);
                        Objects.requireNonNull(dialogConcluir.getWindow()).setLayout(ViewGroup.LayoutParams.
                                MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialogConcluir.setCancelable(true);

                        dialogConcluir.show();

                        Button btn_salvar = dialogConcluir.findViewById(R.id.btn_salvar_conclusao);
                        Button btn_cancelar = dialogConcluir.findViewById(R.id.btn_cancelar_conclusao);
                        CheckBox checkBox = dialogConcluir.findViewById(R.id.chk_conversao);
                        EditText edt_Boleta = dialogConcluir.findViewById(R.id.edt_boleta);
                        EditText edt_feedback = dialogConcluir.findViewById(R.id.edt_feedback);

                        btn_cancelar.setOnClickListener(cancelarConclusaoView -> {
                            dialogConcluir.dismiss();
                        });

                        btn_salvar.setOnClickListener(salvarConclusaoView -> {

                            long currentTimeMillis = System.currentTimeMillis();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                            String datetimefim = dateFormat.format(new Date(currentTimeMillis));
                            String nomeConclusao = nome;

                            boolean check = checkBox.isChecked();
                            String boleta = edt_Boleta.getText().toString().trim();
                            String feed = edt_feedback.getText().toString();

                            // Se check false e feed vazio - digitar feedback
                            if (!check && feed.isEmpty()) {
                                requireActivity().runOnUiThread(() -> {
                                    Utils.showToast(this, "Se não converteu, deixe um feedback, por favor!");
                                });
                                // Se check true e boleta vazio - digitar boleta
                            } else if (check && boleta.isEmpty()) {
                                requireActivity().runOnUiThread(() -> {
                                    Utils.showToast(this, "Se converteu, informe a BOLETA!");
                                });
                            } else if (!boleta.isEmpty() && !check) {
                                requireActivity().runOnUiThread(() -> {
                                    Utils.showToast(this, "Marque a caixa de CONVERTIDO!");
                                });

                            }else{
                                MyDatabase db = Room.databaseBuilder(getContext(), MyDatabase.class, "MeuBD").build();
                                AtendimentoDao atendimentoDao = db.atendimentoDao();

                                Executors.newSingleThreadExecutor().execute(() -> {
                                    Atendimento atendimentoExistente = atendimentoDao.findByName(nomeConclusao);
                                    if (atendimentoExistente != null){
                                        atendimentoExistente.setDateFim(datetimefim);
                                        atendimentoExistente.setConversao(check);

                                        if (!boleta.isEmpty()) {
                                            try {
                                                atendimentoExistente.setBoleta(Integer.parseInt(boleta));
                                            }catch (Exception e){
                                                atendimentoExistente.setBoleta(0);
                                                Log.e("LogUpdate", "Erro na conversão do campo boleta: " + e.getMessage());
                                            }
                                        }else {
                                            atendimentoExistente.setBoleta(0);
                                        }
                                        atendimentoExistente.setFeed(feed);
                                        atendimentoDao.update(atendimentoExistente);
                                        // final do click no salvar conclusao
                                        requireActivity().runOnUiThread(() -> {
                                            voltarPosicao(view, nome);
                                            dialogConcluir.dismiss();
                                            // AQUI QUE O BICHO PEGA

                                        });
                                    }else{
                                        // Lidar com o caso onde o atendimento não é encontrado
                                        requireActivity().runOnUiThread(() -> {
                                            // Você pode adicionar um log ou mostrar uma mensagem ao usuário
                                            Log.d("LogUpdate", "Atendimento não encontrado para o nome: " + nomeConclusao);
                                            // Opcionalmente, você pode mostrar uma mensagem ao usuário aqui
                                        });
                                    }

                                });
                            }

                        });
                    });



                    dialog.show();
                }); // fim do listener Button

            }
        }

    }

    private void voltarPosicao(View view, String nome) {
        // instancia as duas views
        LinearLayout mainViewPendencias = view.findViewById(R.id.mainViewPendencias);
        // limpa a a view
        mainViewPendencias.removeAllViews();

        List<String> fila = carregarVendedoras();
        List<String> pendencia = carregarPendentes();

        int index = pendencia.indexOf(nome);

        String concluido = pendencia.remove(index);
        fila.add(concluido);
        salvarFila(fila);
        salvarPendente(pendencia);

        criarBotoes(view);

       for (String n : pendencia) {
           Button button = ButtonUtils.createCustomButton(requireContext(), nome, pendencia);
          mainViewPendencias.addView(button);
           criarBotoesPendentes(view);
        }
    }

}