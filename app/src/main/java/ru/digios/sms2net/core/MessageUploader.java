package ru.digios.sms2net.core;

import android.content.Context;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.TimerTask;

public class MessageUploader extends TimerTask {
    private static final Logger logger = Log.getLogger(MessageUploader.class);

    private Settings settings = null;
    private MessageDatabaseHelper databaseHelper = null;
    private MessageNetwork messageNetwork = null;

    public MessageUploader(Context context) {
        settings = new Settings(context);
        databaseHelper = new MessageDatabaseHelper(context);
        messageNetwork = new MessageNetwork();
    }

    @Override
    public void run() {
        List<Message> messages = null;
        try {
            messages = databaseHelper.getUnsentMessage();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
            return;
        }

        try {
            if (messageNetwork.send(settings.getHostToConnect(), messages)) {
                databaseHelper.setSentMessages(messages);
                logger.info("Сообщения успешно отправлены:");
                for (Message message : messages) {
                    if (message != null)
                        logger.info(message.toString());
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
            logger.info("Ошибка отправки сообщения на сервер:");
            for (Message message : messages) {
                if (message != null)
                    logger.info(message.toString());
            }
        }
        //Utils.sleep(settings.getSendInterval() * 1000);
    }
}
