package werty.hardcoreexpanded.main;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class HEBlocks 
{
	public static Block ghostAltar;
	
	public static void init()
	{
		ghostAltar = new BlockAltar(Material.rock).setBlockUnbreakable().setBlockName("ghost_altar").setCreativeTab(CreativeTabs.tabMisc).setBlockTextureName(References.MODID + ":altar");
	
		register(ghostAltar);
	}
	
	private static void register(Block block)
	{
		GameRegistry.registerBlock(block, block.getUnlocalizedName().substring(5));
	}
}
