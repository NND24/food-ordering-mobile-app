package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.repository.LocationRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private final LocationRepository locationRepository;

    private final MutableLiveData<Resource<Location>> addLocationResponse = new MutableLiveData<>();
    public LiveData<Resource<Location>> getAddLocationResponse() {
        return addLocationResponse;
    }
    private final MutableLiveData<Resource<Location>> locationResponse = new MutableLiveData<>();
    public LiveData<Resource<Location>> getLocationResponse() {
        return locationResponse;
    }
    private final MutableLiveData<Resource<List<Location>>> userLocationsResponse = new MutableLiveData<>();
    public LiveData<Resource<List<Location>>> getUserLocationsResponse() {
        return userLocationsResponse;
    }
    private final MutableLiveData<Resource<Location>> updateLocationResponse = new MutableLiveData<>();
    public LiveData<Resource<Location>> getUpdateLocationResponse() {
        return updateLocationResponse;
    }
    private final MutableLiveData<Resource<Location>> deleteLocationResponse = new MutableLiveData<>();
    public LiveData<Resource<Location>> getDeleteLocationResponse() {
        return deleteLocationResponse;
    }

    public LocationViewModel(Application application) {
        super(application);
        locationRepository = new LocationRepository(application);
    }

    public void addLocation(Location location) {
        LiveData<Resource<Location>> result = locationRepository.addLocation(location);
        result.observeForever(new Observer<Resource<Location>>() {
            @Override
            public void onChanged(Resource<Location> resource) {
                Log.d("LocationViewModel", "addLocation: " + resource);
                locationResponse.setValue(resource);
            }
        });
    }

    public void getLocation(String locationId) {
        LiveData<Resource<Location>> result = locationRepository.getLocation(locationId);
        result.observeForever(new Observer<Resource<Location>>() {
            @Override
            public void onChanged(Resource<Location> resource) {
                Log.d("LocationViewModel", "getLocation: " + resource);
                locationResponse.setValue(resource);
            }
        });
    }

    public void getUserLocations() {
        LiveData<Resource<List<Location>>> result = locationRepository.getUserLocations();
        result.observeForever(new Observer<Resource<List<Location>>>() {
            @Override
            public void onChanged(Resource<List<Location>> resource) {
                Log.d("LocationViewModel", "getLocation: " + resource);
                userLocationsResponse.setValue(resource);
            }
        });
    }

    public void updateLocation(String id) {
        LiveData<Resource<Location>> result = locationRepository.updateLocation(id);
        result.observeForever(new Observer<Resource<Location>>() {
            @Override
            public void onChanged(Resource<Location> resource) {
                Log.d("LocationViewModel", "updateLocation: " + resource);
                updateLocationResponse.setValue(resource);
            }
        });
    }

    public void deleteLocation(String id) {
        LiveData<Resource<Location>> result = locationRepository.deleteLocation(id);
        result.observeForever(new Observer<Resource<Location>>() {
            @Override
            public void onChanged(Resource<Location> resource) {
                Log.d("LocationViewModel", "deleteLocation: " + resource);
                deleteLocationResponse.setValue(resource);
            }
        });
    }
}
