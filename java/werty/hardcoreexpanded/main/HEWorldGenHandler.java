package werty.hardcoreexpanded.main;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class HEWorldGenHandler implements IWorldGenerator 
{
	public static void load()
	{
		GameRegistry.registerWorldGenerator(new HEWorldGenHandler(), 0);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.provider.isSurfaceWorld())
		{
			this.generateOverworld(world, random, chunkX, chunkZ);
		}
	}
	
	public void generateOverworld(World world, Random rand, int x, int z)
	{
		int x1 = x;
		int y1 = 0;
		int z1 = z;
		BlockPos pos = new BlockPos(x1 * 16, 0, z1 * 16);
		Chunk chunk = world.getChunkFromBlockCoords(pos);

		if(Config.genGhostAltar == true)
		{
			this.generateAltar(world, rand, x1, z1);
		}
	}
	
	private void generateAltar(World world, Random rand, int chunkX, int chunkZ)
	{
		WorldGenAltar gen = new WorldGenAltar();
		for (int i = 0; i < 1; i++)
		{
			int xRand = chunkX * 16 + rand.nextInt(16);
			int yRand = rand.nextInt(100);
			int zRand = chunkZ * 16 + rand.nextInt(16);
			BlockPos position = new BlockPos(xRand, yRand, zRand);
			gen.generate(world, rand, position);
		}
	}
	
}
