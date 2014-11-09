package invmod.common.util;

import invmod.common.mod_Invasion;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionUtil {

	public static void doExplosionB(World world,Explosion explosion,boolean p_77279_1_)
	  {
		world.playSoundEffect(explosion.explosionX, explosion.explosionY, explosion.explosionZ, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

	      if (explosion.explosionSize >= 2.0F && explosion.isSmoking)
	      {
	    	  world.spawnParticle("hugeexplosion", explosion.explosionX, explosion.explosionY, explosion.explosionZ, 1.0D, 0.0D, 0.0D);
	      }
	      else
	      {
	    	  world.spawnParticle("largeexplode", explosion.explosionX, explosion.explosionY, explosion.explosionZ, 1.0D, 0.0D, 0.0D);
	      }

	      Iterator iterator;
	      ChunkPosition chunkposition;
	      int i;
	      int j;
	      int k;
	      Block block;

	      if (explosion.isSmoking)
	      {
	          iterator = explosion.affectedBlockPositions.iterator();

	          while (iterator.hasNext())
	          {
	              chunkposition = (ChunkPosition)iterator.next();
	              i = chunkposition.chunkPosX;
	              j = chunkposition.chunkPosY;
	              k = chunkposition.chunkPosZ;
	              block = world.getBlock(i, j, k);

	              if (p_77279_1_)
	              {
	                  double d0 = (double)((float)i + world.rand.nextFloat());
	                  double d1 = (double)((float)j + world.rand.nextFloat());
	                  double d2 = (double)((float)k + world.rand.nextFloat());
	                  double d3 = d0 - explosion.explosionX;
	                  double d4 = d1 - explosion.explosionY;
	                  double d5 = d2 - explosion.explosionZ;
	                  double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
	                  d3 /= d6;
	                  d4 /= d6;
	                  d5 /= d6;
	                  double d7 = 0.5D / (d6 / (double)explosion.explosionSize + 0.1D);
	                  d7 *= (double)(world.rand.nextFloat() * world.rand.nextFloat() + 0.3F);
	                  d3 *= d7;
	                  d4 *= d7;
	                  d5 *= d7;
	                  world.spawnParticle("explode", (d0 + explosion.explosionX * 1.0D) / 2.0D, (d1 + explosion.explosionY * 1.0D) / 2.0D, (d2 + explosion.explosionZ * 1.0D) / 2.0D, d3, d4, d5);
	                  world.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
	              }

	              if (block.getMaterial() != Material.air)
	              {
	                  if (block.canDropFromExplosion(explosion))
	                  {
	                      if(mod_Invasion.getDestructedBlocksDrop())
	                      {
	                	  block.dropBlockAsItemWithChance(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F / explosion.explosionSize, 0);
	                      }else{
	                	  block.dropBlockAsItemWithChance(world, i, j, k, world.getBlockMetadata(i, j, k), 0.0F, 0);
	                      }
	}

	                  block.onBlockExploded(world, i, j, k, explosion);
	              }
	          }
	      }

	      if (explosion.isFlaming)
	      {
	          iterator = explosion.affectedBlockPositions.iterator();

	          while (iterator.hasNext())
	          {
	              chunkposition = (ChunkPosition)iterator.next();
	              i = chunkposition.chunkPosX;
	              j = chunkposition.chunkPosY;
	              k = chunkposition.chunkPosZ;
	              block = world.getBlock(i, j, k);
	              Block block1 = world.getBlock(i, j - 1, k);

	              if (block.getMaterial() == Material.air && block1.func_149730_j() && world.rand.nextInt(3) == 0)
	              {
	            	  world.setBlock(i, j, k, Blocks.fire);
	              }
	          }
	      }
	  }
}
