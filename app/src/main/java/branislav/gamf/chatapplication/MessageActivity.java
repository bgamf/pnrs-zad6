package branislav.gamf.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        final Button logoutButton = (Button) findViewById(R.id.logoutButton2);
        final Button sendButton = (Button) findViewById(R.id.sendButton);
        final Button refreshButton = (Button) findViewById(R.id.refreshButton1);
        final EditText messageTextEdit = (EditText) findViewById(R.id.messsage);
        final TextView friendFullName = (TextView) findViewById(R.id.friendFullName);

        final SharedPreferences prefs = getSharedPreferences("PrefsFile",MODE_PRIVATE);
        final String receiverUsername = prefs.getString("reciever_username",null);
        final String loggedinUsername = prefs.getString("logged_username",null);
        final DatabaseHelper db = new DatabaseHelper(this);
        final ArrayList<Message> messages = new ArrayList<Message>();

        friendFullName.setText(receiverUsername);

        final HttpHelper httphelper = new HttpHelper();
        final Handler handler = new Handler();
        final CustomAdapter2 adapter = new CustomAdapter2(this);


        updateMessages(adapter);
        /*db.readMessages(messages,receiverUserID,senderUserID);

        adapter.update(messages,senderUserID);
*/
        ListView list = (ListView) findViewById(R.id.customListMessages);
        list.setDivider(null);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               /* MessageItem forDelete = (MessageItem) adapter.getItem(position);
                db.deleteMessage(forDelete.getMessageID());
                db.readMessages(messages,receiverUserID,senderUserID);
                adapter.update(messages,senderUserID);*/
                return true;
            }
        });

        messageTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String message = messageTextEdit.getText().toString();
                if(message.length()!=0)
                    sendButton.setEnabled(true);
                else
                    sendButton.setEnabled(false);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMessages(adapter);
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String message = messageTextEdit.getText().toString();
                Message m = new Message(null,loggedinUsername,null,message);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject object = new JSONObject();
                            object.put("receiver",receiverUsername);
                            object.put("data",message);

                            final boolean response = httphelper.sendMessageToServer(MessageActivity.this,httphelper.URL_MESSAGE_SEND,object);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(response){
                                        Toast.makeText(getApplicationContext(),R.string.message_sent,(int) Toast.LENGTH_SHORT).show();
                                        updateMessages(adapter);
                                    }
                                    else{
                                        Toast.makeText(MessageActivity.this,prefs.getString("sendMessage_error",null),Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }catch(JSONException e){
                            e.printStackTrace();
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                    }
                }).start();





                messageTextEdit.setText("");

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            final boolean response = httphelper.logOutFromServer(MessageActivity.this,httphelper.URL_LOGOUT);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(response){
                                        Toast.makeText(MessageActivity.this,R.string.valid_logout,Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MessageActivity.this,MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(MessageActivity.this,prefs.getString("logout_error",null),Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        } catch(JSONException e){
                            e.printStackTrace();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }
    public void updateMessages(final CustomAdapter2 adapter){
        final HttpHelper httphelper = new HttpHelper();
        final Handler handler = new Handler();
        final SharedPreferences prefs = getSharedPreferences("PrefsFile",MODE_PRIVATE);
        final String receiverUsername = prefs.getString("reciever_username",null);
        final String loggedinUsername = prefs.getString("logged_username",null);
        final ArrayList<Message> messages = new ArrayList<Message>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final JSONArray array = httphelper.getMessagesFromServer(MessageActivity.this,httphelper.URL_MESSAGES + receiverUsername);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(array != null){
                                JSONObject temp;
                                for(int i = 0;i < array.length();i++){
                                    try{
                                        temp = array.getJSONObject(i);
                                        Message m=new Message(null,temp.getString("sender"),null,temp.getString("data"));
                                        messages.add(m);
                                        }catch(JSONException e){
                                            e.printStackTrace();
                                    }
                                }
                                adapter.update(messages,loggedinUsername);
                            }
                            else{
                                Toast.makeText(MessageActivity.this,prefs.getString("getMessages_error",null),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch(JSONException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected  void onResume() {
        super.onResume();
        final CustomAdapter2 adapter = new CustomAdapter2(this);
        updateMessages(adapter);

    }


}
