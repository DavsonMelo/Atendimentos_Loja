package com.example.atendimentosloja.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.atendimentosloja.R;
import com.example.atendimentosloja.utils.SpacingItemDecoration;
import com.example.atendimentosloja.utils.Utils;
import com.example.atendimentosloja.adapters.VendedoraAdapter;
import com.example.atendimentosloja.database.MyDatabase;
import com.example.atendimentosloja.entity.Vendedora;
import java.util.ArrayList;
import java.util.List;

public class VendedorasFragment extends Fragment {
    private EditText editTextId, editTextNome;
    private RecyclerView lvVendedoras;
    private Button btnSalvar, btnCancelar, btnExcluir;
    private MyDatabase db;
    private VendedoraAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendedoras, container, false);

        // Inicialização do banco de dados
        db = MyDatabase.getDatabase(requireContext());

        // Configuração da UI
        configurarUI(view);

        // Observando mudanças na lista de vendedoras
        observarVendedoras();

        return view;
    }

    private void configurarUI(View view) {
        editTextId = view.findViewById(R.id.edit_text_id);
        editTextNome = view.findViewById(R.id.edit_tex_nome);
        lvVendedoras = view.findViewById(R.id.lv_vendedoras);
        btnSalvar = view.findViewById(R.id.btn_salva_vendedora);
        btnCancelar = view.findViewById(R.id.btn_cancelar);
        btnExcluir = view.findViewById(R.id.btn_exclui_vendedora);

        // Configuração do RecyclerView
        lvVendedoras.setLayoutManager(new LinearLayoutManager(getContext()));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        lvVendedoras.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        adapter = new VendedoraAdapter(new ArrayList<>(), vendedora -> {
            editTextId.setText(String.valueOf(vendedora.getId()));
            editTextNome.setText(vendedora.getNome());
        });
        lvVendedoras.setAdapter(adapter);

        // Configuração dos botões
        btnSalvar.setOnClickListener(this::salvarOuEditarVendedora);
        btnCancelar.setOnClickListener(view1 -> limparCampos()); // Chama o método sem parâmetros
        btnExcluir.setOnClickListener(this::excluirVendedora);
    }

    private void observarVendedoras() {
        db.vendedoraDao().getAll().observe(getViewLifecycleOwner(), (List<Vendedora> vendedoras) -> {
            adapter.updateData(vendedoras);
        });
    }

    private void salvarOuEditarVendedora(View view) {
        String nome = editTextNome.getText().toString().trim().toUpperCase();
        if (nome.isEmpty()) {
            Utils.showToast(this, "Preencha o campo de nome");
            return;
        }

        new Thread(() -> {
            int id = editTextId.length() == 0 ? 0 : Integer.parseInt(editTextId.getText().toString().trim());
            Vendedora vendedora = new Vendedora(id, nome);

            if (id == 0) {
                db.vendedoraDao().insert(vendedora);
                requireActivity().runOnUiThread(() -> Utils.showToast(requireContext(), "Vendedora cadastrada com sucesso!"));
            } else {
                db.vendedoraDao().update(vendedora);
                requireActivity().runOnUiThread(() -> Utils.showToast(requireContext(), "Vendedora editada com sucesso!"));
            }

            requireActivity().runOnUiThread(this::limparCampos);
        }).start();
    }

    private void excluirVendedora(View view) {
        if (editTextId.length() == 0) {
            Utils.showToast(this, "Selecione uma vendedora para excluir");
            return;
        }

        new Thread(() -> {
            int id = Integer.parseInt(editTextId.getText().toString().trim());
            Vendedora vendedora = new Vendedora(id, editTextNome.getText().toString().trim().toUpperCase());
            db.vendedoraDao().delete(vendedora);
            requireActivity().runOnUiThread(() -> Utils.showToast(requireContext(), "Vendedora excluída com sucesso!"));
            requireActivity().runOnUiThread(this::limparCampos);
        }).start();
    }

    private void limparCampos() {
        editTextId.setText("");
        editTextNome.setText("");
        fecharTeclado(requireView());
    }

    private void fecharTeclado(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
