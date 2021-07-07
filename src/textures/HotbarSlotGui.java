package textures;

import org.lwjgl.util.vector.Vector2f;

import player.Player;
import util.ItemStack;

public class HotbarSlotGui extends GuiTexture {

	private Player player;
	private int selectedTexture;
	private int textureToUse = 0;
	private int stackTexture;
	private int index;
	private ItemStack stack;

	public HotbarSlotGui(Player playerIn, ItemStack stack, int stackTexture, int slotIndex, int texture, int selectedTexture,
			Vector2f position, Vector2f scale) {
		super(texture, position, scale);
		this.player = playerIn;
		this.stack = stack;
		this.selectedTexture = selectedTexture;
		this.stackTexture = stackTexture;
		this.index = slotIndex;
	}

	public Player getPlayer() {
		return this.player;
	}

	public int getSelectedTexture() {
		return this.selectedTexture;
	}

	public int getTextureToUse() {
		return this.textureToUse;
	}

	public void setSelectedTexture(int selectedTexture) {
		this.selectedTexture = selectedTexture;
	}

	public int getIndex() {
		return this.index;
	}

	public ItemStack getStack() {
		return this.stack;
	}
	
	public int getStackTexture() {
		return this.stackTexture;
	}
}
