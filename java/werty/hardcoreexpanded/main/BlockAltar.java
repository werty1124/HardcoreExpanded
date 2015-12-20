package werty.hardcoreexpanded.main;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class BlockAltar extends Block 
{
	public BlockAltar(Material materialIn) 
	{
		super(materialIn);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(player);
			if(nbt.getBoolean("ghost") == true)
			{
				world.addWeatherEffect(new EntityLightningBolt(world, x, y, z));
				world.func_147480_a(x, y, z, false);
				nbt.setBoolean("ghost", false);
		    	player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Config.healthStarting);
				nbt.setDouble("health", player.getMaxHealth());
				player.addChatMessage(new ChatComponentText("You feel the altar pull you back from the dead!"));
			}
			else
			{
				player.addChatMessage(new ChatComponentText("you can feel a strange presence"));
			}
		}
		return true;
	}
	
}
