package thundersharp.aigs.spectre.ui.activities.lectures;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.FilterData;
import thundersharp.aigs.spectre.core.views.CustomViewPager;
import thundersharp.aigs.spectre.ui.activities.lectures.filter.DifficultySorting;
import thundersharp.aigs.spectre.ui.activities.lectures.filter.LanguageSorting;
import thundersharp.aigs.spectre.ui.activities.lectures.filter.TagsSorting;


public class FilterActivity extends AppCompatActivity {

    private RelativeLayout tags,difficulty,language;
    private CustomViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> tabName;

    private Toolbar toolbar;
    private ArrayList<String> levels, languages, hashes;
    private AppCompatButton btn_apply;

    public static List<String> hash_selected,level_selected,language_selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        language_selected = new ArrayList<>();
        level_selected = new ArrayList<>();
        hash_selected = new ArrayList<>();

        viewPager = findViewById(R.id.vp_sort);
        //vTab = findViewById(R.id.vTab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(n->finish());

        tags = findViewById(R.id.tags);
        difficulty = findViewById(R.id.difficulty);
        language = findViewById(R.id.language);
        btn_apply = findViewById(R.id.btn_apply);

        fragments = new ArrayList<>();
        tabName = new ArrayList<>();
        levels = new ArrayList<>();
        languages = new ArrayList<>();
        hashes = new ArrayList<>();


        levels.addAll(getIntent().getStringArrayListExtra("level"));
        languages.addAll(getIntent().getStringArrayListExtra("language"));
        hashes.addAll(getIntent().getStringArrayListExtra("hashes"));

        Bundle tag = new Bundle();
        tag.putStringArrayList("hash",hashes);
        TagsSorting tagsSorting = new TagsSorting();
        tagsSorting.setArguments(tag);

        tabName.add("Tags");
        fragments.add(tagsSorting);


        Bundle diff_level = new Bundle();
        diff_level.putStringArrayList("lvl",levels);
        DifficultySorting difficultySorting = new DifficultySorting();
        difficultySorting.setArguments(diff_level);

        tabName.add("Difficulty Level");
        fragments.add(difficultySorting);

        btn_apply.setOnClickListener(n-> {
            FilterData filterData = new FilterData(hash_selected,level_selected,language_selected);
            Intent intent = new Intent("filter_data");

            intent.putExtra("data", filterData);

           /* intent.putExtra("tags", hash_selected.toArray());
            intent.putExtra("difficulty",level_selected.toArray());
            intent.putExtra("language", new FilterData());*/
            setResult(RESULT_OK,intent);
            finish();
        });

        Bundle language_sort = new Bundle();
        language_sort.putStringArrayList("lang",languages);
        LanguageSorting languageSorting = new LanguageSorting();
        languageSorting.setArguments(language_sort);

        tabName.add("Language");
        fragments.add(languageSorting);


        FragTabAdapter fragTabAdapter = new FragTabAdapter(getSupportFragmentManager(), fragments,
                tabName);
        viewPager.setAdapter(fragTabAdapter);


        viewPager.setCurrentItem(0,true);
        tags.setBackgroundColor(Color.BLACK);
        difficulty.setBackgroundColor(Color.rgb(38,38,38));
        language.setBackgroundColor(Color.rgb(38,38,38));

        viewPager.setPagingEnabled(false);

        tags.setOnClickListener(t->{
            tags.setBackgroundColor(Color.BLACK);
            difficulty.setBackgroundColor(Color.rgb(38,38,38));
            language.setBackgroundColor(Color.rgb(38,38,38));
            viewPager.setCurrentItem(0,true);
        });

        difficulty.setOnClickListener(g->{
            difficulty.setBackgroundColor(Color.BLACK);
            tags.setBackgroundColor(Color.rgb(38,38,38));
            language.setBackgroundColor(Color.rgb(38,38,38));
            viewPager.setCurrentItem(1,true);
        });

        language.setOnClickListener(h->{

            language.setBackgroundColor(Color.BLACK);
            difficulty.setBackgroundColor(Color.rgb(38,38,38));
            tags.setBackgroundColor(Color.rgb(38,38,38));
            viewPager.setCurrentItem(2,true);

        });


    }

    public class FragTabAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<String> tabName = new ArrayList<>();

        public FragTabAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String>
                tabName) {
            super(fm);
            this.fragments = fragments;
            this.tabName = tabName;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabName.get(position);
        }
    }
}