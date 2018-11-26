package si.fri.rgti;

import kotlin.reflect.jvm.internal.impl.types.checker.TypeCheckerContext;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class TerrainBlock
    extends GameObject {

    // position in game-space
    int x;
    int y;

    static int resolution = 33;

    float [][] heightmap;

    // vertex array
    Vector3f[] vertex_array = null;
    // triangle array
    int[][] triangle_array = null;

    // buffers
    FloatBuffer vertex_buffer = null;
    IntBuffer triangle_buffer = null;

    /**
     * Constructor: constructs an empty - flat - terrain block.
     */
    TerrainBlock () {
    }

    /**
     * Contructor: constructs a TerrainBlock from heightmap.
     * @param heightmap Height map for this terrain block - float[][]
     */
    TerrainBlock (float[][] heightmap) {
        this.heightmap = heightmap;
    }

    /**
     * Creates geometry from heightmaps.
     * Generates vertices, generates triangles index.
     */
    private void createGeometry() {
        // create array
        vertex_array = new Vector3f[resolution*resolution];
        // create geometry
        int va_index = 0;
        for (int i=0; i<resolution; i++) {
            for (int j=0; j<resolution; j++) {
                Vector3f vertex = new Vector3f(i, j, heightmap[i][j]);
                vertex_array[va_index++] = vertex;
            }
        }
        // create triangle array
        triangle_array = new int[(resolution-1)*2*(resolution-1)][3];
        // connect triangles
        int ta_index = 0;
        for (int i=0; i<resolution-1; i++) {
            for (int j=0; j<resolution-1; i++) {
                int[] upper_triangle = {
                        i*(resolution-1)+j,
                        i*(resolution-1)+j+1,
                        (i+1)*(resolution-1)+j+1};
                triangle_array[ta_index++] = upper_triangle;
                int[] lower_triangle = {
                        i*(resolution-1)+j+1,
                        (i+1)*(resolution-1)+j+1,
                        (i+1)*(resolution-1)+j};
            }
        }
    }

    /**
     * Creates buffers for OpenGL if not yet created.
     */
    private void createBuffers() {
        vertex_buffer = BufferUtils.createFloatBuffer(vertex_array.length*3);
        triangle_buffer = BufferUtils.createIntBuffer(triangle_array.length*3);

    }

    /**
     * Render object
     */
    @Override
    public void draw() {
        super.draw();

        // create geometry if not yet done
        if (vertex_array == null || triangle_array == null)
            this.createGeometry();

        // build buffer objects if not yet done
        if (vertex_buffer == null || triangle_buffer == null)
            this.createBuffers();

    }

    float[] getHeightmapBorder(String direction) {
        /**
         * Get float array of height along a chosen border.
         * For direction that don't share a border but only a corner,
         * returns null.
         * :param direction: string in ["F","B","R","L"] return values, others null
         * :return: float array of height values along a border
         */
        float[] retval = new float[resolution];
        switch (direction) {
            case "F":
                retval = heightmap[0];
                break;
            case "B":
                retval = heightmap[resolution-1];
                break;
            case "L":
                for (int i=0; i<resolution; i++) {
                    retval[i] = heightmap[i][0];
                }
                break;
            case "R":
                for (int i=0; i<resolution; i++) {
                    retval[i] = heightmap[i][resolution-1];
                }
                break;
            default:
                retval = null;
        }
        return retval;
    }
}
