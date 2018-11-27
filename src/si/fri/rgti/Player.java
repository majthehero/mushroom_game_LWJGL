package si.fri.rgti;

public class Player
    extends GameObject
    implements Updateable {

    @Override
    public void draw() {
        super.draw();
    }



    @Override
    public void update() {

    }


    public static Matrix4f transformacijaKoordinat(double[] koordKamere, double d) {
        Matrix4f matrikaPogleda = new Matrix4f();
        Matrix4f translacijaKamere = translate(-koordKamere[0], -koordKamere[1], -koordKamere[2]);
        Matrix4f perspektivaKamere = perspective(d);
        Matrix4f.mul(matrikaPogleda, perspektivaKamere, translacijaKamere);
        return matrikaPogleda;
    }

    public static Vector4f perspective(double d) {
        Vector4f p = new Vector4f(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1/d, 0, 0, 0, 0);
        return p;
    }

    public static Vector4f translate(double dx, double dy, double dz) {
        Vector4f p = new Vector4f(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, dx, dy, dz, 1);
    }

}
