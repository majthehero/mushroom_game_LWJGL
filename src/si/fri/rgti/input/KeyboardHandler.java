package si.fri.rgti.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardHandler extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[65536];


    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        // TODO Auto-generated method stub
        keys[key] = action != GLFW_RELEASE;
    }

    // true ce je tipka pritisnjena
    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

}
