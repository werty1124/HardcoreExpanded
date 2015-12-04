package werty.hardcoreexpanded.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class HEItems 
{
	public static Item  heart_empty;
	public static Item  heart_full;
	
	public static void init()
	{
		heart_empty = new ItemHeartEmpty().setUnlocalizedName("heart_empty").setCreativeTab(CreativeTabs.tabMisc);
		heart_full = new ItemHeartFull().setUnlocalizedName("heart_full").setCreativeTab(CreativeTabs.tabMisc);
		
		register(heart_empty);
		register(heart_full);
		
		GameRegistry.addRecipe(new ItemStack(heart_empty, 1), new Object[] {"D D", "DBD", " D ", 'D', Items.diamond, 'B', Blocks.diamond_block});
	}
	
	public static void register(Item item)
	{
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(References.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
