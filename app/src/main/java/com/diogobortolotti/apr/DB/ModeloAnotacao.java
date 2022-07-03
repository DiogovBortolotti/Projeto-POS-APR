package com.diogobortolotti.apr.DB;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;


public class ModeloAnotacao {

    private ModeloAnotacao() {}
//caminho db
    public static final String AUTORIZACAO_CONTENT = "com.diogobortolotti.BancoDb";

    public static final Uri BANCO_CONTENT_URI = Uri.parse("content://" + AUTORIZACAO_CONTENT);

    public static final String PACOTE_ANOTACAO = "caminho de lembrete";

    public static final class EntradadeLembrete implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BANCO_CONTENT_URI, PACOTE_ANOTACAO);

        public static final String LISTA_CONTENT =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTORIZACAO_CONTENT + "/" + PACOTE_ANOTACAO;

        public static final String CONTENT_ITEM_TIPO =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTORIZACAO_CONTENT + "/" + PACOTE_ANOTACAO;

        public final static String TABLE_NAME = "Anotacao";

        public final static String _ID = BaseColumns._ID;

        public static final String KEY_TITULO = "titulo";
        public static final String KEY_DATA = "data";
        public static final String KEY_TEMPO = "hora";
        public static final String KEY_REPETIR = "repetir";
        public static final String KEY_NREPETIR = "repetir_cada";
        public static final String KEY_REPETIR_TIPO = "tipo_de_repeticao";
        public static final String KEY_STATUS = "status";

    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
}
