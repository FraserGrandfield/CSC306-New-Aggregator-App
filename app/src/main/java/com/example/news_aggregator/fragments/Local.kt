package com.example.news_aggregator.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.R
import com.example.news_aggregator.activities.MainActivity
import com.example.news_aggregator.adapters.ArticleRecyclerAdapter
import com.example.news_aggregator.interfaces.TopSpacingItemDecoration
import com.example.news_aggregator.models.DummyData
import com.example.news_aggregator.models.NewsAPI
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_for_you.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch


class Local : Fragment() {
    //TODO add onResueme and refresh the articles
    private lateinit var articleAdapter: ArticleRecyclerAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private final var LOCATION_API_KEY = "AIzaSyBh4CwTScM2R-5C11-VqrxC7IbzmX-7PIs"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    companion object {
        fun newInstance() = Local().apply { arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            articleAdapter = ArticleRecyclerAdapter()
            adapter = articleAdapter
        }
        checkPermissions()
    }

    private fun checkPermissions() {
        fusedLocationClient = view?.context?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }!!
        if (ActivityCompat.checkSelfPermission(
                view?.context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                view?.context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let { requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1) }
            Log.e("permission", "asdf")
        } else {

            fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
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
        }
    }

    private fun getCity(longitude: Double, latitude: Double) {
        //TODO change search to include country.
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://geocodeapi.p.rapidapi.com/GetNearestCities?latitude=$latitude&longitude=$longitude&range=0")
            .get()
            .addHeader("x-rapidapi-key", "029c2937e1msh9f0263c9b0ef31ap169bf3jsn386bc28e2fa2")
            .addHeader("x-rapidapi-host", "geocodeapi.p.rapidapi.com")
            .build()
        var list = ArrayList<DummyData>()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseData = response.body?.string()
                    Log.e("response data", responseData)
                    val json = JSONArray(responseData)
                    var city = ""
                    if (json.length() > 0) {
                        for (i in 0 until json.length()) {
                            val tempJson = json.getJSONObject(i)
                            city += tempJson.get("City").toString() + " OR "
                        }
                        city = city.dropLast(4)
                    } else {
                        //TODO error no city found
                    }
                    Log.e("cityName", city)
                    view?.let { NewsAPI.getArticles("everything", "q", city, "relevancy", false) { it1 ->
                        articleAdapter.submitList(it1)
                        activity?.runOnUiThread {
                            articleAdapter.notifyDataSetChanged()
                        }
                    } }
                }
                response.close()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("permission", "permison changed")
                    checkPermissions()
                } else {
                    view?.let { Snackbar.make(it, "You must enable the use of location to view local articles!", Snackbar.LENGTH_LONG).show() }
                }
                return
            }
        }
    }

    private fun startLocationUpdates() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
}