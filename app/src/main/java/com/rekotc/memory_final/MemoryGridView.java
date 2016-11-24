package com.rekotc.memory_final;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by rekotc on 28/07/16.
 */

public class MemoryGridView extends GridView {

    private static final int MARGIN = 15;

    private Memory mMemory;
    private Context mContext;
    private int screen_width;
    private int screen_height;

    public MemoryGridView(Context context) {
        super(context);

        mContext = context;

        //questa funzione serve per reagire all'input dell'utente nel gioco
        //gridview.setOnItemClickListener
        // Register a callback to be invoked when an item in this AdapterView has been clicked.

        setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mMemory.onPosition(position);
            }
        });



    }

    public MemoryGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mMemory.onPosition(position);
            }
        });


    }

    public MemoryGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mMemory.onPosition(position);
            }
        });


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        update();
    }

    void update() {
        //setAdapter(new ImageAdapter(mContext, getWidth(), getHeight(), MARGIN, mMemory));
        setAdapter(new ImageAdapter(mContext, screen_width, screen_height, MARGIN, mMemory));
    }


    public void setMemory(Memory memory) {
        mMemory = memory;
    }

    public void setScreenSize(int width, int height)
    {
        screen_width = width;
        screen_height = height;

    }



}

