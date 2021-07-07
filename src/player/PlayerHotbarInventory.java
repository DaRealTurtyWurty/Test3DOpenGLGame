package player;

import java.util.ArrayList;
import java.util.List;

import util.Inventory;
import util.ItemStack;
import util.Slot;

public class PlayerHotbarInventory extends Inventory {

	private int selectedIndex;
	private Player player;

	public PlayerHotbarInventory(Player playerIn) {
		super(9, "Player Hotbar");
		this.player = playerIn;
		this.slots = createSlots();
		this.selectedIndex = 0;
	}

	private List<Slot> createSlots() {
		List<Slot> tempSlots = new ArrayList<Slot>();
		for (int index = 0; index < this.getSize(); index++) {
			tempSlots.add(index, new Slot(index, ItemStack.EMPTY));
		}
		return tempSlots;
	}

	public ItemStack getHeldStack() {
		return this.getSlots().get(this.selectedIndex).getStack();
	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	protected void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
}
