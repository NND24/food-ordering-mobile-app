package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.ListCartResponse;
import com.example.food_ordering_mobile_app.repository.CartRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

public class CartViewModel extends AndroidViewModel {
    private final CartRepository cartRepository;

    private final MutableLiveData<Resource<ListCartResponse>> userCartResponse = new MutableLiveData<>();
    public LiveData<Resource<ListCartResponse>> getUserCartResponse() {
        return userCartResponse;
    }
    private final MutableLiveData<Resource<Cart>> detailCartResponse = new MutableLiveData<>();
    public LiveData<Resource<Cart>> getDetailCartResponse() {
        return detailCartResponse;
    }
    private final MutableLiveData<Resource<Cart>> updateCartResponse = new MutableLiveData<>();
    public LiveData<Resource<Cart>> getUpdateCartResponse() {
        return updateCartResponse;
    }

    private final MutableLiveData<Resource<Cart>> clearCartItemResponse = new MutableLiveData<>();
    public LiveData<Resource<Cart>> getClearCartItemResponse() {
        return clearCartItemResponse;
    }

    private final MutableLiveData<Resource<Cart>> clearCartResponse = new MutableLiveData<>();
    public LiveData<Resource<Cart>> getClearCartResponse() {
        return clearCartResponse;
    }

    private final MutableLiveData<Resource<Cart>> completeCartResponse = new MutableLiveData<>();
    public LiveData<Resource<Cart>> getCompleteCartResponse() {
        return completeCartResponse;
    }

    private final MutableLiveData<Resource<Cart>> reOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<Cart>> getReOrderResponse() {
        return reOrderResponse;
    }


    public CartViewModel(Application application) {
        super(application);
        cartRepository = new CartRepository(application);
    }

    public void getUserCart() {
        LiveData<Resource<ListCartResponse>> result = cartRepository.getUserCart();
        result.observeForever(new Observer<Resource<ListCartResponse>>() {
            @Override
            public void onChanged(Resource<ListCartResponse> resource) {
                Log.d("CartViewModel", "getCurrentUser: " + resource);
                userCartResponse.setValue(resource);
            }
        });
    }

    public void getDetailCart(String cartId) {
        LiveData<Resource<Cart>> result = cartRepository.getDetailCart(cartId);
        result.observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> resource) {
                Log.d("CartViewModel", "getCurrentUser: " + resource);
                detailCartResponse.setValue(resource);
            }
        });
    }

    public void updateCart(Cart cart) {
        LiveData<Resource<Cart>> result = cartRepository.updateCart(cart);
        result.observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> resource) {
                Log.d("CartViewModel", "getCurrentUser: " + resource);
                updateCartResponse.setValue(resource);
            }
        });
    }

    public void clearCartItem(String storeId) {
        LiveData<Resource<Cart>> result = cartRepository.clearCartItem(storeId);
        result.observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> resource) {
                Log.d("CartViewModel", "getCurrentUser: " + resource);
                clearCartItemResponse.setValue(resource);
            }
        });
    }

    public void clearCart() {
        LiveData<Resource<Cart>> result = cartRepository.clearCart();
        result.observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> resource) {
                Log.d("CartViewModel", "getCurrentUser: " + resource);
                clearCartResponse.setValue(resource);
            }
        });
    }

    public void completeCart(Cart cart) {
        LiveData<Resource<Cart>> result = cartRepository.completeCart(cart);
        result.observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> resource) {
                Log.d("CartViewModel", "getCurrentUser: " + resource);
                completeCartResponse.setValue(resource);
            }
        });
    }

    public void reOrder(Cart cart) {
        LiveData<Resource<Cart>> result = cartRepository.reOrder(cart);
        result.observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> resource) {
                Log.d("CartViewModel", "reOrderResponse: " + resource);
                reOrderResponse.setValue(resource);
            }
        });
    }
}
