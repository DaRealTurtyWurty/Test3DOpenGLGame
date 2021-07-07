package util;

import java.util.Random;

public class Item {

	private String name;
	public static Random rand = new Random();

	public Item(String registryName) {
		this.name = registryName;
		Items.ITEMS.add(this);
	}

	public ItemStack getDefaultStack() {
		return new ItemStack(this);
	}
	
	public String getRegistryName() {
		return this.name;
	}
}
