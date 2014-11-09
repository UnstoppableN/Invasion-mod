package invmod.common.entity;

import invmod.common.IPathfindable;
import invmod.common.util.CoordsInt;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class PathCreator implements IPathSource {
	private int searchDepth;
	private int quickFailDepth;
	private int[] nanosUsed;
	private int index;

	public PathCreator() {
		this(200, 50);
	}

	public PathCreator(int searchDepth, int quickFailDepth) {
		this.searchDepth = searchDepth;
		this.quickFailDepth = quickFailDepth;
		this.nanosUsed = new int[6];
		this.index = 0;
	}

	public int getSearchDepth() {
		return this.searchDepth;
	}

	public int getQuickFailDepth() {
		return this.quickFailDepth;
	}

	public void setSearchDepth(int depth) {
		this.searchDepth = depth;
	}

	public void setQuickFailDepth(int depth) {
		this.quickFailDepth = depth;
	}

	public Path createPath(IPathfindable entity, int x, int y, int z, int x2, int y2, int z2, float targetRadius, float maxSearchRange, IBlockAccess terrainMap) {
		long time = System.nanoTime();
		Path path = PathfinderIM.createPath(entity, x, y, z, x2, y2, z2, targetRadius, maxSearchRange, terrainMap, this.searchDepth, this.quickFailDepth);
		int elapsed = (int) (System.nanoTime() - time);
		this.nanosUsed[this.index] = elapsed;
		if (++this.index >= this.nanosUsed.length) {
			this.index = 0;
		}
		return path;
	}

	public Path createPath(EntityIMLiving entity, Entity target, float targetRadius, float maxSearchRange, IBlockAccess terrainMap) {
		return createPath(entity, MathHelper.floor_double(target.posX + 0.5D - entity.width / 2.0F), MathHelper.floor_double(target.posY), MathHelper.floor_double(target.posZ + 0.5D - entity.width / 2.0F), targetRadius, maxSearchRange, terrainMap);
	}

	public Path createPath(EntityIMLiving entity, int x, int y, int z, float targetRadius, float maxSearchRange, IBlockAccess terrainMap) {
		CoordsInt size = entity.getCollideSize();
		int startZ;
		int startX;
		int startY;
		if ((size.getXCoord() <= 1) && (size.getZCoord() <= 1)) {
			startX = entity.getXCoord();
			startY = MathHelper.floor_double(entity.boundingBox.minY);
			startZ = entity.getZCoord();
		} else {
			startX = MathHelper.floor_double(entity.boundingBox.minX);
			startY = MathHelper.floor_double(entity.boundingBox.minY);
			startZ = MathHelper.floor_double(entity.boundingBox.minZ);
		}
		return createPath(entity, startX, startY, startZ, MathHelper.floor_double(x + 0.5F - entity.width / 2.0F), y, MathHelper.floor_double(z + 0.5F - entity.width / 2.0F), targetRadius, maxSearchRange, terrainMap);
	}

	public void createPath(IPathResult observer, IPathfindable entity, int x, int y, int z, int x2, int y2, int z2, float maxSearchRange, IBlockAccess terrainMap) {
	}

	public void createPath(IPathResult observer, EntityIMLiving entity, Entity target, float maxSearchRange, IBlockAccess terrainMap) {
	}

	public void createPath(IPathResult observer, EntityIMLiving entity, int x, int y, int z, float maxSearchRange, IBlockAccess terrainMap) {
	}

	public boolean canPathfindNice(IPathSource.PathPriority priority, float maxSearchRange, int searchDepth, int quickFailDepth) {
		return true;
	}
}