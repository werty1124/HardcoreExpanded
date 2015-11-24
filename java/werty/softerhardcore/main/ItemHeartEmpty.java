package werty.softerhardcore.main;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHeartEmpty extends Item 
{
	public ItemHeartEmpty()
	{
		this.setMaxStackSize(1);
	}
	
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		if(NBTHelper.getPersistedPlayerTag(playerIn).getBoolean("ghost") == false)
		{
			return itemStackIn;
		}
		else
		{
			if(playerIn.experienceLevel > Config.healthXP)
			{
				playerIn.experienceLevel -= Config.healthXP;
		         --itemStackIn.stackSize;
		         return new ItemStack(SHItems.heart_full);
			}
		}
		return itemStackIn;
        
    }
}
