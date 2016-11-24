package com.rekotc.memory_final;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by rekotc on 29/07/16.
 */

public class ImageAdapter extends BaseAdapter{

    private Context mContext;
    private int mTileSize;
    private Memory mMemory;

    public ImageAdapter(Context c, int width, int height, int margin , Memory memory )
    {
        mContext = c;
        mMemory = memory;

        //se il telefono è disposto orizzontalmente
        if (width > height)
        {
            mTileSize = getTileSize(height, width, memory.getNumRows(), memory.getNumberOfColumns(), margin);
        }
        //altrimenti il telefono è disposto verticalmente
        else
        {
            mTileSize = getTileSize(width, height, memory.getNumRows(), memory.getNumberOfColumns(), margin);

        }

    }

    private int getTileSize(int width, int height, int numRows, int numColumns, int margin)
    {
        /*int a = max / countMax;
        int b = min / countMin;
        return ((a < b) ? a : b ) - margin;*/

        float scaleFactor = (float)width/ (256*numColumns);
        float newSize = (256*scaleFactor) - margin;

        return Integer.valueOf((int) Math.round(newSize));
    }

    public int getCount()
    {
        return mMemory.getCount();
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    //Get a View that displays the data at the specified position in the data set.
    //getview viene chiamata automaticamente da android
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;

        if (convertView == null)
        {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams((Integer.valueOf((int) Math.round(mTileSize*0.5))), (Integer.valueOf((int) Math.round(mTileSize*0.5)))));
            imageView.setLayoutParams(new GridView.LayoutParams(mTileSize, mTileSize));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else
        {
            imageView = (ImageView) convertView;
        }


        imageView.setImageResource( mMemory.getResId( position ));
        return imageView;
    }
}
