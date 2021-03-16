package com.example.yunusaslanhan.parkuygulamasi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    public double enlem;
    public double boylam;//aynı hata mı aynen bir localde deneyeyim tamam noldu
  /*  static final LatLng otopark1 = new LatLng(40.876675, 31.215072);
    static final LatLng otopark2 = new LatLng(40.843207, 31.153150);
    static final LatLng otopark3 = new LatLng(40.850051, 31.182907);
    static final LatLng otopark4 = new LatLng(40.872130, 31.171446);*/

    Button btnRezervasyon;

    private GoogleMap mMap;
    LocationManager locationManager;
    String provider;
    static List<Marker> markerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        btnRezervasyon = (Button) findViewById(R.id.btnRezervasyon);

        btnRezervasyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MapsActivity.this, Rezervasyon.class);

                startActivity(i);


            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {

            onLocationChanged(location);
        } else {
            enlem = 40.904817;
            boylam = 31.180801;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 100, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            markerList.clear();
            Log.v("otopark dizisi boş mu ", Arrays.toString(MainActivity.otoparkDizisi));
            for (Otopark otopark : MainActivity.otoparkDizisi) {
                NumberFormat format = new DecimalFormat("#.00");
                LatLng otoparkKonumu = new LatLng(Double.parseDouble(otopark.getLatitude()), Double.parseDouble(otopark.getLongitude()));

                String mesafe = format.format(MesafeHesaplama(new LatLng(enlem, boylam), otoparkKonumu));
                MarkerOptions option = new MarkerOptions().position(otoparkKonumu).title(otopark.getIsim())
                        .snippet("Mesafe " + mesafe + " Km - Doluluk " +(otopark.getToplamYerSayisi()- otopark.getBosYerSayisi()) + " / " + otopark.getToplamYerSayisi())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.otopark));
                markerList.add( mMap.addMarker(option));

            }

            mMap.setTrafficEnabled(true);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            UiSettings uis = googleMap.getUiSettings();
            uis.setCompassEnabled(true);
            uis.setZoomControlsEnabled(true);
            uis.setMyLocationButtonEnabled(true);

            //addMarker("Yeni yer",41.053781,28.907972); --haritadaki yerleri fonksiyonla göstermek için


        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(enlem, boylam), 12.5f));

        Toast.makeText(this, "enlem:" + enlem + "boylam:" + boylam, Toast.LENGTH_LONG).show();


       /* Toast.makeText(this, "otopark 1 :"+MesafeHesaplama(konum,otopark1), Toast.LENGTH_LONG).show();//27 21 23 24
        Toast.makeText(this, "otopark 2 "+MesafeHesaplama(konum,otopark2), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "otopark 3 "+MesafeHesaplama(konum,otopark3), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "otopark 4 "+MesafeHesaplama(konum,otopark4), Toast.LENGTH_LONG).show();*/


    }


    public static double MesafeHesaplama(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * 2 * c;
    }

    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double log = location.getLongitude();

        enlem = lat;
        boylam = log;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

        Toast.makeText(this, "Konum Açık", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String s) {

        Toast.makeText(this, "Konum Kapalı", Toast.LENGTH_SHORT).show();

    }



   /* private void addMarker(String title, double latitude, double longitude, Image icon){ -- bu da yer gösterme fonksiyonu

        MarkerOptions m=new MarkerOptions();
        m.title(title);
        m.draggable(true);
        m.position(new LatLng(latitude,longitude));
        mMap.addMarker(m);

    }*/

}