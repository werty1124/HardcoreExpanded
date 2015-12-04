package werty.hardcoreexpanded.main;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockAltar extends Block 
{
	public BlockAltar(Material materialIn) 
	{
		super(materialIn);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(playerIn);
			if(nbt.getBoolean("ghost") == true)
			{
				worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, pos.getX(), pos.getY(), pos.getZ()));
				worldIn.destroyBlock(pos, false);
				nbt.setBoolean("ghost", false);
		    	playerIn.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Config.healthStarting);
				nbt.setDouble("health", playerIn.getMaxHealth());
				playerIn.addChatMessage(new ChatComponentText("You feel the altar pull you back from the dead!"));
			}
			else
			{
				playerIn.addChatMessage(new ChatComponentText("you can feel a strange presence"));
			}
		}
		return true;
	}
	
}
