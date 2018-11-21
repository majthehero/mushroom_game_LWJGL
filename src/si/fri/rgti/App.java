package si.fri.rgti;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;



public class App {

    private long window;



    // entry point
    public static void main(String[] args) {
        new App().run();
    }

    void run() {
        System.out.println("Starting...");

        gl_init();
        loop();

        // cleanup
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // terminate
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    void gl_init() {
        // error callback - will print to std.err
        GLFWErrorCallback.createPrint(System.err).set();

        // init glfw
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw.");
        }

        // config glfw
        glfwDefaultWindowHints(); // unnecessary
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // keep invisible for a time
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // allow resizing

        window = glfwCreateWindow(1024, 640, "The Thing", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Unable to create a window.");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // check in loop
            }
        });

        // centering a window doesn't work correctly (sometimes) if you have more than one screen.
//        // get thread stack and push new frame (WHY LIKE THIS???)
//        try (MemoryStack stack = stackPush()) {
//            IntBuffer pWidth = stack.mallocInt(1); // java has pointers now. wtf
//            IntBuffer pHeight = stack.mallocInt(1);
//
//            glfwGetWindowSize(window, pWidth, pHeight); // get window size
//            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); // resolution of prim montor
//            glfwSetWindowPos(window,
//                    vidmode.width() - pWidth.get(0)/2,
//                    vidmode.height() - pHeight.get()/2); // center window on screen
//        } // stack frame popped now auto

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // this enables vsync
        glfwShowWindow(window); // now show it, after it's set up
    }

    void loop() {
        GL.createCapabilities(); // enable gl
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // as promised, checking for exit - don't - prefer to use si.fri.rgti.menu.
        while(!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // TODO: here goes something

            glfwSwapBuffers(window);
            glfwPollEvents(); // anonymous callback was set, now needs to be called. Weird, usually callbacks are called
        }
    }

}