package a.ghoedev.yoss.Activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import a.ghoedev.yoss.Adapter.ViewPagerAdapter;


import a.ghoedev.yoss.R;
import a.ghoedev.yoss.Util.Method;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PermissionListener {

    private Method method;
    public MaterialToolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DrawerLayout drawer;
    private String[] pageTitle;
    private NavigationView navigationView;
    private ViewPagerAdapter pagerAdapter;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        method = new Method(MainActivity.this);
        method.forceRTLIfSupported();

        pageTitle = new String[]{getResources().getString(R.string.image),
                getResources().getString(R.string.video),
                getResources().getString(R.string.download)};

        toolbar = findViewById(R.id.toolbar_main);
        viewPager = findViewById(R.id.view_pager);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);
        for (int i = 0; i < 3; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //set viewpager adapter
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                navigationView.getMenu().getItem(tab.getPosition()).setChecked(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_side_nav);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        Dexter.withActivity(MainActivity.this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(MainActivity.this)
                .check();

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.Please_click_BACK_again_to_exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);

        //Closing drawer on item click
        drawer.closeDrawers();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.image:
                navigationView.getMenu().getItem(0).setChecked(true);
                viewPager.setCurrentItem(0);
                return true;

            case R.id.video:
                navigationView.getMenu().getItem(1).setChecked(true);
                viewPager.setCurrentItem(1);
                return true;

            case R.id.download:
                navigationView.getMenu().getItem(2).setChecked(true);
                viewPager.setCurrentItem(2);
                return true;

            case R.id.share:
                select(3);
                try {

                    String string = getResources().getString(R.string.Let_me_recommend_you_this_application) + "\n\n" + "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName();

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, string);
                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_one)));

                } catch (Exception e) {
                    //e.toString();
                }
                return true;

            case R.id.rate_app:
                select(4);
                Uri uri = Uri.parse("market://details?id=a.ghoedev.yoss" );
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=a.ghoedev.statussaver" )));
                }
                return true;

            case R.id.more_app:
                select(5);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.play_more_app))));
                return true;

            case R.id.privacy_policy:
                select(6);
                startActivity(new Intent(MainActivity.this, PrivacyPolicy.class));
                return true;

            default:
                return true;
        }
    }

    private void select(int position) {
        navigationView.getMenu().getItem(position).setChecked(false);
        navigationView.getMenu().getItem(position).setCheckable(false);
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        // check for permanent denial of permission
        if (response.isPermanentlyDenied()) {
            // navigate user to ghoedev settings
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.pleas_allow_permission));
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher_round);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.allow_permission),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Dexter.withActivity(MainActivity.this)
                                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .withListener(MainActivity.this)
                                .check();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        token.continuePermissionRequest();
    }
}
