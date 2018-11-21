package si.fri.rgti;

public class TerrainBlock
    extends GameObject {

    int x;
    int y;

    static int resolution = 33;

    float [][] heightmap;

    /* TODO Katja
    nek 2d array  točk
    pa render metoda prazna
     */

    @Override
    public void draw() {
        super.draw();
        // TODO Maj
    }

    public float[] getHeightmapBorder(String direction) {
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
