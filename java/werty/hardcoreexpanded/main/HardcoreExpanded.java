package werty.hardcoreexpanded.main;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION)
public class HardcoreExpanded
{
	@Instance(References.MODID)
	public static HardcoreExpanded instance;
	
	@SidedProxy(clientSide = References.CLIENT_PROXY_CLASS, serverSide = References.SERVER_PROXY_CLASS)
	public static CommonProxy		proxy;
	
	public static boolean hasCheckedVersion = false;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{	
		Config.configInit(event);
		HEItems.init();
		HEBlocks.init();
		MinecraftForge.EVENT_BUS.register(new HEEventHandler());
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerRenders();
		proxy.init(event);	
		HEEventHandler.loadAllowedBlocksandItems();
		HEWorldGenHandler.load();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}