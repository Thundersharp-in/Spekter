package thundersharp.aigs.spectre.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.ProjectsHolderAdapter;
import thundersharp.aigs.spectre.core.models.ProjectShortDescription;

public class ExhibitionHome extends AppCompatActivity {

    private AppCompatButton book;
    private NestedScrollView mainContents;
    private RelativeLayout preAnimation;
    private RecyclerView recyclerProjects;
    private List<ProjectShortDescription> projectShortDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_home);

        book = findViewById(R.id.book);
        mainContents = findViewById(R.id.mainScroll);
        preAnimation = findViewById(R.id.mainContainer);

        recyclerProjects = findViewById(R.id.recyclerProjects);

        setPreAnimation(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPreAnimation(false);
            }
        },4000);

        recyclerProjects.setAdapter(new ProjectsHolderAdapter(getProjectShortDescriptions(10)));

    }

    private List<ProjectShortDescription> getProjectShortDescriptions(int size){
        projectShortDescriptions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            projectShortDescriptions.add(new ProjectShortDescription("Hey "+i,"",""+i));
        }

        return projectShortDescriptions;
    }

    private synchronized void setPreAnimation(boolean animation){
        if (animation){
            book.setVisibility(View.GONE);
            mainContents.setVisibility(View.GONE);
            preAnimation.setVisibility(View.VISIBLE);
        }else {
            book.setVisibility(View.VISIBLE);
            mainContents.setVisibility(View.VISIBLE);
            preAnimation.setVisibility(View.GONE);
        }
    }
}