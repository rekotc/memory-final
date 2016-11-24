package com.rekotc.memory_final;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rekotc on 29/07/16.
 */

public class Tile {


    private static final String ATTR_FOUND = "Found";
    private static final String ATTR_SELECTED = "Selected";
    private static final String ATTR_RESID = "ResId";
    boolean mFound;
    boolean mSelected;
    int mResId;
    private static int mNotFoundResId;

    /**
     * Constructor
     */
    Tile()
    {
    }

    /**
     * Constructor
     */
    Tile(int nResId)
    {
        mResId = nResId;
    }

    /**
     * Constructor from a JSON object
     */
    Tile(JSONObject object)
    {
        try
        {
            mFound = object.getBoolean(ATTR_FOUND);
            mSelected = object.getBoolean(ATTR_SELECTED);
            mResId = object.getInt(ATTR_RESID);
        } catch (JSONException ex)
        {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setNotFoundResId(int nNotFoundResId)
    {
        mNotFoundResId = nNotFoundResId;
    }

    boolean isFound()
    {
        return mFound;
    }

    void setFound(boolean bFound)
    {
        mFound = bFound;
    }

    public int getResId()
    {
        return (mFound || mSelected) ? mResId : mNotFoundResId;
    }

    public void select()
    {
        mSelected = true;
    }

    public void unselect()
    {
        mSelected = false;
    }

    /**
     * Build a JSONObject representing the tile
     * @return a JSONObject
     */
    JSONObject json()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.accumulate(ATTR_FOUND, mFound);
            object.accumulate(ATTR_SELECTED, mSelected);
            object.accumulate(ATTR_RESID, mResId);
        } catch (JSONException ex)
        {
            Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return object;

    }
}
