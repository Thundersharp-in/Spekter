package thundersharp.aigs.spectre.core.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;


import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialog;

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

    public void displayExitDialog(Activity activity){
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        View bottomSheetDialog =LayoutInflater.from(activity).inflate(R.layout.exit_bottom_sheet,null);
        AppCompatButton appCompatButton = bottomSheetDialog.findViewById(R.id.exit);
        AppCompatButton appCompatButtonCancel = bottomSheetDialog.findViewById(R.id.no);

        b.setView(bottomSheetDialog);
        AlertDialog alertDialog = b.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        if (appCompatButtonCancel != null && appCompatButton != null) {
            appCompatButtonCancel.setOnClickListener(e->alertDialog.dismiss());
            appCompatButton.setOnClickListener(f-> activity.finish());
        }

       alertDialog.show();

    }

}
