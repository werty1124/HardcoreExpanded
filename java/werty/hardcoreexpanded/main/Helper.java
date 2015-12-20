package werty.hardcoreexpanded.main;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class Helper 
{
	public static List<Item> woodenTools = new ArrayList<Item>();
	
	
	public static void loadHelper()
	{
		woodenTools.add(Items.wooden_axe);
		woodenTools.add(Items.wooden_hoe);
		woodenTools.add(Items.wooden_pickaxe);
		woodenTools.add(Items.wooden_shovel);
		woodenTools.add(Items.wooden_sword);
	}
}
