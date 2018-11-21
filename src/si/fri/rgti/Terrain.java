package si.fri.rgti;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class Terrain
    extends GameObject {

    /* TODO
    - members: generated terrain blocks
    - methods: generate new block, delete faraway blocks
     */

    int seed = 123;
    Random random;

    ArrayList<TerrainBlock> terrainBlocks;

    Terrain() {

        terrainBlocks = new ArrayList<TerrainBlock>();
        for (int i=-1; i<=1; i++) {
            for (int j=-1; j<=1; j++) {
                terrainBlocks.add(generateBlock(i,j));
            }
        }

        random = new Random(seed);
    }

    TerrainBlock generateBlock(int block_x, int block_y) {

        class _border {
            float[] h_vals;
            String dir;
            _border(float[] h_vals, String dir) {
                this.h_vals = h_vals;
                this.dir = dir;
            }
        }

        // get neighbours that exist
        List<_border> borders = new ArrayList<>();
        for (TerrainBlock tb : terrainBlocks) {
            if (Math.abs(tb.x - block_x) == 1 || Math.abs(tb.y - block_y) == 1) {

                String direction = "";
                int dx = tb.x - block_x;
                int dy = tb.y - block_y;

                if (dx == 1) direction += "F";
                else if (dx == -1) direction += "B";

                if (dy == 1) direction += "R";
                else if (dy == -1) direction += "L";

                _border b = new _border(tb.getHeightmapBorder(direction), direction);

            }
        }

        // run diamond-square algortihm
        // 1. init heightmap and borders
        float [][] heightmap = new float[TerrainBlock.resolution][TerrainBlock.resolution];
        for (_border b : borders) {
            switch (b.dir) {
                case "F":
                    heightmap[0] = b.h_vals;
                    break;
                case "B":
                    heightmap[TerrainBlock.resolution-1] = b.h_vals;
                    break;
                case "L":
                    for (int i=0; i<TerrainBlock.resolution; i++) {
                        heightmap[i][0] = b.h_vals[i];
                    }
                    break;
                case "R":
                    for (int i=0; i<TerrainBlock.resolution; i++) {
                        heightmap[i][TerrainBlock.resolution] = b.h_vals[i];
                    }
                    break;
            }
        }
        // 2. DIAMOND step: get corners, set center to avg + rand


        // 3. SQUARE step: same




        return null;
    }

}











