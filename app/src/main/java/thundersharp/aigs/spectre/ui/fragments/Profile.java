package thundersharp.aigs.spectre.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.browser.Browser;
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

            });
            invCacheRestart.setOnClickListener(nA->{

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

        return root;
    }
}