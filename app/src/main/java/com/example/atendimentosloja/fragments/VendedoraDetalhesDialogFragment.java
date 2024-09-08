package com.example.atendimentosloja.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.atendimentosloja.R;

public class VendedoraDetalhesDialogFragment extends DialogFragment {

    private static final String ARG_NOME = "nome_vendedora";

    public static VendedoraDetalhesDialogFragment newInstance(String nomeVendedora) {
        VendedoraDetalhesDialogFragment fragment = new VendedoraDetalhesDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOME, nomeVendedora);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendedora_detalhes_dialog, container, false);

        String nomeVendedora = getArguments().getString(ARG_NOME);
        TextView tvNome = view.findViewById(R.id.tvNomeVendedora);
        tvNome.setText(nomeVendedora);

        Button btn_fechar = view.findViewById(R.id.btn_fechar_detalhes);
        btn_fechar.setOnClickListener(view1 -> {
            dismiss();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Definir o layout como tela cheia
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Remove o fundo padrão
        }
    }


}
