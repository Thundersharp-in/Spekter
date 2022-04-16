package thundersharp.aigs.spectre.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.ProjectsHolderAdapter;
import thundersharp.aigs.spectre.core.adapters.WorkshopItemHolderAdapter;
import thundersharp.aigs.spectre.core.models.Workshops;

public class WorkshopsHome extends AppCompatActivity {

    private RelativeLayout loader;
    private LinearLayout content;
    private RecyclerView event_Holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshops_home);

        loader = findViewById(R.id.loader);
        content = findViewById(R.id.content);
        event_Holder = findViewById(R.id.event_Holder);

        setUpLoader(true);

        event_Holder.setAdapter(new WorkshopItemHolderAdapter(getRandData()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpLoader(false);
            }
        },3000);

    }

    private List<Workshops> getRandData(){
        return new ArrayList<Workshops>(Arrays.asList(new Workshops(),new Workshops(),new Workshops(),new Workshops(),new Workshops()));
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