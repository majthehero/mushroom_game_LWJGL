package si.fri.rgti;

import org.joml.Matrix4f;

public interface Drawable {
    public void draw(Matrix4f masterMVMat); // TODO might need params
}
