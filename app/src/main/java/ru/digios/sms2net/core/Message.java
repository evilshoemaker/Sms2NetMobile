package ru.digios.sms2net.core;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.Expose;

public class Message implements Serializable {

    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

    private transient int id = 0;
    private Date date = new Date();
    private String phoneNumber = "";
    private String text = "";
    private transient MessageStatus status = MessageStatus.NOT_SENT;

    public Message() {

    }

    public Message(int id, Date date, String phoneNumber, String text, MessageStatus status) {
        this.id = id;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.text = text;
        this.status = status;
        if (status == null) {
            this.status = MessageStatus.NOT_SENT;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return format.format(date) + "; From: " + phoneNumber + "; Text: " + text;
    }
}
