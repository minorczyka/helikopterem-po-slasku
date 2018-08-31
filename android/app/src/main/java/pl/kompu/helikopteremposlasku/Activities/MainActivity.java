package pl.kompu.helikopteremposlasku.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.kompu.helikopteremposlasku.HelikopterApp;
import pl.kompu.helikopteremposlasku.fragments.HistoryFragment;
import pl.kompu.helikopteremposlasku.network.CredentialsProvider;
import pl.kompu.helikopteremposlasku.network.HelikopterService;
import pl.kompu.helikopteremposlasku.fragments.CalendarFragment;
import pl.kompu.helikopteremposlasku.fragments.HomeFragment;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.model.VersionResponse;
import pl.kompu.helikopteremposlasku.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.navigation) NavigationView navigation;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.header_username) TextView navigationUsername;
    private ActionBarDrawerToggle drawerToggle;

    @Inject HelikopterService service;
    @Inject CredentialsProvider credentialsProvider;

    private ArrayList<User> users;
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((HelikopterApp) getApplication()).getHelikopterCompnent().inject(this);

        Intent intent = getIntent();
        users = intent.getParcelableArrayListExtra(LoginActivity.EXTRA_USERS);
        loggedUser = intent.getParcelableExtra(LoginActivity.EXTRA_LOGGED_USER);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        navigation.setNavigationItemSelectedListener(this);
        navigationUsername.setText(loggedUser.name);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = HomeFragment.newInstance(users);
        transaction.replace(R.id.fragment_container, homeFragment).commit();

        checkActualVersion();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        Fragment fragment;
        switch (id) {
            case R.id.nav_home:
                fragment = HomeFragment.newInstance(users);
                break;
            case R.id.nav_calendar:
                fragment = CalendarFragment.newInstance(users);
                break;
            case R.id.nav_histroy:
                fragment = HistoryFragment.newInstance(users);
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_AUTO_LOGIN, false);
                startActivity(intent);
                finish();
                return true;
            default:
                fragment = null;
        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commit();
            drawerLayout.closeDrawers();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void checkActualVersion() {
        String auth = credentialsProvider.getCredentials();
        Call<VersionResponse> responseCall = service.getActualVersion(auth);
        responseCall.enqueue(new Callback<VersionResponse>() {
            @Override
            public void onResponse(Response<VersionResponse> response) {
                if (response.code() == 200) {
                    try {
                        final VersionResponse version = response.body();
                        int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                        if (version.version_code > versionCode) {
                            new MaterialDialog.Builder(MainActivity.this)
                                    .title(R.string.dialog_new_version_title)
                                    .content(R.string.dialog_new_version_content)
                                    .positiveText(R.string.dialog_download)
                                    .negativeText(R.string.dialog_cancel)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(version.link));
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) { }
        });
    }
}
