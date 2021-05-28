package educanet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;

public class Main {
    static Input x = new Input();
    static float[][] coos = x.getCoos();
    static ArrayList<GameObject> gameObjects = new ArrayList<>();
    static boolean collides = false;

    public static void main(String[] args) throws Exception {

        GLFW.glfwInit();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        long window = GLFW.glfwCreateWindow(800, 800, "My first window", 0, 0);
        if (window == 0) {
            GLFW.glfwTerminate();
            throw new Exception("Can't open window");
        }
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL33.glViewport(0, 0, 800, 800);
        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            GL33.glViewport(0, 0, w, h);
        });
        Shaders.initShaders();
        for(float[] data:coos){
            gameObjects.add(new GameObject(data[0],data[1],data[2]));
        }
        GameObject player = new GameObject(0f,0f,0.2f);

        while (!GLFW.glfwWindowShouldClose(window)) {
            collides = false;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
                GLFW.glfwSetWindowShouldClose(window, true); // Send a shutdown signal...
            }

            GL33.glClearColor(0f, 0f, 0f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
            for (GameObject a: gameObjects){
                a.render();
                if(player.colliding2(a)){
                    collides = true;
                }
            }
            player.setColor(collides);
            player.update(window);
            player.render();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
        GLFW.glfwTerminate();
    }

}
