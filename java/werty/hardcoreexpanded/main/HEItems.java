package werty.hardcoreexpanded.main;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HEItems 
{
	public static Item  heart_empty;
	public static Item  heart_full;
	
	public static void init()
	{
		heart_empty = new ItemHeartEmpty().setUnlocalizedName("heart_empty").setCreativeTab(CreativeTabs.tabMisc).setTextureName(References.MODID +":heart_empty");
		heart_full = new ItemHeartFull().setUnlocalizedName("heart_full").setCreativeTab(CreativeTabs.tabMisc).setTextureName(References.MODID +":heart_full");
		
		register(heart_empty);
		register(heart_full);
		
		GameRegistry.addRecipe(new ItemStack(heart_empty, 1), new Object[] {"D D", "DBD", " D ", 'D', Items.diamond, 'B', Blocks.diamond_block});
	}
	
	public static void register(Item item)
	{
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
	}
}
