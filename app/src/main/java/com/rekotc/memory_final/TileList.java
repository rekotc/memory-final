package com.rekotc.memory_final;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rekotc on 29/07/16.
 */

public class TileList extends ArrayList<Tile>

{

    /**
     * Constructor
     */
    TileList()
    {
    }

    /**
     * Constructeur
     * @param serialized A serialized list
     */
    TileList(String serialized)
    {
        try
        {
            JSONArray array = new JSONArray(serialized);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                Tile t = new Tile(object);
                add(t);
            }
        } catch (JSONException ex)
        {
            //Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Serialize the List
     * @return The list as a String
     */
    String serialize()
    {
        JSONArray array = new JSONArray();
        for (Tile t : this)
        {
            array.put(t.json());
        }
        return array.toString();
    }

    ArrayList<Tile> getSelected()
    {
        ArrayList<Tile> list = new ArrayList<Tile>();
        for (Tile t : this)
        {
            if ( t.mSelected && !t.mFound )
            {
                list.add( t );
            }
        }
        return list;
    }
}
