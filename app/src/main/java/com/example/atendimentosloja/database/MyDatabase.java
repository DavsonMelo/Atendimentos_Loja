package com.example.atendimentosloja.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.atendimentosloja.dao.AtendimentoDao;
import com.example.atendimentosloja.dao.VendedoraDao;
import com.example.atendimentosloja.entity.Atendimento;
import com.example.atendimentosloja.entity.Vendedora;

@Database(entities = {Vendedora.class, Atendimento.class}, version = 4, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    public abstract VendedoraDao vendedoraDao();
    public abstract AtendimentoDao atendimentoDao();

    private static volatile MyDatabase INSTANCE;

    // Definindo a migração da versão 3 para a versão 4
    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Comando SQL para adicionar a nova coluna
            database.execSQL("ALTER TABLE Atendimento ADD COLUMN tempoAtendimento INTEGER DEFAULT NULL");
        }
    };

    public static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    MyDatabase.class,
                                    "MeuBD" // Nome do banco de dados
                            )
                            .addMigrations(MIGRATION_3_4)  // Adiciona a migração correta
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
