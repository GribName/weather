package com.example.weather.presentation

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weather.databinding.FragmentListForecastDayBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ListForecastDayFragment : Fragment() {

    private var _binding : FragmentListForecastDayBinding? = null
    private val binding get() = _binding!!

    private val listForecastDayAdapter = ListForecastDayAdapter()

    private val viewModel : ListForecastDayViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = ListForecastDayViewModel() as T
        }
    }

    private var _fusedClient : FusedLocationProviderClient? = null
    private val fusedClient get() = _fusedClient!!

    private val location = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map -> if ( map.values.isNotEmpty() && map.values.all { it } ) startLocation() }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            viewLifecycleOwner.lifecycleScope.launch {

                viewModel.loadWeather( result.lastLocation?.latitude.toString() + "," + result.lastLocation?.longitude.toString() )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListForecastDayBinding.inflate( layoutInflater )
        _fusedClient = LocationServices.getFusedLocationProviderClient( requireActivity() )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listForecastDay.onEach {
            listForecastDayAdapter.setData( it )
        }.launchIn( viewLifecycleOwner.lifecycleScope )

        viewModel.locationName.onEach {
            binding.locationName.text = it.name
        }.launchIn( viewLifecycleOwner.lifecycleScope )

        binding.rv.adapter = listForecastDayAdapter
    }

    @SuppressLint( "MissingPermission" )
    private fun startLocation() {
        val request = LocationRequest.Builder(60_000 )
            .setPriority( Priority.PRIORITY_LOW_POWER )
            .build()

        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    override fun onStop() {
        super.onStop()
        fusedClient.removeLocationUpdates( locationCallback )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _fusedClient = null
    }

    private fun checkPermission() {
        if ( REQUIRED_PERMISSION.all { permission ->
                ContextCompat.checkSelfPermission( requireContext(), permission ) == PackageManager.PERMISSION_GRANTED
            } ) startLocation()
        else location.launch( REQUIRED_PERMISSION )
    }

    companion object {
        private val REQUIRED_PERMISSION : Array<String> = arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
        )
    }
}