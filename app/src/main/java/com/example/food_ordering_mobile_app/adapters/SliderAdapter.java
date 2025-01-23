package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.example.food_ordering_mobile_app.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.on_boarding_1,
            R.drawable.on_boarding_2,
            R.drawable.on_boarding_3
    };

    public String[] slide_headings = {
            "Tìm đồ ăn bạn muốn",
            "Giao hàng nhanh chóng",
            "Theo dõi trực tiếp vị trí",
    };

    public String[] slide_desc_1 = {
            "Khám phá những món ăn ngon nhất từ hơn 1.000",
            "Giao hàng đến tận nhà, cơ quan",
            "Theo dõi vị trí đồ ăn của bạn theo thời gian thực",
    };

    public String[] slide_desc_2 = {
            "nhà hàng và giao hàng nhanh chóng đến tận nhà bạn.",
            "bất cứ đâu bạn ở.",
            "từ trên ứng dụng một khi bạn đặt món.",
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view  = layoutInflater.inflate(R.layout.slide_layout, container, false);

        // Set control
        ImageView imageView =  (ImageView) view.findViewById(R.id.image_slide);
        TextView titleView =  (TextView) view.findViewById(R.id.title_slide);
        TextView descView_1 =  (TextView) view.findViewById(R.id.description_slide_1);
        TextView descView_2 =  (TextView) view.findViewById(R.id.description_slide_2);

        // Set event
        imageView.setImageResource(slide_images[position]);
        titleView.setText(slide_headings[position]);
        descView_1.setText(slide_desc_1[position]);
        descView_2.setText(slide_desc_2[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}