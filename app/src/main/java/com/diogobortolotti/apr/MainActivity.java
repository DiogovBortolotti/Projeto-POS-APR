package com.diogobortolotti.apr;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.diogobortolotti.apr.DB.ModeloAnotacao;
import com.diogobortolotti.apr.DB.ConexaoDb;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mADDBotao;
    private Toolbar mToolbar;
    TelaAlarme mcursorAdptador;
    //se der erro
    ConexaoDb bancoGerar = new ConexaoDb(this);
    ListView anotacaoLista;
    ProgressDialog progdialog;

    private static final int tabela_carregamento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
*/

        anotacaoLista = (ListView) findViewById(R.id.lista);
        View listaVazia = findViewById(R.id.textovazio);
        anotacaoLista.setEmptyView(listaVazia);

        mcursorAdptador = new TelaAlarme(this, null);
        anotacaoLista.setAdapter(mcursorAdptador);

        anotacaoLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AdicionarLembreteAtivity.class);

                Uri currentLoaderUri = ContentUris.withAppendedId(ModeloAnotacao.EntradadeLembrete.CONTENT_URI, id);


                intent.setData(currentLoaderUri);

                startActivity(intent);

            }
        });


        mADDBotao = (FloatingActionButton) findViewById(R.id.fab);

        mADDBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdicionarLembreteAtivity.class);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(tabela_carregamento, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ModeloAnotacao.EntradadeLembrete._ID,
                ModeloAnotacao.EntradadeLembrete.KEY_TITULO,
                ModeloAnotacao.EntradadeLembrete.KEY_DATA,
                ModeloAnotacao.EntradadeLembrete.KEY_TEMPO,
                ModeloAnotacao.EntradadeLembrete.KEY_REPETIR,
                ModeloAnotacao.EntradadeLembrete.KEY_NREPETIR,
                ModeloAnotacao.EntradadeLembrete.KEY_REPETIR_TIPO,
                ModeloAnotacao.EntradadeLembrete.KEY_STATUS

        };

        return new CursorLoader(this,
                ModeloAnotacao.EntradadeLembrete.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mcursorAdptador.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mcursorAdptador.swapCursor(null);

    }
}
