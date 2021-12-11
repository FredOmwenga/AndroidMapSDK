package cis.gvsu.androidmapsdk

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.GeoApiContext
import com.google.maps.PlacesApi
import com.google.maps.model.*
import java.lang.Exception
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mMap: GoogleMap
    lateinit var placesClient: PlacesClient
    lateinit var restaurant: PlacesSearchResult
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var name: TextView? = null
    private var rating: TextView? = null
    private var address: TextView? = null
    private var priceLimit: Int? = null

    private var dinnerFound: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            val location = locationList.last()
            val searchResults = findDinner(location)?.results
            if (searchResults != null) {
                restaurant = searchResults[Random().nextInt(searchResults.size - 1)]
                val lat1 = restaurant.geometry.location.lat
                val lng1 = restaurant.geometry.location.lng
                val latLng = com.google.android.gms.maps.model.LatLng(lat1, lng1)
                mMap.addMarker(MarkerOptions().position(latLng))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0F))
                loadText(restaurant)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // I know this is very taboo, but I couldn't figure out how to grab the api key from the values folder...
        Places.initialize(applicationContext, "AIzaSyDRs63AwSWq8W0RdqPfiD7YnLCCJk3mB5k")
        placesClient = Places.createClient(this)

        val mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val letUsGoButton = findViewById<Button>(R.id.letUsGo)
        letUsGoButton.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=" + restaurant.name + "," + restaurant.vicinity)
            println("google.navigation:q=" + restaurant.name + "," + restaurant.vicinity)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(packageManager)?.let {
                startActivity(mapIntent)
            }
        }

        name = findViewById(R.id.restaurantName)
        rating = findViewById(R.id.restaurantRating)
        address = findViewById(R.id.restaurantAddress)
        priceLimit = intent.getIntExtra("priceLimit", 0)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { return }
        fusedLocationClient?.requestLocationUpdates(
            LocationRequest.create(),
            dinnerFound,
            Looper.myLooper()!!
        )
    }

    fun findDinner(location: Location): PlacesSearchResponse? {
        val latLng = LatLng(location.latitude, location.longitude)
        var request: PlacesSearchResponse? = PlacesSearchResponse()
        val priceLevels = when (priceLimit) {
            4 -> listOf(PriceLevel.EXPENSIVE, PriceLevel.VERY_EXPENSIVE)
            1 -> listOf(PriceLevel.INEXPENSIVE, PriceLevel.INEXPENSIVE)
            else -> {
                listOf(PriceLevel.MODERATE, PriceLevel.MODERATE)
            }
        }
        println(priceLimit)
        val context = GeoApiContext.Builder()

            // These aren't the API keys you're looking for.
            .apiKey("AIzaSyDRs63AwSWq8W0RdqPfiD7YnLCCJk3mB5k")
            .build()
        try {
            request = PlacesApi.nearbySearchQuery(context, latLng)
                .radius(30000)
                .rankby(RankBy.PROMINENCE)
                .type(PlaceType.RESTAURANT)
                .minPrice(priceLevels[0])
                .maxPrice(priceLevels[1])
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return request
    }

    fun loadText(restaurant: PlacesSearchResult) {
        name?.text = restaurant.name
        rating?.text = restaurant.rating.toString() + " out of 5 " + "(" + restaurant.userRatingsTotal + ")"
        address?.text = restaurant.vicinity
    }
}


