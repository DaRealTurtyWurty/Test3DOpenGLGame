package textures;

public class ParticleTexture {

	private int textureID, numOfRows;
	private boolean additive;

	public ParticleTexture(int textureID, int numOfRows) {
		this.textureID = textureID;
		this.numOfRows = numOfRows;
	}

	public boolean isAdditive() {
		return additive;
	}

	public void setAdditive(boolean additive) {
		this.additive = additive;
	}

	public int getTextureID() {
		return textureID;
	}

	public int getNumOfRows() {
		return numOfRows;
	}
}
