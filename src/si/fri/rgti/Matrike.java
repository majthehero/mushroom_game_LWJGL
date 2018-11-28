package si.fri.rgti;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Matrike {

    /*
    // Setup projection matrix
    public Matrix4f projectionMat(Matrix4f mat, double left, double right, double bottom, double top, double near, double far, double width, double height) {
        projectionMatrix = new Matrix4f();
        float fieldOfView = 60f;
        float aspectRatio = (float)width / (float)height;
        float near_plane = 0.1f;
        float far_plane = 100f;

        float y_scale = this.coTangent(this.degreesToRadians(fieldOfView / 2f));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = far_plane - near_plane;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
        projectionMatrix.m33 = 0;


        //Matrix4f projectionMatrix = new Matrix4f();
        mat.perspective(left, right, bottom, top, near, far, projectionMatrix);
        return projectionMatrix;
    }

    */

    // translating (moving) our view matrix over a certain vector
    public Matrix4f translateMat(float x, float y, float z, Matrix4f matTrans) {
        matTrans.translate(x, y, z);
        return matTrans;
    }

    public Matrix4f scale(float x, float y, float z, Matrix4f matScale) {
        matScale.scale(x, y, z);
        return matScale;
    }

    public Matrix4f rotateX(double angle, Matrix4f matX) {
        matX.rotate((float)Math.toRadians(angle), 1f, 0f, 0f);
        return matX;
    }

    public Matrix4f rotateY(double angle, Matrix4f matY) {
        matY.rotate((float)Math.toRadians(angle), 0f, 1f, 0f);
        return matY;
    }

    public Matrix4f rotateZ(double angle, Matrix4f matZ) {
        matZ.rotate((float)Math.toRadians(angle), 0f, 0f, 1f);
        return matZ;
    }


    public Matrix4f modelMatrix(Matrix4f modelMatrix, float x, float y, float z, double kotX, double kotY, double kotZ) {
        // model matrix, this has to be done in a specific order: scale, translate and rotate.
        modelMatrix.scale(x, y, z);
        modelMatrix.translate(x, y, z);
        modelMatrix.rotate((float)Math.toRadians(kotX), 0f, 0f, 1f);
        modelMatrix.rotate((float)Math.toRadians(kotY),0f, 1f, 0f);
        modelMatrix.rotate((float)Math.toRadians(kotZ), 1f, 0f, 0f);
    }



}
