package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import models.RawModel;
import objects.Camera;
import renderEngine.DisplayManager;
import renderEngine.ModelLoader;

public class SkyboxRenderer {

	private static final float SIZE = 500f;

	private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,
			-SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE,
			-SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,
			-SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,
			SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE,
			-SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE,
			-SIZE, SIZE };

	private static String[] TEXTURE_FILES = { "right", "left", "top", "bottom", "back", "front" };
	private static String[] NIGHT_TEXTURE_FILES = { "nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack",
			"nightFront" };
	private RawModel cube;
	private int textureID;
	private int nightTextureID;
	private SkyboxShader shader;
	private float time = 8000;

	public SkyboxRenderer(ModelLoader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		textureID = loader.loadCubeMap(TEXTURE_FILES);
		nightTextureID = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Camera camera, float red, float green, float blue) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColour(red, green, blue);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void bindTextures() {
		time += (DisplayManager.getFrameTimeSeconds() * 1000) / 5;
		time %= 24000;
		int texture1;
		int texture2;
		float blendFactor;
		
		//blend buffers: (0-5000, 5000-8000, 8000-16000, 16000-19000, 19000-24000)
		if(time >= 0 && time < 5000) {
			texture1 = nightTextureID;
			texture2 = nightTextureID;
			blendFactor = (time - 0)/(5000 - 0);
		} else if(time >= 5000 && time < 8000) {
			texture1 = nightTextureID;
			texture2 = textureID;
			blendFactor = (time - 5000)/(8000 - 5000);
		} else if(time >= 8000 && time < 16000) {
			texture1 = textureID;
			texture2 = textureID;
			blendFactor = (time - 8000)/(16000 - 8000);
		} else if (time >= 16000 && time < 19000) {
			texture1 = textureID;
			texture2 = nightTextureID;
			blendFactor = (time - 16000)/(19000 - 16000);
		} else {
			texture1 = nightTextureID;
			texture2 = nightTextureID;
			blendFactor = (time - 19000)/(24000 - 19000);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		
		shader.loadBlendFactor(blendFactor);
	}
}
