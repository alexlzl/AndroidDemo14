package com.lyl.bottombar;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

import com.lyl.bottombar.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private BottomBar bottomBar;

    private  BottomBarTab nearby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_nearby) {
                    // 选择指定 id 的标签
                    nearby = bottomBar.getTabWithId(R.id.tab_nearby);
                    nearby.setBadgeCount(5);
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_nearby) {
                    // 已经选择了这个标签，又点击一次。即重选。
                    nearby.removeBadge();
                }
            }
        });
    }
}
