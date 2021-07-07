package objects;

import org.lwjgl.util.vector.Vector3f;

import core.MainGameLoop;
import models.TexturedModel;
import renderEngine.ModelLoader;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import util.ItemStack;

public class ItemEntity extends Entity {

	private ItemStack itemStack;

	public ItemEntity(ItemStack stack, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(new TexturedModel(OBJLoader.loadObjModel("person", new ModelLoader()),
				new ModelTexture(new ModelLoader().loadTexture("playerTexture", 0.0f))), position, rotX, rotY, rotZ,
				scale);
		MainGameLoop.items.add(this);
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public void bob() {
		// increasePosition(0.0f, (float) Math.sin(Math.PI / 2.0f) * 2 - 1, 0.0f);
		// increasePosition((float) Math.sin(Math.PI / 2.0f) * 2 - 1, 0.0f, 0.0f);
	}
}
