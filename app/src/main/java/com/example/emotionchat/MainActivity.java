package com.example.emotionchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip_EditText = (EditText) findViewById(R.id.ip_EditText);
        port_EditText = (EditText)findViewById(R.id.port_EditText);
        connectBtn = (Button)findViewById(R.id.connect_Button);
        showText = (TextView)findViewById(R.id.showText_TextView);
        editText_message = (EditText)findViewById(R.id.EditText_message);
        Button_send = (Button)findViewById(R.id.Button_send);
        threadList = new LinkedList<SocketClient>();

        ip_EditText.setText("192.168.83.1");
        port_EditText.setText("5001");

        msghandler = new Handler(){
            @Override
            public void handleMessage(Message hdmsg) {
                if(hdmsg.what == 1111){
                    showText.append(hdmsg.obj.toString() + "\n");
                }
            }
        };

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new SocketClient(ip_EditText.getText().toString(),port_EditText.getText().toString());
                threadList.add(client);
                client.start();
            }
        });

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_message.getText().toString() != null){
                    send = new SendThread(socket);
                    send.start();

                    editText_message.setText("");
                }
            }
        });

    }


    String streammsg = "";
    TextView showText;
    Button connectBtn;
    Button Button_send;
    EditText ip_EditText;
    EditText port_EditText;
    EditText editText_message;
    Handler msghandler;



    SocketClient client;
    ReceiveThread receive;
        SendThread send;
        Socket socket;

        PipedInputStream sendstream = null;
        PipedOutputStream receivestream = null;

        LinkedList<SocketClient> threadList;



        class SocketClient extends Thread {
            boolean threadAlive;
            String ip;
            String port;
            String mac;

        OutputStream outputStream = null;
        BufferedReader  br = null;

        private DataOutputStream output = null;

        public SocketClient(String ip, String port){
            threadAlive = true;
            this.ip = ip;
            this.port = port;
        }

        public void run(){
            try{
                socket = new Socket(ip, Integer.parseInt(port));
                output = new DataOutputStream(socket.getOutputStream());
                receive = new ReceiveThread(socket);
                receive.start();




                WifiManager mng = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();

                output.writeUTF(mac);

            } catch (IOException e ){
                e.printStackTrace();
            }
        }

    }

    class ReceiveThread extends Thread{
        private Socket socket = null;
        DataInputStream input;

        public ReceiveThread(Socket socket){
            this.socket = socket;
            try{
                input = new DataInputStream(socket.getInputStream());
            }catch(Exception e){
            }
        }

        //msg 리시브 -> 핸들러 전달


        @Override
        public void run() {
            try{
                while(input != null){
                    String msg = input.readUTF();
                    if(msg != null){
                        Log.d(ACTIVITY_SERVICE, "test");
                        Message hdmsg = msghandler.obtainMessage();
                        hdmsg.what = 1111;
                        hdmsg.obj = msg;
                        msghandler.sendMessage(hdmsg);
                        Log.d(ACTIVITY_SERVICE, hdmsg.obj.toString());

                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    class SendThread extends Thread {
        private Socket socket;
        String sendmsg = editText_message.getText().toString();
        DataOutputStream output;

        public SendThread(Socket socket){
            this.socket = socket;
            try{
                output = new DataOutputStream(socket.getOutputStream());
            }catch (Exception e){

            }
        }


        public void run() {
            try{
                //msg 전송부 (mag으로 사용자 식별)
                Log.d(ACTIVITY_SERVICE, "1111");
                String mac = null;
                WifiManager mng = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();

                if (output != null){
                    if (sendmsg != null){
                        output.writeUTF(mac + " : " + sendmsg);
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            } catch (NullPointerException npe){
                npe.printStackTrace();
            }
        }
    }


}
