package werty.hardcoreexpanded.main;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenAltar extends WorldGenerator
{

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) 
	{	
		if(rand.nextInt(Config.altarGenChance) == 1)
		{
			 int randX = x + rand.nextInt(8) - rand.nextInt(8);
			 int randY = y + rand.nextInt(4) - rand.nextInt(4);
			 int randZ = z + rand.nextInt(8) - rand.nextInt(8);

		     if (world.isAirBlock(randX, randY, randZ) && world.getBlock(randX, randY - 1, randZ) == Blocks.grass)
		     {
		         world.setBlock(randX, randY, randZ, HEBlocks.ghostAltar);
		     }
		}
	    return true;
	}
}
