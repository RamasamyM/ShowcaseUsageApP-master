package com.example.admin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.google.android.gms.analytics.Tracker;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "Mainactivity";
    private static final String FAB1 = "action_one";
    private static final String FAB2 = "action_two";
    private static final String FAB3 = "action_three";
    private FloatingActionButton mainFab, chartFab , emptyFab ;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private static final String SHOWCASEVIEW  = "showcase";
    private Boolean isFabOpen = true;
    RecyclerView recyclerView;
    private Tracker  mTracker;
    private String name= null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbar(toolbar, SHOWCASEVIEW, "Welcome to the ToolBar click me! ");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mainFab = (FloatingActionButton) findViewById(R.id.main_fab_button);
        chartFab = (FloatingActionButton) findViewById(R.id.pie_chart_fab_button);
        //     chartFab.setVisibility(View.GONE);
        emptyFab= (FloatingActionButton)findViewById(R.id.empty_fab_button);
        //     emptyFab.setVisibility(View.GONE);
        mainFab.setOnClickListener(this);
        setFloatingActionButton(mainFab, SHOWCASEVIEW,"Welcome to the mainFab click me!");
        chartFab.setOnClickListener(this);
        emptyFab.setOnClickListener(this);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        /*setFloatingActionButton(this.chartFab, SHOWCASEVIEW,"Welcome to the chartFab click me!");
        setFloatingActionButton(this.emptyFab, SHOWCASEVIEW,"Welcome to the chartFab click me!");*/
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item1) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            startActivity(new Intent(getApplication(), SecondActivity.class));
        }
        else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void setFloatingActionButton(FloatingActionButton view, String usageId, String text)
    {

        new CustomClassView.Builder(this)
                .enableDotAnimation(true)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.ALL)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(usageId) //THIS SHOULD BE UNIQUE ID
                .show();
    }

    private void setToolbar(View view, String usageId, String text)
    {
        new CustomClassView.Builder(this)
                .enableDotAnimation(true)
                .setFocusGravity(FocusGravity.LEFT)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(usageId) //THIS SHOULD BE UNIQUE ID
                .show();
    }
    @Override
    public void onClick(View v)
    {
        int id =v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.main_fab_button:
                animateFAB();
                break;
            case R.id.pie_chart_fab_button:
                intent = new Intent(this,SecondActivity.class);
                Log.i(TAG, "pie chart activity call");
                break;
            case R.id.empty_fab_button:
                intent= new Intent(this, SipActivity.class);
                Log.i(TAG, "empty chart activity call");
            default:
                break;
        }
        if(intent!=null) {
            startActivity(intent);
        }
    }
    public void animateFAB()
    {
        if (isFabOpen)
        {
            mainFab.startAnimation(rotate_backward);
            chartFab.startAnimation(fab_close);
            emptyFab.startAnimation(fab_close);
            chartFab.setClickable(false);
            emptyFab.setClickable(false);
            isFabOpen= false;
            Log.d("Ramasamy", "close");
        }
        else
        {
            mainFab.startAnimation(rotate_forward);
            chartFab.startAnimation(fab_open);
            emptyFab.startAnimation(fab_open);
            if(!getDefaults("shown",getApplicationContext()))
            {
                if(chartFab != null)
                {
                   /* chartFab.setOnClickListener(this);
                      setFloatingActionButton(chartFab, SHOWCASEVIEW,"Welcome to the chartFab click me!");*/
                }
                if(emptyFab!= null)
                {
                   /* new ShowcaseView.Builder(this)
                            .setTarget(new ViewTarget(findViewById(R.id.empty_fab_button)))
                            .setContentText("welcome to the cardFAB action")
                            .hideOnTouchOutside()
                            .useDecorViewAsParent()
                            .build();
                    System.out.println("floating button for  emptyFab");*/
                }
                setDefaults("shown",true,getApplicationContext());
            }
            emptyFab.setClickable(true);
            chartFab.setClickable(true);
            isFabOpen = true;
            Log.d("Ramasamy", "open");
        }
    }
    private void setDefaults(String key, boolean value, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    private boolean getDefaults(String key, Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }
    @Override
    protected void onResume()
    {

        super.onResume();

    }
}
