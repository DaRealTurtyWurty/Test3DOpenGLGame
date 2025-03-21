package textures;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;

	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;

		float offset = DisplayManager.getWidth() - DisplayManager.getHeight();
		float scaleX = (scale.x / DisplayManager.getWidth()) * offset;
		float scaleY = (scale.y / DisplayManager.getHeight()) * offset;
		this.scale = new Vector2f(scaleX, scaleY);
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
}
