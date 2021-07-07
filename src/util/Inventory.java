package util;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

	protected int size;
	protected String inventoryName;
	protected List<Slot> slots;
	protected List<ItemStack> stacks = new ArrayList<ItemStack>();

	public Inventory(int size, String inventoryName) {
		this.size = size;
		this.inventoryName = inventoryName;
	}

	public String getInventoryName() {
		return this.inventoryName;
	}

	public int getSize() {
		return this.size;
	}

	public List<ItemStack> getStacks() {
		return this.stacks;
	}

	public List<Slot> getSlots() {
		return this.slots;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	public void setStackInSlot(ItemStack stack, int slotIndex) {
		this.slots.get(slotIndex).setStack(stack);
	}

	public void switchStacksInSlots(int slot1, int slot2) {
		ItemStack stack1 = this.slots.get(slot1).getStack();
		this.slots.get(slot1).setStack(this.slots.get(slot2).getStack());
		this.slots.get(slot2).setStack(stack1);
	}
}
