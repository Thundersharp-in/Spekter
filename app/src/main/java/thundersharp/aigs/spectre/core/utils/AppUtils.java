package thundersharp.aigs.spectre.core.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import thundersharp.aigs.spectre.startup.SplashScreen;

public final class AppUtils {

    public static void restartApp(Context context){
        Intent mStartActivity = new Intent(context, SplashScreen.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0); //you can also kill your app's process
    }

}
