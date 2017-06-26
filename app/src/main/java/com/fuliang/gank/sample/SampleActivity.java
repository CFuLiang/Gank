package com.fuliang.gank.sample;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fuliang.gank.SlidingRootNavBuilder;
import com.fuliang.gank.sample.adapter.MenuAdapter;
import com.fuliang.gank.sample.fragment.AllListFragment;
import com.fuliang.gank.sample.fragment.CollectFragment;
import com.fuliang.gank.sample.fragment.WelfareFragment;
import com.fuliang.gank.sample.helper.BusinessHelper;
import com.fuliang.gank.sample.model.WeatherModel;
import com.fuliang.gank.sample.model.WeatherResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.functions.Action1;

public class SampleActivity extends AppCompatActivity{

    private String[] screenTitles;

    private SlidingRootNavBuilder builder;
    private WelfareFragment welfareFragment;
    private AllListFragment allListFragment;
    private CollectFragment collectFragment;
    private MenuAdapter adapter;
    private long exitTime = 0;
    private WeatherModel model;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        setSupportActionBar(toolbar);
        builder = new SlidingRootNavBuilder(this);
        builder .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        screenTitles = loadTitleString();

        adapter = new MenuAdapter(this);
        adapter.setOnItemClickListener(new MenuAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                adapter.setSelected(position);
                show(position);
            }
        });
        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        show(0);
        LinearLayout about = (LinearLayout)findViewById(R.id.about_layout);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        getWeather();
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Snackbar.make(getWindow().getDecorView(),"再按一次退出", BaseTransientBottomBar.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            quit();
        }
    }

    protected void quit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        moveTaskToBack(true);
        finish();
    }

    private void showDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title("About");
        builder.negativeText("关闭");
        builder.negativeColorRes(R.color.colorAccent);
        builder.content(R.string.about);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.build().show();
    }

    public void show(int position){
        adapter.setSelected(position);
        Fragment selectedScreen;
        if (position == 5){
            if (welfareFragment == null){
                welfareFragment = new WelfareFragment();
            }
            selectedScreen = welfareFragment;
        }else if (position == 7){
            if (collectFragment == null){
                collectFragment = new CollectFragment();
            }
            selectedScreen = collectFragment;
        }
        else {
            if (allListFragment == null) {
                allListFragment = new AllListFragment();
            }
            selectedScreen = allListFragment;
            if (allListFragment.isAdded()){
                allListFragment.setType(screenTitles[position]);
            }else {
                Bundle bundle = new Bundle();
                bundle.putString("type",screenTitles[position]);
                allListFragment.setArguments(bundle);
            }
        }
        if (getSupportActionBar() != null){
            if (position == 0){
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }else {
                getSupportActionBar().setTitle(screenTitles[position]);
            }
        }
        builder.hideMenu();
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private String[] loadTitleString() {
        return getResources().getStringArray(R.array.title_list);
    }

    private void getWeather(){
        Map<String,String > map = new HashMap<>();
        model = new WeatherModel();
        model.location = "beijing";
        map.put("key",model.key);
        map.put("location",model.location);
        map.put("language",model.language);
        map.put("unit",model.unit);
        BusinessHelper.getWeather(map).subscribe(new Action1<WeatherResponse>() {
            @Override
            public void call(WeatherResponse weatherResponse) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

}
