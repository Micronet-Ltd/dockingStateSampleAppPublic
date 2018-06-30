package com.micronet.dockingstatesample;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    protected static MainActivity sInstance = null;
    private final String TAG = getClass().getSimpleName();
    protected int mDockState = -1;

    private IntentFilter filter = new IntentFilter(Intent.ACTION_DOCK_EVENT);
    private DockStateReceiver receiver = new DockStateReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();
        sInstance = this;
        registerReceiver(receiver, filter);
    }

    /*
     * get the current docking state
     * from the last ACTION_DOCK_EVENT sticky intent
     */
    public int getCurrentDockState() {
        int currentDockState = -1;
        /*
         * Receiving the current docking state
         */
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_DOCK_EVENT);
        Intent intent = registerReceiver(null, ifilter);
        if (intent != null) {
            currentDockState = intent.getIntExtra(Intent.EXTRA_DOCK_STATE, -1);
        }
        return currentDockState;
    }

    @Override
    public void onPause() {
        unregisterReceiver(receiver);
        sInstance = null;
        super.onPause();
    }

    /*
     * Called when the user clicks the Send button
     */
    public void exitApp(View view) {
        finish();
    }

    protected void displayCradleState() {
        String cradleStateMsg, ignitionStateMsg;
        int cradleStateImage, ignitionStateInage;
        switch (mDockState) {
            case Intent.EXTRA_DOCK_STATE_UNDOCKED:
                cradleStateMsg = getString(R.string.not_in_cradle_state_text);
                ignitionStateMsg = getString(R.string.ignition_unknown_state_text);
                cradleStateImage = R.drawable.ic_out_of_cradle;
                ignitionStateInage = R.drawable.ic_ignition_unknown;
                break;
            case Intent.EXTRA_DOCK_STATE_DESK:
            case Intent.EXTRA_DOCK_STATE_LE_DESK:
            case Intent.EXTRA_DOCK_STATE_HE_DESK:
                cradleStateMsg = getString(R.string.in_cradle_state_text);
                //ignitionStateMsg = getString(R.string.ignition_off_state_text);
                ignitionStateMsg = getString(R.string.ignition_off_state_text);
                cradleStateImage = R.drawable.ic_in_cradle;
                ignitionStateInage = R.drawable.ic_ignition_off;
                break;
            case Intent.EXTRA_DOCK_STATE_CAR:
                cradleStateMsg = getString(R.string.in_cradle_state_text);
                ignitionStateMsg = getString(R.string.ignition_on_state_text);
                cradleStateImage = R.drawable.ic_in_cradle;
                ignitionStateInage = R.drawable.ic_ignition_on;
                break;
            default:
                /* this state indicates un-defined docking state */
                cradleStateMsg = getString(R.string.not_in_cradle_state_text);
                ignitionStateMsg = getString(R.string.ignition_unknown_state_text);
                cradleStateImage = R.drawable.ic_out_of_cradle;
                ignitionStateInage = R.drawable.ic_ignition_unknown;
                break;
        }

        TextView cradleStateTextview = (TextView) findViewById(R.id.cradle_state_textview);
        TextView ignitionStateTextview = (TextView) findViewById(R.id.ignition_state_textview);
        ImageView cradleStateImageview = (ImageView) findViewById(R.id.cradle_state_imageview);
        ImageView ignitionStateImageview = (ImageView) findViewById(R.id.ignition_state_imageview);
        cradleStateTextview.setText(cradleStateMsg);
        ignitionStateTextview.setText(ignitionStateMsg);
        cradleStateImageview.setImageResource(cradleStateImage);
        ignitionStateImageview.setImageResource(ignitionStateInage);
    }
}