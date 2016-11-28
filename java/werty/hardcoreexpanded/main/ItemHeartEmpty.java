package werty.hardcoreexpanded.main;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
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
		        		 playerIn.addChatMessage(new TextComponentString("You feel weak after transfering energy to the crystal"));
		        	 }
		        	 playerIn.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, Config.sicknessTicks, 1, false, false));//weakness
		         }
		         return new ItemStack(HEItems.heart_full);
			}
		}
		return itemStackIn;
        
    }
}
