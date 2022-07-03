package com.diogobortolotti.apr.FuncaoAnotacao;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import com.diogobortolotti.apr.AdicionarLembreteAtivity;
import com.diogobortolotti.apr.R;
import com.diogobortolotti.apr.DB.ModeloAnotacao;




public class AnotacaoServicos extends IntentService {
    private static final String TAG = AnotacaoServicos.class.getSimpleName();

    private static final int NOTIFICATION_ID = 11;

    Cursor cursor;

    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent acao = new Intent(context, AnotacaoServicos.class);
        acao.setData(uri);
        return PendingIntent.getService(context, 0, acao, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public AnotacaoServicos() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        Intent action = new Intent(this, AdicionarLembreteAtivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        String descricao = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                descricao = ModeloAnotacao.getColumnString(cursor, ModeloAnotacao.EntradadeLembrete.KEY_TITULO);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Notification nota = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.título_lembrete))
                .setContentText(descricao)
                .setSmallIcon(R.drawable.notificaobar)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, nota);
        run();


    }


    public void run() {
                final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                //vibracao 5000
                v.vibrate(5000);
                Uri notificacaosom = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                Ringtone reproduzir = RingtoneManager.getRingtone(this, notificacaosom);
                try {
            reproduzir.play();
            if (reproduzir.isPlaying()) {
                //tempo para parar de tocar a notificacao
                wait(3000);
                reproduzir.stop();
            }
        } catch (Exception e) {
        }

    };

}
