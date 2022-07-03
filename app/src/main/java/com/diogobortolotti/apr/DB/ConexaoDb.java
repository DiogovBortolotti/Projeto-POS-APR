package com.diogobortolotti.apr.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




public class ConexaoDb extends SQLiteOpenHelper {

    private static final String DATABASE_NOME = "aprDb.db";

    private static final int VERSAO = 1;

    public ConexaoDb(Context context) {
        super(context, DATABASE_NOME, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_ALARM_TABLE =  "CREATE TABLE " + ModeloAnotacao.EntradadeLembrete.TABLE_NAME + " ("
                + ModeloAnotacao.EntradadeLembrete._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ModeloAnotacao.EntradadeLembrete.KEY_TITULO + " TEXT NOT NULL, "
                + ModeloAnotacao.EntradadeLembrete.KEY_DATA + " TEXT NOT NULL, "
                + ModeloAnotacao.EntradadeLembrete.KEY_TEMPO + " TEXT NOT NULL, "
                + ModeloAnotacao.EntradadeLembrete.KEY_REPETIR + " TEXT NOT NULL, "
                + ModeloAnotacao.EntradadeLembrete.KEY_NREPETIR + " TEXT NOT NULL, "
                + ModeloAnotacao.EntradadeLembrete.KEY_REPETIR_TIPO + " TEXT NOT NULL, "
                + ModeloAnotacao.EntradadeLembrete.KEY_STATUS + " TEXT NOT NULL " + " );";


        sqLiteDatabase.execSQL(SQL_CREATE_ALARM_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
