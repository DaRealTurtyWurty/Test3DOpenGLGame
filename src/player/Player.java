package player;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import objects.Entity;
import objects.ItemEntity;
import renderEngine.DisplayManager;
import terrains.Terrain;
import util.ItemStack;

public class Player extends Entity {

	private static final float RUN_SPEED = 40;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 18;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	private PlayerHotbarInventory hotbarInventory;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.hotbarInventory = new PlayerHotbarInventory(this);
	}

	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
			this.getHotbar().setSelectedIndex(0);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			this.getHotbar().setSelectedIndex(1);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
			this.getHotbar().setSelectedIndex(2);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
			this.getHotbar().setSelectedIndex(3);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
			this.getHotbar().setSelectedIndex(4);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
			this.getHotbar().setSelectedIndex(5);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
			this.getHotbar().setSelectedIndex(6);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_7)) {
			this.getHotbar().setSelectedIndex(7);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_8)) {
			this.getHotbar().setSelectedIndex(8);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			this.dropHeldItem();
		}
	}

	public void dropHeldItem() {
		ItemStack heldItem = this.getHotbar().getHeldStack();
		heldItem.shrinkStack(1);
		this.getHotbar().setStackInSlot(heldItem, this.getHotbar().getSelectedIndex());
		new ItemEntity(new ItemStack(heldItem.getItem(), 1), this.getPosition(), this.getRotX(), this.getRotY(),
				this.getRotZ(), this.getScale());
	}

	public PlayerHotbarInventory getHotbar() {
		return this.hotbarInventory;
	}
}
