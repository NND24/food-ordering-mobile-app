package com.example.food_ordering_mobile_app.ui.customer.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.FavoriteAdapter;
import com.example.food_ordering_mobile_app.models.MessageResponse;
import com.example.food_ordering_mobile_app.models.favorite.FavoriteResponse;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private FavoriteViewModel favoriteViewModel;
    private RecyclerView favoriteRecyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Store> favoriteList;
    private Button btnRemoveAllFavorite;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView);
        btnRemoveAllFavorite = view.findViewById(R.id.btnRemoveAllFavorite);

        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupUserFavorite();

        btnRemoveAllFavorite.setOnClickListener(this::handleRemoveAllFavorite);

        favoriteViewModel.getRemoveAllFavoriteResponse().observe(getViewLifecycleOwner(), new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        favoriteViewModel.getUserFavorite();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        favoriteViewModel.getRemoveFavoriteResponse().observe(getViewLifecycleOwner(), new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        favoriteViewModel.getUserFavorite();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        return view;
    }

    private void handleRemoveAllFavorite(View view) {
        favoriteViewModel.removeAllFavorite();
    }

    private void setupUserFavorite() {
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteList = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(requireActivity(), getContext(), favoriteList);
        favoriteRecyclerView.setAdapter(favoriteAdapter);

        favoriteViewModel.getUserFavoriteResponse().observe(getViewLifecycleOwner(), new Observer<Resource<FavoriteResponse>>() {
            @Override
            public void onChanged(Resource<FavoriteResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        favoriteList.clear();
                        favoriteList.addAll(resource.getData().getData().getStore());
                        favoriteAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        favoriteViewModel.getUserFavorite();
    }

    private void refreshData() {
        favoriteViewModel.getUserFavorite();
    }
}