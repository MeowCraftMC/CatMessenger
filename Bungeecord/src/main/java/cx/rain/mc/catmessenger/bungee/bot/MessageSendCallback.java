package cx.rain.mc.catmessenger.bungee.bot;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import cx.rain.mc.catmessenger.bungee.MessengerBungee;

import java.io.IOException;

public class MessageSendCallback<REQ extends BaseRequest<REQ, RES>, RES extends BaseResponse> implements Callback<REQ, RES> {
    // qyl27: oh, they are Type Gymnastics.
    public static final MessageSendCallback SEND_MESSAGE_CALLBACK = new MessageSendCallback<>();

    @Override
    public void onResponse(REQ request, RES response) {
    }

    @Override
    public void onFailure(REQ request, IOException ex) {
        MessengerBungee.getInstance().getSLF4JLogger().error("Request failed.", ex);
    }
}
