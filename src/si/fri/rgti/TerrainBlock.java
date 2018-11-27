package si.fri.rgti;

import kotlin.reflect.jvm.internal.impl.types.checker.TypeCheckerContext;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class TerrainBlock
    extends GameObject {

    // position
    int x;
    int y;

    // game object
    ArrayList<GameObject> gameObjects = new ArrayList<>();

    // geometry
    static int resolution = 33;
    final static float SCALING_FACTOR = 0.5f;
    float [][] heightmap;
    // vertex array
    Vector3f[] vertex_array = null;
    // triangle array
    int[][] triangle_array = null;
    // VBOs
    FloatBuffer vertex_buffer = null;
    IntBuffer triangle_buffer = null;

    int vbo_vertex_handle;
    int vbo_triangle_handle;

    /**
     * Contructor: constructs a TerrainBlock from heightmap.
     * @param heightmap Height map for this terrain block - float[][]
     */
    TerrainBlock (float[][] heightmap) {
        this.heightmap = heightmap;
        diamond_square(0, resolution-1, 0, resolution-1, 0);
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
        // fill buffers
        for (Vector3f v : vertex_array)
            vertex_buffer.put(new float[] {v.x, v.y, v.z});
        for (int[] t : triangle_array)
            triangle_buffer.put(t);
        // bind VBOs - WRONG FIXME: wrong
        vbo_vertex_handle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle);
        glBufferData(GL_ARRAY_BUFFER, vertex_buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        vbo_triangle_handle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo_triangle_handle);
        glBufferData(GL_ARRAY_BUFFER, triangle_buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // TODO: colors
    }

    /**
     * Render object
     */
    @Override
    public void draw() {
        super.draw(); // useless, eh
        // draw all containing items
        for (GameObject gobj : gameObjects) {
            gobj.draw();
        }

        // create geometry if not yet done
        if (vertex_array == null || triangle_array == null)
            this.createGeometry();
        // build buffer objects if not yet done
        if (vertex_buffer == null || triangle_buffer == null)
            this.createBuffers();

        // reder geometry
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo_vertex_handle); // vertex buffer
        glBufferData(GL_ARRAY_BUFFER, vertex_buffer, GL_STATIC_DRAW);

        // Draw
        glDrawArrays(GL_TRIANGLES, 0, vertex_array.length*3);

        glDisableVertexAttribArray(0);

    }

    // geometry generation
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

    /**
     * Gets average of four points in the height map, ignoring those out of bounds.
     * @params index pairs for all four points
     * @return average of those points that are in the height map
     */
    float diamond_get_avg(int xUp, int yUp,
                          int xRight, int yRight,
                          int xDown, int yDown,
                          int xLeft, int yLeft) {
        float vUp = 0;
        float vRight = 0;
        float vDown = 0;
        float vLeft = 0;
        boolean bUp = true;
        boolean bRight = true;
        boolean bDown  = true;
        boolean bLeft = true;

        try { vUp = heightmap[xUp][yUp];
        } catch (IndexOutOfBoundsException e) { bUp = false; }

        try { vRight = heightmap[xRight][yRight];
        } catch (IndexOutOfBoundsException e) { bRight = false; }

        try { vDown = heightmap[xDown][yDown];
        } catch (IndexOutOfBoundsException e) { bDown = false; }

        try { vLeft = heightmap[xLeft][yLeft];
        } catch (IndexOutOfBoundsException e) { bLeft = false; }

        int num = 0;
        float sum = 0;
        if (bUp) {
            sum += vUp;
            num++;
        }
        if (bRight) {
            sum += vRight;
            num++;
        }
        if (bDown) {
            sum += vDown;
            num++;
        }
        if (bLeft) {
            sum += vLeft;
            num++;
        }
        return sum/(float)num;
    }

    void diamond_square(int x_min, int x_max,
                             int y_min, int y_max,
                             int curr_iter) {
        // square step
//        System.out.println("DiamondSquare: x_min " + x_min + " x_max " + x_max +
//                " y_min " + y_min + " y_max " + y_max +
//                " iteration " + curr_iter);
        int x_center = (x_max - x_min)/2 + x_min;
        int y_center = (y_max - y_min)/2 + y_min;
        float rand_mod = (float) (Math.random() * Math.pow(SCALING_FACTOR, curr_iter));
        if (heightmap[x_center][y_center] == 0)
            heightmap[x_center][y_center] =
                (heightmap[x_min][y_min] + heightmap[x_max][y_min] +
                        heightmap[x_max][y_max] + heightmap[x_min][y_max]) / 4 +
                        rand_mod;
        // diamond step
        int diff = (x_max - x_min)/2;
        // left
        if (heightmap[x_min][y_center] == 0)
            heightmap[x_min][y_center]= diamond_get_avg(
                x_min, y_center + diff,
                x_min + diff, y_center,
                x_min, y_center - diff,
                x_min - diff, y_center) + (float)(Math.random() * Math.pow(0.5, curr_iter));
        // up
        if (heightmap[x_center][y_min] == 0)
            heightmap[x_center][y_min]= diamond_get_avg(
                x_center, y_min + diff,
                x_center + diff, y_min,
                x_center, y_min - diff,
                x_center - diff, y_min) + (float)(Math.random() * Math.pow(0.5, curr_iter));
        // right
        if (heightmap[x_max][y_center] == 0)
            heightmap[x_max][y_center] = diamond_get_avg(
                x_max, y_center + diff,
                x_max + diff, y_center,
                x_max, y_center - diff,
                x_max - diff, y_center) + (float)(Math.random() * Math.pow(0.5, curr_iter));
        // down
        if (heightmap[x_center][y_max] == 0)
            heightmap[x_center][y_max] = diamond_get_avg(
                x_center, y_max + diff,
                x_center + diff, y_max,
                x_center, y_max - diff,
                x_center - diff, y_max) + (float)(Math.random() * Math.pow(0.5, curr_iter));

        // recure into sub-squares
        // left up
        if (Math.pow(2, curr_iter) < resolution) {
            diamond_square(x_min, x_center, y_min, y_center, curr_iter + 1);
            // right up
            diamond_square(x_center, x_max, y_min, y_center, curr_iter + 1);
            // right down
            diamond_square(x_center, x_max, y_center, y_max, curr_iter + 1);
            // left down
            diamond_square(x_min, x_center, y_center, y_max, curr_iter + 1);
        }
    }

}
