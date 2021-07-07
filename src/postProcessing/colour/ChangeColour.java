package postProcessing.colour;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;
import postProcessing.PostProcessing;

public class ChangeColour {

	private ImageRenderer renderer;
	private ColourShader shader;

	public ChangeColour() {
		shader = new ColourShader();
		renderer = new ImageRenderer();
	}

	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}

	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		shader.loadPermutation(PostProcessing.SHADER_CONFIG.getColour());
		shader.loadContrast(PostProcessing.SHADER_CONFIG.getContrast());
		renderer.renderQuad();
		shader.stop();
	}
}
