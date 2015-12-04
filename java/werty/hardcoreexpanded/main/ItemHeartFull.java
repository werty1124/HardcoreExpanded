package werty.hardcoreexpanded.main;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemHeartFull extends Item 
{
	public ItemHeartFull()
	{
		this.maxStackSize = 1;
	}
	
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
	    --itemStackIn.stackSize;
	    if(NBTHelper.getPersistedPlayerTag(playerIn).hasKey("ghost") && NBTHelper.getPersistedPlayerTag(playerIn).getBoolean("ghost"))
	    {
	    	NBTHelper.getPersistedPlayerTag(playerIn).setBoolean("ghost", false);
	    	playerIn.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Config.healthStarting);
			NBTHelper.getPersistedPlayerTag(playerIn).setDouble("health", playerIn.getMaxHealth());
			playerIn.addChatMessage(new ChatComponentText("You have been brought back from the dead!"));
	    }
	    else
	    {
	    	if(playerIn.getMaxHealth() < Config.healthMax)
	    	{
	    		playerIn.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(playerIn.getMaxHealth() + 2D);
				NBTHelper.getPersistedPlayerTag(playerIn).setDouble("health", playerIn.getMaxHealth());
	    	}
	    }
        return itemStackIn;
    }
}
