package com.diogobortolotti.apr;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import java.util.Calendar;
import com.diogobortolotti.apr.DB.ModeloAnotacao;
import com.diogobortolotti.apr.FuncaoAnotacao.AgendamentoConfig;
import com.getbase.floatingactionbutton.FloatingActionButton;




public class AdicionarLembreteAtivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTENCIA_DE_TABELA = 0;

    @Override
    public void onTimeSet(TimePickerDialog view, int HoraDia, int minuto, int segundos) {
        mHora = HoraDia;
        mMinuto = minuto;
        if (minuto < 10) {
            mTempo = HoraDia + ":" + "0" + minuto;
        } else {
            mTempo = HoraDia + ":" + minuto;
        }
        mTempoTexto.setText(mTempo);
    }


    private Toolbar mToolbar;
    private EditText mTituloTexto;
    private TextView mDataTexto, mTempoTexto, mRepetirLemTexto, mNaoRepetirLemTexto, mTipoRepeticao;
    private FloatingActionButton mBotao1;
    private FloatingActionButton mBotao2;
    private Calendar mCalendario;
    private int mAno, mMes, mHora, mMinuto, mDia;
    private long mRepetirTempo;
    private Switch mRepetirSwitch;
    private String mTitulo;
    private String mTempo;
    private String mData;
    private String mRepetir;
    private String mNRepetir;
    private String mTipoRepetir;
    private String mStatus;

    private Uri mLemebreteUri;
    private boolean mMudarTabela = false;

    //orientacao ao db
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_TEMPO = "tempo";
    private static final String KEY_DATA = "data";
    private static final String KEY_REPETIR = "repetir";
    private static final String KEY_NREPETIR = "repetir_quanto";
    private static final String KEY_REPETIR_TIPO = "tipo_repeticao";
    private static final String KEY_STATUS = "status";



    private static final long milMinuto = 60000L;
    private static final long milHoras = 3600000L;
    private static final long milDia = 86400000L;
    private static final long milSemana = 604800000L;
    private static final long milMes = 2592000000L;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mMudarTabela = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_lembrete_ativity);

        Intent intent = getIntent();
        mLemebreteUri = intent.getData();

        if (mLemebreteUri == null) {

            setTitle(getString(R.string.adicionar_Titulo_Anotacao));


            invalidateOptionsMenu();
        } else {

            setTitle(getString(R.string.editar_Titulo_Anotacao));


            getLoaderManager().initLoader(EXISTENCIA_DE_TABELA, null, this);
        }



        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTituloTexto = (EditText) findViewById(R.id.titulolembrete);
        mDataTexto = (TextView) findViewById(R.id.set_date);
        mTempoTexto = (TextView) findViewById(R.id.set_time);
        mRepetirLemTexto = (TextView) findViewById(R.id.repetir_text);
        mNaoRepetirLemTexto = (TextView) findViewById(R.id.set_repeat_no);
        mTipoRepeticao = (TextView) findViewById(R.id.set_repeat_type);
        mRepetirSwitch = (Switch) findViewById(R.id.repeat_switch);
        mBotao1 = (FloatingActionButton) findViewById(R.id.Botao1floating);
        mBotao2 = (FloatingActionButton) findViewById(R.id.Botao2floating);


        mStatus = "true";
        mRepetir = "true";
        mNRepetir = Integer.toString(1);
        mTipoRepetir = "Hora";

        mCalendario = Calendar.getInstance();
        mHora = mCalendario.get(Calendar.HOUR_OF_DAY);
        mMinuto = mCalendario.get(Calendar.MINUTE);
        mAno = mCalendario.get(Calendar.YEAR);
        mMes = mCalendario.get(Calendar.MONTH) + 1;
        mDia = mCalendario.get(Calendar.DATE);

        mData = mDia + "/" + mMes + "/" + mAno;
        mTempo = mHora + ":" + mMinuto;


        mTituloTexto.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitulo = s.toString().trim();
                mTituloTexto.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        mDataTexto.setText(mData);
        mTempoTexto.setText(mTempo);
        mNaoRepetirLemTexto.setText(mNRepetir);
        mTipoRepeticao.setText(mTipoRepetir);
        mRepetirLemTexto.setText("Cada " + mNRepetir + " " + mTipoRepetir + "(s)");


        if (savedInstanceState != null) {
            String salvarTitulo = savedInstanceState.getString(KEY_TITULO);
            mTituloTexto.setText(salvarTitulo);
            mTitulo = salvarTitulo;

            String salvarTempo = savedInstanceState.getString(KEY_TEMPO);
            mTempoTexto.setText(salvarTempo);
            mTempo = salvarTempo;

            String salvarData = savedInstanceState.getString(KEY_DATA);
            mDataTexto.setText(salvarData);
            mData = salvarData;

            String salvarRepeticao = savedInstanceState.getString(KEY_REPETIR);
            mRepetirLemTexto.setText(salvarRepeticao);
            this.mRepetir = salvarRepeticao;

            String salvarNaoRepeticaoO = savedInstanceState.getString(KEY_NREPETIR);
            mNaoRepetirLemTexto.setText(salvarNaoRepeticaoO);
            mNRepetir = salvarNaoRepeticaoO;

            String salvarTipo = savedInstanceState.getString(KEY_REPETIR_TIPO);
            mTipoRepeticao.setText(salvarTipo);
            mTipoRepetir = salvarTipo;

            mStatus = savedInstanceState.getString(KEY_STATUS);
        }


        if (mStatus.equals("false")) {
            mBotao1.setVisibility(View.VISIBLE);
            mBotao2.setVisibility(View.GONE);

        } else if (mStatus.equals("true")) {
            mBotao1.setVisibility(View.GONE);
            mBotao2.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.titulo_de_atividades);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITULO, mTituloTexto.getText());
        outState.putCharSequence(KEY_TEMPO, mTempoTexto.getText());
        outState.putCharSequence(KEY_DATA, mDataTexto.getText());
        outState.putCharSequence(KEY_REPETIR, mRepetirLemTexto.getText());
        outState.putCharSequence(KEY_NREPETIR, mNaoRepetirLemTexto.getText());
        outState.putCharSequence(KEY_REPETIR_TIPO, mTipoRepeticao.getText());
        outState.putCharSequence(KEY_STATUS, mStatus);
    }//salvar


    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getSupportFragmentManager(), "Caixa de Tempo");
    }


    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getSupportFragmentManager(), "Caixa de Data");
    }






    @Override
    public void onDateSet(DatePickerDialog view, int Ano, int MesdoAno, int DiadoMes) {
        MesdoAno ++;
        mDia = DiadoMes;
        mMes = MesdoAno;
        mAno = Ano;
        mData = DiadoMes + "/" + MesdoAno + "/" + Ano;
        mDataTexto.setText(mData);
    }


    public void selectFab1(View v) {
        mBotao1 = (FloatingActionButton) findViewById(R.id.Botao1floating);
        mBotao1.setVisibility(View.GONE);
        mBotao2 = (FloatingActionButton) findViewById(R.id.Botao2floating);
        mBotao2.setVisibility(View.VISIBLE);
        mStatus = "true";
    }


    public void selectFab2(View v) {
        mBotao2 = (FloatingActionButton) findViewById(R.id.Botao2floating);
        mBotao2.setVisibility(View.GONE);
        mBotao1 = (FloatingActionButton) findViewById(R.id.Botao1floating);
        mBotao1.setVisibility(View.VISIBLE);
        mStatus = "false";
    }


    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepetir = "true";
            mRepetirLemTexto.setText("Cada " + mNRepetir + " " + mTipoRepetir + "(s)");
        } else {
            mRepetir = "false";
            mRepetirLemTexto.setText(R.string.repetiroff);
        }
    }


    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Minuto";
        items[1] = "Hora";
        items[2] = "Dia";
        items[3] = "Semana";
        items[4] = "Mes";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecionar Tipo");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mTipoRepetir = items[item];
                mTipoRepeticao.setText(mTipoRepetir);
                mRepetirLemTexto.setText("Every " + mNRepetir + " " + mTipoRepetir + "(s)");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Digite Numero");


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mNRepetir = Integer.toString(1);
                            mNaoRepetirLemTexto.setText(mNRepetir);
                            mRepetirLemTexto.setText("Cada " + mNRepetir + " " + mTipoRepetir + "(s)");
                        }
                        else {
                            mNRepetir = input.getText().toString().trim();
                            mNaoRepetirLemTexto.setText(mNRepetir);
                            mRepetirLemTexto.setText("Cada " + mNRepetir + " " + mTipoRepetir + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_adicionar_lembrete, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mLemebreteUri == null) {
            MenuItem menuItem = menu.findItem(R.id.descartar_lembrete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.salvar_lembrete:


                if (mTituloTexto.getText().toString().length() == 0){
                    mTituloTexto.setError("Titulo do lembrete esta em branco");
                }

                else {
                    saveReminder();
                    finish();
                }
                return true;

            case R.id.descartar_lembrete:

                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:

                if (!mMudarTabela) {
                    NavUtils.navigateUpFromSameTask(AdicionarLembreteAtivity.this);
                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                NavUtils.navigateUpFromSameTask(AdicionarLembreteAtivity.this);
                            }
                        };


                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.descartar_alteracao);
        builder.setPositiveButton(R.string.descartar, discardButtonClickListener);
        builder.setNegativeButton(R.string.continuar_editando, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deletar_anotacao);
        builder.setPositiveButton(R.string.deletar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deleteReminder();
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteReminder() {

        if (mLemebreteUri != null) {

            int rowsDeleted = getContentResolver().delete(mLemebreteUri, null, null);


            if (rowsDeleted == 0) {

                Toast.makeText(this, getString(R.string.deletado_com_erro),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.deletado_com_sucesso),
                        Toast.LENGTH_SHORT).show();
            }
        }


        finish();
    }


    public void saveReminder(){
        ContentValues values = new ContentValues();
        values.put(ModeloAnotacao.EntradadeLembrete.KEY_TITULO, mTitulo);
        values.put(ModeloAnotacao.EntradadeLembrete.KEY_DATA, mData);
        values.put(ModeloAnotacao.EntradadeLembrete.KEY_TEMPO, mTempo);
        values.put(ModeloAnotacao.EntradadeLembrete.KEY_REPETIR, mRepetir);
        values.put(ModeloAnotacao.EntradadeLembrete.KEY_NREPETIR, mNRepetir);
        values.put(ModeloAnotacao.EntradadeLembrete.KEY_REPETIR_TIPO, mTipoRepetir);
        values.put(ModeloAnotacao.EntradadeLembrete.KEY_STATUS, mStatus);

        mCalendario.set(Calendar.MONTH, --mMes);
        mCalendario.set(Calendar.YEAR, mAno);
        mCalendario.set(Calendar.DAY_OF_MONTH, mDia);
        mCalendario.set(Calendar.HOUR_OF_DAY, mHora);
        mCalendario.set(Calendar.MINUTE, mMinuto);
        mCalendario.set(Calendar.SECOND, 0);

        long selectedTimestamp =  mCalendario.getTimeInMillis();


        if (mTipoRepetir.equals("Minuto")) {
            mRepetirTempo = Integer.parseInt(mNRepetir) * milMinuto;
        } else if (mTipoRepetir.equals("Hora")) {
            mRepetirTempo = Integer.parseInt(mNRepetir) * milHoras;
        } else if (mTipoRepetir.equals("Dia")) {
            mRepetirTempo = Integer.parseInt(mNRepetir) * milDia;
        } else if (mTipoRepetir.equals("Semana")) {
            mRepetirTempo = Integer.parseInt(mNRepetir) * milSemana;
        } else if (mTipoRepetir.equals("Mes")) {
            mRepetirTempo = Integer.parseInt(mNRepetir) * milMes;
        }

        if (mLemebreteUri == null) {

            Uri newUri = getContentResolver().insert(ModeloAnotacao.EntradadeLembrete.CONTENT_URI, values);
            if (newUri == null) {

                Toast.makeText(this, getString(R.string.editado_erro),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.editado_salvo),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mLemebreteUri, values, null, null);


            if (rowsAffected == 0) {

                Toast.makeText(this, getString(R.string.editado_atualizarerro),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.editado_atualizar),
                        Toast.LENGTH_SHORT).show();
            }
        }


        if (mStatus.equals("true")) {
            if (mRepetir.equals("true")) {
                new AgendamentoConfig().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mLemebreteUri, mRepetirTempo);
            } else if (mRepetir.equals("false")) {
                new AgendamentoConfig().setAlarm(getApplicationContext(), selectedTimestamp, mLemebreteUri);
            }
            //TESTES
          //  Toast.makeText(this, "ID " + selectedTimestamp,Toast.LENGTH_LONG).show();
        }


        Toast.makeText(getApplicationContext(), "Salvo",
                Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
                ModeloAnotacao.EntradadeLembrete.KEY_STATUS,
        };


        return new CursorLoader(this,
                mLemebreteUri,projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }


        if (cursor.moveToFirst()) {
            int tituloColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_TITULO);
            int dataColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_DATA);
            int horaColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_TEMPO);
            int repeticaoColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_REPETIR);
            int quantidadeRepeticaoColuna = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_NREPETIR);
            int tipoRepeticao = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_REPETIR_TIPO);
            int statusRepeticao = cursor.getColumnIndex(ModeloAnotacao.EntradadeLembrete.KEY_STATUS);

            String titulo = cursor.getString(tituloColuna);
            String data = cursor.getString(dataColuna);
            String hora = cursor.getString(horaColuna);
            String repeticao = cursor.getString(repeticaoColuna);
            String repeticaoN = cursor.getString(quantidadeRepeticaoColuna);
            String tipoRepet = cursor.getString(tipoRepeticao);
            String status = cursor.getString(statusRepeticao);




            mTituloTexto.setText(titulo);
            mDataTexto.setText(data);
            mTempoTexto.setText(hora);
            mNaoRepetirLemTexto.setText(repeticaoN);
            mTipoRepeticao.setText(tipoRepet);
            mRepetirLemTexto.setText("Cada " + repeticaoN + " " + tipoRepet + "(s)");

            if (repeticao.equals("false")) {
                mRepetirSwitch.setChecked(false);
                mRepetirLemTexto.setText(R.string.repetiroff);

            } else if (repeticao.equals("true")) {
                mRepetirSwitch.setChecked(true);
            }

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
