package com.example.warantee;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.Autocomplete;
//import com.google.android.libraries.places.widget.AutocompleteActivity;

public class AddWaranteeForm3 extends AppCompatActivity implements OnMapReadyCallback {

//implements OnMapReadyCallback

    private Button next;
    private GoogleMap map;
//    PlaceAutocompleteFragment placeAutoComplete;

    PlacesClient placesClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_warantee_form3);


//        next = (Button)  findViewById(R.id.nextPageButton) ;
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openAddWaranteeForm3();
//            }
//        });


        //Maps Coooodeeee
        String apikey = "AIzaSyCo1MnSwVR08jLFGwYS97Jit54-fXwYa9k";

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apikey);
        }

        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.AutoCompleteMap);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng laLng = place.getLatLng();

                Log.i("PlaceApi", "onPlaceSelected: "+laLng.latitude+"\n"+laLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

//        placeAutoComplete = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete);
//        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                Log.d("Maps", "Place selected: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.d("Maps", "An error occurred: " + status);
//            }
//        });
//
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }
//    //----------------------------- OpenAddWarantee Function -----------------------
//    public void openAddWaranteeForm3(){
//        Intent intent = new Intent(this, AddWaranteeForm2.class);
//
//        startActivity(intent);
//    }
//    //--------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
    }
    //--------------------------------------------------
    //----------------------------- End of OpenAddWarantee -------------------------
}
