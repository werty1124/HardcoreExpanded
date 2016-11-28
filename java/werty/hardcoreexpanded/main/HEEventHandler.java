package werty.hardcoreexpanded.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class HEEventHandler 
{
	
	private static List<Block> interactableBlocks = new ArrayList<Block>();
	private static List<Block> breakableBlocks = new ArrayList<Block>();
	
	private static List<Item> usableItems = new ArrayList<Item>();

	public static void loadAllowedBlocksandItems()
	{
		interactableBlocks.add(Blocks.ACACIA_DOOR);
		interactableBlocks.add(Blocks.OAK_DOOR);
		interactableBlocks.add(Blocks.BIRCH_DOOR);
		interactableBlocks.add(Blocks.SPRUCE_DOOR);
		interactableBlocks.add(Blocks.DARK_OAK_DOOR);
		interactableBlocks.add(Blocks.LEVER);
		interactableBlocks.add(Blocks.WOODEN_BUTTON);
		interactableBlocks.add(Blocks.STONE_BUTTON);
		interactableBlocks.add(HEBlocks.ghostAltar);
		
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
		
		usableItems.add(HEItems.heart_full);
		usableItems.add(HEItems.heart_empty);
		
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
		if (event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);

			if(!event.getWorld().isRemote)
			{
				if(player.worldObj.getWorldInfo().isHardcoreModeEnabled())
				{
					event.getEntity().addChatMessage(new TextComponentString("SofterHardcore is meant to be played in survival. It will NOT prevent the deletion of worlds!"));
				}
				if(Config.checkForUpdates && !HardcoreExpanded.hasCheckedVersion)
				{
					check.run();
					event.getEntity().addChatMessage(VersionChecker.uptoDate);
					HardcoreExpanded.hasCheckedVersion = true;
				}
			}	
			if(nbt.hasKey("ghost") && nbt.getBoolean("ghost") == true)
			{
				if(!event.getWorld().isRemote)
				{
					event.getEntity().addChatMessage(new TextComponentString("You have exhausted all of your lives and can no longer interact with the world"));
				}
			}
			else
			{
				player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Config.healthStarting);
				if(nbt.hasKey("health"))
				{
					player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(nbt.getDouble("health"));
				}
			}
			
			if(!Config.ghostMode && player.getMaxHealth() <= 0)
			{
				addDebuffs(player);
				player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Config.healthStarting/2);
				nbt.setDouble("health", player.getMaxHealth());
			}
		}
	}
	
	public void addDebuffs(EntityPlayer player)
	{
		player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, Config.sicknessTicks, 0, false, false));
		player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, Config.sicknessTicks, 0, false, false));
		player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, Config.sicknessTicks/4, 0, false, false));
		player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, Config.sicknessTicks, 0, false, false));
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(player.getMaxHealth() - 2D);
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
		if(event.getEntity()instanceof EntityPlayer)
		{	
			EntityPlayer player = (EntityPlayer) event.getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);

			if(nbt.getBoolean("ghost") == true)
			{
				if(player.getFoodStats().getFoodLevel() < 20)
				{
					player.getFoodStats().setFoodLevel(20);
				}
				if(Config.ghostInvisibility == true)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 5, 0, false, false));
				}
			}
			
			if(Config.healthEffects)
			{
				if(player.getHealth() == player.getMaxHealth())
				{
					player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 5, 0, false, false));
				}
				if(player.getHealth() < player.getMaxHealth()/3)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 50, 0, false, false));
				}
				if(player.getHealth() < player.getMaxHealth()/2)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 50, 0, false, false));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			Item item = event.getItem().getEntityItem().getItem();
			if(nbt.getBoolean("ghost") == true && usableItems.contains(item) == false)
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer && event.getSource().getEntity() != null)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			if(!(event.getSource().getEntity() instanceof EntityPlayer))
			{
				event.setAmount(event.getAmount() + Config.damageBoost);
			}
			
			if(nbt.getBoolean("ghost") == true)
			{
				event.setCanceled(true);
			}
			
		}
		
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			if(nbt.getBoolean("ghost") == true)
			{
				event.setCanceled(true);
			}
		}
			
		if(event.getSource().getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			
			Item item = null;
			
			if(player.getHeldItem(EnumHand.MAIN_HAND) != null)
			{
				item = player.getHeldItem(EnumHand.MAIN_HAND).getItem();
			}
			
			if(item != null && (item == Items.WOODEN_AXE || item == Items.WOODEN_SWORD || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_PICKAXE || item == Items.WOODEN_HOE) && Config.woodenToolDamage == false)
			{
				event.setAmount(0);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerAttack(LivingAttackEvent event)
	{
		if(event.getSource().getEntity() instanceof EntityPlayer && event.getSource().getEntity() != null)
		{
			EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			
			if(nbt.getBoolean("ghost") == true)
			{
				event.setCanceled(true);
			}
		
		}
		
		if(event.getEntityLiving()instanceof EntityPlayer && Config.mobEffects == true)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			Random rand = new Random();
			
			if(event.getSource().getEntity() instanceof EntityZombie && event.getSource().getEntity() != null)
			{
				if(rand.nextInt(100) < Config.zombiePoisonChance)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.POISON, 150, 0, false, false));
				}
			}
			
			if("fall".equals(event.getSource().damageType))
			{
				if(rand.nextInt(100) < Config.fallStunChance)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 150, 0, false, false));
				}
			}
		}
		
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
	{
		EntityPlayer player = (EntityPlayer) event.getEntity();
		NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
	
		if(nbt.getBoolean("ghost") == true)
		{
			Block block = player.worldObj.getBlockState(event.getPos()).getBlock();
			if(!interactableBlocks.contains(block))
			{
				event.setCanceled(true);
			}
		}
	}
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event)
	{
		EntityPlayer player = (EntityPlayer) event.getEntity();
		NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);

		if(nbt.getBoolean("ghost") == true)
		{
			Block block = player.worldObj.getBlockState(event.getPos()).getBlock();
			if(!breakableBlocks.contains(block))
			{
				event.setCanceled(true);
			}
			if(player.getHeldItem(EnumHand.MAIN_HAND) != null)
			{
				if(usableItems.contains(player.getHeldItem(EnumHand.MAIN_HAND).getItem()))
				{
					event.setCanceled(false);
				}
			}
		}
	}
}
