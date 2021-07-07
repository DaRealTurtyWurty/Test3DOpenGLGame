package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bloom.BrightFilter;
import bloom.CombineFilter;
import config.ShaderConfig;
import models.RawModel;
import postProcessing.blur.HorizontalBlur;
import postProcessing.blur.VerticalBlur;
import postProcessing.colour.ChangeColour;
import renderEngine.ModelLoader;

public class PostProcessing {

	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static RawModel quad;

	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBlur2;
	private static VerticalBlur vBlur2;
	private static ChangeColour changeColour;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;

	public static final ShaderConfig SHADER_CONFIG = new ShaderConfig();

	public static void init(ModelLoader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		changeColour = new ChangeColour();
		hBlur = new HorizontalBlur(Display.getWidth() / PostProcessing.SHADER_CONFIG.getBlurFactor(),
				Display.getHeight() / PostProcessing.SHADER_CONFIG.getBlurFactor());
		vBlur = new VerticalBlur(Display.getWidth() / PostProcessing.SHADER_CONFIG.getBlurFactor(),
				Display.getHeight() / PostProcessing.SHADER_CONFIG.getBlurFactor());
		if (PostProcessing.SHADER_CONFIG.getBlurFactor() >= 2) {
			hBlur2 = new HorizontalBlur(Display.getWidth() / (PostProcessing.SHADER_CONFIG.getBlurFactor() / 2),
					Display.getHeight() / (PostProcessing.SHADER_CONFIG.getBlurFactor() / 2));
			vBlur2 = new VerticalBlur(Display.getWidth() / (PostProcessing.SHADER_CONFIG.getBlurFactor() / 2),
					Display.getHeight() / (PostProcessing.SHADER_CONFIG.getBlurFactor() / 2));
		} else {
			hBlur2 = new HorizontalBlur(Display.getWidth(), Display.getHeight());
			vBlur2 = new VerticalBlur(Display.getWidth(), Display.getHeight());
		}

		brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
		combineFilter = new CombineFilter();
	}

	public static void doPostProcessing(int colourTexture, int brightTexture) {
		start();
		if (PostProcessing.SHADER_CONFIG.getBlurFactor() >= 2) {
			if (PostProcessing.SHADER_CONFIG.shouldUseBloom()) {
				//brightFilter.render(colourTexture);
				hBlur.render(brightTexture);
				vBlur.render(hBlur.getOutputTexture());
				hBlur2.render(vBlur.getOutputTexture());
				vBlur2.render(hBlur2.getOutputTexture());
				combineFilter.render(colourTexture, vBlur2.getOutputTexture());
			} else {
				hBlur.render(colourTexture);
				vBlur.render(hBlur.getOutputTexture());
				hBlur2.render(vBlur.getOutputTexture());
				vBlur2.render(hBlur2.getOutputTexture());
				changeColour.render(vBlur2.getOutputTexture());
			}
		} else {
			changeColour.render(colourTexture);
		}
		end();
	}

	public static void cleanUp() {
		hBlur.cleanUp();
		vBlur.cleanUp();
		hBlur2.cleanUp();
		vBlur2.cleanUp();
		changeColour.cleanUp();
		brightFilter.cleanUp();
	}

	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
