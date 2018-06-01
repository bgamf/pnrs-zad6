package branislav.gamf.chatapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements ServiceConnection {
    private IMyNBinder service = null;
    final HttpHelper httphelper = new HttpHelper();
    final Handler handler = new Handler();

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder){
        service = NBinder.Stub.asInterface(binder);
        try{
            service.setCallback(new CallbackClass());
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name){
        service = null;
    }

    private class CallbackClass extends IMyNCallback.Stub{

        @Override
        public void onCallbackCall() throws RemoteException{

            final NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
                    getApplicationContext(),null).setContentText("New message!").setSmallIcon(R.drawable.ic_launcher_background).setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ic_launcher_background)).setContentTitle("Chat application").setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat nManager = NotificationManagerCompat.from(getApplicationContext());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        final boolean response = httphelper.getNotification(ContactsActivity.this);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(response){
                                    nManager.notify(1,nBuilder.build());
                                }
                            }
                        });
                    } catch(IOException e){
                        e.printStackTrace();
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        final Button logoutButton = (Button) findViewById(R.id.logoutButton1);
        final Button refreshButton = (Button) findViewById(R.id.refreshButton);


        final String MY_PREFS_NAME = "PrefsFile";



        updateList();

        bindService(new Intent(ContactsActivity.this, NService.class),this, Context.BIND_AUTO_CREATE);



        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //startActivity(new Intent(ContactsActivity.this,MainActivity.class));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final boolean response = httphelper.logOutFromServer(ContactsActivity.this, httphelper.URL_LOGOUT);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (response) {
                                        Toast.makeText(ContactsActivity.this, R.string.valid_logout, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ContactsActivity.this, MainActivity.class));
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        Toast.makeText(ContactsActivity.this, prefs.getString("logout_error", null), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        });


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
            }
        });

    }
    public void updateList(){
        final HttpHelper httphelper = new HttpHelper();
        final Handler handler = new Handler();
        final CustomAdapter adapter = new CustomAdapter(this);
        final SharedPreferences prefs = getSharedPreferences("PrefsFile",MODE_PRIVATE);
        String userID = prefs.getString("loggedin_userID",null);
        final ListView listViewContacts = (ListView) findViewById(R.id.customListView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final JSONArray array = httphelper.getContactsFromServer(ContactsActivity.this,httphelper.URL_CONTACTS);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                if(array != null){
                                    JSONObject temp;
                                    for(int i = 0; i < array.length(); i++){
                                        temp = array.getJSONObject(i);
                                        if(!temp.getString(httphelper.USERNAME).equals(prefs.getString("logged_username",null))){
                                            adapter.AddContact(new ContactItem(temp.getString(httphelper.USERNAME), getResources().getDrawable(R.drawable.ic_send_black_24dp)));
                                        }
                                    }
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                            listViewContacts.setAdapter(adapter);
                        }

                    });
                } catch(JSONException e){
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
