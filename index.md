## Overview

The purpose of this tutorial is to demonstrate how to use the Maps SDK for Android. The application we'll be creating is called "What's For Dinner" and is a meant to be a simple way to explain how to integrate the Google's maps SDK into any Android application. The demo app itself consist of two pages, with the first being a screen with three buttons for the user to select the price point they are comfortable with. Once the user selects their desired price point, then the app will grab their location data and search within a 30km radius for any restaurants that fit their selection. Once the restaurants are gathered, the app then selects a random restaurant from the list and creates a marker on the map, which it then centers the view on. From here, the user has the option of “Try again” or “Let’s Go!”. Selecting the ladder will open up google maps on the user's phone and automatically navigate the them to the restaurant displayed. 

![MainScreen](https://github.com/FredOmwenga/AndroidMapSDK/blob/master/Screens.PNG)

## Getting Started

For this tutorial, I'll be using Android Studio(Artic Fox) along with Android Studio's built in mobile device emulator. You can download Android studio here (https://developer.android.com/studio) and find steps for getting the emulator up and running here (https://developer.android.com/studio/run/managing-avds). You’ll also have to take some additional steps to add a location to the emulator and enable gps, which can be found here (https://newbedev.com/how-to-emulate-gps-location-in-the-android-emulator) I’ve included a list of all the dependencies that you’ll need to add to the app level build.gradle file below for the functionality we’ll need. Other than that, there isn't anything else you'll need to get started on this demo.

```markdown
  dependencies {
    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.4.0'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'com.google.android.gms:play-services-maps:18.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.2'
    testImplementation 'junit:junit:4+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.3.5'
    implementation 'androidx.navigation:navigation-ui-ktx:2.3.5'
    implementation 'com.google.android.gms:play-services-location:18.0.0'
    implementation 'com.google.android.libraries.places:places:2.5.0'
    implementation 'com.google.maps.android:places-ktx:0.4.0'
    implementation 'com.google.maps:google-maps-services:0.11.0'
    implementation 'androidx.activity:activity-ktx:1.4.0'
    implementation 'androidx.fragment:fragment-ktx:1.4.0'

}
```


## Coding Time

To begin, we’re going to take advantage of Android Studio new project feature that allows you to bring in all the needed dependencies, along with a bunch of boiler plate code to get a user set up with a basic Google Maps Activity. To do this, all you need to do is select the “Google Maps Activity” screen when creating a new project.

![newProject](https://github.com/FredOmwenga/AndroidMapSDK/blob/master/NewProject.PNG)

This creates MapsActivity.jk file and a layout with a fragment that displays the google map. Before you’re able to run the application, you’ll need to provide a valid API Key. This link (https://developers.google.com/maps/documentation/javascript/get-api-key) will walk you through all the steps for creating an API Key and enabling the Maps SDK on your Google account. Once you’ve completed that and have your shiny new API Key, you can replace the “YOUR_KEY_HERE” in the values xml folder found at app/res/values/google_maps_api.xml. 

Lastly, you'll create a new need to create a new layout resource file under layouts for the MapsActivity class and call it activity_maps. Then go into the MapsActivity class and set the content view by adding the line below to the onCreate method that is called when the activity is displayed.

```markdown
        setContentView(R.layout.activity_maps)
```

Now that you’ve got that taken care of, go ahead and run the application on your emulator and you should be able to see the map displayed. Ignore the location for now, that’s just a default location that was added when we created the project. 

From here, we’re good to begin to create the main screen that a user will land on when launching the application. This means that we’re going to want to create a new .kt class file for the main screen. I’ve included a snippet of what that class should look like to start off. 

```markdown
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
```

Now that we’ve got a main class, we’re going to want to also create a new layout resource file under the layout directory. In this layout, we can go ahead and set up the main screen that the user will use to select the price range their interested in. This consist of adding three buttons and styling the page to your liking. I went with a 90’s Seinfeld theme, but how you design and style it is completely up to you. 

With the main screen styled to our liking, it’s time to add a little functionality to the buttons we created for it. This will involve creating a way to navigate away from the main screen to the map screen. Since our main page is just a fragment, we’re going to need a navigation activity to everything in, so go ahead and create a new .kt class file and call it NavigationActivity and another layout resource file under the layout directory called activity_navigation. 

```markdown
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NavigationActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
    }
}
```

Lastly, you’ll need to go to the manifest directory and in the AndroidManifest.xml file add the newly created NavigationActivity here, as seen in below.

```markdown
        <activity android:label="NavigationActivity" android:exported="false" android:name=".NavigationActivity"/>
```

Next, we’re going to want to create a navigation controller that will allow us to transition from the main screen to the map screen. To do this, you’ll need to create a new Android Resource directory with the resource type being set to navigation. In this directory, go ahead and create a new layout resource file by the name of nav_graph. Here is where you’re going to add our mainFragment and mapsActivity layouts. To do this, just hit the outline of a phone with a green plus sign in the bottom right corner, which can be found at the top of the UI view section and select them from the generated list of layouts. 

Now, we can go back to the activity_navigation layout screen and add a new NavHostFragment to the layout, which will prompt you to select the nav_graph layout you just created. Now you should be getting an error message asking you to pick a layout that you’d like previewed. Go ahead and select Pick Layout and select the main layout and the error message should go away. 

One last step before we’ve hit our next checkpoint, and that is to let the application know that you want the NavigationActivity to be initially launched when the application is opened. To do this, go back to the manifest directory and in the AndroidManifest.xml file, switch the .MapsActivity and the .NavigationActivity so that the NavigationActivity is the one that handles the launcher intent. Now everything should be set up to have the main layout displaying when the app is launched. 

The next obstacle to clear is getting from the main screen to the maps screen. To do this, we’re going to use create a new intent for each of the three buttons that were added to the main screen. This will help us out in the future when we’ll need to communicate the user’s selection to the map screen when we transition, but for now we’re just going to use the intent to trigger a screen transition when a button is clicked. I’ve included the code below that you’ll need to add to the onViewCreated method. All three of the buttons that were created should now be successfully transitioning to the maps screen when pressed.

```markdown
        val richButton = view.findViewById(R.id.rich) as Button
        richButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            startActivityForResult(intent, 0)
        }

        val poorButton = view.findViewById(R.id.poor) as Button
        poorButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            startActivityForResult(intent, 1)
        }

        val inBetweenButton = view.findViewById(R.id.inBetween) as Button
        inBetweenButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            startActivityForResult(intent, 0)
        }
```

Now that we can transition to the map screen, it’s time to start stylizing this screen too. We’re going to need to crop the map down so that we have roughly the bottom third of the screen available to display information about the restaurant selected. To achieve this, I created a new constraints layout and placed the. The information to be displayed will include three text views (name, rating, and address). Once you’ve got those added and styled to your liking, check to make sure everything is still loading up correctly when the screen transitions. 

With all the screens set up and stylized, it’s time to go out and find the data we need to populate the text views we created. First, we’re going to have to find the user’s location, so we know where to look for possible restaurant options. This will require us to add a new class variable called fusedLocationClient. After adding this, we’ll need to initiate it in the onCreate method by calling the getFusedLocationProviderClient method which is part of Android’s LocationServices.   

```markdown
    private var fusedLocationClient: FusedLocationProviderClient? = null
    
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        ...
```

Now, you can go ahead and remove the code that is creating the marker and add in the following lines of code. This code is responsible for keeping the Google play services connection active while the app is open. Permission to access the user’s location data is required for this call to execute, so we’re also going to add in a permissions check first, to ensure we’re allowed to access the user’s location data. 

```markdown
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
```

You might’ve noticed that there is an error being cause by a line of code that we just added. This is because we still need to implement the callback function that requestLocationUpdates is expecting. This code can be found below. This callback function is where the meat of our maps activity will take place. Right now, we’re just going to be getting the user’s location, then focusing the view of the map onto it when the screen loads up. To do this, we’re simply going to ask Android what the last location recorded for the user was, as it’s not too important for us to have real time location data for us to achieve the functionality we’re looking to achieve. 

```markdown
    private var dinnerFound: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            val location = locationList.last()
            val lat = location.latitude
            val lng = location.longitude
            val latLng = com.google.android.gms.maps.model.LatLng(lat, lng)
            mMap.addMarker(MarkerOptions().position(latLng))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0F))
        }
    }
```

At this point, the map should be automatically focusing in on the location that you set up at the beginning of the tutorial. 
With that working, it’s time to start adding in the logic that will allow us to search for some restaurants. This will require us to use another closely related SDK that Google provides, which is the Places SDK. To do this, you’ll need to create a new class variable and then initialize it in the onCreate method using the code provided below. Make sure to replace “YOUR_KEY_HERE” with the API Key you created at the beginning of the tutorial.

```markdown
    lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Places.initialize(applicationContext, YOUR_KEY_HERE)
        placesClient = Places.createClient(this)

        val mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)
        ...
```

Before we go any further, lets finish setting up the intents that we created a little while ago in the main screen activity. One of the ways we can use the Places SDK is to search for a place using filters, such as type, distance, ranking etc.., which we’re going to be taking advantage of.  To do this, we’ll need to know what price the user chose on the main screen initially. This is where the intents will come in. We can add data to the intents we created for the buttons earlier, then call that data after we transition to the maps screen. This only requires us to add an additional line to each of the intents, which will leave them looking like the code shown below. 

```markdown
val richButton = view.findViewById(R.id.rich) as Button
        richButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            intent.putExtra("priceLimit", 4)
            startActivityForResult(intent, 0)
        }

        val poorButton = view.findViewById(R.id.poor) as Button
        poorButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            intent.putExtra("priceLimit", 1)
            startActivityForResult(intent, 1)
        }

        val inBetweenButton = view.findViewById(R.id.inBetween) as Button
        inBetweenButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            intent.putExtra("priceLimit", 42)
            startActivityForResult(intent, 0)
        }
```

The intents are now passing an int value that corresponds to the price rating value that Google assigns to each restaurant. To use those values, we’ll first need to assign them to a new class variable we’ll create in MapsActivity. We can do this by adding in the following line of code to MapsActivity’s onCreate method. While we’re adding in new class variables, you’ll want to create some for the text views that we’ll be populating pretty soon.

```markdown
    private var name: TextView? = null
    private var rating: TextView? = null
    private var address: TextView? = null
    private var priceLimit: Int? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Places.initialize(applicationContext, YOUR_KEY_HERE)
        placesClient = Places.createClient(this)

        val mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)
        priceLimit = intent.getIntExtra("priceLimit", 0)
        name = findViewById(R.id.restaurantName)
        rating = findViewById(R.id.restaurantRating)
        address = findViewById(R.id.restaurantAddress)
        ...
```

We’re now ready to create the method that will search for a restaurant that matching the user’s price preference. This method will be called in the callback function that we created earlier and will be passed the last location of the user. Using that location, we’re able to add a radius filter of 30km to the search. We’re also going to create a list that represents a range of prices that match the user’s price preference, which will be used to further tailor the search results to the user’s needs. Lastly, we’re going to add in a type of restaurant to the search, so that we are given places that aren’t viable options for the user. Once we get a successful result returned, we’ll then pass it back to the call back function to be used to populate our empty text views. Here is the code for the method I’ve described. 

```markdown
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
        val context = GeoApiContext.Builder()

            // These aren't the API keys you're looking for.
            .apiKey(YOUR_KEY_HERE)
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
```

Now that we’ve managed to grab a list of possible options, it’s time to randomly select the winner. I’ve added the updated callback function below. I’ve also added an additional class variable that will be needed to save the restaurant that is randomly selected from the list that is returned from the findDinner method. 

```markdown
    lateinit var restaurant: PlacesSearchResult
    ...
    ...
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
    
    fun loadText(restaurant: PlacesSearchResult) {
        name?.text = restaurant.name
        rating?.text = restaurant.rating.toString() + " out of 5 " + "(" + restaurant.userRatingsTotal + ")"
        address?.text = restaurant.vicinity
    }
```

And with those last edits, the application should be successfully returning a restaurant that matches the price preference the user selects. Go ahead and test it out before we move on!

All that’s left to do is add some functionality to the two buttons we created earlier. First we’ll add a listener to the “Try Again?” button that will take us back to the main screen for the user to make another selection. To do this, add the following lines of to the onCreate method. 

```markdown
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
```

And finally, we’re going to add a listener to the “Let’s Go” button that will pass on the restaurant we’ve selected to the device’s native Google Map application. We’re going to call the Google Maps app in a way that will automatically begin navigating the user to the restaurant when it launches. This is done by creating an intent that will contain the restaurant name and general vicinity, which is enough information for Google to find the selected restaurant. 

```markdown
      val letUsGoButton = findViewById<Button>(R.id.letUsGo)
        letUsGoButton.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=" + restaurant.name + "," + restaurant.vicinity)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(packageManager)?.let {
                startActivity(mapIntent)
            }
        }
```

Congratulations! With that, you should never have to worry about who’s turn it is to pick a place anymore. Next, we’ll work on creating an app that picks who gets to pay.

## Wrap-up

Now that you've completed this tutorial, you should have a basic understanding of how to implement the Maps SDK in your native Android applications. You also saw how to use the Places SDK to further utilize a Google map. If you want to continue learning about the Maps and Places SDK, here are some useful links to get you started. 

https://developers.google.com/maps/documentation/places/web-service/search

https://developers.google.com/maps/documentation/android-sdk/overview?section=tutorials

https://developers.google.com/maps/documentation/places/android-sdk/reference/com/google/android/libraries/places/api/Places



