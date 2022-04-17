package thundersharp.aigs.spectre.ui.activities.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.ProjectsHolderAdapter;
import thundersharp.aigs.spectre.core.adapters.WorkshopItemHolderAdapter;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.interfaces.OnWorkshopFetchSuccess;
import thundersharp.aigs.spectre.core.models.Workshops;

public class WorkshopsHome extends AppCompatActivity {

    private RelativeLayout loader;
    private LinearLayout content;
    private RecyclerView event_Holder;
    private WorkshopItemHolderAdapter workshopItemHolderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshops_home);
        findViewById(R.id.exit).setOnClickListener(k->finish());

        loader = findViewById(R.id.loader);
        content = findViewById(R.id.content);
        event_Holder = findViewById(R.id.event_Holder);

        setUpLoader(true);

        DatabaseHelpers
                .getInstance()
                .setOnWorkshopsFetchListener(new OnWorkshopFetchSuccess() {
                    @Override
                    public void onFetchProjectsSuccess(List<Workshops> response) {
                        workshopItemHolderAdapter = new WorkshopItemHolderAdapter(response);
                        event_Holder.setAdapter(workshopItemHolderAdapter);
                        setUpLoader(false);
                    }

                    @Override
                    public void onFetchError(Exception e) {
                        setUpLoader(false);
                    }
                });

        ((EditText)findViewById(R.id.searchbar)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (workshopItemHolderAdapter != null)
                    workshopItemHolderAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private synchronized void setUpLoader(boolean status){
        if (status){
            loader.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        }else {
            loader.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }
    }
}