package com.microtecweb.css_mobile;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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
import entity.EConstant;
import entity.EDrawerItem;


public class MainMenuActivity extends ActionBarActivity {

    private Fragment fragment = null;
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
        setContentView(R.layout.activity_main_menu);
        dataList = new ArrayList<EDrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        dataList.add(new EDrawerItem("Service Entry", R.drawable.ic_open_service));
        dataList.add(new EDrawerItem("My Open Service", R.drawable.ic_action_cloud));
        dataList.add(new EDrawerItem("Completed Service", R.drawable.ic_close_service));
        dataList.add(new EDrawerItem("Todo List", R.drawable.ic_action_search));
        dataList.add(new EDrawerItem("Service History", R.drawable.ic_action_email));
        dataList.add(new EDrawerItem("Talk to Leader", R.drawable.ic_talk_leader));
        dataList.add(new EDrawerItem("About", R.drawable.ic_action_about));
        dataList.add(new EDrawerItem("Help", R.drawable.ic_action_help));
        dataList.add(new EDrawerItem("Logout", R.drawable.ic_quit));
        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            SelectItem(1);
        }
    }

    public void SelectItem(int position) {
        FragmentManager frgManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;

        if(position != 8) {
            fragmentTransaction = frgManager.beginTransaction();
            Bundle args = new Bundle();
            while (getFragmentManager().getBackStackEntryCount() > 0) {
                Fragment openServiceDetailFragment = getFragmentManager().findFragmentByTag("OpenServiceDetailFragment");
                if (openServiceDetailFragment != null) {
                    fragmentTransaction.remove(openServiceDetailFragment);
                    getFragmentManager().popBackStackImmediate();
                    fragmentTransaction.commit();
                }
                Fragment openServiceFragment = getFragmentManager().findFragmentByTag("OpenServiceFragment");
                if (openServiceFragment != null) {
                    fragmentTransaction.remove(openServiceFragment);
                    getFragmentManager().popBackStackImmediate();
                    fragmentTransaction.commit();
                }
                break;
            }
        }
        boolean flagSetFragment = true;
        switch (position) {
            case 0:
                fragment = new RequestServiceFragment();
                fragmentTransaction = frgManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, "RequestServiceFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1:
                fragment = new OpenServiceFragment();
                fragmentTransaction = frgManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, "OpenServiceFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case 2:
                fragment = new CloseServiceFragment();
                fragmentTransaction = frgManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, "CloseServiceFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case 4:
                fragment = new ServiceHistoryFragment();
                fragmentTransaction = frgManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, "ServiceHistoryFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 5:
                Intent myIntent = new Intent(this, ChatActivity.class);
                startActivity(myIntent);
                break;

            case 8:
                flagSetFragment = false;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Confirm logout");
                alertDialogBuilder
                        .setMessage("Are you sure you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final SharedPreferences sharedpreferences = getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(EConstant.MY_PREFERENCES_PASSWORD, "");
                                editor.putBoolean(EConstant.MY_PREFERENCES_REMEMBER, false);
                                editor.commit();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            default:
                break;
        }
        if (flagSetFragment) {
            mDrawerList.setItemChecked(position, true);
            setTitle(dataList.get(position).getItemName());
            mDrawerLayout.closeDrawer(mDrawerList);
        }
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
                Fragment closeServiceFragment = getFragmentManager().findFragmentByTag("CloseServiceFragment");
                if (closeServiceFragment != null) {
                    if (!closeServiceFragment.isVisible())
                        fragmentTransaction.show(closeServiceFragment);
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
        } else if (requestCode == EConstant.REQUEST_CODE_PHOTO_GALLERY_FOR_REQUESET_SERVICE || requestCode == EConstant.REQUEST_CODE_TAKE_PHOTO_FOR_REQUESET_SERVICE) {
            RequestServiceFragment a = (RequestServiceFragment) fragment;
            a.onActivityResult(requestCode, resultCode, data);
        }
    }
}
