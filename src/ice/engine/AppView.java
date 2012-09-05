package ice.engine;

/**
 * User: Jason
 * Date: 11-12-3
 * Time: 下午6:39
 */
public interface AppView {
    public interface OnSizeChangeListener {
        void onSizeChange(float width, float height);
    }

    void showScene(Scene scene);

    void switchScene(Scene newScene);

    void addOnSizeChangeListener(OnSizeChangeListener listener);

    void removeOnSizeChangeListener(OnSizeChangeListener listener);

    int getWidth();

    int getHeight();

}
