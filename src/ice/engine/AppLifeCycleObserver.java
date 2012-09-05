package ice.engine;

/**
 * User: jason
 * Date: 12-9-4
 * Time: 下午6:02
 */
public interface AppLifeCycleObserver {
    void onCreate(App app);

    void onResume(App app);

    void onPause(App app);

    void onDestroy(App app);
}
