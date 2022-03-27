package thundersharp.aigs.spectre.ui.activities.lectures;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.ui.fragments.lectures.Courses;
import thundersharp.aigs.spectre.ui.fragments.lectures.FreeCources;
import thundersharp.aigs.spectre.ui.fragments.lectures.LiveTraining;

public class ServiceMain extends AppCompatActivity {

    private TabLayout tl_service;
    private ViewPager vp_service;

    private Integer pos;
    private EditText search_bar_edit_text;
    private ImageButton search_bar_voice_icon;
    Courses courses;
    LiveTraining liveTraining;
    FreeCources freeCources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_main);

        tl_service = findViewById(R.id.tl_service);
        vp_service = findViewById(R.id.vp_service);
        search_bar_edit_text = findViewById(R.id.search_bar_edit_text);
        search_bar_voice_icon = findViewById(R.id.search_bar_voice_icon);

        findViewById(R.id.draweropener).setOnClickListener(h -> finish());

        tl_service.addTab(tl_service.newTab().setText("Courses"));
        tl_service.addTab(tl_service.newTab().setText("Live Trainings"));
        tl_service.addTab(tl_service.newTab().setText("Information Services"));

        pos = getIntent().getIntExtra("pos",0);
        if (pos == null) {
            pos = 0;
        }
        gettabs(pos);

        search_bar_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (vp_service.getCurrentItem()){
                    //TODO check for page changed
                    case 0:
                        //Bundle bundle = new Bundle();
                        //bundle.putCharSequence("filter_s",s);
                        //courses.setArguments(bundle);
                        courses.applyFilter(s.toString());
                        break;
                    case 1:
                        //Bundle bundle1 = new Bundle();
                        //bundle1.putCharSequence("filter_s",s);
                        //liveTraining.setArguments(bundle1);
                        liveTraining.applyFilter(s.toString());
                        break;
                    case 2:
                        //Bundle bundle2 = new Bundle();
                        //bundle2.putCharSequence("filter_s",s);
                        //freeCources.setArguments(bundle2);
                        freeCources.applyFilter(s.toString());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + vp_service.getCurrentItem());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_bar_voice_icon.setOnClickListener(v->{

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search for courses");

            try {
                startActivityForResult(intent, 1);
            } catch (Exception exception) {
                Toast.makeText(ServiceMain.this, "EXCEPTION: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                search_bar_edit_text.setText(Objects.requireNonNull(result).get(0));
            }
        }

    }

    private synchronized void gettabs(Integer pos){
        new Handler().post(() -> {

            final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            //ViewPagerAdapter(getParentFragmentManager());
            courses = new Courses();
            liveTraining = new LiveTraining();
            freeCources = new FreeCources();
            viewPagerAdapter.addFragment(courses,"Courses");
            viewPagerAdapter.addFragment(liveTraining,"Live Training");
            viewPagerAdapter.addFragment(freeCources,"Free Courses");

            vp_service.setAdapter(viewPagerAdapter);
            tl_service.setupWithViewPager(vp_service);
            tl_service.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    vp_service.setCurrentItem(tab.getPosition());
                    if (!search_bar_edit_text.getText().toString().isEmpty())
                        switch (vp_service.getCurrentItem()){
                            case 0:
                                courses.applyFilter(search_bar_edit_text.getText().toString());
                                break;
                            case 1:
                                liveTraining.applyFilter(search_bar_edit_text.getText().toString());
                                break;
                            case 2:
                                freeCources.applyFilter(search_bar_edit_text.getText().toString());
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + vp_service.getCurrentItem());
                        }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    switch (vp_service.getCurrentItem()){
                        case 0:
                            courses.applyFilter("");
                            break;
                        case 1:
                            liveTraining.applyFilter("");
                            break;
                        case 2:
                            freeCources.applyFilter("");
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + vp_service.getCurrentItem());
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            vp_service.setCurrentItem(pos);

        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}