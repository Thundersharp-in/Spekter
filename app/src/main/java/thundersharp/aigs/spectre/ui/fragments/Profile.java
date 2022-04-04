package thundersharp.aigs.spectre.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.browser.Browser;
import thundersharp.aigs.spectre.core.utils.AppUtils;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;

public class Profile extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);

        root.findViewById(R.id.recovery).setOnClickListener(n->{

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View bottomView = LayoutInflater.from(getContext()).inflate(R.layout.item_view_recovery,null);

            AppCompatButton invalidateCache = bottomView.findViewById(R.id.invCache);
            AppCompatButton invCacheRestart = bottomView.findViewById(R.id.invCacheRestart);
            AppCompatButton resetFirebase = bottomView.findViewById(R.id.resetFirebase);
            AppCompatButton clearLocal = bottomView.findViewById(R.id.clearLocal);

            invalidateCache.setOnClickListener(o->{
                Toast.makeText(getContext(), "Cleared application cache :"+deleteCache(getContext()), Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            invCacheRestart.setOnClickListener(nA->{
                Toast.makeText(getContext(), "Cleared application cache :"+deleteCache(getContext()), Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
                AppUtils.restartApp(getActivity());
            });
            resetFirebase.setOnClickListener(t->{

            });
            clearLocal.setOnClickListener(o->{

            });

            bottomSheetDialog.setContentView(bottomView);

            bottomSheetDialog.show();
        });

        root.findViewById(R.id.logoutCard).setOnClickListener(op->{
            Progressbars.getInstance().displayLogoutDialog(getActivity());
        });

        root.findViewById(R.id.support_n).setOnClickListener(op->{
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_support,null,false);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();

        });

        root.findViewById(R.id.tos).setOnClickListener(n-> Browser.loadUrl(getContext(),"http://thundersharp.in/"));
        root.findViewById(R.id.privacy).setOnClickListener(n-> Browser.loadUrl(getContext(),"http://thundersharp.in/privacy_policy"));

        root.findViewById(R.id.device_tokens).setOnClickListener(nW-> {
            AlertDialog alertDialog = Progressbars.getInstance().createDefaultProgressBar(getActivity());
            alertDialog.show();
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("FCM_TOKEN")
                        .setValue(task.getResult())
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                alertDialog.dismiss();
                                Toast.makeText(getContext(), "TOKENS REFRESHED", Toast.LENGTH_SHORT).show();
                            }else {
                                alertDialog.dismiss();
                                Toast.makeText(getContext(), "FAILED TO RE_REGISTER TOKEN :"+ task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


            });
        });

        root.findViewById(R.id.device_token_info).setOnClickListener(kl -> {
            new AlertDialog.Builder(getContext())
                    .setMessage("If your mobile client is not able to properly receive push notifications, then you can try fixing it by re registering the device's new tokens !")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        });

        return root;
    }

    public static boolean deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            return deleteDir(dir);
        } catch (Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}