package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.repository.OrderRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private final OrderRepository orderRepository;

    private final MutableLiveData<Resource<ApiResponse<List<Order>>>> currentOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Order>>>> getCurrentOrderResponse() {
        return currentOrderResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<Order>>>> historyOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Order>>>> getHistoryOrderResponse() {
        return historyOrderResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<Order>>> orderDetailResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<Order>>> getOrderDetailResponse() {
        return orderDetailResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> cancelOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getCancelOrderResponse() {
        return cancelOrderResponse;
    }

    public OrderViewModel(Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
    }

    public void getCurrentOrder() {
        LiveData<Resource<ApiResponse<List<Order>>>> result = orderRepository.getUserOrder();
        result.observeForever(new Observer<Resource<ApiResponse<List<Order>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Order>>> resource) {
                currentOrderResponse.setValue(resource);
            }
        });
    }

    public void getHistoryOrder() {
        LiveData<Resource<ApiResponse<List<Order>>>> result = orderRepository.getUserOrder();
        result.observeForever(new Observer<Resource<ApiResponse<List<Order>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Order>>> resource) {
                historyOrderResponse.setValue(resource);
            }
        });
    }

    public void getOrderDetail(String orderId) {
        LiveData<Resource<ApiResponse<Order>>> result = orderRepository.getOrderDetail(orderId);
        result.observeForever(new Observer<Resource<ApiResponse<Order>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Order>> resource) {
                orderDetailResponse.setValue(resource);
            }
        });
    }

    public void cancelOrder(String orderId) {
        LiveData<Resource<ApiResponse<String>>> result = orderRepository.cancelOrder(orderId);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                cancelOrderResponse.setValue(resource);
            }
        });
    }
}
