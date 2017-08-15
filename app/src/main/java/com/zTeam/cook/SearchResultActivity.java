package com.zTeam.cook;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Richard on 8/10/17.
 */

public class SearchResultActivity extends AppCompatActivity {
    private TextView tv;
    //存放查询字符串数组
    private Bundle bundle;
    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searcher);
        tv = (TextView)findViewById(R.id.tV);
        Log.i("msg","INTO SearchResultsActivity");
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(getIntent());
    }

    /**
     * @param intent
     * 处理intent
     */
    private void handleIntent(Intent intent){

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            bundle = intent.getExtras();
            query =bundle.getString(SearchManager.QUERY);
            Log.i("msg",query);
            tv.setText(query);
        }
    }

}
