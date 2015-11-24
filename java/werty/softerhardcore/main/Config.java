package werty.softerhardcore.main;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config 
{

	public static double    healthStarting;
	public static double    healthMax;
	public static double    deathAmount;
	
	public static int    	damageBoost;
	public static int    	healthXP;
	
	public static boolean   ghostMode;
	
	public static void configInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(new File("config/SofterHardcore.cfg"));
		config.load();
		healthStarting = config.get("Mechanics", "StartingHealth", 20D, "Starting health. Vanilla is 20").getDouble();
		healthMax = config.get("Mechanics", "MaxHealth", 20D, "Max health. Vanilla is 20").getDouble();
		deathAmount = config.get("Mechanics", "DeathAmount", 2D, "Amount of healh taken on death").getDouble();
		damageBoost = config.get("Mechanics", "DamageBoost", 2, "Damage boost added to monsters").getInt();
		healthXP = config.get("Mechanics", "ExperienceForHeart", 10, "Experience level needed to fill a heart").getInt();
		ghostMode = config.get("Mechanics", "GhostMode", true, "If player will spawn with debuffs lasting 10 minutes").getBoolean();
		config.save();
	}
}
