package br.com.ml2s.gefi;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.List;

/**
 * Created by marcossantos on 15/08/2014.
 */
public class Home extends FragmentActivity {

    static final int QTD_PAGES = 2;
    MyPageAdapter myPageAdapter;
    ViewPager vpHome;
    ActionBar actionBar;

    public static class MyPageAdapter extends FragmentPagerAdapter {
        public MyPageAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public int getCount(){
            return QTD_PAGES;
        }

        @Override
        public Fragment getItem(int posicao){
            switch (posicao){
                case 0:
                    return ExibeTexto.init();
                case 1:
                    return Menu.init();
                default:
                    return null;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        myPageAdapter = new MyPageAdapter(getSupportFragmentManager());

        vpHome = (ViewPager)findViewById(R.id.vp_home);

        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int posicao){
                super.onPageSelected(posicao);
                actionBar.setSelectedNavigationItem(posicao);
            }
        };

        vpHome.setOnPageChangeListener(pageChangeListener);
        vpHome.setAdapter(myPageAdapter);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                vpHome.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        ActionBar.Tab tab = actionBar.newTab().setText("Home").setTabListener(tabListener);
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("Menu").setTabListener(tabListener);
        actionBar.addTab(tab);

    }

}
