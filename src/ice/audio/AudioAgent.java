package ice.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * User: jason
 * Date: 12-3-27
 * Time: 上午11:36
 */
public class AudioAgent {

    private static final String TAG = AudioAgent.class.getSimpleName();

    private static AudioAgent instance;

    public static AudioAgent build(Context context, String audioActionURL) {
        instance = new AudioAgent(context, audioActionURL);
        return instance;
    }

    public static AudioAgent get() {
        return instance;
    }

    public AudioAgent(Context context, String audioActionURL) {
        this.context = context;
        this.audioActionURL = audioActionURL;

        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder binder) {
                service = IAudio.Stub.asInterface(binder);
                Log.i(TAG, "onServiceConnected " + service);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.i(TAG, "onServiceDisconnected " + service);
                service = null;
            }
        };
    }

    public void bind() {

        if (service != null) return;

        context.bindService(
                new Intent(audioActionURL),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    public void release() {

        if (service != null) {
            try {
                service.release();
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        if (service != null) {
            context.unbindService(serviceConnection);
            service = null;
        }

        try {
            context.stopService(new Intent(audioActionURL));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMusic(String res) {
        playMusic(res, false);
    }

    public void playMusic(String res, boolean loop) {

        if (silence) return;

        Log.i(TAG, "playMusic " + service);

        if (service == null) return;

        try {
            service.playMusic(res, loop);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void playSound(String res) {
        if (silence) return;

        if (service == null) return;

        try {
            service.playSound(res);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pause(String res) {
        if (silence) return;

        if (service == null) return;

        try {
            service.pause(res);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void resume(String res) {
        if (silence) return;
        if (service == null) return;

        try {
            service.resume(res);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stop(String res) {

        if (service == null) return;

        try {
            service.stop(res);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public boolean isSilence() {
        return silence;
    }

    public void setSilence(boolean silence) {
        this.silence = silence;
    }

    private boolean silence;
    private Context context;
    private IAudio service;
    private String audioActionURL;
    private ServiceConnection serviceConnection;
}
