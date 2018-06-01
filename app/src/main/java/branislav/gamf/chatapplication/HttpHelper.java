package branislav.gamf.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


public class HttpHelper {

    private static final int CODE_SUCCESS = 200;
    private static final int CODE_USER_EXISTS = 409;
    private static final int READ_TIMEOUT = 1000;
    private static final int CONNECT_TIMEOUT = 10000;
    public static final String MY_PREFS_NAME = "PrefsFile";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String SESSION_ID = "sessionid";
    public static final String SENDER = "sender";
    public static final String RECEIVER = "receiver";
    public static final String DATA = "data";



    public static final String URL_SERVER = "http://18.205.194.168:80";
    public static final String URL_LOGIN = URL_SERVER + "/login";
    public static final String URL_REGISTER = URL_SERVER + "/register";
    public static final String URL_CONTACTS = URL_SERVER + "/contacts";
    public static final String URL_MESSAGES = URL_SERVER + "/message/";
    public static final String URL_MESSAGE_SEND = URL_SERVER + "/message";
    public static final String URL_LOGOUT = URL_SERVER + "/logout";
    public static final String URL_NOTIFICATION = URL_SERVER + "/getfromservice";

    public boolean getNotification(Context context) throws IOException,JSONException{
        HttpURLConnection connection = null;
        java.net.URL url = new URL(URL_NOTIFICATION);
        connection = (HttpURLConnection) url.openConnection();

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId",null);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("sessionid",sessionId);
        connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
        connection.setRequestProperty("Accept","application/json");

        try{
            connection.connect();
        }catch(IOException e){
            return false;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }

        bufferedReader.close();

        Boolean response = Boolean.valueOf(stringBuilder.toString());

        connection.disconnect();
        return response;

    }




    public boolean registerOnServer(Context context,String URL,JSONObject jsonObject) throws IOException{
        HttpURLConnection connection;
        java.net.URL url = new URL(URL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        connection.setDoOutput(true);
        connection.setDoInput(true);

        try {
            connection.connect();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

        DataOutputStream ostream = new DataOutputStream(connection.getOutputStream());

        ostream.writeBytes(jsonObject.toString());
        ostream.flush();
        ostream.close();

        int response = connection.getResponseCode();

        if(response != CODE_SUCCESS){
            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString("register_error", connection.getResponseMessage().toString());
            editor.apply();
            return false;
        }

        connection.disconnect();

        return true;
    }

    public boolean logInOnServer(Context context,String URL,JSONObject jsonObject)throws IOException{
        HttpURLConnection connection;
        java.net.URL url = new URL(URL);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");

        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        connection.setDoOutput(true);
        connection.setDoInput(true);

        try {
            connection.connect();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

        DataOutputStream ostream = new DataOutputStream(connection.getOutputStream());

        ostream.writeBytes(jsonObject.toString());
        ostream.flush();
        ostream.close();

        int response = connection.getResponseCode();

        String sessionId = connection.getHeaderField(SESSION_ID);

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

        if(response != CODE_SUCCESS){
            editor.putString("login_error",connection.getResponseMessage());
            editor.apply();
            return false;
        }
        editor.putString("sessionId",sessionId);
        editor.apply();

        connection.disconnect();

        return true;

    }

    public boolean logOutFromServer(Context context,String URL) throws IOException,JSONException{
        HttpURLConnection connection;
        java.net.URL url = new URL(URL);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId",null);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(SESSION_ID,sessionId);

        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        connection.setDoInput(true);
        connection.setDoOutput(true);

        try{
            connection.connect();
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }

        int response = connection.getResponseCode();
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE).edit();

        if(response != CODE_SUCCESS){
            editor.putString("logout_error",connection.getResponseMessage());
            editor.apply();
            return false;
        }

        connection.disconnect();

        return true;

    }

    public JSONArray getContactsFromServer(Context context,String URL) throws IOException,JSONException{
        HttpURLConnection connection;
        java.net.URL url = new URL(URL);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId",null);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(SESSION_ID,sessionId);

        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        try{
            connection.connect();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();

        String line;

        while((line = reader.readLine()) != null){
            builder.append(line + "\n");
        }

        reader.close();

        String jsonString = builder.toString();

        int response = connection.getResponseCode();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE).edit();

        connection.disconnect();

        if(response != CODE_SUCCESS){
            editor.putString("getContacts_error",connection.getResponseMessage());
            editor.apply();
            return null;
        }

        return new JSONArray(jsonString);

    }

    public JSONArray getMessagesFromServer(Context context,String URL) throws IOException,JSONException{
        HttpURLConnection connection;
        java.net.URL url = new URL(URL);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId",null);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(SESSION_ID,sessionId);

        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        try{
            connection.connect();
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null){
            builder.append(line + "\n");
        }

        reader.close();

        String jsonString = builder.toString();

        int response = connection.getResponseCode();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE).edit();

        connection.disconnect();

        if(response != CODE_SUCCESS){
            editor.putString("getMessages_error",connection.getResponseMessage());
            editor.apply();
            return null;
        }
        return new JSONArray(jsonString);
    }

    public boolean sendMessageToServer(Context context,String URL,JSONObject jsonObject) throws IOException,JSONException{
        HttpURLConnection connection;
        java.net.URL url= new URL(URL);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty(SESSION_ID, sessionId);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        connection.setDoOutput(true);
        connection.setDoInput(true);

        try {
            connection.connect();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

        DataOutputStream ostream = new DataOutputStream(connection.getOutputStream());

        ostream.writeBytes(jsonObject.toString());
        ostream.flush();
        ostream.close();

        int response = connection.getResponseCode();

        if(response != CODE_SUCCESS){
            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString("sendMessage_error", connection.getResponseMessage());
            editor.apply();
            return false;
        }

        connection.disconnect();

        return true;
    }

}