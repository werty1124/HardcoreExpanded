package werty.hardcoreexpanded.main;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockAltar extends Block 
{
	public BlockAltar(Material materialIn) 
	{
		super(materialIn);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			NBTTagCompound nbt = NBTHelper.getPersistedPlayerTag(playerIn);
			if(nbt.getBoolean("ghost"))
			{
				worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, pos.getX(), pos.getY(), pos.getZ(), false));
				worldIn.destroyBlock(pos, false);
				nbt.setBoolean("ghost", false);
		    	playerIn.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Config.healthStarting);
				nbt.setDouble("health", playerIn.getMaxHealth());
				playerIn.addChatMessage(new TextComponentString("You feel the altar pull you back from the dead!"));
			}
			else
			{
				playerIn.addChatMessage(new TextComponentString("you can feel a strange presence"));
			}
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {return BlockRenderLayer.CUTOUT;}

	@Override
	public boolean isOpaqueCube(IBlockState iBlockState) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState iBlockState) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}
	
}
