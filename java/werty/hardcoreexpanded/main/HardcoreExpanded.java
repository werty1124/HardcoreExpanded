package werty.hardcoreexpanded.main;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

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
		Config.configInit();
		HEItems.init();
		HEBlocks.init();
		Helper.loadHelper();
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