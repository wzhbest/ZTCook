package com.zTeam.cook;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Richard on 2017-08-11.
 * HomeActivity 主界面
 */

public class HomeActivity extends AppCompatActivity {

    public final static String TAG = "HomeActivity";
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private SearchView searchView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.e(TAG,"actionbar1 = " + getSupportActionBar() + "*****actionbar2 = " + getActionBar());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_front);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_news:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_lib:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_find:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.item_more:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(BaseFragment.newInstance("新闻"));
        adapter.addFragment(BaseFragment.newInstance("图书"));
        adapter.addFragment(BaseFragment.newInstance("发现"));
        adapter.addFragment(BaseFragment.newInstance("更多"));
        viewPager.setAdapter(adapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // 加入含有search view的菜单
        getMenuInflater().inflate(R.menu.bmap_menu, menu);

        setSearchView(menu);
        //getActionBar().setDisplayShowTitleEnabled(false);

        // 获取SearchView对象
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if(searchView == null){
            Log.e("SearchView","Fail to get Search View.");
            return true;
        }

//        searchView.setIconifiedByDefault(false); // 缺省值就是true，可能不专门进行设置，false和true的效果图如下，true的输入框更大
//        searchView.setQueryHint("搜索");
//        searchView.setSubmitButtonEnabled(true);
//        menu.getItem(0).setActionView(searchView);

        searchView.

        // 获取搜索服务管理器
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // searchable activity的component name，由此系统可通过intent进行唤起
        ComponentName cn = new ComponentName(this,SearchResultActivity.class);
        // 通过搜索管理器，从searchable activity中获取相关搜索信息，就是searchable的xml设置。如果返回null，表示该activity不存在，或者不是searchable
        SearchableInfo info = searchManager.getSearchableInfo(cn);
        if(info == null){
            Log.e("SearchableInfo","Fail to get search info.");
        }
        // 将searchable activity的搜索信息与search view关联
        searchView.setSearchableInfo(info);

        return true;
    }


    private void setSearchView(Menu menu) {

        MenuItem item = menu.getItem(0);
        searchView = new SearchView(this);
        searchView.setIconifiedByDefault(false);//设置展开后图标的样式,false时ICON在搜索框外,true为在搜索框内，无法修改
        searchView.setQueryHint("搜索");
        searchView.setSubmitButtonEnabled(true);//设置最右侧的提交按钮
//        searchView.setOnQueryTextListener(this);

        searchView.setPadding(0,20,0,20);

        //设置搜索框文字颜色
        TextView textView = (TextView)searchView
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setHintTextColor(Color.GRAY);
        textView.setTextColor(Color.WHITE);

        View searchPlate = (View)searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.setBackground(getDrawable(R.drawable.search_plate_bg));

        item.setActionView(searchView);
    }

    @Override
    protected void onNewIntent(Intent intent) {  //activity重新置顶
        super.onNewIntent(intent);
        //doSearchQuery(intent);
    }

    // 对searchable activity的调用仍是标准的intent，我们可以从intent中获取信息，即要搜索的内容
    private void doSearchQuery(String newTex){
        if(newTex == null)
            return;


    }
}