package ice.graphic.texture;

/**
 * User: jason
 * Date: 12-3-31
 * Time: 下午3:33
 */
public class Etc1ShareStrategy implements ShareStrategy {

    public Etc1ShareStrategy(int rawResId) {
        this.rawResId = rawResId;
    }

    @Override
    public Texture createShared() {
        return new ETC1Texture(rawResId);
    }

    private int rawResId;
}
