package si.fri.rgti;

import org.joml.Matrix4f;

public class Matrike {


    // Setup projection matrix
    public Matrix4f projectionMat(Matrix4f mat, double left, double right, double bottom, double top, double near, double far) {
        Matrix4f projectionMatrix = mat.perspective(left, right, bottom, top, near, far);
        return projectionMatrix;
    }

    // translating (moving) our view matrix over a certain vector
    public Matrix4f translateMat(Vector3f vect, Matrix4f mat) {
        Matrix4f.translate(vect, mat, mat);
        return mat;
    }

    public Matrix4f scale(Vector3f vect, Matrix4f mat) {
        Matrix4f.translate(vect, mat, mat);
        return mat;
    }

    public Matrix4f rotateX(double angle, Matrix4f mat) {
        Matrix4f.rotate(Math.toRadians(angle), new Vector3f(1, 0, 0), mat, mat);
        return mat;
    }

    public Matrix4f rotateY(double angle, Matrix4f mat) {
        Matrix4f.rotate(Math.toRadians(angle), new Vector3f(0, 1, 0), mat, mat);
        return mat;
    }

    public Matrix4f rotateZ(double angle, Matrix4f mat) {
        Matrix4f.rotate(Math.toRadians(angle), new Vector3f(0, 0, 1), mat, mat);
        return mat;
    }


    public Matrix4f modelMatrix(Matrix4f modelMatrix, ) {
        // model matrix, this has to be done in a specific order: scale, translate and rotate.
        Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
        Matrix4f.translate(modelPos, modelMatrix, modelMatrix);
        Matrix4f.rotate(this.degreesToRadians(modelAngle.z), new Vector3f(0, 0, 1),
                modelMatrix, modelMatrix);
        Matrix4f.rotate(this.degreesToRadians(modelAngle.y), new Vector3f(0, 1, 0),
                modelMatrix, modelMatrix);
        Matrix4f.rotate(this.degreesToRadians(modelAngle.x), new Vector3f(1, 0, 0),
                modelMatrix, modelMatrix);
    }



}
