package ice.model.vertex;

import android.graphics.PointF;
import ice.util.BufferUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static ice.model.Constants.MAX_UNSIGNED_BYTE_VALUE;
import static ice.model.Constants.MAX_UNSIGNED_SHORT_VALUE;
import static ice.util.BufferUtil.byteBuffer;
import static ice.util.BufferUtil.shortBuffer;


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

        float[] vertices = buildVertexData(new PointF(), width, height, enableZ, stepX, stepY, maxU, maxV);

        vertexData.setVertices(vertices);

        return vertexData;
    }

    public static float[] buildVertexData(PointF leftBottom, float width, float height, boolean enableZ, int stepX, int stepY, float maxU, float maxV) {
        int positionDimension = enableZ ? 3 : 2;
        int squareNum = stepX * stepY;
        int verticesCount = squareNum * 2 * 3;

        float[] vertices = new float[verticesCount * (positionDimension + 2)];

        int[] indexes = new int[]{
//                3, 0, 2,
//                1, 2, 0
                0, 1, 2,
                2, 3, 0
        };

        float left = leftBottom.x;
        float bottom = leftBottom.y;
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
                                left + currentStepX * eachSquareWidth,
                                bottom + currentStepY * eachSquareHeight,
                                0,
                                maxU * currentStepX / (float) stepX,
                                maxV * (stepY - currentStepY) / (float) stepY
                        },
                        {
                                //右下角
                                left + (currentStepX + 1) * eachSquareWidth,
                                bottom + currentStepY * eachSquareHeight,
                                0,
                                maxU * (currentStepX + 1) / (float) stepX,
                                maxV * (stepY - currentStepY) / (float) stepY
                        },
                        {
                                //右上角
                                left + (currentStepX + 1) * eachSquareWidth,
                                bottom + (currentStepY + 1) * eachSquareHeight,
                                0,
                                maxU * (currentStepX + 1) / (float) stepX,
                                maxV * (stepY - currentStepY - 1) / (float) stepY
                        },
                        {
                                //左上角
                                left + currentStepX * eachSquareWidth,
                                bottom + (currentStepY + 1) * eachSquareHeight,
                                0,
                                maxU * currentStepX / (float) stepX,
                                maxV * (stepY - currentStepY - 1) / (float) stepY
                        }

                };
            } else {
                data = new float[][]{
                        {
                                //左下角
                                left + currentStepX * eachSquareWidth,
                                bottom + currentStepY * eachSquareHeight,
                                maxU * currentStepX / (float) stepX,
                                maxV * (stepY - currentStepY) / (float) stepY
                        },
                        {
                                //右下角
                                left + (currentStepX + 1) * eachSquareWidth,
                                bottom + currentStepY * eachSquareHeight,
                                maxU * (currentStepX + 1) / (float) stepX,
                                maxV * (stepY - currentStepY) / (float) stepY
                        },
                        {
                                //右上角
                                left + (currentStepX + 1) * eachSquareWidth,
                                bottom + (currentStepY + 1) * eachSquareHeight,
                                maxU * (currentStepX + 1) / (float) stepX,
                                maxV * (stepY - currentStepY - 1) / (float) stepY
                        },
                        {
                                //左上角
                                left + currentStepX * eachSquareWidth,
                                bottom + (currentStepY + 1) * eachSquareHeight,
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

    public static Buffer buildSimpleVertexData(int width, int height, int stepX, int stepY) {

        PointF leftTop = new PointF(0, height);

        float eachW = width / (float) stepX;
        float eachH = -height / (float) stepY;

        int size = (stepX + 1) * (stepY + 1) * (3 + 3 + 2);

        FloatBuffer buffer = BufferUtil.floatBuffer(size);

        for (int j = 0; j < stepY + 1; j++) {
            for (int i = 0; i < stepX + 1; i++) {

                buffer.put(leftTop.x + i * eachW);     //x
                buffer.put(leftTop.y + j * eachH);     //y
                buffer.put(0);             //z

                buffer.put(i / (float) stepX);        //u
                buffer.put(j / (float) stepY);        //v

                buffer.put(0);        //nx
                buffer.put(0);        //ny
                buffer.put(1);        //nz
            }

        }

        buffer.position(0);

        return buffer;
    }

    public static Buffer stripTrianglesIndices(int stepX, int stepY) {
        int vertexCount = (stepX + 1) * (stepY + 1);
        int maxIndex = vertexCount - 1;
        int indicesCount = vertexCount * 2 - (stepX + 1) * 2 + (stepY / 2) * 4;

        Buffer buffer;
        if (maxIndex <= 0) {
            throw new IllegalArgumentException();
        } else if (maxIndex <= MAX_UNSIGNED_BYTE_VALUE) {
            buffer = byteBuffer(indicesCount);
            fillByteStripIndices(stepX, stepY, (ByteBuffer) buffer);
        } else if (maxIndex <= MAX_UNSIGNED_SHORT_VALUE) {
            buffer = shortBuffer(indicesCount);
            fillShortStripIndices(stepX, stepY, (ShortBuffer) buffer);
        } else {
            throw new IllegalArgumentException("too big index " + maxIndex);
        }

        if (buffer.position() != buffer.capacity()) {
            throw new IllegalStateException();
        }

        buffer.position(0);

        return buffer;
    }

    private static void fillByteStripIndices(int stepX, int stepY, ByteBuffer buffer) {
        int upLineStartIndex;
        int downLineStartIndex;

        for (int i = 0; i < stepY; i++) {
            upLineStartIndex = i * (stepX + 1);
            downLineStartIndex = (i + 1) * (stepX + 1);

            if (i % 2 == 0) {
                for (int j = 0; j <= stepX; j++) {
                    buffer.put((byte) (upLineStartIndex + j));
                    buffer.put((byte) (downLineStartIndex + j));
                }
            } else {
                buffer.put((byte) (upLineStartIndex + stepX));
                buffer.put((byte) (downLineStartIndex + stepX));

                for (int j = stepX; j >= 0; j--) {
                    buffer.put((byte) (downLineStartIndex + j));
                    buffer.put((byte) (upLineStartIndex + j));
                }

                buffer.put((byte) upLineStartIndex);
                buffer.put((byte) downLineStartIndex);
            }
        }
    }

    private static void fillShortStripIndices(int stepX, int stepY, ShortBuffer buffer) {
        int upLineStartIndex;
        int downLineStartIndex;

        for (int i = 0; i < stepY; i++) {
            upLineStartIndex = i * (stepX + 1);
            downLineStartIndex = (i + 1) * (stepX + 1);

            if (i % 2 == 0) {
                for (int j = 0; j <= stepX; j++) {
                    buffer.put((short) (upLineStartIndex + j));
                    buffer.put((short) (downLineStartIndex + j));
                }
            } else {
                buffer.put((short) (upLineStartIndex + stepX));
                buffer.put((short) (downLineStartIndex + stepX));

                for (int j = stepX; j >= 0; j--) {
                    buffer.put((short) (downLineStartIndex + j));
                    buffer.put((short) (upLineStartIndex + j));
                }

                buffer.put((short) upLineStartIndex);
                buffer.put((short) downLineStartIndex);
            }
        }
    }

    public static int[] makesTriangleStripIndex(int stepX, int stepY) {
        int vertexCount = (stepX + 1) * (stepY + 1);
        int[] indices = new int[vertexCount * 2 - (stepX + 1) * 2];

        int upLineStartIndex;
        int downLineStartIndex;

        int index = 0;

        for (int i = 0; i < stepY; i++) {
            upLineStartIndex = i * (stepX + 1);
            downLineStartIndex = (i + 1) * (stepX + 1);

            if (i % 2 == 0) {
                for (int j = 0; j <= stepX; j++) {
                    indices[index++] = upLineStartIndex + j;
                    indices[index++] = downLineStartIndex + j;
                }
            } else {
                for (int j = stepX; j >= 0; j--) {
                    indices[index++] = upLineStartIndex + j;
                    indices[index++] = downLineStartIndex + j;
                }
            }
        }

        return indices;
    }

    public static int[] buildTriangleStripIndex(int stepX, int stepY) {
        int vertexCount = (stepX + 1) * (stepY + 1);
        int[] indices = new int[vertexCount * 2 - (stepX + 1) * 2];

        int upLineStartIndex;
        int downLineStartIndex;

        int index = 0;

        for (int i = 0; i < stepY; i++) {
            upLineStartIndex = i * (stepX + 1);
            downLineStartIndex = (i + 1) * (stepX + 1);

            if (i % 2 == 0) {
                for (int j = 0; j <= stepX; j++) {
                    indices[index++] = upLineStartIndex + j;
                    indices[index++] = downLineStartIndex + j;
                }
            } else {
                for (int j = stepX; j >= 0; j--) {
                    indices[index++] = upLineStartIndex + j;
                    indices[index++] = downLineStartIndex + j;
                }
            }
        }

        return indices;
    }

    public static int[] makeTriangleStripIndex(int stepX, int stepY) {
        int[] indices = new int[stepX * stepY * 3];

        int index = 0;

        // Mesh indices
        int cols = stepX + 1;
        int rows = stepY + 1;

        for (int x = 0; x < cols - 1; x++) {

            for (int y = 0; y < rows - 1; y++) {
                int i = y + x * rows;

                indices[index++] = i;
                indices[index++] = i + 4;
                indices[index++] = (i + 4) - 3;
            }

        }


        return indices;
    }

}
