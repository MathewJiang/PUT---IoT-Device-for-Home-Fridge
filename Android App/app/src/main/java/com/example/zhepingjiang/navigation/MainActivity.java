package com.example.zhepingjiang.navigation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public String dbResult;

    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "QR Scanner (Coming Soon)", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        handler = new Handler(getApplicationContext().getMainLooper());

        displaySelectedScreen(R.id.content_main);

//        new downloadDBResultTask().execute("");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
////                    f_dbResult = ServerRequestHandler.getAlltemp();
//                    Log.i(TAG, "The value for f_dbResult is: " + f_dbResult);
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            resultTextView.setText("reached");
//                        }
//                    });
//                }
//            }
//        }).start();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Helper function for switch between fragments
     */
    private void displaySelectedScreen(int id){
        Fragment f = null;

        switch (id){
            case R.id.nav_display:
                f = new DisplayItemFragment();
                break;
            case R.id.nav_add:
                f = new AddItemFragment();
                break;
            case R.id.nav_delete:
                f = new DeleteItemFragment();
                break;
            default:
                f = new DisplayItemFragment();
                break;
        }

        if (f != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, f);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_display) {
            // Handle the camera action
        } else if (id == R.id.nav_add) {

        } else if (id == R.id.nav_delete) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        displaySelectedScreen(id);
        return true;
    }


//    /*
//     * The three types used by an asynchronous task are the following:
//     * Params, the type of the parameters sent to the task upon execution.
//     * Progress, the type of the progress units published during the background computation.
//     * Result, the type of the result of the background computation.
//     */
//    private class downloadDBResultTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//
//                String url_str = "http://ece496puts.ddns.net:59496/raw_sql/use putsDB;select std_name from purchase_history;";
//                url_str = url_str.replaceAll(" ", "%20");
//                System.out.println("url_str is " + url_str);
//
//                URL url = new URL(url_str);
//
//
//                StringBuilder result = new StringBuilder();
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    result.append(line);
//                }
//                rd.close();
//
//                System.out.println("\n>>>>Start printing result>>>>\n");
//                String result_str = result.toString();
//
//                result_str = result_str.replace("<!DOCTYPE html    PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en-US\" xml:lang=\"en-US\"><head><title>sql response</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /></head><body><h1>",
//                        "");
//                result_str = result_str.replace("</h1>", "");
//                System.out.println(result_str);
//
//                System.out.println("\n>>>>End printing result>>>>\n");
//
//                return result_str;
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//    }
}
