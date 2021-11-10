package com.mason.sample.masonhealth.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mason.sample.masonhealth.R;

public class ItemView extends LinearLayout {
    private String label;
    private int iconImage = -1;

    public ItemView(Context context) {
        super(context);
        init(context);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        TypedArray attrArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ItemListView, 0, 0);
        try{
            label = attrArray.getString(R.styleable.ItemListView_title);
            iconImage = attrArray.getResourceId(R.styleable.ItemListView_icon , -1);
        } finally {
            attrArray.recycle();
        }

        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_list, this, true);
        TextView labelText = (TextView) view.findViewById(R.id.icon_text);
        ImageView icon = (ImageView) view.findViewById(R.id.icon_image);
        labelText.setText(label);
        icon.setImageResource(iconImage);
    }
}
