package com.ciejaycoding.letsmeat.view.address

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ciejaycoding.letsmeat.databinding.FragmentAddAddressBinding
import com.ciejaycoding.letsmeat.models.Address
import com.ciejaycoding.letsmeat.models.Contacts
import com.ciejaycoding.letsmeat.utils.ProgressDialog
import com.ciejaycoding.letsmeat.utils.UiState
import com.ciejaycoding.letsmeat.viewmodel.AuthViewModel
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddAddressFragment : Fragment() {
    private lateinit var binding : FragmentAddAddressBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest
    private val authViewModel : AuthViewModel by viewModels()
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val location = p0.lastLocation
            location?.let {
               getLocationInfo(it.latitude,it.longitude)
            }
        }
    }
    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    getLastLocation()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)-> {
                    getLastLocation()
                } else -> {
                Toast.makeText(binding.root.context,"Permission Denied!",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddAddressBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(view.context);
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        binding.buttonMyLocation.setOnClickListener {
            if (checkPermission()) {
                getLastLocation()
            }
            requestPermissions()
        }
        binding.buttonSubmit.setOnClickListener {
            if (!validateInputs()) {
                return@setOnClickListener
            }
            uid?.let {
                authViewModel.addAddress(uid,provideInputs())
            }
        }
        authViewModel.addAddress.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Failed ->{
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDialog.showLoadingDialog("Creating Address....")
                }
                is UiState.Success -> {
                    progressDialog.stopLoading()
                    Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }
    private fun validateInputs() : Boolean {
        //Contact
        val name = binding.inputName.text.toString()
        val contact = binding.inputContact.text.toString()
        //address
        val addressLine = binding.inputRegion.text.toString()
        val postal = binding.inputPostalCode.text.toString()
        val street = binding.inputStreet.text.toString()
        if (name.isEmpty()) {
            binding.inputName.error ="enter name!"
            return false
        } else if (contact.isEmpty()) {
            binding.inputContact.error = "invalid contact!"
            return false
        } else if (contact.length != 11 || !contact.startsWith("09")) {
            binding.inputContact.error = "invalid contact!"
            return false
        } else if (addressLine.isEmpty()) {
            binding.inputRegion.error = "invalid address line!"
            return false
        } else if (postal.isEmpty()) {
            binding.inputPostalCode.error ="Invalid postal code!"
            return false
        } else if (street.isEmpty()) {
            binding.inputStreet.error = "Invalid street name!"
            return false
        } else {
            return true
        }
    }
    private fun provideInputs() : Address{
        //Contact
        val name = binding.inputName.text.toString()
        val contact = binding.inputContact.text.toString()
        //address
        val addressLine = binding.inputRegion.text.toString()
        val postal = binding.inputPostalCode.text.toString()
        val street = binding.inputStreet.text.toString()
        return Address(Contacts(name,contact),addressLine,postal.toInt(),street)
    }

    private fun getLastLocation() {
        if(!isLocationServiceEnabled()) {
            Toast.makeText(binding.root.context,"Please enable your location service",Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result == null) {
                    getNewLocation()
                    return@addOnCompleteListener
                }
                task.result?.let {
                    getLocationInfo(it.latitude,it.longitude)
                }
            }
        }
    }

    private fun getNewLocation() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(0)
            .setMaxUpdateDelayMillis(2)
            .build()
    }

    private fun getLocationInfo(latitude : Double ,longitude : Double) {
        val geocoder = Geocoder(binding.root.context, Locale.getDefault())
        val address = geocoder.getFromLocation(latitude,longitude,1)
        val result = StringBuilder();
        result.append(address[0].adminArea + "\n")
        result.append(address[0].subAdminArea + "\n")
        result.append(address[0].locality + "\n")
        result.append(address[0].featureName)
        binding.inputRegion.setText(result
        )
    }

    private fun checkPermission() : Boolean{
        if (ActivityCompat.checkSelfPermission(binding.root.context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(binding.root.context,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    /**
     * Checks if your gps is enabled
     */
    private fun isLocationServiceEnabled() : Boolean {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}