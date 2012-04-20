package ice.engine;

import android.util.Log;

public abstract class SceneProvider<T extends Scene> {

    protected SceneProvider() {
        tag = getClass().getSimpleName();
    }

    protected void onCreate() {
        scene = onCreateScene();
        scene.onCreate();
        Log.i(tag, "onCreate");
    }

    protected abstract T onCreateScene();

    protected void onResume() {
        getScene().onResume();
        Log.i(tag, "onResume");
    }

    protected void onPause() {
        getScene().onPause();
        Log.i(tag, "onPause");
    }

    protected void onStop() {
        getScene().onStop();
        Log.i(tag, "onStop");
    }

    protected void setIntentMsg(Object msg) {
        this.intentMsg = msg;
    }

    public T getScene() {
        return scene;
    }

    protected boolean isEntry() {
        return false;
    }

    protected boolean onBackPressed() {
        return false;
    }

    public App getApp() {
        return EngineContext.getInstance().getApp();
    }

    protected Object intentMsg;
    private final String tag;
    protected T scene;
}
