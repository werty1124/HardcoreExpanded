package werty.hardcoreexpanded.main;

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
	public static int		altarGenChance;
	public static int       zombiePoisonChance;
	public static int		fallStunChance;
	
	public static boolean   ghostMode;
	public static boolean   checkForUpdates;
	public static boolean	healthEffects;
	public static boolean   fiilEffects;
	public static boolean   ghostInvisibility;
	public static boolean   ghostFillHeart;
	public static boolean   genGhostAltar;
	public static boolean   mobEffects;
	public static boolean   woodenToolDamage;
	
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
		ghostFillHeart = config.get("Mechanics", "GhostHeartFill", false, "Can ghost players fill hearts?").getBoolean();
		healthEffects = config.get("Mechanics", "HealthEffects", true, "Debuffs on thresholds at 1/2 and 1/3 health and a damage boost at full health").getBoolean();
		woodenToolDamage = config.get("Mechanics", "WoodenToolDamage", false, "Should wooden tools do damage").getBoolean();
		
		interactableBlocks = config.get("Ghost", "GhostInteractableBlocks", "modid:blockid", "Blocks ghost players can interact with using right click. Add blocks seperated with a , and using ModID:BlockID").getString();
		breakableBlocks = config.get("Ghost", "GhostBreakableBlocks", "modid:blockid", "Blocks ghost players can break with. Add blocks seperated with a , and using ModID:BlockID").getString();
		usableItems = config.get("Ghost", "GhostUsableItems", "modid:itemid", "Items ghost players can pick up and use. Add items seperated with a , and using ModID:itemID").getString();
		
		genGhostAltar = config.get("World", "GenGhostAltar", true, "Should ghost altars generate?").getBoolean();
		altarGenChance = config.get("World", "GhostAltarChance", 5, "Ghost altar generation chance").getInt();
		
		mobEffects = config.get("Mobs", "MobEffects", true, "Should mobs have a chance to cause debuffs? Set to fale to disable all effects under mobs").getBoolean();
		zombiePoisonChance = config.get("Mobs", "ZombiePoisonChance", 20, "Zombie poison chance. Set to 0 to disable").getInt();
		fallStunChance = config.get("Mobs", "FallStunChance", 15, "Chance to be stunned on fall damage. Set to 0 to disable").getInt();
		config.save();
	}
}
