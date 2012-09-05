package ice.engine;

import android.content.Context;

public interface App {

    Context getContext();

    AppView getRender();

    SceneProviderManager getSceneProviderManager();

    void exit();

}
