package berkea.allnearyou;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by berkeatac on 04/12/2017.
 */

@IgnoreExtraProperties
public class MusicPoint extends Point{
    String uri;
    String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public MusicPoint(double latitude, double longitude, String name, String uri, String user) {
        super(latitude, longitude, name);
        this.user = user;
        this.uri = uri;
    }

    public MusicPoint(){
        super();
    }
}
