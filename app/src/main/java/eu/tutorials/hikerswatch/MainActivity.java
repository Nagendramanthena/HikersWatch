package eu.tutorials.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    LocationManager lm;
    LocationListener ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        ll = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location loc) {
                TextView lat = findViewById(R.id.textView7);
                TextView lon = findViewById(R.id.textView8);
                TextView address = findViewById(R.id.textView);
                TextView alt = findViewById(R.id.textView2);

                Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
                String adr = "Could not find the address :(";
                try {
                    List<Address> la = gc.getFromLocation(loc.getLatitude(), loc.getLongitude(),1);
                    if(la!=null && la.size()>0){
                        adr = "Address:\n";
                        if(la.get(0).getThoroughfare()!=null){
                            adr += la.get(0).getThoroughfare()+"\n";
                        }
                        if(la.get(0).getLocality()!=null){
                            adr += la.get(0).getLocality()+" ";
                        }
                        if(la.get(0).getPostalCode()!=null)adr+=la.get(0).getPostalCode()+" ";
                        if(la.get(0).getAdminArea()!=null)adr += la.get(0).getAdminArea()+" ";
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                address.setText(adr);


                lat.setText("Latitude:"+loc.getLatitude());
                lon.setText("Longitude:"+loc.getLongitude());
                alt.setText("Altitude:"+loc.getAltitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("Status",provider);
            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll);
            Location lkl = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lkl!=null){
                updateLocation(lkl);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll);
        }
    }

    public void updateLocation(Location loc){



    }

}