package si.fri.rgti;

import kotlin.reflect.jvm.internal.impl.types.checker.TypeCheckerContext;
import org.joml.Vector3f;

public class TerrainBlock
    extends GameObject {

    int x;
    int y;

    static int resolution = 33;

    float [][] heightmap;

    // vertex array
    Vector3f[] vertex_array;
    // triangle array
    int[][] triangle_array = new int[(resolution-1)*2*(resolution-1)][3]; // num of tri wrt res

    TerrainBlock () {
    }

    TerrainBlock (float[][] heightmap) {
        this.heightmap = heightmap;
    }

    @Override
    public void draw() {
        super.draw();

        // create vertex array if not created
        if (vertex_array == null) {
            // create array
        }
        // create triangle array if not created

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
