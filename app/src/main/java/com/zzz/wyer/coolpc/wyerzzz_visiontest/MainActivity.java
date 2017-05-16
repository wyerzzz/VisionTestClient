package com.zzz.wyer.coolpc.wyerzzz_visiontest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ImageButton ib_up,ib_down,ib_left,ib_right,ib_select;
    private TextView tv_clientMsg,tv_backLogin,tv_start;
    private String ip;

    public static final int FUNC_LOGIN = 1001;
    public static final int BUFFER_SIZE = 256;
    boolean logon = false;

    private Socket socket;
    private ConnSock connThread;
    private BufferedReader clientR;
    private BufferedWriter clientW;
    public String name;
    public String getVission;

    public int eye_index = 0;
    private String[] eyes = {"LEFT", "RIGHT"};
    public String[] visions = {" "," "};
    public String left_vision,right_vision;

    private DBSQL myRecord;
//=======================================================================
    public static final int FUNC_UP = 0;
    public static final int FUNC_DOWN = 1;
    public static final int FUNC_LEFT = 2;
    public static final int FUNC_RIGHT = 3;
    public static final int FUNC_PASS = 4;
    public static final int FUNC_START = 5;
//======================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("DrVision_Client");

        findViews();
        login();
    }

    private void findViews(){
        ib_up = (ImageButton)findViewById(R.id.ib_up);
        ib_down = (ImageButton)findViewById(R.id.ib_down);
        ib_left = (ImageButton)findViewById(R.id.ib_left);
        ib_right = (ImageButton)findViewById(R.id.ib_right);
        ib_select = (ImageButton)findViewById(R.id.ib_select);
        tv_clientMsg = (TextView)findViewById(R.id.tv_clientMsg);
        tv_backLogin = (TextView)findViewById(R.id.tv_backLogin);
        tv_start = (TextView)findViewById(R.id.tv_start);

        tv_clientMsg.setText("Client Msg : ");
        tv_start.setEnabled(false);
        tv_start.setAlpha(0.3f);

        buttonEnable(false,0.3f);
    }
    //button click
    public void onClick(View view) throws IOException {
        switch (view.getId()){
            case R.id.ib_up:
                if(socket.isConnected()){
                    try {
                        clientW.write(FUNC_UP);
                        clientW.flush();
                        tv_clientMsg.setText("Client Msg : Up");
                        Thread.sleep(300);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("btnTag","up");
                }else{
                    clientW.close();
                }
                break;
            case R.id.ib_down:
                if(socket.isConnected()){
                    try {
                        clientW.write(FUNC_DOWN);
                        clientW.flush();
                        tv_clientMsg.setText("Client Msg : Down");
                        Thread.sleep(300);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("btnTag","down");
                }else{
                    clientW.close();
                }
                break;
            case R.id.ib_left:
                if(socket.isConnected()){
                    try {
                        clientW.write(FUNC_LEFT);
                        clientW.flush();
                        tv_clientMsg.setText("Client Msg : Left");
                        Thread.sleep(300);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("btnTag","left");
                }else{
                    clientW.close();
                }
                break;
            case R.id.ib_right:
                if(socket.isConnected()){
                    try {
                        clientW.write(FUNC_RIGHT);
                        clientW.flush();
                        tv_clientMsg.setText("Client Msg : Right");
                        Thread.sleep(300);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("btnTag","right");
                }else{
                    clientW.close();
                }
                break;
            case R.id.ib_select:
                if(socket.isConnected()){
                    try {
                        clientW.write(FUNC_PASS);
                        clientW.flush();
                        tv_clientMsg.setText("Client Msg : Pass");
                        Thread.sleep(300);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("btnTag","pass");
                }else{
                    clientW.close();
                }
                break;
            case R.id.tv_start:
                new AlertDialog.Builder(this)
                        .setTitle("Test the __ eye.")
                        .setSingleChoiceItems(eyes, eye_index, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i >= 0) {
                                    eye_index = i;
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startTest();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;
            case R.id.tv_backLogin:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Back to Log In?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (socket != null) {
                                    try {
                                        socket.close();
                                        Log.d("back", "close socket");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                buttonEnable(false, 0.3f);
                                tv_start.setEnabled(false);
                                tv_start.setAlpha(0.3f);
                                login();
                            }
                        })
                        .show();
                break;
        }
    }
    //socket connect
    private class ConnSock extends Thread{
        private String name,ip;

        public ConnSock(String name,String ip){
            this.name = name;
            this.ip = ip;
        }

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(1000);
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr,5055);
                boolean isConnected = socket.isConnected();
                Log.d("ConnSock isconnected",isConnected+"");
                new ConnectTask().execute(isConnected);

                clientR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                clientW = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Log.d("BufferedReader isReady",clientR.ready()+"");
                while (isConnected){
                    String message ;
                    int charsRead = 0;
                    char[] buffer = new char[BUFFER_SIZE];
                    while ((charsRead = clientR.read(buffer)) != -1) {
                        message = new String(buffer).substring(0, charsRead);
                        Log.d("getMessage ",message);
                        getVission = message;
                        new getMsgTask().execute(message);
                    }
                }
                clientW.close();
                clientR.close();
                socket.close();
                Log.d("thread","close socket");

            } catch (UnknownHostException e) {
                e.printStackTrace();
                new ConnectTask().execute(false);
                Log.d("connThread","Unknown");
                if (socket != null){
                    try {
                        socket.close();
                        Log.d("Unkuown","close socket");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                new ConnectTask().execute(false);
                Log.d("connThread","IOE");
                if (socket != null){
                    try {
                        socket.close();
                        Log.d("IOE","close socket");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //Task
    class ConnectTask extends AsyncTask<Boolean,Boolean,Void>{
        @Override
        protected Void doInBackground(Boolean... booleen) {
            Log.d("Task","tag/"+booleen[0]);
            publishProgress(booleen);
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            if (values[0] == true) {
                Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();
                tv_clientMsg.setText("Client Msg : Connected");
                tv_start.setEnabled(true);
                tv_start.setAlpha(1.0f);
            }else{
                tv_clientMsg.setText("Client Msg : Unconnected");
                Toast.makeText(MainActivity.this,"Connection Error.",Toast.LENGTH_SHORT).show();
            }
            Log.d("Task:isconnected",values[0]+"");
        }
    }
    class getMsgTask extends AsyncTask<String,String,Void>{

        @Override
        protected Void doInBackground(String... msg) {
            publishProgress(msg[0]);
            visions[eye_index] = msg[0];
            return null;
        }

        @Override
        protected void onProgressUpdate(String... msg) {
            super.onProgressUpdate(msg);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DrVision")
                    .setMessage("Your Vision : "+msg[0]+"\n"+
                            "Continue to test the other eye?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            eye_index = 1-eye_index;
                            startTest();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(MainActivity.this,"insert",Toast.LENGTH_SHORT).show();
                            left_vision = visions[0];
                            right_vision = visions[1];
                            Log.d("vision_result","left/"+left_vision+","+"right/"+right_vision);
                            visions = new String[]{" "," "};
                            for (String s : visions){
                                Log.d("visions[]",s);
                            }
                            Calendar mCal = Calendar.getInstance();
                            CharSequence s = DateFormat.format("yyyy/MM/dd  kk:mm", mCal.getTime());
                            String getDateTime = s.toString();
                            Log.d("getTime",getDateTime);

                            myRecord = DBSQL.getInstance(MainActivity.this);
                            long result = myRecord.dataInsert(getDateTime,name,left_vision,right_vision);
                            if (result>=0){
                                Toast.makeText(MainActivity.this,"Test completed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();

            buttonEnable(false,0.3f);
            tv_start.setEnabled(true);
            tv_start.setAlpha(1.0f);
            Log.d("GetVision",eyes[eye_index]+":"+msg[0]);
        }
    }

    //login
    public void login(){
        if(!logon){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent,FUNC_LOGIN);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FUNC_LOGIN){
            if (resultCode == RESULT_OK){
                name = data.getStringExtra("NAME");
                ip = data.getStringExtra("SERVER_IP");
                Log.d("name/ip",name+"/"+ip);

                Toast.makeText(MainActivity.this, "The client is waiting...", Toast.LENGTH_SHORT).show();
                connThread = new ConnSock(name, ip);
                connThread.start();
            }else{
                Log.d("resultcode",resultCode+"");
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(MainActivity.this).setMessage("exit the app?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("no", null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void buttonEnable(boolean enable,float alpha){
        ib_up.setEnabled(enable);
        ib_down.setEnabled(enable);
        ib_left.setEnabled(enable);
        ib_right.setEnabled(enable);
        ib_select.setEnabled(enable);

        ib_up.setAlpha(alpha);
        ib_down.setAlpha(alpha);
        ib_left.setAlpha(alpha);
        ib_right.setAlpha(alpha);
        ib_select.setAlpha(alpha);
    }

    private void startTest(){
        try {
            if (socket.isConnected()) {

                clientW.write(FUNC_START);
                clientW.flush();
                tv_clientMsg.setText("Client Msg : Start the test");

                buttonEnable(true, 1.0f);
                tv_start.setEnabled(false);
                tv_start.setAlpha(0.3f);
                Log.d("btnTag", "start");
                Log.d("eye_index", eye_index + "");//0 or 1
            } else {
                clientW.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}