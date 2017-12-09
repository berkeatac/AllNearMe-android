package berkea.allnearyou;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static berkea.allnearyou.R.id.map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    Boolean placed = false;
    GoogleMap gMap;
    List<Marker> maList = new ArrayList<>();
    List<MusicPoint> mpList = new ArrayList<>();
    Marker lastOpened = null;
    String TAG = "ok";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        /*** Firebase'e MP objesini yazdırma kısmı
         MusicPoint mp = new MusicPoint("berkenin listi", new LatLng(60.0, 60.0), "user berke", "spoti uri");
         //Firebase Database usage
         DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
         mDatabase.child("markers").child(mp.getName()).setValue(mp);
         */
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setRotateGesturesEnabled(false);


        /*** GOKCEADA
        // Add a marker in Gokceada,
        // and move the map's camera to the same location.
        LatLng gokceada = new LatLng(40.166912, 25.839558);
        googleMap.addMarker(new MarkerOptions().position(gokceada)
                .title("Spotify Playlist"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(gokceada));
        */



        //set map type, initial zoom and bounds of map segment shown
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        /*** THIS IS FOR GOKCEADA
        googleMap.setMinZoomPreference((float)10.3);
        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(40.064221, 25.623931), new LatLng(40.264311, 26.054574)));
        */

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.988962, 29.025087)));
        googleMap.setMinZoomPreference((float)11.5);
        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(40.843742, 28.831196), new LatLng(41.152091, 29.265156)));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot = dataSnapshot.child("markers");
                Log.e("Count " ,""+ dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    MusicPoint mp = postSnapshot.getValue(MusicPoint.class);
                    Log.e("Get Data", mp.getName() + " " + mp.getLatitude() + " " + mp.getLongitude());
                    mpList.add(mp);
                }

                for(int i=0; i<mpList.size(); i++){
                    Log.d(TAG, "onMapReady: GİRDİ");
                    MusicPoint mp = mpList.get(i);
                    LatLng cor = new LatLng(mp.getLatitude(), mp.getLongitude());
                    Log.d(TAG, cor.toString());
                    gMap.addMarker(new MarkerOptions().position(cor).title(mp.getName()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String spotifyPackageName = "com.spotify.music";
                String viewAction = Intent.ACTION_VIEW;
                String spotifyUriString = "";

                for(MusicPoint mp: mpList){
                    if (marker.getPosition().latitude == mp.getLatitude()){
                        spotifyUriString = mp.getUri();
                    }
                }

                Uri spotifyUri = Uri.parse(spotifyUriString);

                Intent spotifyIntent = new Intent(viewAction, spotifyUri);
                spotifyIntent.setPackage(spotifyPackageName);

                startActivity(spotifyIntent);
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (gMap != null && placed.equals(false)){
                    //MarkerOptions mo = new MarkerOptions().position(latLng);
                    //Marker ma = gMap.addMarker(mo);
                    //maList.add(ma);
                    //placed = false;
                }

            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Check if there is an open info window
                if (lastOpened != null) {
                    // Close the info window
                    lastOpened.hideInfoWindow();

                    // Is the marker the same marker that was already open
                    if (lastOpened.equals(marker)) {
                        // Nullify the lastOpened object
                        lastOpened = null;
                        // Return so that the info window isn't opened again
                        return true;
                    }
                }

                // Open the info window for the marker
                marker.showInfoWindow();
                // Re-assign the last opened such that we can close it later
                lastOpened = marker;

                // Event was handled by our code do not launch default behaviour.
                return true;
            }
        });

    }

}
