package com.example.smartfarm;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class AreaButton extends LinearLayout {
    LinearLayout layout;
    Button btn;

    public AreaButton(Context context) {
        super(context);
        initView();
    }

    public AreaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public AreaButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.area_button, this, false);
        addView(v);

//        layout = (LinearLayout) findViewById(R.id.area_view);
        btn = findViewById(R.id.area_button);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.area);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.area, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        String btnText = typedArray.getString(R.styleable.area_btn_text);
        btn.setText(btnText);

        typedArray.recycle();
    }

    void setBtnText(String btnText) {
        btn.setText(btnText);
    }
}
