package ru.digios.sms2net.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private int id;
    private Date date;
    private String phoneNumber;
    private String text;
    private MessageStatus status = MessageStatus.NOT_SENT;

    public Message() {

    }

    public Message(int id, Date date, String phoneNumber, String text, MessageStatus status) {
        this.id = id;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.text = text;
        this.status = status;
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
}
