package werty.softerhardcore.main;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SHBlocks 
{
	public static Block ghostAltar;
	
	public static void init()
	{
		ghostAltar = new BlockAltar(Material.rock).setBlockUnbreakable().setUnlocalizedName("ghost_altar").setCreativeTab(CreativeTabs.tabMisc);
		
		register(ghostAltar);
	}
	
	private static void register(Block block)
	{
		GameRegistry.registerBlock(block, block.getUnlocalizedName().substring(5));
	}
	
	public static void registerRender(Block block)
	{
		Item item = Item.getItemFromBlock(block);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(References.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
