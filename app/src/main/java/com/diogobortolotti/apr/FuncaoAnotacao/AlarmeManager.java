package com.diogobortolotti.apr.FuncaoAnotacao;

import android.app.AlarmManager;
import android.content.Context;



public class AlarmeManager {
    private static final String TAG = AlarmeManager.class.getSimpleName();
    private static AlarmManager AlarmeAnotacaoManager;
    public static synchronized void injectAlarmManager(AlarmManager alarmManager) {
        if (AlarmeAnotacaoManager != null) {
            throw new IllegalStateException("Mudado Horario");
        }
        AlarmeAnotacaoManager = alarmManager;
    }
        static synchronized AlarmManager getAlarmManager(Context context) {
        if (AlarmeAnotacaoManager == null) {
            AlarmeAnotacaoManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return AlarmeAnotacaoManager;
    }
}
