package org.ccccoder.rapbattle.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ccccoder.rapbattle.MainActivity;
import org.ccccoder.rapbattle.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

/**
 * Created by Sangdeok on 2017-08-05.
 */

public class LoginFragment extends Fragment {
    private EditText input_id;
    private EditText input_pw;
    private Button login_btn;
    private Button register_btn;
    private CheckBox autologin;
    private TextView message;
    Boolean loginChecked;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String login_result ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginChecked = true;
        pref = getActivity().getSharedPreferences("Auto_loginInfo2", Activity.MODE_PRIVATE);
        editor = pref.edit();
        input_id = (EditText)view.findViewById(R.id.input_id);
        input_pw = (EditText)view.findViewById(R.id.input_pw);
        input_pw.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        input_pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        login_btn = (Button)view.findViewById(R.id.login);
        register_btn = (Button)view.findViewById(R.id.register);
        message = (TextView)view.findViewById(R.id.message);
        autologin = (CheckBox)view.findViewById(R.id.autologin);
        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                     loginChecked = true;
                    else {
                    loginChecked = false;
                    editor.clear();
                    editor.commit();
                    }

            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if autoLogin checked, get input
                if (pref.getBoolean("autologin", false)) {
                    input_id.setText(pref.getString("idInput", ""));
                    input_pw.setText(pref.getString("pwInput", ""));
                    // goto mainActivity
                    String id = pref.getString("idInput", "");
                    String pw = pref.getString("pwInput", "");
                    loginCheck(id, pw);//로그인
                    editor.clear(); //실패시 초기화
                    editor.commit();
                    message.setText(login_result);
                } else {
                    // if autoLogin unChecked
                    String id = input_id.getText().toString();
                    String password = input_pw.getText().toString();
                    // save id, password to Database
                        // goto mainActivity
                        loginCheck(id,password);
                        message.setText(login_result);

                }

            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    }
        });

        return view;
    }

    private void SaveLoginInfo(String id, String pw) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("idInput", id);
        editor.putString("pwInput", pw);
        editor.commit();
        //editor.remove("score");
        //editor.clear();
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
                        if (autologin.isChecked() == true) {
                            editor.clear();
                            editor.putBoolean("autologin", true);
                            editor.putString("idInput", id);
                            editor.putString("pwInput", pw);
                            editor.commit();
                        }
                        EndHandler.sendEmptyMessage(0);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_fragment_layout, new RecordFragment());
                        ft.commit();
                    } else {
                        login_result = responseString;
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
                Toast.makeText(getActivity().getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
            }else if(msg.what==1){
                Toast.makeText(getActivity().getApplicationContext(),login_result,Toast.LENGTH_LONG).show();
            }
        }
    };


}
