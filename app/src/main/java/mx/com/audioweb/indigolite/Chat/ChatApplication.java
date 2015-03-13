package mx.com.audioweb.indigolite.Chat;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by pramos on 2/18/15.
 */
public class ChatApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "ToFEGL8WaKsqfGYbhGWhfzcuM6G5yuiTrDNFUMka";
    public static final String YOUR_CLIENT_KEY = "qRwS99PFbtr9f8KxxiNxuz0InwSQBFbq1Vk8VdYP";

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models here
        ParseObject.registerSubclass(Message.class);

        // Add your initialization code here
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        // Test creation of object
        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        /*ParseObject myObject = new ParseObject("Messages");
        myObject.deleteInBackground();
        myObject.saveInBackground();
        myObject = new ParseObject("Chat");
        myObject.deleteInBackground();
        myObject.saveInBackground();
        myObject = new ParseObject("Message");
        myObject.deleteInBackground();
        myObject.saveInBackground();*/
    }
}