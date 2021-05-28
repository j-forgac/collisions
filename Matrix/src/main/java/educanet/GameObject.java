package educanet;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GameObject {
	private float speed = 0.00006f;

	private FloatBuffer cfb;

	private final int[] indices = {
			0, 1, 3, // First triangle
			1, 2, 3 // Second triangle
	};

	private final float[] colors = {
			1f, 0f, 1f, 1f,
			1f, 1f, 1f, 1f,
			1f, 1f, 1f, 1f,
			1f, 1f, 1f, 1f,
	};

	private final float[] green = {
			1f, 0f, 0f, 1f,
			1f, 0f, 0f, 1f,
			1f, 0f, 0f, 1f,
			1f, 0f, 0f, 1f,
	};
	private final float[] red = {
			0f, 1f, 0f, 1f,
			0f, 1f, 0f, 1f,
			0f, 1f, 0f, 1f,
			0f, 1f, 0f, 1f,
	};


	private float x;
	private float y;
	private float size;


	private final int squareVaoId;
	private final int uniformMatrixLocation;
	private final int squareColorId;

	Matrix4f matrix;
	FloatBuffer matrixFloatBuffer;

	public GameObject(float x, float y, float size) {
		matrix = new Matrix4f().identity();
		matrixFloatBuffer = BufferUtils.createFloatBuffer(16);
		this.x = x;
		this.y = y;
		this.size = size;

		float[] vertices = {
				x + size, y, 0.0f, //right top
				x + size, y - size, 0.0f, //right bottom
				x, y - size, 0.0f, //left bottom
				x, y, 0.0f,//left top
		};

		squareVaoId = GL33.glGenVertexArrays();
		int squareEboId = GL33.glGenBuffers();
		int squareVboId = GL33.glGenBuffers();
		squareColorId = GL33.glGenBuffers();

		uniformMatrixLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");
		GL33.glBindVertexArray(squareVaoId);
		GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
		IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
				.put(indices)
				.flip();
		GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);
		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
		cfb = BufferUtils.createFloatBuffer(colors.length).put(colors).flip();
		GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cfb, GL33.GL_STATIC_DRAW);
		GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
		GL33.glEnableVertexAttribArray(1);

		MemoryUtil.memFree(cfb);

		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);
		FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
				.put(vertices)
				.flip();

		GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
		GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
		GL33.glEnableVertexAttribArray(0);
		GL33.glUseProgram(Shaders.shaderProgramId);
		matrix.get(matrixFloatBuffer);
		GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

		MemoryUtil.memFree(fb);
		MemoryUtil.memFree(ib);
	}

	public void render() {
		matrix.get(matrixFloatBuffer);
		GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);
		GL33.glUseProgram(Shaders.shaderProgramId);
		GL33.glBindVertexArray(squareVaoId);
		GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
	}

	public void update(long window) {
		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
			matrix = matrix.translate(0f, speed, 0f);
			y += speed;
			moveMatrix();
		}

		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
			matrix = matrix.translate(0, -speed, 0f);
			y -= speed;
			moveMatrix();
		}

		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
			matrix = matrix.translate(speed, 0f, 0f);
			x += speed;
			moveMatrix();
		}

		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
			matrix = matrix.translate(-speed, 0f, 0f);
			x -= speed;
			moveMatrix();
		}

	}

	public void moveMatrix() {
		matrix.get(matrixFloatBuffer);
		GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);
	}

	public void setColor(boolean colliding) {
		GL33.glBindVertexArray(squareVaoId);

		GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
		cfb.put(colliding ? green : red).flip();

		GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
		GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cfb, GL33.GL_STATIC_DRAW);
		GL33.glEnableVertexAttribArray(1);
	}

	/*doesn't work
	public boolean isColliding(GameObject a, long window) {
		float y1 = y - size / 2;
		float x1 = x + size / 2;
		float w1 = size / 2;
		float h1 = size / 2;
		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
			System.out.println("player: " + x1 + " : " + y1);
		}

		float y2 = a.y + a.size / 2;
		float x2 = a.x + a.size / 2;
		float w2 = a.size / 2;
		float h2 = a.size / 2;
		return (y1 < y2 ? y1 + h1 > y2 - h2 : y1 - h1 < y2 + h2) && (x1 < x2 ? x1 + h1 > x2 - w2 : x1 - w1 < x2 + w2);
	}*/

	public boolean colliding2(GameObject a){
			float radius1 = size/2;
			float x1 = x + radius1;
			float y1 = y - radius1;

			float radius2 = a.size/2;
			float x2 = a.x + radius2;
			float y2 = a.y - radius2;

			float averageRadius = (a.size + size) / 2;
			if (this.notCollidingOnAxis(x2, x1, averageRadius)) {
				return (this.notCollidingOnAxis(y2, y1, averageRadius));
			} else {
				return false;
			}

	}

	public boolean notCollidingOnAxis(float elemAxis, float playerAxis, float averageRadius) {
		return ((Math.abs(playerAxis - elemAxis) - averageRadius) < 0);
	}

}
