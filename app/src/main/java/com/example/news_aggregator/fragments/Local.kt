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
import com.example.news_aggregator.models.NewsAPI
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_for_you.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class Local : Fragment() {
    private lateinit var articleAdapter: ArticleRecyclerAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var hasRequestedPermission = false

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            articleAdapter = ArticleRecyclerAdapter()
            adapter = articleAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        getLocation()
    }

    private fun getLocation() {
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


    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}