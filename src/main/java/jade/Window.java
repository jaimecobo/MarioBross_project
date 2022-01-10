package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;

    public float r, g, b, a;
    private boolean fadeToBack = false;

    private static Window window = null;

//    private static int currentScene = -1;
    private static Scene currentScene;

    private Window(){
//        this.width = 1920;
        this.width = 1900;
//        this.height = 1080;
        this.height = 1000;
        this.title = "MarioBros";
        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    public static  void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
//                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get() {
        if (Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

        public void init() {
            //Setup an error callback
            GLFWErrorCallback.createPrint(System.err).set();

            //Initialize GLFW
            if (!glfwInit()) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }

            //Configure GLFW
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

            //Create the window
            glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
            if (glfwWindow == NULL){
                throw new IllegalStateException("Failed to create the GLFW window.");
            }

            glfwSetCursorPosCallback(glfwWindow, MouseListener :: mousePosCallback);
            glfwSetMouseButtonCallback(glfwWindow, MouseListener :: mouseButtonCallback);
            glfwSetScrollCallback(glfwWindow, MouseListener :: mouseScrollCallback);
            glfwSetKeyCallback(glfwWindow, KeyListener :: keyCallback);

            //Make the OpenGL context current
            glfwMakeContextCurrent(glfwWindow);

            //Enable v-sync
            glfwSwapInterval(1);

            //Make the window visible
            glfwShowWindow(glfwWindow);

            // This line is critical for LWJGL's interoperation with GLFW's
            // OpenGL context, or any context that is managed externally.
            // LWJGL detects the context that is current in the current thread,
            // creates the GLCapabilities instance and makes the OpenGL
            // bindings available for use.
            GL.createCapabilities();

            Window.changeScene(0);
        }

        public void loop() {

            float beginTime = Time.getTime();
            float endTime;
            float dt = -1.0f;

            while (!glfwWindowShouldClose(glfwWindow)) {
                //Poll events
                glfwPollEvents();

                glClearColor(r, g, b, a);
                glClear(GL_COLOR_BUFFER_BIT);

                if (dt >= 0) {
                    currentScene.update(dt);
                }

//                if (fadeToBack){
//                    r = Math.max(r - 0.01f, 0);
//                    g = Math.max(g - 0.01f, 0);
//                    b = Math.max(b - 0.01f, 0);
//
//                }
//
//                if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
////                    System.out.println("Space key is pressed");
//                    fadeToBack = true;
//                }


                glfwSwapBuffers(glfwWindow);

                endTime = Time.getTime();
                dt = endTime - beginTime;
                beginTime = endTime;

            }
        }

}