package ice.engine;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * User: jason
 * Date: 12-9-4
 * Time: 下午5:50
 */
public class SceneProviderManager implements AppLifeCycleObserver, GlRenderer.OnPreparedListener {
    private App app;
    private SceneProvider topProvider;
    private Stack<Class<? extends SceneProvider>> providerStack;
    private Map<Class<? extends SceneProvider>, SoftReference<SceneProvider>> providerCache;

    public SceneProviderManager(App app, Class<? extends SceneProvider> entry) {
        this.app = app;

        providerStack = new Stack<Class<? extends SceneProvider>>();
        providerCache = new HashMap<Class<? extends SceneProvider>, SoftReference<SceneProvider>>();

        providerStack.push(entry);
    }

    public void intent(Class<? extends SceneProvider> to) {
        intent(to, null);
    }

    public void intent(Class<? extends SceneProvider> to, Object msg) {
        topProvider.onPause();

        SceneProvider toProvider = findFromCache(to);
        if (toProvider == null) {
            toProvider = buildInstance(to);
            toProvider.setIntentMsg(msg);
            toProvider.onCreate();
        }

        toProvider.onResume();
        topProvider = toProvider;

        if (toProvider.isEntry())
            providerStack.clear();

        providerStack.push(to);

        switchToScene(toProvider);
    }

    private void switchToScene(SceneProvider sceneProvider) {
        app.getRender().switchScene(sceneProvider.getScene());
    }

    public void back() {

        if (providerStack.size() <= 1) {
            app.exit();
            return;
        }

        Class<? extends SceneProvider> currentProviderClass = providerStack.pop();

        topProvider = findFromCache(currentProviderClass);

        if (topProvider.isEntry()) {
            providerStack.clear();
            app.exit();
            return;
        }

        Class<? extends SceneProvider> topProviderClass = providerStack.peek();
        SceneProvider nextProvider = findFromCache(topProviderClass);
        if (nextProvider == null) {
            nextProvider = buildInstance(topProviderClass);
            nextProvider.onCreate();
        }

        if (nextProvider.isEntry()) {
            providerStack.clear();
            providerStack.push(nextProvider.getClass());
        }

        topProvider.onPause();
        nextProvider.onResume();
        topProvider = nextProvider;
        switchToScene(nextProvider);
    }

    private SceneProvider findFromCache(Class<? extends SceneProvider> providerClass) {

        SoftReference<SceneProvider> cache = providerCache.get(providerClass);

        if (cache != null) {
            SceneProvider instance = cache.get();

            if (instance != null)
                return instance;

        }

        return null;
    }

    private SceneProvider buildInstance(Class<? extends SceneProvider> providerClass) {

        SceneProvider providerInstance = null;
        try {
            providerInstance = providerClass.newInstance();
            providerCache.put(providerClass, new SoftReference<SceneProvider>(providerInstance));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return providerInstance;
    }


    @Override
    public void onPrepared() {
        Class<? extends SceneProvider> topProviderClass = providerStack.peek();

        topProvider = findFromCache(topProviderClass);

        if (topProvider == null) {
            topProvider = buildInstance(topProviderClass);
            topProvider.onCreate();
        }

        topProvider.onResume();
        app.getRender().showScene(topProvider.getScene());
    }

    @Override
    public void onCreate(final App app) {
        if (topProvider != null)
            topProvider.onCreate();
    }

    @Override
    public void onResume(App app) {
        if (topProvider != null)
            topProvider.onResume();
    }

    @Override
    public void onPause(App app) {
        if (topProvider != null)
            topProvider.onPause();
    }

    @Override
    public void onDestroy(App app) {
        if (topProvider != null)
            topProvider.onStop();
    }

}
