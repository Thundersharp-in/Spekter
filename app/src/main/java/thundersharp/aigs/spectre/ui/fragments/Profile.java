package thundersharp.aigs.spectre.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.browser.Browser;
import thundersharp.aigs.spectre.core.exceptions.ArgumentsMissingException;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.helpers.ProfileDataSync;
import thundersharp.aigs.spectre.core.interfaces.BasicDataInterface;
import thundersharp.aigs.spectre.core.interfaces.ProfileSync;
import thundersharp.aigs.spectre.core.models.ProfileData;
import thundersharp.aigs.spectre.core.models.SubscriptionDetails;
import thundersharp.aigs.spectre.core.models.TicketsData;
import thundersharp.aigs.spectre.core.progress.BrowseProgress;
import thundersharp.aigs.spectre.core.progress.BrowseProgressStall;
import thundersharp.aigs.spectre.core.starters.Tickets;
import thundersharp.aigs.spectre.core.utils.AppUtils;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.core.utils.TimeUtils;
import thundersharp.aigs.spectre.ui.activities.auth.IntroActivity;
import thundersharp.aigs.spectre.ui.activities.exhibition.ExhibitionHome;
import thundersharp.aigs.spectre.ui.activities.lectures.SubsribedLectures;

public class Profile extends Fragment {

    private ProfileDataSync profileDataSync;
    private ProfileData profileData;
    private AlertDialog alertDialog;

    private TextView name,acharyan,email,phone,profile_description,textName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(getActivity());

        profileDataSync = ProfileDataSync.getInstance(getActivity());
        name = root.findViewById(R.id.profile_name);
        acharyan = root.findViewById(R.id.profile_type);
        email = root.findViewById(R.id.email);
        phone = root.findViewById(R.id.phone);
        textName = root.findViewById(R.id.textName);
        profile_description = root.findViewById(R.id.profile_description);

        if (profileDataSync.initializeLocalStorage().doDataExists()){
            profileData = profileDataSync.initializeLocalStorage().pullDataBack();
            updateUi(profileData);
        }else {
            profileDataSync
                    .initializeLocalStorage()
                    .setUid(FirebaseAuth.getInstance().getUid())
                    .setProfileSyncListener(new ProfileSync() {
                        @Override
                        public void onProfileDataSyncSuccess(DataSnapshot dataSnapshot) {
                            profileData = dataSnapshot.getValue(ProfileData.class);
                            profileDataSync.saveDataLocally(profileData);
                            updateUi(profileData);
                        }

                        @Override
                        public void onProfileDataSyncFailure(Exception exception) {
                            Toast.makeText(getContext(),exception.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        root.findViewById(R.id.resync_profile).setOnClickListener(n->{
            AlertDialog alertDialog = Progressbars.getInstance().createDefaultProgressBar(getActivity());
            alertDialog.show();
            profileDataSync
                    .initializeLocalStorage()
                    .setUid(FirebaseAuth.getInstance().getUid())
                    .setProfileSyncListener(new ProfileSync() {
                        @Override
                        public void onProfileDataSyncSuccess(DataSnapshot dataSnapshot) {
                            profileData = dataSnapshot.getValue(ProfileData.class);
                            profileDataSync.saveDataLocally(profileData);
                            updateUi(profileData);
                            alertDialog.dismiss();
                        }

                        @Override
                        public void onProfileDataSyncFailure(Exception exception) {
                            Toast.makeText(getContext(),exception.toString(),Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });

        });


        root.findViewById(R.id.recovery).setOnClickListener(n->{

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View bottomView = LayoutInflater.from(getContext()).inflate(R.layout.item_view_recovery,null);

            AppCompatButton invalidateCache = bottomView.findViewById(R.id.invCache);
            AppCompatButton invCacheRestart = bottomView.findViewById(R.id.invCacheRestart);
            AppCompatButton resetFirebase = bottomView.findViewById(R.id.resetFirebase);
            AppCompatButton clearLocal = bottomView.findViewById(R.id.clearLocal);

            invalidateCache.setOnClickListener(o->{
                deleteCache(getContext());
                Toast.makeText(getContext(), "Cleared application cache ", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });
            invCacheRestart.setOnClickListener(nA->{
                deleteCache(getContext());
                Toast.makeText(getContext(), "Cleared application cache ", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
                AppUtils.restartApp(getActivity());
            });
            resetFirebase.setOnClickListener(t->{

                    alertDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                        }
                    },2000);


            });
            clearLocal.setOnClickListener(o->{
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setMessage("You will be logged out now, Proceed ?")
                                .setPositiveButton("OK,LOGOUT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseAuth.getInstance().signOut();
                                        profileDataSync.initializeLocalStorage().clearAllData();
                                        BrowseProgressStall.getInstance(getActivity()).clear();
                                        BrowseProgress.getInstance(getActivity()).clear();
                                        getActivity().finish();
                                        startActivity(new Intent(getContext(), IntroActivity.class));
                                    }
                                })
                                        .setNegativeButton("STAY HERE",(dialogInterface, i) -> dialogInterface.dismiss()).show();

            });

            bottomSheetDialog.setContentView(bottomView);

            bottomSheetDialog.show();
        });

        root.findViewById(R.id.logoutCard).setOnClickListener(op->{
            Progressbars.getInstance().displayLogoutDialog(getActivity());
        });

        root.findViewById(R.id.evenr).setOnClickListener(m->{
            alertDialog.show();
            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.PASSES)
                    .child(FirebaseAuth.getInstance().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                DatabaseHelpers
                                        .getInstance()
                                        .setBasicDataInterface(new BasicDataInterface() {
                                            @Override
                                            public void dataFetchSuccess(DataSnapshot snapshotBasic) {
                                                try {
                                                    Tickets
                                                            .getInstance(getActivity())
                                                            .setTicketsData(new TicketsData(
                                                                    TimeUtils.getTimeFromTimeStamp(snapshotBasic.child("EVENT_DATE").getValue(String.class)),
                                                                    snapshot.child("SLOT").getValue(String.class),
                                                                    "TECH EXHIBITION",
                                                                    "Spekter is the technical fest of AIGS with over thousands of visitors it provides students with a plethora of opportunities for students to experience the beauty and amalgamations of science and technology.",
                                                                    snapshot.child("NAME").getValue(String.class),
                                                                    snapshot.child("EMAIL").getValue(String.class),
                                                                    snapshot.child("PHONE").getValue(String.class),
                                                                    snapshotBasic.child("VENUE").getValue(String.class),
                                                                    null,
                                                                    snapshot.child("ID").getValue(String.class)))
                                                            .showTickets();
                                                } catch (ArgumentsMissingException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFetchError(Exception e) {
                                                Toast.makeText(getContext(), "Server error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } else {
                                Toast.makeText(getContext(), "No passes issued kindly issue a pass first.", Toast.LENGTH_LONG).show();
                            }

                            alertDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });

        });

        root.findViewById(R.id.support_n).setOnClickListener(op->{
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_support,null,false);
            bottomSheetDialog.setContentView(view);

            bottomSheetDialog.findViewById(R.id.appFe).setOnClickListener(k->{
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("email/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"spekter_aigs@acharya.ac.in"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"Support on a spekter event.");
                intent.putExtra(Intent.EXTRA_TEXT, new String("__________________________________________________________\n"));
                intent.putExtra(Intent.EXTRA_TEXT, new String(
                        "DEVICE: "+ Build.DEVICE + "\nMANUFACTURER: "
                                + Build.MANUFACTURER + "\nBOOTLOADER: "
                                + Build.BOOTLOADER + "\nPRODUCT: "
                                + Build.PRODUCT + "\nUSER: "
                                + Build.USER + "\nDEVICE ID: "
                                + Build.ID + "\nCPU_ABI: "
                                + Build.CPU_ABI + "\n\n"
                                +"PROFILE ID : "+FirebaseAuth.getInstance().getUid()+"\n"
                                +"Email : "+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"\n\n"
                                + "________Your message after here _______\n"));
                Intent mailer = Intent.createChooser(intent, "Choose a email app to send support message for courses.");
                startActivity(mailer);
            });

            bottomSheetDialog.findViewById(R.id.invCache).setOnClickListener(k->{
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("email/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact_spekter@acharya.ac.in"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"Support on Spekter app.");
                intent.putExtra(Intent.EXTRA_TEXT, new String("__________________________________________________________\n"));
                intent.putExtra(Intent.EXTRA_TEXT, new String(
                        "DEVICE: "+ Build.DEVICE + "\nMANUFACTURER: "
                                + Build.MANUFACTURER + "\nBOOTLOADER: "
                                + Build.BOOTLOADER + "\nPRODUCT: "
                                + Build.PRODUCT + "\nUSER: "
                                + Build.USER + "\nDEVICE ID: "
                                + Build.ID + "\nCPU_ABI: "
                                + Build.CPU_ABI + "\n\n"
                                +"PROFILE ID : "+FirebaseAuth.getInstance().getUid()+"\n"
                                +"Email : "+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"\n\n"
                                + "________Your message after here _______\n"));
                Intent mailer = Intent.createChooser(intent, "Choose a email app to send support message for courses.");
                startActivity(mailer);
            });


            bottomSheetDialog.show();

        });

        root.findViewById(R.id.paymentsD).setOnClickListener(o->new AlertDialog.Builder(getContext()).setMessage("This option is currently not available for your account.").setPositiveButton("OK",(dialogInterface, i) -> dialogInterface.dismiss()).show());
        root.findViewById(R.id.subscription).setOnClickListener(j->startActivity(new Intent(getActivity(), SubsribedLectures.class)));
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

    private void updateUi(ProfileData profileData){
        name.setText(profileData.name);
        if (profileData.acharyan){
            acharyan.setText("Acharyan");
        }else acharyan.setText("Non-Acharyan");
        profile_description.setText("Hello " +profileData.name+" if you made some recent changes which are not reflected here then you can click on 'Re-sync profile' below to load the changes.");
        email.setText(profileData.email);
        phone.setText(profileData.phone);
        textName.setText(profileData.name);

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