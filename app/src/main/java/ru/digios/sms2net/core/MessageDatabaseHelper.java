package ru.digios.sms2net.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.digios.sms2net.MainActivity;

public class MessageDatabaseHelper extends SQLiteOpenHelper implements IDatabaseMessageHandler {
    private static final Logger logger = ru.digios.sms2net.core.Log.getLogger(MessageDatabaseHelper.class);

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "messages.db";
    private static final String TABLE_MESSAGES = "messages";
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TEXT = "text";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_STATUS = "status";


    public MessageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " REAL,"
                + KEY_TEXT + " TEXT,"
                + KEY_PH_NO + " TEXT,"
                + KEY_STATUS + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, message.getDate().getTime());
        values.put(KEY_PH_NO, message.getPhoneNumber());
        values.put(KEY_TEXT, message.getText());
        values.put(KEY_STATUS, message.getStatus() == null ? "NOT_SENT" : message.getStatus().toString());

        db.insert(TABLE_MESSAGES, null, values);
        db.close();
    }

    @Override
    public Message getMessage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGES,
                new String[] { KEY_ID, KEY_DATE, KEY_PH_NO, KEY_TEXT, KEY_STATUS },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Message contact = new Message(Integer.parseInt(cursor.getString(0)),
                new Date(cursor.getLong(1)),
                cursor.getString(2),
                cursor.getString(3),
                MessageStatus.valueOf(cursor.getString(4))
                );
        cursor.close();
        return contact;
    }

    @Override
    public boolean isMessageExist(Message message) {
        String countQuery = "SELECT date, text FROM " + TABLE_MESSAGES + " WHERE text = '" + message.getText()
                + "' AND phone_number = '" + message.getPhoneNumber()// + "'";
                + "' AND date >= " + String.valueOf(message.getDate().getTime() - 30) + " AND date <= " + String.valueOf(message.getDate().getTime() + 30);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        /*cursor.moveToFirst();
        while (cursor.moveToNext()) {
            long t = cursor.getInt(0);
            String text = cursor.getString(1);
            t = t;
        }

        int count  = cursor.getInt(0);*/
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    @Override
    public List<Message> getAllMessage() {
        List<Message> messageList = new ArrayList<Message>();
        /*String selectQuery = "SELECT "
                + KEY_ID + KEY_DATE + KEY_PH_NO + KEY_TEXT + KEY_STATUS
                + " FROM " + TABLE_MESSAGES;*/

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MESSAGES,
                new String[] { KEY_ID, KEY_DATE, KEY_PH_NO, KEY_TEXT, KEY_STATUS },
                null,
                null, null, null, null, null);//db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setId(cursor.getInt(0));
            message.setDate(new Date(cursor.getLong(1)));
            message.setPhoneNumber(cursor.getString(2));
            message.setText(cursor.getString(3));
            message.setStatus(MessageStatus.valueOf(cursor.getString(4)));
            messageList.add(message);
        }
        cursor.close();

        return messageList;
    }

    @Override
    public List<Message> getUnsentMessage() {
        List<Message> messageList = new ArrayList<Message>();
        /*String selectQuery = "SELECT "
                + KEY_ID + ", " + KEY_DATE + KEY_PH_NO + KEY_TEXT + KEY_STATUS
                + " FROM " + TABLE_MESSAGES + " WHERE status = 'SENT'";*/

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = db.query(TABLE_MESSAGES,
                new String[] { KEY_ID, KEY_DATE, KEY_PH_NO, KEY_TEXT, KEY_STATUS },
                "status=?",
                new String[] { "NOT_SENT" }, null, null, null, null);

        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setId(cursor.getInt(0));
            message.setDate(new Date(cursor.getLong(1)));
            message.setPhoneNumber(cursor.getString(2));
            message.setText(cursor.getString(3));
            message.setStatus(MessageStatus.valueOf(cursor.getString(4)));
            messageList.add(message);
        }
        cursor.close();

        return messageList;
    }

    /*@Override
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        int count  = cursor.getCount();
        cursor.close();

        return count;
    }*/

    @Override
    public int updateMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, message.getDate().getTime());
        values.put(KEY_PH_NO, message.getPhoneNumber());
        values.put(KEY_TEXT, message.getText());
        values.put(KEY_STATUS, message.getStatus().toString());

        return db.update(TABLE_MESSAGES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(message.getId()) });
    }

    @Override
    public void deleteMessage(Message contact) {
        return;
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String countQuery = "DELETE FROM " + TABLE_MESSAGES;
        db.execSQL(countQuery);
    }

    @Override
    public void setSentMessages(List<Message> messages) {
        String ids = "(" + Utils.concatStringsIds(messages) + ")";

        SQLiteDatabase db = this.getWritableDatabase();

        //String countQuery = "UPDATE  " + TABLE_MESSAGES + " SET " + KEY_STATUS + " = " + MessageStatus.SENT.toString();
        //db.execSQL(countQuery);
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, MessageStatus.SENT.toString());
        int rowcount = db.update(TABLE_MESSAGES, values, KEY_ID + " IN " + ids,
                new String[] {  });
        logger.info("Количество строк после отправки: " + String.valueOf(rowcount));
        /*List<String> ids = new ArrayList<>();
        for (Message message : messages) {
            ids.add(String.valueOf(message.getId()));
        }
        Utils.concatStrings()*/
    }
}
