package werty.hardcoreexpanded.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
	}
	
	@Override
	public void registerRenders()
	{
		HEItems.registerRender(HEItems.heart_empty);
		HEItems.registerRender(HEItems.heart_full);
		
		HEBlocks.registerRender(HEBlocks.ghostAltar);
	}

	
	@Override
	public void openGUI(Object gui)
	{
		Minecraft.getMinecraft().displayGuiScreen((GuiScreen) gui);
	}
}
