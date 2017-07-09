package ru.digios.sms2net.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDatabaseHelper extends SQLiteOpenHelper implements IDatabaseMessageHandler {

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
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " INTEGER,"
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
        values.put(KEY_STATUS, message.getStatus().toString());

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
        String countQuery = "SELECT * FROM " + TABLE_MESSAGES + " WHERE text = '" + message.getText() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count  = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    @Override
    public List<Message> getAllMessage() {
        List<Message> messageList = new ArrayList<Message>();
        String selectQuery = "SELECT "
                + KEY_ID + KEY_DATE + KEY_PH_NO + KEY_TEXT + KEY_STATUS
                + " FROM " + TABLE_MESSAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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
        String selectQuery = "SELECT "
                + KEY_ID + KEY_DATE + KEY_PH_NO + KEY_TEXT + KEY_STATUS
                + " FROM " + TABLE_MESSAGES + " WHERE status = 'SENT'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        int count  = cursor.getCount();
        cursor.close();

        return count;
    }

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
}
