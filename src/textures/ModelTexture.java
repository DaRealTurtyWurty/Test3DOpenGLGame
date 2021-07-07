package textures;

public class ModelTexture {

	private int textureID;
	private int normalID;
	private int specularID;

	private float shineDamper = 1;
	private float reflectivity = 0;

	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;

	private int numberOfRows = 1;

	public ModelTexture(int texture) {
		this.textureID = texture;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public int getID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public int getNormalID() {
		return normalID;
	}

	public void setNormalID(int normalID) {
		this.normalID = normalID;
	}
	
	public void setSpecularID(int specularID) {
		this.specularID = specularID;
		this.hasSpecularMap = true;
	}
	
	public boolean isHasSpecularMap() {
		return this.hasSpecularMap;
	}
	
	public int getSpecularID() {
		return this.specularID;
	}
}
