package postProcessing.colour;

import org.lwjgl.util.vector.Vector4f;

import shaders.ShaderProgram;

public class ColourShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/postProcessing/colour/contrastVertex.glsl";
	private static final String FRAGMENT_FILE = "/postProcessing/colour/contrastFragment.glsl";

	private int location_permutation;
	private int location_contrast;

	public ColourShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_permutation = super.getUniformLocation("permutation");
		location_contrast = super.getUniformLocation("contrast");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadPermutation(Vector4f permutation) {
		super.loadVector4f(location_permutation, permutation);
	}

	public void loadContrast(float contrast) {
		super.loadFloat(location_contrast, contrast);
	}
}
