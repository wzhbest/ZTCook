package com.fedming.bottomnavigationdemo;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by bruce on 2016/11/1.
 * HomeActivity 主界面
 */

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        doSearchQuery(getIntent());

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bmap_menu, menu);


        //getActionBar().setDisplayShowTitleEnabled(false);

        // 获取SearchView对象
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        if(searchView == null){
            Log.e("SearchView","Fail to get Search View.");
            return true;
        }
        searchView.setIconifiedByDefault(true); // 缺省值就是true，可能不专门进行设置，false和true的效果图如下，true的输入框更大

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
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchItem.expandActionView();

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {  //activity重新置顶
        super.onNewIntent(intent);
        doSearchQuery(intent);
    }

    // 对searchable activity的调用仍是标准的intent，我们可以从intent中获取信息，即要搜索的内容
    private void doSearchQuery(Intent intent){
        if(intent == null)
            return;

        String queryAction = intent.getAction();
        if( Intent.ACTION_SEARCH.equals( intent.getAction())){  //如果是通过ACTION_SEARCH来调用，即如果通过搜索调用
            String queryString = intent.getStringExtra(SearchManager.QUERY); //获取搜索内容

        }

    }
}