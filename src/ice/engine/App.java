package ice.engine;

import android.content.Context;
import android.content.SharedPreferences;

public interface App {

    Context getContext();

    AppView getRender();

    SharedPreferences getPreferences();

    int getWidth();

    int getHeight();

    void exit();

    void intent(Class<? extends SceneProvider> to);

    void intent(Class<? extends SceneProvider> toClass, Object msg);

    void back();
}
