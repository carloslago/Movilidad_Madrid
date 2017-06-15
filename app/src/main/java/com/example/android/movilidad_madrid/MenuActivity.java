package com.example.android.movilidad_madrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


//        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
//        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

//        Log.d("a", ""+hasLoggedIn);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("hasLoggedIn", false);
//        editor.apply();
//        hasLoggedIn = false;
//        if (hasLoggedIn){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        } else {
//            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
//        }
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
            intent.putExtra("ID_USUARIO", getIntent().getStringExtra("ID_USUARIO"));
            intent.putExtra("DESTINOS", getIntent().getStringExtra("DESTINOS"));
            intent.putExtra("FAVORITOS", getIntent().getStringExtra("FAVORITOS"));
            startActivity(intent);
//            startActivity(new Intent(MenuActivity.this, MapsActivity.class));
        } else if (id == R.id.nav_Recientes) {
            Intent intent = new Intent(MenuActivity.this, DestinosRecientesActivity.class);
            intent.putExtra("ID_USUARIO", getIntent().getStringExtra("ID_USUARIO"));
            intent.putExtra("DESTINOS", getIntent().getStringExtra("DESTINOS"));
            intent.putExtra("FAVORITOS", getIntent().getStringExtra("FAVORITOS"));
            startActivity(intent);

        } else if (id == R.id.nav_favourite) {
            Intent intent = new Intent(MenuActivity.this, FavoritosActivity.class);
            intent.putExtra("ID_USUARIO", getIntent().getStringExtra("ID_USUARIO"));
            intent.putExtra("DESTINOS", getIntent().getStringExtra("DESTINOS"));
            intent.putExtra("FAVORITOS", getIntent().getStringExtra("FAVORITOS"));
            startActivity(intent);
        } else if (id == R.id.nav_exit_to_app) {
            SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
            boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
            Log.d("a", ""+hasLoggedIn);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("hasLoggedIn", false);
            editor.apply();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
