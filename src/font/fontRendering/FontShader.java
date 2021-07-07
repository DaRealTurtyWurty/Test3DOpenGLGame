package font.fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShaderProgram;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/font/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "/font/fontRendering/fontFragment.txt";

	private int location_colour;
	private int location_translation;
	private int location_outlineColour;
	private int location_glow;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
		location_outlineColour = super.getUniformLocation("outlineColour");
		location_glow = super.getUniformLocation("glow");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	protected void loadGlow(boolean glows) {
		super.loadBoolean(location_glow, glows);
	}

	protected void loadColour(Vector3f colour, Vector3f outlineColour) {
		super.loadVector(location_colour, colour);
		super.loadVector(location_outlineColour, outlineColour);
	}

	protected void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}
}
