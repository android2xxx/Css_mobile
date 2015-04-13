package com.microtecweb.css_mobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomDrawerAdapter;
import entity.EDrawerItem;
import entity.EConstant;


public class MainMenuActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<EDrawerItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.AppBaseTheme);
        //setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        //setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.activity_main_menu);

        dataList = new ArrayList<EDrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        dataList.add(new EDrawerItem("Service Entry", R.drawable.ic_open_service));
        dataList.add(new EDrawerItem("My Open Service", R.drawable.ic_action_cloud));
        dataList.add(new EDrawerItem("Completed Service", R.drawable.ic_close_service));
        dataList.add(new EDrawerItem("Todo List", R.drawable.ic_action_search));
        dataList.add(new EDrawerItem("Service History", R.drawable.ic_action_email));
        dataList.add(new EDrawerItem("Talk to Leader", R.drawable.ic_talk_leader));
        dataList.add(new EDrawerItem("About", R.drawable.ic_action_about));
        dataList.add(new EDrawerItem("Help", R.drawable.ic_action_help));
        dataList.add(new EDrawerItem("Logout", R.drawable.ic_action_about));
        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            SelectItem(1);
        }
    }

    Fragment fragment = null;

    public void SelectItem(int position) {
        FragmentManager frgManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = frgManager.beginTransaction();

        Bundle args = new Bundle();

        while (getFragmentManager().getBackStackEntryCount() > 0) {
            Fragment openServiceDetailFragment = getFragmentManager().findFragmentByTag("OpenServiceDetailFragment");
            if (openServiceDetailFragment != null) {
                fragmentTransaction.remove(openServiceDetailFragment);
                getFragmentManager().popBackStackImmediate();
            }

            Fragment openServiceFragment = getFragmentManager().findFragmentByTag("OpenServiceFragment");
            if (openServiceFragment != null) {
                fragmentTransaction.remove(openServiceFragment);
                getFragmentManager().popBackStackImmediate();
            }
        }

        switch (position) {
            case 0:
                fragment = new RequestService();
                fragmentTransaction.replace(R.id.content_frame, fragment, "RequestServiceFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1:
                fragment = new OpenServiceFragment();

                fragmentTransaction.replace(R.id.content_frame, fragment, "OpenServiceFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case 4:
                fragment = new ServiceHistoryFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment, "ServiceHistoryFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 5:
                /*fragment = new ServiceHistoryFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment, "ChatToLeader");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                */
                Intent myIntent = new Intent(this, ChatActivity.class);
                startActivity(myIntent);
                break;

            case 8:
                final SharedPreferences sharedpreferences = getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(EConstant.MY_PREFERENCES_PASSWORD, "");
                editor.putBoolean(EConstant.MY_PREFERENCES_REMEMBER, false);
                System.exit(0);
                break;
            default:
                break;
        }
        mDrawerList.setItemChecked(position, true);
        setTitle(dataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerVisible(Gravity.START))
            mDrawerLayout.closeDrawer(Gravity.START);
        else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment openServiceDetailFragment = getFragmentManager().findFragmentByTag("OpenServiceDetailFragment");
                if (openServiceDetailFragment != null)
                    fragmentTransaction.hide(openServiceDetailFragment);
                Fragment openServiceFragment = getFragmentManager().findFragmentByTag("OpenServiceFragment");
                if (openServiceFragment != null) {
                    if (!openServiceFragment.isVisible())
                        fragmentTransaction.show(openServiceFragment);
                    else
                        super.onBackPressed();
                }
                Fragment serviceHistoryFragment = getFragmentManager().findFragmentByTag("ServiceHistoryFragment");
                if (serviceHistoryFragment != null) {
                    if (!serviceHistoryFragment.isVisible())
                        fragmentTransaction.show(serviceHistoryFragment);
                    else
                        super.onBackPressed();
                }
                Fragment requestServiceFragment = getFragmentManager().findFragmentByTag("RequestServiceFragment");
                if (requestServiceFragment != null) {
                    if (!requestServiceFragment.isVisible())
                        fragmentTransaction.show(requestServiceFragment);
                    else
                        super.onBackPressed();
                }
                fragmentTransaction.commit();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }


    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EConstant.REQUEST_CODE_PHOTO_GALLERY || requestCode == EConstant.REQUEST_CODE_TAKE_PHOTO) {
            OpenServiceFragment a = (OpenServiceFragment) fragment;
            a.openServiceDetailFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
