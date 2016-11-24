package com.rekotc.memory_final;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
//random per la scelta dei tiles
import java.util.Random;


/**
 * Created by rekotc on 29/07/16.
 */

public class Memory
    {

        private static final int SOUND_FAILED = 2000;
        private static final int SOUND_SUCCEED = 2001;
        private static final String PREF_LIST = "list";
        private static final String PREF_MOVE_COUNT = "move_count";
        private static final String PREF_SELECTED_COUNT = "seleted_count";
        private static final String PREF_FOUND_COUNT = "found_count";
        private static final String PREF_LAST_POSITION = "last_position";
        private static final String PREF_TILE_VERSO = "tile_verso";
        private static final int NUM_ROWS = 4;
        private static final int NUM_COLUMNS = 2;

        private int num_cards = 12;
        private int difficulty_level = 1;
        private int set_size = 16;
        private int num_columns = 4;
        //private static final int SET_SIZE = (NUM_ROWS*NUM_COLUMNS) / 2;
        //private static final int SET_SIZE = NUM_CARDS / 2;
        private int mSelectedCount;
        private int mMoveCount;
        private int mFoundCount;
        private Tile mT1;
        private Tile mT2;
        private TileList mList = new TileList();
        private int[] mTiles;
        private OnMemoryListener mListener;
        private static int[] mSounds;
        private int mTileVerso;

        public Memory(int[] tiles, int[] sounds, int tile_verso, OnMemoryListener listener)
        {
            mTiles = tiles;
            mSounds = sounds;
            mListener = listener;
            mTileVerso = tile_verso;
            Tile.setNotFoundResId(mTileVerso);


        }

        public Memory(int[] tiles, int[] sounds, int tile_verso)
        {
            mTiles = tiles;
            mSounds = sounds;

            mTileVerso = tile_verso;
            Tile.setNotFoundResId(mTileVerso);


        }

        public void onResume(SharedPreferences prefs)
        {
            String serialized = prefs.getString(PREF_LIST, null);
            if (serialized != null)
            {
                mList = new TileList(serialized);
                mMoveCount = prefs.getInt(PREF_MOVE_COUNT, 0);
                ArrayList<Tile> list = mList.getSelected();
                mSelectedCount = list.size();
                mT1 = (mSelectedCount > 0) ? list.get(0) : null;
                mT2 = (mSelectedCount > 1) ? list.get(1) : null;
                mFoundCount = prefs.getInt(PREF_FOUND_COUNT, 0);
                mTileVerso = prefs.getInt(PREF_TILE_VERSO, R.drawable.not_found_default);
                Tile.setNotFoundResId(mTileVerso);
            }

            initSounds();
        }

        public void onPause(SharedPreferences preferences, boolean quit)
        {
            SharedPreferences.Editor editor = preferences.edit();
            if (!quit)
            {
                // Paused without quit - save state
                editor.putString(PREF_LIST, mList.serialize());
                editor.putInt(PREF_MOVE_COUNT, mMoveCount);
                editor.putInt(PREF_SELECTED_COUNT, mSelectedCount);
                editor.putInt(PREF_FOUND_COUNT, mFoundCount);
                editor.putInt(PREF_TILE_VERSO, mTileVerso);
            }
            else
            {
                editor.remove(PREF_LIST);
                editor.remove(PREF_MOVE_COUNT);
                editor.remove(PREF_SELECTED_COUNT);
                editor.remove(PREF_FOUND_COUNT);
                editor.remove(PREF_LAST_POSITION);
                editor.remove(PREF_TILE_VERSO);
            }
            editor.apply();
        }

        public int getCount()
        {
            return mList.size();
        }

        public int getNumRows()
        {
            return NUM_ROWS;
        }

        public int getNumColumns()
        {
            return NUM_COLUMNS;
        }

        public int getResId(int position)
        {
            return mList.get(position).getResId();
        }

        public void updateSet(int[] tiles)
        {
            mTiles = tiles;
            int c = 3;
        }

        public void reset()
        {
            mFoundCount = 0;
            mMoveCount = 0;
            mList.clear();
            for (Integer tile : getTileSet())
            {
                addRandomly(tile);
            }
        }

        private void initSounds()
        {
        SoundManager.instance().addSound(SOUND_FAILED, R.raw.failed);
        SoundManager.instance().addSound(SOUND_SUCCEED, R.raw.succeed);
        for (int i = 0; i < mSounds.length; i++)
        {
            SoundManager.instance().addSound(i, mSounds[i]);
        }
        }

        public interface OnMemoryListener
        {

            void onComplete(int moveCount);

            void onUpdateView();
        }

        public void onPosition(int position)
        {
            Tile tile = mList.get(position);

            if (tile.mSelected)
            {
                // clicked on an already open item
                return;
            }

            tile.select();
            int sound = tile.mResId % mSounds.length;
            playSound(sound);

            switch (mSelectedCount)
            {
                case 0:
                    mT1 = tile;
                    break;

                case 1:
                    mT2 = tile;
                    if (mT1.getResId() == mT2.getResId())
                    {
                        mT1.setFound(true);
                        mT2.setFound(true);
                        mFoundCount += 2;
                        playSound(SOUND_SUCCEED);
                    }
                    else
                    {
//                    playSound( SOUND_FAILED );
                    }
                    break;

                case 2:
                    if (mT1.getResId() != mT2.getResId())
                    {
                        mT1.unselect();
                        mT2.unselect();
                    }
                    mSelectedCount = 0;
                    mT1 = tile;
                    break;
            }
            mSelectedCount++;
            mMoveCount++;
            updateView();
            checkComplete();
        }

        private void updateView()
        {
            mListener.onUpdateView();
        }

        private void checkComplete()
        {
            if (mFoundCount == mList.size())
            {
                mListener.onComplete(mMoveCount);
            }
        }

        /**
         * Add a pair of pieces randomly in the list
         * @param nResId The resid of the piece
         */
        private void addRandomly(int nResId)
        {
            double dPos = Math.random() * mList.size();
            int nPos = (int) dPos;
            //aggiungo il primo tassello nResId
            mList.add(nPos, new Tile(nResId));
            dPos = Math.random() * mList.size();
            nPos = (int) dPos;
            //aggiungo il secondo tassello nResId
            mList.add(nPos, new Tile(nResId));

        }

        private int rand(int nSize)
        {
            double dPos = Math.random() * nSize;
            return (int) dPos;
        }

        private List<Integer> getTileSet()
        {
            List<Integer> list = new ArrayList<Integer>();
            int lastElem = mTiles.length-1;
            Random rand = new Random();

            //i primi 10 li metto a caso
            for(int i=0;i<set_size-10;i++){

                int randomNum = rand.nextInt((lastElem) + 1);
                int t = mTiles[randomNum];
                list.add(t);
            }


            //while (list.size() < SET_SIZE)
            while (list.size() < set_size)
            {
                //nextInt(n) estrae un valore nell'intervallo [0,n)
                //per includere anche n basta aggiungere 1 all'argomento n stesso
                int randomNum = rand.nextInt((lastElem) + 1);
                int t = mTiles[randomNum];
                list.add(t);
                //ora devo prendere l'elemento di mTiles in posizione randomNumber e spostarlo in fondo
                int temp = mTiles[lastElem];
                mTiles[lastElem] = mTiles[randomNum];
                mTiles[randomNum] = temp;
                lastElem--;

            }
            return list;
        }

        private void playSound(int index)
        {
            if (PreferencesService.instance().isSoundEnabled())
            {
                SoundManager.instance().playSound(index);
            }
        }

        public void setDifficultyLevel(int diff)
        {
            difficulty_level = diff;

            switch(difficulty_level){

                case 1:
                    num_cards = 12;
                    num_columns = 3;
                    break;
                case 2:
                    num_cards = 24;
                    num_columns = 4;
                    break;
                case 3:
                    num_cards = 40;
                    num_columns = 5;
                    break;
            }

            set_size = num_cards/2;
        }

        public int getNumberOfColumns()
        {
            return num_columns;
        }

    }
