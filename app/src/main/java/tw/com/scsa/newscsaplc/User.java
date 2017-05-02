package tw.com.scsa.newscsaplc;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Initial_1 on 2016/11/28.
 */

@IgnoreExtraProperties
public class User {
    private String message ;
    private String timeStamp ;

    public User() {
    }

    public User(String DataTime, String BitData) {
        this.message = message;
        this.timeStamp = timeStamp;
    }
    public String getmessage() {
        return message;
    }

    public String gettimeStamp() {
        return timeStamp;
    }


}
