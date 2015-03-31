package mx.com.audioweb.indigolite.Chat;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Jc on 2/18/15.
 */
@ParseClassName("TestMessage")
public class Message extends ParseObject {
    public String getUserId() {
        return getString("userId");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public String getBody() {
        return getString("body");
    }

    public void setBody(String body) {
        put("body", body);
    }

    public String getInitial() {
        return getString("initial");
    }

    public void setInitial(String initial) {
        put("initial", initial);
    }
}