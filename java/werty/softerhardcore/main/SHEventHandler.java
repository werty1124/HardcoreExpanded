package werty.softerhardcore.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SHEventHandler 
{
	
	private static List<Block> interactableBlocks = new ArrayList<Block>();
	private static List<Block> breakableBlocks = new ArrayList<Block>();
	
	private static List<Item> usableItems = new ArrayList<Item>();

	public static void loadAllowedBlocksandItems()
	{
		interactableBlocks.add(Blocks.acacia_door);
		interactableBlocks.add(Blocks.oak_door);
		interactableBlocks.add(Blocks.birch_door);
		interactableBlocks.add(Blocks.spruce_door);
		interactableBlocks.add(Blocks.dark_oak_door);
		interactableBlocks.add(Blocks.lever);
		interactableBlocks.add(Blocks.wooden_button);
		interactableBlocks.add(Blocks.stone_button);
		interactableBlocks.add(SHBlocks.ghostAltar);
		
		for(String s : Config.interactableBlocks.split(","))
		{
			String[] blockID = s.split(":");
			if(blockID != null && blockID.length >= 1)
			{
				interactableBlocks.add(Block.getBlockFromItem(GameRegistry.findItem(blockID[0].trim(), blockID[1].trim())));
			}
		}
		
		for(String s : Config.breakableBlocks.split(","))
		{
			String[] blockID = s.split(":");
			if(blockID != null && blockID.length >= 1)
			{
				breakableBlocks.add(Block.getBlockFromItem(GameRegistry.findItem(blockID[0].trim(), blockID[1].trim())));
			}
		}
		
		usableItems.add(SHItems.heart_full);
		usableItems.add(SHItems.heart_empty);
		
		for(String s : Config.usableItems.split(","))
		{
			String[] itemID = s.split(":");
			if(itemID != null && itemID.length >= 1)
			{
				usableItems.add(GameRegistry.findItem(itemID[0].trim(), itemID[1].trim()));
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event)
	{
		VersionChecker check = new VersionChecker(References.VERSION, "https://raw.githubusercontent.com/werty1124/SofterHardcore/master/version.txt", References.NAME);
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);

			if(!event.world.isRemote)
			{
				if(player.worldObj.getWorldInfo().isHardcoreModeEnabled())
				{
					event.entity.addChatMessage(new ChatComponentText("SofterHardcore is meant to be played in survival. It will NOT prevent the deletion of worlds!"));
				}
				if(Config.checkForUpdates && !SofterHardcore.hasCheckedVersion)
				{
					check.run();
					event.entity.addChatMessage(VersionChecker.uptoDate);
					SofterHardcore.hasCheckedVersion = true;
				}
			}	
			if(nbt.hasKey("ghost") && nbt.getBoolean("ghost") == true)
			{
				if(!event.world.isRemote)
				{
					event.entity.addChatMessage(new ChatComponentText("You have exhausted all of your lives and can no longer interact with the world"));
				}
			}
			else
			{
				player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Config.healthStarting);
				if(nbt.hasKey("health"))
				{
					player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(nbt.getDouble("health"));
				}
			}
			
			if(Config.ghostMode == false && player.getMaxHealth() <= 0)
			{
				addDebuffs(player);
				player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Config.healthStarting/2);
				nbt.setDouble("health", player.getMaxHealth());
			}
		}
	}
	
	public void addDebuffs(EntityPlayer player)
	{
		player.addPotionEffect(new PotionEffect(Potion.weakness.id, Config.sicknessTicks, 0, false, false));
		player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, Config.sicknessTicks, 0, false, false));
		player.addPotionEffect(new PotionEffect(Potion.blindness.id, Config.sicknessTicks/4, 0, false, false));
		player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, Config.sicknessTicks, 0, false, false));
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(player.getMaxHealth() - 2D);
			nbt.setDouble("health", player.getMaxHealth());
			if(player.getMaxHealth() <= 0)
			{
				if(Config.ghostMode)
				{
					nbt.setBoolean("ghost", true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onUpdate(LivingUpdateEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{	
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);

			if(nbt.getBoolean("ghost") == true)
			{
				if(player.getFoodStats().getFoodLevel() < 20)
				{
					player.getFoodStats().setFoodLevel(20);
				}
				if(Config.ghostInvisibility == true)
				{
					player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 50, 0, false, false));
				}
			}
			
			if(Config.healthEffects)
			{
				if(player.getHealth() == player.getMaxHealth())
				{
					player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 5, 0, false, false));
				}
				if(player.getHealth() < player.getMaxHealth()/3)
				{
					player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 50, 0, false, false));
				}
				if(player.getHealth() < player.getMaxHealth()/2)
				{
					player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 50, 0, false, false));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			Item item = event.item.getEntityItem().getItem();
			if(nbt.getBoolean("ghost") == true && usableItems.contains(item) == false)
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event)
	{
		if(event.entity instanceof EntityPlayer && event.source.getEntity() != null)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			if(!(event.source.getEntity() instanceof EntityPlayer))
			{
				event.ammount += Config.damageBoost;
			}
			
			if(nbt.getBoolean("ghost") == true)
			{
				event.setCanceled(true);
			}
			
		}
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			if(nbt.getBoolean("ghost") == true)
			{
				event.setCanceled(true);
			}
		}
			
	}
	
	@SubscribeEvent
	public void onPlayerAttack(LivingAttackEvent event)
	{
		if(event.source.getEntity() instanceof EntityPlayer && event.source.getEntity() != null)
		{
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			if(nbt.getBoolean("ghost") == true)
			{
				event.setCanceled(true);
			}	
		}
		
		if(event.entityLiving instanceof EntityPlayer && Config.mobEffects == true)
		{
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			Random rand = new Random();
			
			if(event.source.getEntity() instanceof EntityZombie && event.source.getEntity() != null)
			{
				if(rand.nextInt(100) < Config.zombiePoisonChance)
				{
					player.addPotionEffect(new PotionEffect(Potion.poison.id, 150, 0, false, false));
				}
			}
			
			if("fall".equals(event.source.damageType))
			{
				if(rand.nextInt(100) < Config.fallStunChance)
				{
					player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 150, 0, false, false));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		EntityPlayer player = (EntityPlayer) event.entity;
		NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
	
		if(nbt.getBoolean("ghost") == true)
		{
			Block block = player.worldObj.getBlockState(event.pos).getBlock();
			if(event.action == Action.RIGHT_CLICK_BLOCK && interactableBlocks.contains(block))
			{
				
			}
			else if(event.action == Action.LEFT_CLICK_BLOCK && breakableBlocks.contains(block))
			{
				
			}
			else
			{
				event.setCanceled(true);
			}	
			
			if(event.action != Action.LEFT_CLICK_BLOCK && player.getHeldItem() != null)
			{
				if(usableItems.contains(player.getHeldItem().getItem()))
				{
					event.setCanceled(false);
				}
			}
		}
	}
}
