package com.example.news_aggregator.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.ArticleRecyclerAdapter
import com.example.news_aggregator.interfaces.TopSpacingItemDecoration
import com.example.news_aggregator.utils.NewsAPI
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_local.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

/**
 * Local fragment to display articles local to the user.
 * @property articleAdapter ArticleRecyclerAdapter
 * @property fusedLocationClient FusedLocationProviderClient
 * @property locationRequest LocationRequest
 * @property locationCallback LocationCallback
 * @property hasRequestedPermission Boolean
 */
class Local : Fragment() {
    private lateinit var articleAdapter: ArticleRecyclerAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var hasRequestedPermission = false

    /**
     * Inflate the local fragment.
     * @param inflater LayoutInflater
     * @param container ViewGroup?
     * @param savedInstanceState Bundle?
     * @return View?
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_local, container, false)
    }
    companion object {
        fun newInstance() = Local().apply {
            arguments = Bundle().apply {
            }
        }
    }

    /**
     * Attach the adapter to the recycle view.
     * @param view View
     * @param savedInstanceState Bundle?
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view_local.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            articleAdapter = ArticleRecyclerAdapter()
            adapter = articleAdapter
        }
    }

    /**
     * Calls getLocation every time the fragment is shown.
     */
    override fun onResume() {
        super.onResume()
        getLocation()
    }

    /**
     * Gets the latitude and longitude of the device.
     */
    private fun getLocation() {
        fusedLocationClient = view?.context?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }!!
        //Checking if the app has location permissions.
        if (ActivityCompat.checkSelfPermission(
                view?.context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                view?.context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //If the app does not have permissions then ask for them.
            if (!hasRequestedPermission) {
                activity?.let {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }
                hasRequestedPermission = true
            } else {
                view?.let {
                    Snackbar.make(
                        it,
                        getString(R.string.snackbar_enable_locations),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            //Get the current location and call getCity function.
            locationRequest = LocationRequest()
            locationRequest.interval = 50000
            locationRequest.fastestInterval = 50000
            locationRequest.smallestDisplacement = 170f //0.1 miles
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    if (locationResult.locations.isNotEmpty()) {
                        val location = locationResult.lastLocation
                        getCity(location.longitude, location.latitude)
                        stopLocationUpdates()
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    /**
     * Uses a reverse geocoding api to get the city from the long and lat.
     * @param longitude Double
     * @param latitude Double
     */
    private fun getCity(longitude: Double, latitude: Double) {
        val client = OkHttpClient()
        val url = getString(R.string.geocode_url) + getString(R.string.geocode_latitude) +
                "=" + latitude + "&" +
                getString(R.string.geocode_longitude) + "=" + longitude +
                "&" + getString(R.string.geocode_url_range)
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader(getString(R.string.geocode_key_text), getString(R.string.geocode_key))
            .addHeader(getString(R.string.geocode_host_text), getString(R.string.geocode_host))
            .build()
        client.newCall(request).enqueue(object : Callback {

            /**
             * Failed to request for the city.
             * @param call Call
             * @param e IOException
             */
            override fun onFailure(call: Call, e: IOException) {
                view?.let {
                    Snackbar.make(
                        it,
                        getString(R.string.snackbar_cannot_get_location),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                e.printStackTrace()
            }

            /**
             * Get the city from the response and search news api for articles with the city as a
             * parameter and then submit the articles to the adapter.
             * @param call Call
             * @param response Response
             */
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val responseData = response.body?.string()
                    val json = JSONArray(responseData)
                    var city = ""
                    if (json.length() > 0) {
                        for (i in 0 until json.length()) {
                            val tempJson = json.getJSONObject(i)
                            city += tempJson.get(getString(R.string.geocode_city))
                                .toString() + " OR "
                        }
                        city = city.dropLast(4)
                    } else {
                        view?.let {
                            Snackbar.make(
                                it,
                                getString(R.string.snackbar_cannot_get_location),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    view?.let {
                        context?.let { it1 ->
                            NewsAPI.getArticles(
                                getString(R.string.news_api_everything),
                                getString(R.string.news_api_q),
                                city,
                                getString(R.string.news_api_relevancy),
                                false,
                                it1
                            ) { list ->
                                articleAdapter.submitList(list)
                                activity?.runOnUiThread {
                                    articleAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
                response.close()
            }
        })
    }

    /**
     * Request location permissions only once.
     * @param requestCode Int
     * @param permissions Array<String>
     * @param grantResults IntArray
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    view?.let {
                        Snackbar.make(
                            it,
                            getString(R.string.snackbar_must_enable_location),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
                return
            }
        }
    }

    /**
     * Stop location updates.
     */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}