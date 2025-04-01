package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.ListOrderResponse;
import com.example.food_ordering_mobile_app.models.order.OrderResponse;
import com.example.food_ordering_mobile_app.repository.OrderRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

public class OrderViewModel extends AndroidViewModel {
    private final OrderRepository orderRepository;

    private final MutableLiveData<Resource<ListOrderResponse>> currentOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<ListOrderResponse>> getCurrentOrderResponse() {
        return currentOrderResponse;
    }
    private final MutableLiveData<Resource<ListOrderResponse>> historyOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<ListOrderResponse>> getHistoryOrderResponse() {
        return historyOrderResponse;
    }
    private final MutableLiveData<Resource<OrderResponse>> orderDetailResponse = new MutableLiveData<>();
    public LiveData<Resource<OrderResponse>> getOrderDetailResponse() {
        return orderDetailResponse;
    }

    public OrderViewModel(Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
    }

    public void getCurrentOrder() {
        LiveData<Resource<ListOrderResponse>> result = orderRepository.getUserOrder();
        result.observeForever(new Observer<Resource<ListOrderResponse>>() {
            @Override
            public void onChanged(Resource<ListOrderResponse> resource) {
                currentOrderResponse.setValue(resource);
            }
        });
    }

    public void getHistoryOrder() {
        LiveData<Resource<ListOrderResponse>> result = orderRepository.getUserOrder();
        result.observeForever(new Observer<Resource<ListOrderResponse>>() {
            @Override
            public void onChanged(Resource<ListOrderResponse> resource) {
                historyOrderResponse.setValue(resource);
            }
        });
    }

    public void getOrderDetail(String orderId) {
        LiveData<Resource<OrderResponse>> result = orderRepository.getOrderDetail(orderId);
        result.observeForever(new Observer<Resource<OrderResponse>>() {
            @Override
            public void onChanged(Resource<OrderResponse> resource) {
                Log.d("OrderViewModel", "getOrderDetail: " + resource);
                orderDetailResponse.setValue(resource);
            }
        });
    }
}
