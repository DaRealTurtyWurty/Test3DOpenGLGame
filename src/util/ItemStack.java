package util;

public class ItemStack {

	public static final ItemStack EMPTY = new ItemStack(Items.AIR);
	private Item item;
	private int count;

	public ItemStack(Item item) {
		this.item = item;
		this.count = 1;
	}

	public ItemStack(Item item, int count) {
		this.item = item;
		this.count = count;
	}

	public Item getItem() {
		return this.item;
	}

	public int getCount() {
		return this.count;
	}

	public void shrinkStack(int count) {
		this.count -= count;
	}

	public void growStack(int count) {
		this.count += count;
	}

	public boolean isEmpty() {
		return this.item.equals(Items.AIR) || this.count == 0 || this.item.getRegistryName().equalsIgnoreCase("air");
	}
}
