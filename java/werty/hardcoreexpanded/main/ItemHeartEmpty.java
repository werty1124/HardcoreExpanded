package werty.hardcoreexpanded.main;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemHeartEmpty extends Item 
{
	public ItemHeartEmpty()
	{
		this.setMaxStackSize(1);
	}
	
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		if(NBTHelper.getPersistedPlayerTag(playerIn).getBoolean("ghost") == true && Config.ghostFillHeart == false)
		{
			return itemStackIn;
		}
		else
		{
			if(playerIn.experienceLevel >= Config.healthXP)
			{
				playerIn.experienceLevel -= Config.healthXP;
		         --itemStackIn.stackSize;
		         if(Config.fiilEffects)
		         {
		        	 if(!worldIn.isRemote)
		        	 {
		        		 playerIn.addChatMessage(new ChatComponentText("You feel weak after transfering energy to the crystal"));
		        	 }
		        	 playerIn.addPotionEffect(new PotionEffect(Potion.weakness.id, Config.sicknessTicks, 0, false, false));
		         }
		         return new ItemStack(HEItems.heart_full);
			}
		}
		return itemStackIn;
        
    }
}
