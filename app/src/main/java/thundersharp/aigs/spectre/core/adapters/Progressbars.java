package thundersharp.aigs.spectre.core.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;


import thundersharp.aigs.spectre.R;

public class Progressbars {

    private static Progressbars progressbars = null;
    private Activity activity;

    public Progressbars(){}

    public static Progressbars getInstance(){
        if (progressbars == null){
            progressbars = new Progressbars();
        }
        return progressbars;
    }

    public AlertDialog createDefaultProgressBar(Activity activity){
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setView(LayoutInflater.from(activity).inflate(R.layout.progress_bar,null));
        AlertDialog alertDialog = b.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return alertDialog;
    }

}
