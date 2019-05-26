package com.example.smartfarm;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Farm extends RelativeLayout {
    RelativeLayout farm_layout;
    ImageView image;
    LinearLayout text_layout;
    TextView name;
    TextView location;
    TextView total_area;

    public Farm(Context context) {
        super(context);
        initView();
    }

    public Farm(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public Farm(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.farm, this, false);
        addView(v);
        farm_layout = (RelativeLayout) findViewById(R.id.farm_layout);
        image = (ImageView) findViewById(R.id.farm_image);
        text_layout = (LinearLayout) findViewById(R.id.farm_text_layout);
        name = (TextView) findViewById(R.id.farm_name);
        location = (TextView) findViewById(R.id.farm_location);
        total_area = (TextView) findViewById(R.id.farm_total_area);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.farm);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.farm, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int image_resID = typedArray.getResourceId(R.styleable.farm_image, R.drawable.farm1);
        image.setImageResource(image_resID);

        String name_string = typedArray.getString(R.styleable.farm_name);
        name.setText(name_string);

        String location_string = typedArray.getString(R.styleable.farm_location);
        location.setText(location_string);

        String total_area_string = typedArray.getString(R.styleable.farm_total_area);
        total_area.setText(total_area_string);

        typedArray.recycle();
    }

    void setFarm_layout() {
//        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) farm_layout.getLayoutParams();
//        params.height = image.getHeight();
        farm_layout.setMinimumHeight(image.getHeight());
    }

    void setFarmImage(int image_resID) {
        image.setImageResource(image_resID);
    }

    void setFarmName(String name_string) {
        name.setText(name_string);
    }

    void setFarmLocation(String location_string) {
        location.setText(location_string);
    }

    void setFarmTotalArea(String total_area_string) {
        total_area.setText(total_area_string);
    }

}
