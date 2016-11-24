package com.rekotc.memory_final;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import android.util.DisplayMetrics;

import java.text.MessageFormat;

public class MainActivity extends Activity implements View.OnClickListener, Memory.OnMemoryListener{

    private static final int[] tiles_default =
            {
                    R.drawable.default_1, R.drawable.default_2,
                    R.drawable.default_3, R.drawable.default_4, R.drawable.default_5, R.drawable.default_6,
                    R.drawable.default_7, R.drawable.default_8, R.drawable.default_9, R.drawable.default_10,
                    R.drawable.default_11, R.drawable.default_12, R.drawable.default_13, R.drawable.default_14,
                    R.drawable.default_15, R.drawable.default_16, R.drawable.default_17, R.drawable.default_18,
                    R.drawable.default_19, R.drawable.default_20, R.drawable.default_21, R.drawable.default_22,
                    R.drawable.default_23, R.drawable.default_24, R.drawable.default_25, R.drawable.default_26,
                    R.drawable.default_27, R.drawable.default_28, R.drawable.default_29, R.drawable.default_30,
                    R.drawable.default_31, R.drawable.default_32, R.drawable.default_33, R.drawable.default_34
            };


    private static final int[] tiles_christmas =
            {
                    R.drawable.christmas_1, R.drawable.christmas_2,
                    R.drawable.christmas_3, R.drawable.christmas_4, R.drawable.christmas_5, R.drawable.christmas_6,
                    R.drawable.christmas_7, R.drawable.christmas_8, R.drawable.christmas_9, R.drawable.christmas_10,
                    R.drawable.christmas_11, R.drawable.christmas_12, R.drawable.christmas_13, R.drawable.christmas_14 ,
                    R.drawable.christmas_15, R.drawable.christmas_16, R.drawable.christmas_17, R.drawable.christmas_18,
                    R.drawable.christmas_19, R.drawable.christmas_20, R.drawable.christmas_21, R.drawable.christmas_22
            };


    private static final int[] tiles_easter =
            {
                    R.drawable.easter_1, R.drawable.easter_2,
                    R.drawable.easter_3, R.drawable.easter_4, R.drawable.easter_5, R.drawable.easter_6,
                    R.drawable.easter_7, R.drawable.easter_8, R.drawable.easter_9, R.drawable.easter_10,
                    R.drawable.easter_11, R.drawable.easter_12, R.drawable.easter_13, R.drawable.easter_14
            };

    private static final int[] tiles_tux =
            {
                    R.drawable.tux_1, R.drawable.tux_2,
                    R.drawable.tux_3, R.drawable.tux_4, R.drawable.tux_5, R.drawable.tux_6,
                    R.drawable.tux_7, R.drawable.tux_8, R.drawable.tux_9, R.drawable.tux_10,
                    R.drawable.tux_11, R.drawable.tux_12, R.drawable.tux_13, R.drawable.tux_14,
                    R.drawable.tux_15, R.drawable.tux_16, R.drawable.tux_17, R.drawable.tux_18,
                    R.drawable.tux_19, R.drawable.tux_20, R.drawable.tux_21, R.drawable.tux_22,
                    R.drawable.tux_23, R.drawable.tux_24, R.drawable.tux_25, R.drawable.tux_26,
                    R.drawable.tux_27, R.drawable.tux_28, R.drawable.tux_29, R.drawable.tux_30,
                    R.drawable.tux_31, R.drawable.tux_32, R.drawable.tux_33
            };

    private static final int[] tiles_russia =
            {
                    R.drawable.russia_1, R.drawable.russia_2,
                    R.drawable.russia_3, R.drawable.russia_4, R.drawable.russia_5, R.drawable.russia_6,
                    R.drawable.russia_7, R.drawable.russia_8, R.drawable.russia_9, R.drawable.russia_10,
                    R.drawable.russia_11, R.drawable.russia_12, R.drawable.russia_13, R.drawable.russia_14,
                    R.drawable.russia_15, R.drawable.russia_16, R.drawable.russia_17, R.drawable.russia_18
            };

    private static final int[][] icons_set = { tiles_default , tiles_christmas, tiles_easter, tiles_tux, tiles_russia};

    private static final int[] sounds = {
            R.raw.blop, R.raw.chime, R.raw.chtoing, R.raw.tic, R.raw.toc,
            R.raw.toing, R.raw.toing2, R.raw.toing3, R.raw.toing4, R.raw.toing5,
            R.raw.toing6, R.raw.toong, R.raw.tzirlup, R.raw.whiipz
    };

    private static final int[] not_found_tile_set =
            {
                   R.drawable.not_found_default,
                   R.drawable.not_found_christmas,
                   R.drawable.not_found_easter,
                   R.drawable.not_found_tux,
                   R.drawable.not_found_russia
            };

    private static final String PREF_STARTED = "started";
    private static final int SOUND_NEW_GAME = 1000;
    public static final int SPLASH_SCREEN_ROTATION_COUNT = 2;
    private static final int SPLASH_SCREEN_ROTATION_DURATION = 2000;
    private static final int GAME_SCREEN_ROTATION_COUNT = 2;
    private static final int GAME_SCREEN_ROTATION_DURATION = 2000;
    private static final String KEY_VERSION = "version";
    private static final int DEFAULT_VERSION = 1;  // should be set to 0 after 1.4
    protected boolean mQuit;
    private ViewGroup mContainer;
    private View mSplash;
    public Button mButtonPlay;

    private int screen_width;
    private int screen_height;

    //gestione spinner del tema
    private Spinner mSpinner;
    private int mIconSet;


    //gestione spinner del livello difficoltà
    private Spinner mSpinnerDiff;

    private boolean mStarted;


    private Memory mMemory;
    private MemoryGridView mGridView;

    private static final int MARGIN = 5;
    //livelli disponibili 1 - 2 - 3
    private int difficulty_level = 1;

    protected void setNewGame()
    {

            int set = PreferencesService.instance().getIconsSet();
            mMemory = new Memory( icons_set[ set ], sounds , not_found_tile_set[ set ], this);

            mMemory.reset();
            //mGridView = (MemoryGridView) findViewById(R.id.gridview);

      /*  setContentView(R.layout.gamegrid_screen);
        mGridView = (MemoryGridView) findViewById(R.id.gamegrid);
     //   setContentView(R.layout.main);

        mGridView.setAdapter(new ImageAdapter(mGridView.getContext(), screen_width, screen_height, MARGIN, mMemory));
        mGridView.setMemory(mMemory);
        mGridView.setScreenSize(screen_width,screen_height);*/
    }

    //aggiorno la griglia di gioco
    private void drawGrid()
    {
        mGridView.update();

    }

    public void onUpdateView()
    {
        drawGrid();
    }

    public void onComplete(int countMove)
    {
        int nHighScore = PreferencesService.instance().getHiScore();
        String title = getString(R.string.success_title);
        Object[] args = { countMove, nHighScore };
        String message = MessageFormat.format(getString(R.string.success), args );
        int icon = R.drawable.win;
        if (countMove < nHighScore)
        {
            title = getString(R.string.hiscore_title);
            message = MessageFormat.format(getString(R.string.hiscore), args );
            icon = R.drawable.hiscore;

            PreferencesService.instance().saveHiScore(countMove);
        }
        this.showEndDialog(title, message, icon);
    }

    protected void showEndDialog(String title, String message, int icon)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setIcon(icon);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.new_game),
                new DialogInterface.OnClickListener()
                {

                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        onNewGame();
                    }
                });
        builder.setNegativeButton(getString(R.string.quit),
                new DialogInterface.OnClickListener()
                {

                    public void onClick(DialogInterface dialog, int id)
                    {
                        quit();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void onNewGame()
    {
        if( PreferencesService.instance().isSoundEnabled() )
        {
            SoundManager.instance().playSound(SOUND_NEW_GAME);
        }
        setNewGame();
    }

    void quit()
    {
        mQuit = true;
        MainActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //default layout
        //setContentView(R.layout.activity_main);
       // setContentView(R.layout.main);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;

        //inizializzo il singleTon che userò per gestire le preferenze
        PreferencesService.init( this );

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        SoundManager.init(MainActivity.this);

        setContentView(R.layout.main);
        mContainer = (ViewGroup) findViewById(R.id.container);
        mSplash = (View) findViewById(R.id.splash);

        mButtonPlay = (Button) findViewById(R.id.button_play);
        mButtonPlay.setOnClickListener(this);

        ImageView image = (ImageView) findViewById(R.id.image_splash);
        image.setImageResource(R.drawable.splash);

        //gestisco lo spinner del tema
        mSpinner = (Spinner) findViewById(R.id.spinner_theme);

        mIconSet = PreferencesService.instance().getIconsSet();
        mSpinner.setSelection( mIconSet );

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setIconSet(position);
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                setIconSet( 0 );
            }

        });

        mSpinnerDiff = (Spinner) findViewById(R.id.spinner_diff);

        mSpinnerDiff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                difficulty_level = position +1;
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                difficulty_level = 1;
            }

        });




        setNewGame();

    }

    protected View getGameView()
    {
        return mGridView;
    }

    public void onClick(View v) {

        if (v == mButtonPlay)
        {
            applyRotation(0, SPLASH_SCREEN_ROTATION_COUNT * 360);
            //newGame();
        }
    }

    protected void onResume()
    {
        super.onResume();

        mMemory.onResume( PreferencesService.instance().getPrefs() );

        SharedPreferences prefs = getPreferences(0);
        mStarted = prefs.getBoolean(PREF_STARTED, false);
        if (mStarted)
        {
            mSplash.setVisibility(View.GONE);
           // mMemory.setDifficultyLevel(difficulty_level);
            //getGameView().setVisibility(View.VISIBLE);
        } else
        {
            mSplash.setVisibility(View.VISIBLE);
            //getGameView().setVisibility(View.GONE);
        }

        if (!SoundManager.isInitialized())
        {
            SoundManager.init(this);
        }
        SoundManager.instance().addSound(SOUND_NEW_GAME, R.raw.new_game);

        //setContentView(R.layout.gamegrid_screen);
        //mGridView = (MemoryGridView) findViewById(R.id.gamegrid);
        //boh forse
        //drawGrid();

    }

    private final class DisplayNextView implements Animation.AnimationListener
    {

        private DisplayNextView()
        {
        }

        public void onAnimationStart(Animation animation)
        {
            if( PreferencesService.instance().isSoundEnabled() )
            {
                SoundManager.instance().playSound(SOUND_NEW_GAME);
            }
        }

        public void onAnimationEnd(Animation animation)
        {
            mContainer.post(new SwapViews());
        }

        public void onAnimationRepeat(Animation animation)
        {
        }
    }

    private final class SwapViews implements Runnable
    {

        public void run()
        {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            Rotate3dAnimation rotation;

            //nascondo la splashscreen iniziale
            mSplash.setVisibility(View.GONE);

            setContentView(R.layout.gamegrid_screen);
            mGridView = (MemoryGridView) findViewById(R.id.gamegrid);
            //   setContentView(R.layout.main);
            mMemory.setDifficultyLevel(difficulty_level);
            mMemory.reset();

            int test = mMemory.getNumberOfColumns();
            mGridView.setNumColumns(test);
            mGridView.setAdapter(new ImageAdapter(mGridView.getContext(), screen_width, screen_height, MARGIN, mMemory));
            mGridView.setMemory(mMemory);
            mGridView.setScreenSize(screen_width,screen_height);

            //setContentView(R.layout.gamegrid_screen);
            //mGridView = (MemoryGridView) findViewById(R.id.gamegrid);
            //int test = mMemory.getNumberOfColumns();
            //mGridView.setNumColumns(4);

            //mGridView.setAdapter(new ImageAdapter(mGridView.getContext(), screen_width, screen_height, MARGIN, mMemory));
            //mGridView.setMemory(mMemory);
            //mGridView.setScreenSize(screen_width,screen_height);
            //drawGrid();
            //mostro la screen di gioco
            getGameView().setVisibility(View.VISIBLE);
            getGameView().requestFocus();


            //aggiorno il valore del set selezionato

            rotation = new Rotate3dAnimation(0, 360 * GAME_SCREEN_ROTATION_COUNT, centerX, centerY, 310.0f, false);

            rotation.setDuration(GAME_SCREEN_ROTATION_DURATION);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            mContainer.startAnimation(rotation);
            mStarted = true;
        }
    }

    public void applyRotation(float start, float end)
    {

        // Find the center of the container
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(SPLASH_SCREEN_ROTATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView());

        mContainer.startAnimation(rotation);

    }

    private void setIconSet(int iconSet)
    {
        if( iconSet != mIconSet )
        {
            PreferencesService.instance().saveIconsSet( iconSet );
            Toast.makeText(this, R.string.message_effect_new_game, Toast.LENGTH_LONG).show();
            mIconSet = iconSet;
        }
    }

    private void setDifficultyLevel(int diff)
    {

    }


}
