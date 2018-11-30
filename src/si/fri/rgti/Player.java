package si.fri.rgti;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import si.fri.rgti.input.KeyboardHandler;
import si.fri.rgti.input.MouseHandler;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;


public class Player extends GameObject implements Updateable {
    public int HP;
    public int maxHP;
    public int pobraneGobe;
    public int pobranaZelisca;
    public int goal;

    long window_ref;
    // position in game space
    float x;
    float y;
    float z;
    // orientation in game space - in RADIANS - add change of mouse position, divided by something that makes sense
    float rot_x;
    float rot_y;
    float rot_z;

    // velocities
    float x_vel, y_vel, z_vel;


    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback mouseCallback;

    Player(long window_ref) {
        this.window_ref = window_ref;
    }

    @Override
    public void update() {
        // TODO
    }

    @Override
    public void draw(Matrix4f mvMat) {
        ;; // do nothing
    }

    // check for collision
    public boolean collisionCheck() {

        return false;
    }

    public Matrix4f modelMatrix(Matrix4f modelMatrix, float x, float y, float z, double kotX, double kotY, double kotZ) {
        // model matrix, this has to be done in a specific order: scale, translate and rotate.
        modelMatrix.scale(x, y, z);
        modelMatrix.translate(x, y, z);
        modelMatrix.rotate((float)Math.toRadians(kotX), 0f, 0f, 1f);
        modelMatrix.rotate((float)Math.toRadians(kotY),0f, 1f, 0f);
        modelMatrix.rotate((float)Math.toRadians(kotZ), 1f, 0f, 0f);
        return modelMatrix;
    }

    public void movementDirection(){
        glfwSetKeyCallback(window_ref, keyCallback = new KeyboardHandler());
        glfwSetCursorPosCallback(window_ref, mouseCallback = new MouseHandler());


        if(KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
            // naprej = 1.0;
            x_vel = 1;
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
            // levo
            y_vel = 1;
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
            // nazaj
            y_vel = -1;
        } else if(KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
            // desno
            x_vel = -1;
        }

    }

    private void init() {
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                System.out.println("OpenGL ERROR: " + error + " : " + description);
            }
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window_ref, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
            }
        });
    }


    // h in w zaslona
    public void spremembaMiske(long width, long height) {

        double prevX = 0;
        double prevY = 0;

        while (!glfwWindowShouldClose(window_ref)) {

            boolean mouseLocked = false;
            if (glfwGetMouseButton(window_ref, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                glfwSetCursorPos(window_ref, width/2, height/2);
                mouseLocked = true;
            }

            if (mouseLocked){
                DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

                glfwGetCursorPos(window_ref, x, y);
                x.rewind();
                y.rewind();

                double newX = x.get();
                double newY = y.get();

                double deltaX = newX - 400;
                double deltaY = newY - 300;

                boolean rotX = newX != prevX;
                boolean rotY = newY != prevY;

                prevX = newX;
                prevY = newY;


                glfwSetCursorPos(window_ref, width/2, height/2);
            }

        }

    }


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

    public Matrix4f perspective(double d) {
        Matrix4f matPersp = new Matrix4f(
          1f, 0f, 0f, 0f,
          0f, 1f, 0f, 0f,
          0f, 0f, 1f, 0f,
          0f, 0f, (float)(1/d), 0f
        );
        return matPersp;
    }




}
