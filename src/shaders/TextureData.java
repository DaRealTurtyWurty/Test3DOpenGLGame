package shaders;

import java.nio.ByteBuffer;

public class TextureData {

	private int width, height;
	private ByteBuffer buffer;

	public TextureData(ByteBuffer bufferIn, int widthIn, int heightIn) {
		this.buffer = bufferIn;
		this.width = widthIn;
		this.height = heightIn;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}
}
