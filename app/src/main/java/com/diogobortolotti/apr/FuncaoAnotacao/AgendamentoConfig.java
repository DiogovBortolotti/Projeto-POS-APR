package com.diogobortolotti.apr.FuncaoAnotacao;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;



public class AgendamentoConfig {



    public void setAlarm(Context context, long tempoAlarme, Uri lembrarAnotacao) {
        AlarmManager managerControleAlarme = AlarmeManager.getAlarmManager(context);

        PendingIntent operacao = AnotacaoServicos.getReminderPendingIntent(context, lembrarAnotacao);

//controlador de versoes
        if (Build.VERSION.SDK_INT >= 30) {
            managerControleAlarme.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tempoAlarme, operacao);
        } else if (Build.VERSION.SDK_INT >= 19) {
            managerControleAlarme.setExact(AlarmManager.RTC_WAKEUP, tempoAlarme, operacao);
        } else {
            managerControleAlarme.set(AlarmManager.RTC_WAKEUP, tempoAlarme, operacao);

        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime) {
        AlarmManager manager = AlarmeManager.getAlarmManager(context);
        PendingIntent operation =
                AnotacaoServicos.getReminderPendingIntent(context, reminderTask);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);


    }


}