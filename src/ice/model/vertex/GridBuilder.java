package ice.model.vertex;

/**
 * User: jason
 * Date: 13-1-15
 */
public class GridBuilder {

    public static AbstractVertexData createVertexData(float width, float height, boolean enableZ, int stepX, int stepY) {
        return createVertexData(width, height, enableZ, stepX, stepY, 1, 1, true);
    }

    public static AbstractVertexData createVertexData(float width, float height, boolean enableZ, int stepX, int stepY, float maxU, float maxV, boolean vboMode) {

        int positionDimension = enableZ ? 3 : 2;

        VertexAttribute[] attributesArray = new VertexAttribute[]{
                new VertexAttribute(
                        VertexAttribute.Usage.Position,
                        positionDimension
                ),

                new VertexAttribute(
                        VertexAttribute.Usage.TextureCoordinates,
                        2
                )
        };

        int squareNum = stepX * stepY;
        int verticesCount = squareNum * 2 * 3;

        AbstractVertexData vertexData;

        if (vboMode) {
            vertexData = new VertexBufferObject(
                    verticesCount,
                    new VertexAttributes(attributesArray)
            );
        } else {
            vertexData = new VertexArray(
                    verticesCount,
                    new VertexAttributes(attributesArray)
            );
        }

        float[] vertices = buildVertexData(width, height, enableZ, stepX, stepY, maxU, maxV);

        vertexData.setVertices(vertices);

        return vertexData;
    }

    public static float[] buildVertexData(float width, float height, boolean enableZ, int stepX, int stepY, float maxU, float maxV) {
        int positionDimension = enableZ ? 3 : 2;
        int squareNum = stepX * stepY;
        int verticesCount = squareNum * 2 * 3;

        float[] vertices = new float[verticesCount * (positionDimension + 2)];

        int[] indexes = new int[]{
                3, 0, 2,
                1, 2, 0
        };

        float bottomLeftX = -width / 2;
        float bottomLeftY = -height / 2;
        float eachSquareWidth = width / stepX;
        float eachSquareHeight = height / stepY;

        for (int square = 0, elementIndex = 0; square < squareNum; square++) {

            int currentStepX = square % stepX;
            int currentStepY = square / stepY;

            float[][] data;

            if (enableZ) {
                data = new float[][]{
                        {
                                //左下角
                                bottomLeftX + currentStepX * eachSquareWidth,
                                bottomLeftY + currentStepY * eachSquareHeight,
                                0,
                                maxU * currentStepX / (float) stepX,
                                maxV * (stepY - currentStepY) / (float) stepY
                        },
                        {
                                //右下角
                                bottomLeftX + (currentStepX + 1) * eachSquareWidth,
                                bottomLeftY + currentStepY * eachSquareHeight,
                                0,
                                maxU * (currentStepX + 1) / (float) stepX,
                                maxV * (stepY - currentStepY) / (float) stepY
                        },
                        {
                                //右上角
                                bottomLeftX + (currentStepX + 1) * eachSquareWidth,
                                bottomLeftY + (currentStepY + 1) * eachSquareHeight,
                                0,
                                maxU * (currentStepX + 1) / (float) stepX,
                                maxV * (stepY - currentStepY - 1) / (float) stepY
                        },
                        {
                                //左上角
                                bottomLeftX + currentStepX * eachSquareWidth,
                                bottomLeftY + (currentStepY + 1) * eachSquareHeight,
                                0,
                                maxU * currentStepX / (float) stepX,
                                maxV * (stepY - currentStepY - 1) / (float) stepY
                        }

                };
            } else {
                data = new float[][]{
                        {
                                //左下角
                                bottomLeftX + currentStepX * eachSquareWidth,
                                bottomLeftY + currentStepY * eachSquareHeight,
                                maxU * currentStepX / (float) stepX,
                                maxV * (stepY - currentStepY) / (float) stepY
                        },
                        {
                                //右下角
                                bottomLeftX + (currentStepX + 1) * eachSquareWidth,
                                bottomLeftY + currentStepY * eachSquareHeight,
                                maxU * (currentStepX + 1) / (float) stepX,
                                maxV * (stepY - currentStepY) / (float) stepY
                        },
                        {
                                //右上角
                                bottomLeftX + (currentStepX + 1) * eachSquareWidth,
                                bottomLeftY + (currentStepY + 1) * eachSquareHeight,
                                maxU * (currentStepX + 1) / (float) stepX,
                                maxV * (stepY - currentStepY - 1) / (float) stepY
                        },
                        {
                                //左上角
                                bottomLeftX + currentStepX * eachSquareWidth,
                                bottomLeftY + (currentStepY + 1) * eachSquareHeight,
                                maxU * currentStepX / (float) stepX,
                                maxV * (stepY - currentStepY - 1) / (float) stepY
                        }

                };
            }

            for (int index : indexes) {
                float[] v = data[index];

                for (float value : v) {
                    vertices[elementIndex++] = value;
                }

            }

        }
        return vertices;
    }

}
