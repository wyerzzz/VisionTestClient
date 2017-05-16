package com.zzz.wyer.coolpc.wyerzzz_visiontest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button b_login, b_DB;
    private EditText ed_userName, ed_ServerIP;
    private String name, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        findViews();
    }


    private void findViews() {
        b_login = (Button) findViewById(R.id.b_login);
        b_DB = (Button) findViewById(R.id.b_DB);
        ed_userName = (EditText) findViewById(R.id.ed_userName);
        ed_ServerIP = (EditText) findViewById(R.id.ed_ServerIP);

    }

    public void menuSelect(View view) {
        switch (view.getId()) {
            case R.id.b_login:
                name = ed_userName.getText().toString();
                ip = ed_ServerIP.getText().toString();

                if (name.trim().equals("")||ip.trim().equals("")){
                    Toast.makeText(this,"Both fields are required.",Toast.LENGTH_SHORT).show();
                }else {

                    new AlertDialog.Builder(LoginActivity.this).setTitle("Log in")
                            .setMessage("Remember the user name?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
                                    setting.edit().putString("NAME", name)
                                            .commit();

                                    getIntent().putExtra("NAME", name);
                                    getIntent().putExtra("SERVER_IP", ip);
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
                                    setting.edit().putString("NAME", "")
                                            .commit();

                                    getIntent().putExtra("NAME", name);
                                    getIntent().putExtra("SERVER_IP", ip);
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                }
                            })
                            .show();
                }

                break;
            case R.id.b_DB:
                startActivity(new Intent(LoginActivity.this,RecordActivity.class));
                Log.d("intent:","login to record");
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
        ed_userName.setText(setting.getString("NAME", name));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(LoginActivity.this).setMessage("exit the app?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("no",null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
