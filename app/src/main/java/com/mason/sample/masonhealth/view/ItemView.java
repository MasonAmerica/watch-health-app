package com.mason.sample.masonhealth.view;
/**
 * Copyright(C) 2021 Mason America. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
