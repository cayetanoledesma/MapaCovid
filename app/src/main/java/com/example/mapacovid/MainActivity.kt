package com.example.mapacovid

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.mapacovid.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var radio : Circle
    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        map.mapType = GoogleMap.MAP_TYPE_HYBRID //El tipo del mapa se vuelve hibrido
        map.isTrafficEnabled = true  //trafico activado

        val uiSettings = map.uiSettings //Para poder habilitar funciones en el mapa

        uiSettings.isZoomControlsEnabled = true //controles de zoom

        uiSettings.isCompassEnabled = true //mostrar la brújula

        uiSettings.isZoomGesturesEnabled = true //gestos de zoom

        uiSettings.isScrollGesturesEnabled = true //Gestos de scroll

        uiSettings.isTiltGesturesEnabled = true //Gestos de ángulo

        uiSettings.isRotateGesturesEnabled = true //Gestos de rotación




        map.setOnMapClickListener {
            if(::radio.isInitialized){
                radio.remove()
                if (::marker.isInitialized){
                    marker.remove()
                    val marcador = MarkerOptions()
                        .position(it)
                        .title("Esta es su posición")
                        .snippet("deje pulsado y arrastre a otra ubicación")
                        .flat(true)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    marker = map.addMarker(marcador)!!
                    marker?.tag = "localizacion"
                    AñadirRadio(marker, map)
                } else{
                    val marcador = MarkerOptions()
                        .position(it)
                        .title("Esta es su posición")
                        .snippet("deje pulsado y arrastre a otra ubicación")
                        .flat(true)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    marker = map.addMarker(marcador)!!
                    marker?.tag = "localizacion"
                    AñadirRadio(marker, map)
                }
            } else {
                val marcador = MarkerOptions()
                    .position(it)
                    .title("Esta es su posición")
                    .snippet("deje pulsado y arrastre a otra ubicación")
                    .flat(true)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                marker = map.addMarker(marcador)!!
                marker?.tag = "localizacion"
                AñadirRadio(marker, map)
            }
        }

        map.setOnMapLoadedCallback {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(38.0944131,-3.6309966), 15f))
        }

        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {

            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(marker: Marker) {
                AñadirRadio(marker, map)
            }

            override fun onMarkerDragStart(p0: Marker) {
                if(::radio.isInitialized){
                    radio.remove()
                }
            }
        })
    }

    fun AñadirRadio(marker: Marker, map: GoogleMap) {
        val latLng1 = marker.position
        val latitud = latLng1.latitude
        val longitud = latLng1.longitude

        val circleOptions = CircleOptions()
            .center(LatLng(latitud, longitud))
            .radius(1000.0)
            .strokeColor(Color.MAGENTA)
            .strokeWidth(10f)
            .clickable(true)
            .fillColor(ContextCompat.getColor(this@MainActivity, R.color.semitrasparente))
        radio = map.addCircle(circleOptions)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(circleOptions.center!!, 15f))
    }
}


