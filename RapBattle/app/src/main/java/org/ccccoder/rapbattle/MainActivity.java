package org.ccccoder.rapbattle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ccccoder.rapbattle.Fragment.LoginFragment;
import org.ccccoder.rapbattle.Fragment.RecordFragment;
import org.ccccoder.rapbattle.Fragment.RecordListFragment;
import org.ccccoder.rapbattle.Fragment.TitleListFragment;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences("Auto_loginInfo2", Activity.MODE_PRIVATE);
        editor = pref.edit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        isAuto(); //자동로그인 or 로그인 창이동
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = new LoginFragment();
        String title = getString(R.string.app_name);

        if (id == R.id.make_rap) {
            fragment = new RecordFragment();
            title = "랩 만들기";
        } else if (id == R.id.my_rap) {
            fragment = new TitleListFragment();
            title = "나의 랩 모음";
        } else if (id == R.id.title_list) {
            fragment = new TitleListFragment();
            title = "주제 모음";
        } else if (id == R.id.bit_list) {
            fragment = new RecordListFragment();
            title = "비트 모음";
        } else if (id == R.id.hot_list) {
            fragment = new RecordListFragment();
            title = "HOT 리스트";
        } else if (id == R.id.logout) {
            editor.clear(); //실패시 초기화
            editor.commit();
            fragment = new LoginFragment();
            title = "로그아웃";
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }
   /*
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
   */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loginCheck(final String id,final String pw){
        Thread thread = new Thread() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();

                String urlString = "http://ccccode.herokuapp.com/login";
                try {
                    URI url = new URI(urlString);

                    HttpPost httpPost = new HttpPost();
                    httpPost.setURI(url);

                    List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("idInput", id));
                    nameValuePairs.add(new BasicNameValuePair("pwInput", pw));

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                    HttpResponse response = httpClient.execute(httpPost);
                    String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                    Log.d(TAG, responseString);
                    if(responseString.contains("success")) {
                        EndHandler.sendEmptyMessage(0);
                    } else {
                        EndHandler.sendEmptyMessage(1);
                    }
                //Toast.makeText(getActivity().getApplicationContext(),responseString,Toast.LENGTH_LONG).show();
                } catch (URISyntaxException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }

            }

        };
        thread.start();
    }
    public Handler EndHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==0){
                Toast.makeText(getApplicationContext(),"자동 로그인 완료",Toast.LENGTH_LONG).show();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_fragment_layout, new RecordFragment());
                ft.commit();
            }else if(msg.what==1){
                editor.clear(); //실패시 초기화
                editor.commit();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_fragment_layout, new LoginFragment());
                ft.commit();
            }
        }
    };
    private void isAuto() {
        if (pref.getBoolean("autologin", false)) {
            // goto mainActivity
            String id = pref.getString("idInput", "");
            String pw = pref.getString("pwInput", "");
            loginCheck(id, pw);//로그인
        }else
        {
            editor.clear(); //실패시 초기화
            editor.commit();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, new LoginFragment());
            ft.commit();
        }
    }

}
