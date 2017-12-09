package berkea.allnearyou;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by berkeatac on 04/12/2017.
 */

@IgnoreExtraProperties
public class MusicPoint {
    double latitude;
    double longitude;
    String name;
    String uri;
    String user;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
        this.uri = uri;
    }

    public MusicPoint(){

    }
}
