package ice.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import ice.graphic.projection.PerspectiveProjection;
import ice.res.Res;

import java.util.LinkedList;
import java.util.List;

/**
 * User: ice
 * Date: 12-1-6
 * Time: 下午3:09
 */
public abstract class Game extends Activity implements App {
    private static final String TAG = Game.class.getName();

    private GameView gameView;
    private SceneProviderManager sceneProviderManager;
    private List<AppLifeCycleObserver> lifeCycleObservers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //todo warnning ! 本来放在onCreate里会合理些，但那样onPause、onResume间切换绘制帧数明显递减，先这样解决吧，还过的去
        setContentView(gameView = buildGameView());

        Rect rect = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);

        EngineContext.build(this);
        Res.built(this);

        sceneProviderManager = new SceneProviderManager(this, getEntry()); //启动时，保证是主界面的入口
        gameView.getRenderer().setOnPreparedListener(sceneProviderManager);
        lifeCycleObservers = new LinkedList<AppLifeCycleObserver>();

        lifeCycleObservers.add(gameView);
        lifeCycleObservers.add(sceneProviderManager);

        notifyCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        notifyResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        notifyPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        notifyDestroy();
    }

    protected abstract Class<? extends SceneProvider> getEntry();

    @Override
    public AppView getRender() {
        return gameView;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "back");
        sceneProviderManager.back();
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public SceneProviderManager getSceneProviderManager() {
        return sceneProviderManager;
    }

    protected GameView buildGameView() {

        return new GameView(this) {

            @Override
            protected GlRenderer onCreateGlRenderer() {

                PerspectiveProjection projection = new PerspectiveProjection(new GLU(), 60);

                return new GlRenderer(projection);
            }
        };
    }

    private void notifyCreate() {
        Log.i(TAG, "onCreate");

        for (AppLifeCycleObserver observer : lifeCycleObservers) {
            observer.onCreate(this);
        }
    }

    private void notifyResume() {
        Log.i(TAG, "onResume");

        for (AppLifeCycleObserver observer : lifeCycleObservers) {
            observer.onResume(this);
        }

    }

    private void notifyPause() {
        Log.i(TAG, "onPause");

        for (AppLifeCycleObserver observer : lifeCycleObservers) {
            observer.onPause(this);
        }
    }

    private void notifyDestroy() {
        Log.i(TAG, "onDestroy");

        for (AppLifeCycleObserver observer : lifeCycleObservers) {
            observer.onDestroy(this);
        }
    }

}
