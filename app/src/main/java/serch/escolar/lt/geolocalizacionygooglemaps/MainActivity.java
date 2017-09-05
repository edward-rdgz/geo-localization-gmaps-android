package serch.escolar.lt.geolocalizacionygooglemaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {


    private static final long TIEMPO_MINIMO = 10 * 1000; //10 segundos
    private static final long DISTANCIA_MINIMA = 5; // 5 metros

    private LocationManager locationManager;
    private Location location = null;
    private LocationProvider provider;
    Criteria criterio;
    private String proveedor;

    Button buttonUbicacion, buttonMap;
    TextView textViewProvedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonUbicacion = (Button) findViewById(R.id.buttonUbicacion);
        buttonMap = (Button)findViewById(R.id.buttonMap);
        textViewProvedores = (TextView) findViewById(R.id.textViewProveedor);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_COARSE);

        proveedor = locationManager.getBestProvider(criterio, true);
        provider = locationManager.getProvider(proveedor);

        textViewProvedores.setText("Mejor proveedor: " + provider.getName().toString() +
                " " + locationManager.isProviderEnabled(proveedor));
        location = locationManager.getLastKnownLocation(proveedor);

        if (location != null)
            buttonUbicacion.setText("Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude());


        buttonUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location != null)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("geo:"+location.getLatitude()+","+location.getLongitude()));
                        startActivity(intent);
                    }
                else
                {
                    Toast.makeText(getApplicationContext(),"No se encontro ubicacion",Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();

        locationManager.requestLocationUpdates(proveedor, TIEMPO_MINIMO, DISTANCIA_MINIMA, this);
        location = locationManager.getLastKnownLocation(proveedor);
        textViewProvedores.setText("Mejor proveedor: " + provider.getName().toString() + " " + locationManager.isProviderEnabled(proveedor));
    }


    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
        textViewProvedores.setText("Mejor proveedor: " + provider.getName().toString() + " " + locationManager.isProviderEnabled(proveedor));
    }

    @Override
    public void onLocationChanged(Location location) {

        proveedor = locationManager.getBestProvider(criterio, true);

            buttonUbicacion.setText("Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude());
        textViewProvedores.setText("Mejor proveedor: " + provider.getName().toString() + " " + locationManager.isProviderEnabled(proveedor));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        location = locationManager.getLastKnownLocation(proveedor);
        textViewProvedores.setText("Mejor proveedor: " + provider + locationManager.isProviderEnabled(proveedor));
    }

    @Override
    public void onProviderEnabled(String provider) {
        location = locationManager.getLastKnownLocation(proveedor);
        textViewProvedores.setText("Mejor proveedor: " + provider+ locationManager.isProviderEnabled(proveedor));
    }

    @Override
    public void onProviderDisabled(String provider) {
        location = locationManager.getLastKnownLocation(proveedor);
        textViewProvedores.setText("Mejor proveedor: " + provider  + locationManager.isProviderEnabled(proveedor));
    }
}
