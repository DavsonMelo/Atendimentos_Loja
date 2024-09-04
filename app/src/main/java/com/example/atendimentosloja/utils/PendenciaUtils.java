package com.example.atendimentosloja.utils;

import android.content.Context;

import androidx.room.Room;

import com.example.atendimentosloja.dao.AtendimentoDao;
import com.example.atendimentosloja.database.MyDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class PendenciaUtils {

    public interface PendenciaCallback {
        void onResult(boolean hasPendencia);
    }

    public static MyDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context, MyDatabase.class, "MeuBD").build();
    }

    public static void checkPendencias(Context context, String nome, PendenciaCallback callback) {
        MyDatabase db = getDatabase(context);
        AtendimentoDao atendimentoDao = db.atendimentoDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<String> pendentes = atendimentoDao.getFuncionariasPendentes();
            boolean result = pendentes.contains(nome);

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                callback.onResult(result);
            });
        });
    }
}
