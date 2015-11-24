package werty.softerhardcore.main;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SHEventHandler 
{
	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event)
	{
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
			}	
			if(nbt.hasKey("ghost") && nbt.getBoolean("ghost"))
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
		player.addPotionEffect(new PotionEffect(Potion.weakness.id, 12000, 0, false, false));
		player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 12000, 0, false, false));
		player.addPotionEffect(new PotionEffect(Potion.blindness.id, 3000, 0, false, false));
		player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 12000, 0, false, false));
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

			if(nbt.getBoolean("ghost") == true)
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
	}
	
	private static List<Block> allowedBlocks = new ArrayList<Block>();
	
	public static void loadAllowedBlocks()
	{
		allowedBlocks.add(Blocks.acacia_door);
		allowedBlocks.add(Blocks.oak_door);
		allowedBlocks.add(Blocks.birch_door);
		allowedBlocks.add(Blocks.spruce_door);
		allowedBlocks.add(Blocks.dark_oak_door);
		allowedBlocks.add(Blocks.lever);
		allowedBlocks.add(Blocks.wooden_button);
		allowedBlocks.add(Blocks.stone_button);
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		EntityPlayer player = (EntityPlayer) event.entity;
		NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
	
		if(nbt.getBoolean("ghost") == true)
		{
			Block block = player.worldObj.getBlockState(event.pos).getBlock();
			if(allowedBlocks.contains(block))
			{
				
			}
			else
			{
				event.setCanceled(true);
			}	
			if(event.action != Action.LEFT_CLICK_BLOCK && player.getHeldItem() != null)
			{
				if(player.getHeldItem().getItem() == SHItems.heart_full)
				{
					event.setCanceled(false);
				}
			}
		}
	}
}
