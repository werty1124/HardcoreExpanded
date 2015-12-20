package werty.hardcoreexpanded.main;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;

public class HEWorldGenHandler implements IWorldGenerator 
{
	public static void load()
	{
		GameRegistry.registerWorldGenerator(new HEWorldGenHandler(), 0);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		if (chunkGenerator instanceof ChunkProviderGenerate)
		{
			this.generateOverworld(world, random, chunkX, chunkZ);
		}
	}
	
	public void generateOverworld(World world, Random rand, int x, int z)
	{
		int x1 = x;
		int y1 = 0;
		int z1 = z;
		
		Chunk chunk = world.getChunkFromBlockCoords(x1, z1);
		WorldChunkManager chunkManager = world.getWorldChunkManager();
		BiomeGenBase biome = world.getBiomeGenForCoords(x1, z1);
		
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
			gen.generate(world, rand, xRand, yRand, zRand);
		}
	}
	
}
