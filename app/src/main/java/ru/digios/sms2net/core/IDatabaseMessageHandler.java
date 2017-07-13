package ru.digios.sms2net.core;

import java.util.List;

public interface IDatabaseMessageHandler {
    public void addMessage(Message message);
    public Message getMessage(int id);
    boolean isMessageExist(Message message);
    public List<Message> getAllMessage();
    public List<Message> getUnsentMessage();
    //public int getContactsCount();
    public int updateMessage(Message contact);
    public void deleteMessage(Message contact);
    public void deleteAll();
    public void setSentMessages(List<Message> messages);
}
