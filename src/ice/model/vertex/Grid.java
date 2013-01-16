package ice.model.vertex;

import static java.lang.String.format;

/**
 * User: jason
 * Date: 13-1-15
 */
public class Grid extends VertexBufferObject {

    private int stepX, stepY;
    private int width, height;

    public Grid(int width, int height, int stepX, int stepY) {
        this(width, height, stepX, stepY, false);
    }

    public Grid(int width, int height, int stepX, int stepY, boolean enableZ) {

        if (width <= 0 || height <= 0 || stepX < 1 || stepY < 1) {
            throw new IllegalArgumentException(format("%d,%d,%d,%d", width, height, stepX, stepY));
        }

        this.width = width;
        this.height = height;
        this.stepX = stepX;
        this.stepY = stepY;

        setupVertexes(enableZ);
    }

    private void setupVertexes(boolean enableZ) {
        int squareNum = stepX * stepY;
        int verticesCount = squareNum * 2 * 3;

        VertexAttribute[] attributesArray = new VertexAttribute[]{
                new VertexAttribute(
                        VertexAttribute.Usage.Position,
                        enableZ ? 3 : 2
                ),

                new VertexAttribute(
                        VertexAttribute.Usage.TextureCoordinates,
                        2
                )
        };

        init(verticesCount, new VertexAttributes(attributesArray));

        float[] vertexes = GridBuilder.buildVertexData(width, height, enableZ, stepX, stepY, 1, 1);

        setVertices(vertexes);
    }

    public int getStepX() {
        return stepX;
    }

    public int getStepY() {
        return stepY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
