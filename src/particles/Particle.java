package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import objects.Camera;
import renderEngine.DisplayManager;
import textures.ParticleTexture;

public class Particle {

	private static final float GRAVITY = -50;

	private Vector3f position, velocity;
	private float gravityEffect, lifeLength, rotation, scale, blendFactor, distance, elapsedTime = 0;

	private ParticleTexture texture;
	private Vector2f texOffset1 = new Vector2f(), texOffset2 = new Vector2f();
	private Vector3f reusableChange = new Vector3f();
	private boolean alive = false;

	public Particle() {
		
	}
	
	public void setActive(ParticleTexture textureIn, Vector3f position, Vector3f velocity, float gravityEffect,
			float lifeLength, float rotation, float scale) {
		alive = true;
		this.texture = textureIn;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public float getBlendFactor() {
		return blendFactor;
	}

	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getDistance() {
		return distance;
	}

	protected boolean update(Camera camera) {
		velocity.y += GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		reusableChange.set(velocity);
		reusableChange.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(reusableChange, position, position);
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		updateTextureCoords();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}

	private void updateTextureCoords() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumOfRows() * texture.getNumOfRows();
		float atlasProgress = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgress);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blendFactor = atlasProgress % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumOfRows();
		int row = index / texture.getNumOfRows();
		offset.x = (float) column / texture.getNumOfRows();
		offset.y = (float) row / texture.getNumOfRows();
	}
}
