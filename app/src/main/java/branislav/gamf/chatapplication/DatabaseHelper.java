package branislav.gamf.chatapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "chatapplication.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_CONTACTS = "contacts";
    private static final String COLUMN_CONTACT_ID = "ContactID";
    private static final String COLUMN_USERNAME = "UserName";
    private static final String COLUMN_FIRSTNAME = "FirstName";
    private static final String COLUMN_LASTNAME = "LastName";

    private static final String TABLE_NAME_MESSAGES = "messages";
    private static final String COLUMN_MESSAGE_ID = "MessageID";
    private static final String COLUMN_SENDER_ID = "SenderID";
    private static final String COLUMN_RECEIVER_ID = "ReceiverID";
    private static final String COLUMN_MESSAGE = "Message";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryTableContacts ="CREATE TABLE " + TABLE_NAME_CONTACTS + " (" +
                COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_FIRSTNAME + " TEXT, " +
                COLUMN_LASTNAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT);";

        String queryTableMessages ="CREATE TABLE " + TABLE_NAME_MESSAGES +" (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_SENDER_ID + " TEXT, " +
                COLUMN_RECEIVER_ID + " TEXT, " +
                COLUMN_MESSAGE + " TEXT);";

        db.execSQL(queryTableContacts);
        db.execSQL(queryTableMessages);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertContact(Contact c){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID,c.getcUserID());
        values.put(COLUMN_FIRSTNAME,c.getcFirstName());
        values.put(COLUMN_LASTNAME,c.getcLastName());
        values.put(COLUMN_USERNAME,c.getcUserName());

        db.insert(TABLE_NAME_CONTACTS,null,values);
    }

    public String searchForContact(String name,int mod){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS,null,null,null,null,null,null,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return "-1";
        }
        String temp = "";
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            if(mod == 1){
                temp = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
            }
            else if(mod == 2){
                temp = cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME));
            }
            if(temp.equals(name)){
                String retVal = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
                cursor.close();
                return retVal;

            }
        }
        cursor.close();
        return "-1";

    }

    public void readContacts(ArrayList<Contact> contacts){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS,null,null,null,null,null,null,null);

        if(cursor.getCount() <= 0){
        }
        else {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Contact temp = new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME))
                );
                contacts.add(temp);

            }
        }
        cursor.close();
    }

    public void insertMessage(Message m){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE_ID , m.getmMessageID());
        values.put(COLUMN_SENDER_ID , m.getmSenderID());
        values.put(COLUMN_RECEIVER_ID , m.getmReceiverID());
        values.put(COLUMN_MESSAGE, m.getmMessage());

        db.insert(TABLE_NAME_MESSAGES , null , values);
        close();
    }
    public void readMessages(ArrayList<Message> messages,String receiverUserID,String senderUserID){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGES,null,"(SenderID =? AND ReceiverID =?) OR (SenderID =? AND ReceiverID =?)",
                new String[]{senderUserID, receiverUserID, receiverUserID, senderUserID},null,null,null,null);
        messages.clear();
        if(cursor.getCount() <= 0){
        }
        else{
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){

                Message temp = new Message(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE)));
                messages.add(temp);

            }
        }
        cursor.close();
    }

    public void deleteMessage(String messageID){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGES,null,null,null,null,null,null,null);

        if(cursor.getCount() <= 0){
        }
        else{
            db.delete(TABLE_NAME_MESSAGES,"MessageID=?",new String[]{messageID});
        }
        cursor.close();
    }


}
