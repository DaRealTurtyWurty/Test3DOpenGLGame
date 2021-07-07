package util;

public class Slot {
	
	private int index;
	private ItemStack stack;

	public Slot(int index, ItemStack stack) {
		this.index = index;
		if(stack == null) {
			stack = ItemStack.EMPTY;
		} else {
			this.stack = stack;
		}
	}
	
	public int getSlotIndex() {
		return this.index;
	}
	
	public ItemStack getStack() {
		return this.stack;
	}
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
	}
}
