package werty.softerhardcore.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config 
{

	public static double    healthStarting;
	public static double    healthMax;
	public static double    deathAmount;
	
	public static int    	damageBoost;
	public static int    	healthXP;
	public static int		sicknessTicks;
	
	public static boolean   ghostMode;
	public static boolean   checkForUpdates;
	public static boolean	healthEffects;
	public static boolean   fiilEffects;
	public static boolean   ghostInvisibility;
	
	public static String    interactableBlocks;
	public static String    breakableBlocks;
	public static String    usableItems;
	
	public static void configInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(new File("config/SofterHardcore.cfg"));
		config.load();
		healthStarting = config.get("Mechanics", "StartingHealth", 20D, "Starting health. Vanilla is 20").getDouble();
		healthMax = config.get("Mechanics", "MaxHealth", 20D, "Max health. Vanilla is 20").getDouble();
		sicknessTicks = config.get("Mechanics", "SicknessTicks", 12000, "How long the sickness will last 12000 = 10 minutes").getInt();
		deathAmount = config.get("Mechanics", "DeathAmount", 2D, "Amount of healh taken on death").getDouble();
		damageBoost = config.get("Mechanics", "DamageBoost", 2, "Damage boost added to monsters").getInt();
		healthXP = config.get("Mechanics", "ExperienceForHeart", 10, "Experience level needed to fill a heart").getInt();
		ghostMode = config.get("Mechanics", "GhostMode", true, "If player will spawn with debuffs").getBoolean();
		fiilEffects = config.get("Mechanics", "HeartFillEffects", true, "If player suffer temp. Debuff on filling up a heart").getBoolean();
		ghostInvisibility = config.get("Mechanics", "GhostInvisibility", true, "Gain Invisibility as a ghost").getBoolean();
		checkForUpdates = config.get("Mechanics", "CheckForUpdates", true, "Should mod check for updates?").getBoolean();
		healthEffects = config.get("Mechanics", "HealthEffects", true, "Debuffs on thresholds at 1/2 and 1/3 health and a damage boost at full health").getBoolean();
		
		interactableBlocks = config.get("Ghost", "GhostInteractableBlocks", "modid:blockid", "Blocks ghost players can interact with using right click. Add blocks seperated with a , and using ModID:BlockID").getString();
		breakableBlocks = config.get("Ghost", "GhostBreakableBlocks", "modid:blockid", "Blocks ghost players can break with. Add blocks seperated with a , and using ModID:BlockID").getString();
		usableItems = config.get("Ghost", "GhostUsableItems", "modid:itemid", "Items ghost players can pick up and use. Add items seperated with a , and using ModID:itemID").getString();
		config.save();
	}
}
