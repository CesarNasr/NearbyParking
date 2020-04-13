package com.example.nearbyparking.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nearbyparking.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jem.rubberpicker.RubberSeekBar;

import java.util.List;

import Helpers.LocationHelper;
import Helpers.PermissionHelper;
import models.Results;
import models.Root;
import network.GetDataService;
import network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static Helpers.Constants.API_KEY;
import static Helpers.Constants.GPS_REQUEST_CODE;
import static Helpers.Constants.task_successfull;

public class MyMapFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private RubberSeekBar rangeSeekbar;
    private LocationManager locationManager;
    private Context context;
    private Criteria criteria;
    private String bestProvider;
    private ImageView refreshBtn;

    private double latitude;
    private double longitude;
    private double radius = 1000;
    private String latlong;
    private LatLng latLngObj;
    private Circle drawnCircle;
    private Location myLocation;

    public MyMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (getActivity() != null) {
          // fetching the map object from xml layout:fragment_map.xml
            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                //calling to create actually the map (we are initiating it)
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        refreshBtn = view.findViewById(R.id.btn_refresh);
        rangeSeekbar = view.findViewById(R.id.range_seekbar);
        rangeSeekbar.setCurrentValue((int) radius);
        rangeSeekbar.setOnRubberSeekBarChangeListener(new RubberSeekBar.OnRubberSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RubberSeekBar rubberSeekBar, int i, boolean b) {
            }
            @Override
            public void onStartTrackingTouch(RubberSeekBar rubberSeekBar) {
            }
            @Override
            public void onStopTrackingTouch(RubberSeekBar rubberSeekBar) {
                radius = rubberSeekBar.getCurrentValue();
                if (mMap != null)
                    mMap.clear();
                Toast.makeText(context, (int) radius + " meters", Toast.LENGTH_LONG).show();
                drawCircle(latLngObj, radius);
                showMyLoc(latLngObj);
                apicall(latlong);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MyMapFragment.this);
        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //when map is created, it returns a callback with the object of the map and we assign it ti mMap parameter
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        checkForLocation(context);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    checkForLocation(context);
                }
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            // go to real google maps when clicking on the Info window of the marker
            // second click
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (mMap != null)
                    loadNavigationView(marker.getPosition().latitude, marker.getPosition().longitude);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                checkForLocation(context);
            } else {
                Toast.makeText(context, "Turn on GPS for better performance", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkForLocation(final Context context) {
    // checks permission location
    // checks for GPS
    //  calls API
        final LocationHelper locationHelper = new LocationHelper();
        PermissionHelper.locationPermissionRequest(getActivity(), context, new PermissionHelper.OnPermissionsGranted() {
            @SuppressLint("MissingPermission")
            @Override
            public void onTaskCompleted(String state) {
            //map shows user's location
                if (state.equals(task_successfull)) {
                    mMap.setMyLocationEnabled(true);
                    locationHelper.isGpsAvailable(context, getActivity(), locationHelper.createGoogleApiClientInstance(context), new LocationHelper.GpsListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void gpsSuccess() {
                            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                            criteria = new Criteria();
                            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
                            myLocation = LocationHelper.getLastKnownLocation(context);
                            if (myLocation == null) {
                                locationManager.requestLocationUpdates(bestProvider, 1000, 0, MyMapFragment.this);
                            } else {
                                try {
                                    double latitude = myLocation.getLatitude();
                                    double longitude = myLocation.getLongitude();
//                                    Toast.makeText(context, latitude + longitude + "", Toast.LENGTH_SHORT).show();
                                    latLngObj = new LatLng(latitude, longitude);
                                    drawCircle(latLngObj, radius);
                                    showMyLoc(latLngObj);
                                    latlong = latitude + "," + longitude;
                                    apicall(latlong);
                                } catch (NullPointerException e) {
                                    //if best location returns null, we need to request location updates to get the location
                                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, MyMapFragment.this);
                                }
                            }
                        }
                        @Override
                        public void gpsFailure() {
                            Toast.makeText(getActivity(), "GPS Failure ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    void apicall(String latlong) {
    //calls the API of google maps
    //and send the parameters needed in addition to fetching the results
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Root> call = service.getPlaces(latlong, String.valueOf(radius), "parking", "false", API_KEY);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                List<Results> results = response.body().getResults();
                if (results.size() > 0) {
                    addMarkers(results);
                    for (int i = 0; i < results.size(); i++) {
                        Log.e("Parking" + i + "Name  ", results.get(i).getName());
                    }
                } else {
//                    Toast.makeText(getActivity(), "No parking found", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getActivity(), "API Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(MyMapFragment.this);
        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLngObj = new LatLng(latitude, longitude);
        drawCircle(latLngObj, radius);
        showMyLoc(latLngObj);
        latlong = latitude + "," + longitude;
        apicall(latlong);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }

    void showMyLoc(LatLng latLngObj) {
        if (mMap != null) {
            if (latLngObj != null) {
                CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLngObj, getZoomLevel(drawnCircle));
//                CameraUpdate zoom = CameraUpdateFactory.zoomTo(getZoomLevel(drawnCircle)); // zoom in to the cricle
                mMap.moveCamera(center);
//                mMap.animateCamera(zoom);
            } else {
                Toast.makeText(context, "Location not detected", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Map NULL", Toast.LENGTH_SHORT).show();
        }
    }

    public int getZoomLevel(Circle circle) { // simple math to get the zoom level according to the circle I draw
        int zoomLevel = 11;
        if (circle != null) {
            double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    private void drawCircle(LatLng point, double radius) {
        if (drawnCircle != null) {
            drawnCircle.remove();
        }
        if (point != null) {
            // Instantiating CircleOptions to draw a circle around the marker
            CircleOptions circleOptions = new CircleOptions();
            // Specifying the center of the circle
            circleOptions.center(point);
            // Radius of the circle
            circleOptions.radius(radius);
            // Border color of the circle
            circleOptions.strokeColor(Color.BLACK);
            // Fill color of the circle
            circleOptions.fillColor(0x30ff0000);
            // Border width of the circle
            circleOptions.strokeWidth(2);
            drawnCircle = mMap.addCircle(circleOptions);
        }
    }

    void addMarkers(List<Results> results) {
    //adds all fetched parking to the map as markers.
    //putting the lat and long double values in a LONGLAT object(android SDK) to draw it oon the map
    //creating a marker
    //setting position for the marker
    //setting the title for the marker
    //this will be displayed pn taping on marker
        LatLng markerLatLng;
        for (int i = 0; i < results.size(); i++) {
            Results result = results.get(i);
            markerLatLng = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();
            // Setting the position for the marker
            markerOptions.position(markerLatLng);
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            String distanceToParking = " - " + LocationHelper.calculateDistanceDifference(result, myLocation); // returns distance between current location and parking
            markerOptions.title(result.getName() + distanceToParking); //  name of the parking plus the distance between it and my location
//            markerOptions.icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_local_parking_black_24dp));
            mMap.addMarker(markerOptions);

        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_local_parking_black_24dp);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void loadNavigationView(double lat, double lng) {
        //taking us to google maps nv through long and lat and uri
        Uri navigation = Uri.parse("google.navigation:q=" + lat + "," + lng + "");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        startActivity(navigationIntent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // do nothing
        return false;
    }
}
