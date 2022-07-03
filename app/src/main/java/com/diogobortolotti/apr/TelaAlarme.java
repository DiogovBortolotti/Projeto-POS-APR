package com.diogobortolotti.apr;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.diogobortolotti.apr.DB.ModeloAnotacao;



public class TelaAlarme extends CursorAdapter {

    private TextView mTituloTexto, mDataHoraTexto, mRepeticaoInfo;
    private ImageView mImagemAt , mMinaturaImagem;
    private ColorGenerator geradorCores = ColorGenerator.DEFAULT; //gerador de cores
    private TextDrawable mDrawableBuilder;

    public TelaAlarme(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.alarme_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mTituloTexto = (TextView) view.findViewById(R.id.titulodesigner);
        mDataHoraTexto = (TextView) view.findViewById(R.id.dataehoradesginer);
        mRepeticaoInfo = (TextView) view.findViewById(R.id.repetirinfodesigner);
        mImagemAt = (ImageView) view.findViewById(R.id.statusdesigner);
        mMinaturaImagem = (ImageView) view.findViewById(R.id.imagemmindesinger);

        int tituloColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_TITULO);
        int dataColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_DATA);
        int tempoColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_TEMPO);
        int repetirColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_REPETIR);
        int desavRepetirColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_NREPETIR);
        int tipoRepetirColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_REPETIR_TIPO);
        int atividadeColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_STATUS);

        String titulo = cursor.getString(tituloColuna);
        String data = cursor.getString(dataColuna);
        String hora = cursor.getString(tempoColuna);
        String repetir = cursor.getString(repetirColuna);
        String desavRepetir = cursor.getString(desavRepetirColuna);
        String tipoRepetir = cursor.getString(tipoRepetirColuna);
        String ativo = cursor.getString(atividadeColuna);

        String datahora = data + " " + hora;

            //informação
        veTitulo(titulo);
        veData(datahora);
        repeticaoInfo(repetir, desavRepetir, tipoRepetir);
        definirStatusDaDefinicao(ativo);




    }

    //Definição de Lembrante
    public void veTitulo(String titulo) {
        mTituloTexto.setText(titulo);
        String letter = "A";

        if(titulo != null && !titulo.isEmpty()) {
            letter = titulo.substring(0, 1);
        }

        int color = geradorCores.getRandomColor();

      //Circulo do implementacao
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mMinaturaImagem.setImageDrawable(mDrawableBuilder);
    }

    //ve as horas e data
    public void  veData(String datahora) {
        mDataHoraTexto.setText(datahora);
    }

   //ver quando vai repetir
    public void repeticaoInfo(String repetir, String desavRepetir, String tipoRepetir) {
        if(repetir.equals("true")){
            mRepeticaoInfo.setText("Cada " + desavRepetir + " " + tipoRepetir + "(s)");
        }else if (repetir.equals("false")) {
            mRepeticaoInfo.setText("Repetir Vezes");
        }
    }

    //ativar ou desativar marcador
    public void definirStatusDaDefinicao(String status){
        if(status.equals("true")){
            mImagemAt.setImageResource(R.drawable.notificacao_);
        }else if (status.equals("false")) {
            mImagemAt.setImageResource(R.drawable.notificacao_off);
        }
    }
}

