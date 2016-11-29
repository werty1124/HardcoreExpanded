package werty.hardcoreexpanded.main;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class HEWorldGenHandler implements IWorldGenerator {
    private void generateAltar(World world, BlockPos pos) {
        final BlockPos newPos = world.getTopSolidOrLiquidBlock(pos);

        world.setBlockState(newPos, HEBlocks.ghostAltar.getDefaultState());
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.isSurfaceWorld() && Config.genGhostAltar) {
            final BlockPos basePos = new BlockPos(chunkX * 16 + random.nextInt(16), 0, chunkZ * 16 + random.nextInt(16));
            if (random.nextInt(Config.altarGenChance * 100) == 0) {
                BlockPos finalPos = basePos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
                generateAltar(world, finalPos);
                FMLLog.getLogger().log(Level.INFO, "New shrine at: (" + finalPos.getX() + " " + finalPos.getY() + " " + finalPos.getZ() + ")");
            }
        }
    }
}

