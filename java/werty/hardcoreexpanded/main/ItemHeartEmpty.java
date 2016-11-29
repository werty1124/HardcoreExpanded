package werty.hardcoreexpanded.main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemHeartEmpty extends Item 
{
	public ItemHeartEmpty()
	{
		this.setMaxStackSize(1);
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
		if(NBTHelper.getPersistedPlayerTag(playerIn).getBoolean("ghost") == true && Config.ghostFillHeart == false)
		{
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
		}
		else
		{
			if(playerIn.experienceLevel >= Config.healthXP)
			{
				playerIn.experienceLevel -= Config.healthXP;
		         --itemStackIn.stackSize;
		         if(Config.fillEffects)
		         {
		        	 if(!worldIn.isRemote)
		        	 {
		        		 playerIn.addChatMessage(new TextComponentString("You feel weak after transfering energy to the crystal"));
		        	 }
		        	 playerIn.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, Config.sicknessTicks, 1, false, false));//weakness
		         }
		         return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(HEItems.heart_full));
			}
		}
		return new ActionResult(EnumActionResult.FAIL, itemStackIn);
        
    }
}
