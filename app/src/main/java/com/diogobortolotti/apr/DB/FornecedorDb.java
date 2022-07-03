package com.diogobortolotti.apr.DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class FornecedorDb extends ContentProvider {

    public static final String LOG_TAG = FornecedorDb.class.getSimpleName();
    private static final int ANOTACAO = 100;
    private static final int ANOTACAO_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ModeloAnotacao.AUTORIZACAO_CONTENT, ModeloAnotacao.PACOTE_ANOTACAO, ANOTACAO);

        sUriMatcher.addURI(ModeloAnotacao.AUTORIZACAO_CONTENT, ModeloAnotacao.PACOTE_ANOTACAO + "/#", ANOTACAO_ID);

    }

    private ConexaoDb mbBancoDbHelper;

    @Override
    public boolean onCreate() {
        mbBancoDbHelper = new ConexaoDb(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase dbsql = mbBancoDbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ANOTACAO:
                cursor = dbsql.query(ModeloAnotacao.EntradadeLembrete.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ANOTACAO_ID:
                selection = ModeloAnotacao.EntradadeLembrete._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = dbsql.query(ModeloAnotacao.EntradadeLembrete.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Não e possivel consultar URI desconhecido " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ANOTACAO:
                return ModeloAnotacao.EntradadeLembrete.LISTA_CONTENT;
            case ANOTACAO_ID:
                return ModeloAnotacao.EntradadeLembrete.CONTENT_ITEM_TIPO;
            default:
                throw new IllegalStateException("Desconhecido URI " + uri + " ... " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ANOTACAO:
                return insertReminder(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insercão Incompativel " + uri);
        }
    }

    private Uri insertReminder(Uri uri, ContentValues values) {

        SQLiteDatabase dbqlite = mbBancoDbHelper.getWritableDatabase();

        long id = dbqlite.insert(ModeloAnotacao.EntradadeLembrete.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Linha Invalida " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase dbqlite = mbBancoDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ANOTACAO:
                rowsDeleted = dbqlite.delete(ModeloAnotacao.EntradadeLembrete.TABLE_NAME, selection, selectionArgs);
                break;
            case ANOTACAO_ID:
                selection = ModeloAnotacao.EntradadeLembrete._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = dbqlite.delete(ModeloAnotacao.EntradadeLembrete.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete não foi possivel " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ANOTACAO:
                return updateReminder(uri, contentValues, selection, selectionArgs);
            case ANOTACAO_ID:
                selection = ModeloAnotacao.EntradadeLembrete._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateReminder(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Atualizacão e possivel " + uri);
        }
    }

    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase dbsql = mbBancoDbHelper.getWritableDatabase();

        int rowsUpdated = dbsql.update(ModeloAnotacao.EntradadeLembrete.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

}
