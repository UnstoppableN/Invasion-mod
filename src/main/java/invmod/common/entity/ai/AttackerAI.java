package invmod.common.entity.ai;

//NOOB HAUS: Done

import invmod.common.IBlockAccessExtended;
import invmod.common.IPathfindable;
import invmod.common.TerrainDataLayer;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.IPathSource;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathCreator;
import invmod.common.entity.PathNode;
import invmod.common.entity.Scaffold;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.Distance;
import invmod.common.util.IPosition;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;

public class AttackerAI 
{
	private INexusAccess nexus;
	private IPathSource pathSource;
	private IntHashMap entityDensityData;
	private List<Scaffold> scaffolds;
	private int scaffoldLimit;
	private int minDistanceBetweenScaffolds;
	private int nextScaffoldCalcTimer;
	private int updateScaffoldTimer;
	private int nextEntityDensityUpdate;

	public AttackerAI(INexusAccess nexus) 
	{
		this.nexus = nexus;
		this.pathSource = new PathCreator();
		this.pathSource.setSearchDepth(8500);
		this.pathSource.setQuickFailDepth(8500);
		this.entityDensityData = new IntHashMap();
		this.scaffolds = new ArrayList();
	}

	public void update() 
	{
		this.nextScaffoldCalcTimer -= 1;
		if (--this.updateScaffoldTimer <= 0) 
		{
			this.updateScaffoldTimer = 40;
			updateScaffolds();

			this.scaffoldLimit = (2 + this.nexus.getCurrentWave() / 2);
			this.minDistanceBetweenScaffolds = (90 / (this.nexus.getCurrentWave() + 10));
		}

		if (--this.nextEntityDensityUpdate <= 0) 
		{
			this.nextEntityDensityUpdate = 20;
			updateDensityData();
		}
	}

	public IBlockAccessExtended wrapEntityData(IBlockAccess terrainMap) 
	{
		TerrainDataLayer newTerrain = new TerrainDataLayer(terrainMap);
		newTerrain.setAllData(this.entityDensityData);
		return newTerrain;
	}

	public int getMinDistanceBetweenScaffolds() 
	{
		return this.minDistanceBetweenScaffolds;
	}

	public List<Scaffold> getScaffolds() 
	{
		return this.scaffolds;
	}

	public boolean askGenerateScaffolds(EntityIMLiving entity) 
	{
		if ((this.nextScaffoldCalcTimer > 0) || (this.scaffolds.size() > this.scaffoldLimit)) 
		{
			return false;
		}
		this.nextScaffoldCalcTimer = 200;
		List newScaffolds = findMinScaffolds(entity, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
		if ((newScaffolds != null) && (newScaffolds.size() > 0)) 
		{
			addNewScaffolds(newScaffolds);
			return true;
		}

		return false;
	}

	public List<Scaffold> findMinScaffolds(IPathfindable pather, int x, int y, int z) 
	{
		Scaffold scaffold = new Scaffold(this.nexus);
		scaffold.setPathfindBase(pather);
		Path basePath = createPath(scaffold, x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), 12.0F);
		if (basePath == null) {
			return new ArrayList();
		}
		List scaffoldPositions = extractScaffolds(basePath);
		if (scaffoldPositions.size() > 1) {
			float lowestCost = (1.0F / 1.0F);
			int lowestCostIndex = -1;
			for (int i = 0; i < scaffoldPositions.size(); i++) {
				TerrainDataLayer terrainMap = new TerrainDataLayer(getChunkCache(x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), 12.0F));
				Scaffold s = (Scaffold) scaffoldPositions.get(i);
				terrainMap.setData(s.getXCoord(), s.getYCoord(), s.getZCoord(), Integer.valueOf(200000));
				Path path = createPath(pather, x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), terrainMap);
				if ((path.getTotalPathCost() < lowestCost) && (path.getFinalPathPoint().equals(this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord()))) {
					lowestCostIndex = i;
				}
			}

			if (lowestCostIndex >= 0) {
				List s = new ArrayList();
				s.add(scaffoldPositions.get(lowestCostIndex));
				return s;
			}

			List costDif = new ArrayList(scaffoldPositions.size());
			for (int i = 0; i < scaffoldPositions.size(); i++) {
				TerrainDataLayer terrainMap = new TerrainDataLayer(getChunkCache(x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), 12.0F));
				Scaffold s = (Scaffold) scaffoldPositions.get(i);
				for (int j = 0; j < scaffoldPositions.size(); j++) {
					if (j != i) {
						terrainMap.setData(s.getXCoord(), s.getYCoord(), s.getZCoord(), Integer.valueOf(200000));
					}
				}
				Path path = createPath(pather, x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), terrainMap);

				if (!path.getFinalPathPoint().equals(this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord())) {
					costDif.add(s);
				}

			}

			return costDif;
		}

		if (scaffoldPositions.size() == 1) {
			return scaffoldPositions;
		}

		return null;
	}

	public void addScaffoldDataTo(IBlockAccessExtended terrainMap) {
		for (Scaffold scaffold : this.scaffolds) {
			for (int i = 0; i < scaffold.getTargetHeight(); i++) {
				int data = terrainMap.getLayeredData(scaffold.getXCoord(), scaffold.getYCoord() + i, scaffold.getZCoord());
				terrainMap.setData(scaffold.getXCoord(), scaffold.getYCoord() + i, scaffold.getZCoord(), Integer.valueOf(data | 0x4000));
			}
		}
	}

	public Scaffold getScaffoldAt(IPosition pos) {
		return getScaffoldAt(pos.getXCoord(), pos.getYCoord(), pos.getZCoord());
	}

	public Scaffold getScaffoldAt(int x, int y, int z) {
		for (Scaffold scaffold : this.scaffolds) {
			if ((scaffold.getXCoord() == x) && (scaffold.getZCoord() == z)) {
				if ((scaffold.getYCoord() <= y) && (scaffold.getYCoord() + scaffold.getTargetHeight() >= y))
					return scaffold;
			}
		}
		return null;
	}

	public void onResume() {
		for (Scaffold scaffold : this.scaffolds) {
			scaffold.forceStatusUpdate();
		}
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		//Had to put extra int param in 1.7.2, used 0 not sure why
		NBTTagList nbtScaffoldList = nbttagcompound.getTagList("scaffolds", 0);
		for (int i = 0; i < nbtScaffoldList.tagCount(); i++) {
			Scaffold scaffold = new Scaffold(this.nexus);
			scaffold.readFromNBT((NBTTagCompound) nbtScaffoldList.getCompoundTagAt(i));
			this.scaffolds.add(scaffold);
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		NBTTagList nbttaglist = new NBTTagList();
		for (Scaffold scaffold : this.scaffolds) {
			NBTTagCompound nbtscaffold = new NBTTagCompound();
			scaffold.writeToNBT(nbtscaffold);
			nbttaglist.appendTag(nbtscaffold);
		}
		nbttagcompound.setTag("scaffolds", nbttaglist);
	}

	private Path createPath(IPathfindable pather, int x1, int y1, int z1, int x2, int y2, int z2, IBlockAccess terrainMap) {
		return this.pathSource.createPath(pather, x1, y1, z1, x2, y2, z2, 1.1F, 12.0F + (float) Distance.distanceBetween(x1, y1, z1, x2, y2, z2), terrainMap);
	}

	private Path createPath(IPathfindable pather, int x, int y, int z, int x2, int y2, int z2, float axisExpand) {
		TerrainDataLayer terrainMap = new TerrainDataLayer(getChunkCache(x, y, z, x2, y2, z2, axisExpand));
		addScaffoldDataTo(terrainMap);
		return createPath(pather, x, y, z, x2, y2, z2, terrainMap);
	}

	private ChunkCache getChunkCache(int x1, int y1, int z1, int x2, int y2, int z2, float axisExpand) {
		int d = (int) axisExpand;
		int cX2;
		int cX1;
		if (x1 < x2) {
			cX1 = x1 - d;
			cX2 = x2 + d;
		} else {
			cX2 = x1 + d;
			cX1 = x2 - d;
		}
		int cY2;
		int cY1;
		if (y1 < y2) {
			cY1 = y1 - d;
			cY2 = y2 + d;
		} else {
			cY2 = y1 + d;
			cY1 = y2 - d;
		}
		int cZ2;
		int cZ1;
		if (z1 < z2) {
			cZ1 = z1 - d;
			cZ2 = z2 + d;
		} else {
			cZ2 = z1 + d;
			cZ1 = z2 - d;
		}
		return new ChunkCache(this.nexus.getWorld(), cX1, cY1, cZ1, cX2, cY2, cZ2, 0);
	}

	private List<Scaffold> extractScaffolds(Path path) {
		List scaffoldPositions = new ArrayList();
		boolean flag = false;
		int startHeight = 0;
		for (int i = 0; i < path.getCurrentPathLength(); i++) {
			PathNode node = path.getPathPointFromIndex(i);
			if (!flag) {
				if (node.action == PathAction.SCAFFOLD_UP) {
					flag = true;
					startHeight = node.getYCoord() - 1;
				}

			} else if (node.action != PathAction.SCAFFOLD_UP) {
				Scaffold scaffold = new Scaffold(node.getPrevious().getXCoord(), startHeight, node.getPrevious().getZCoord(), node.getYCoord() - startHeight, this.nexus);
				orientScaffold(scaffold, this.nexus.getWorld());
				scaffold.setInitialIntegrity();
				scaffoldPositions.add(scaffold);
				flag = false;
			}
		}

		return scaffoldPositions;
	}

	private void orientScaffold(Scaffold scaffold, IBlockAccess terrainMap) {
		int mostBlocks = 0;
		int highestDirectionIndex = 0;
		for (int i = 0; i < 4; i++) {
			int blockCount = 0;
			for (int height = 0; height < scaffold.getYCoord(); height++) {
				if (terrainMap.getBlock(scaffold.getXCoord() + invmod.common.util.CoordsInt.offsetAdjX[i], scaffold.getYCoord() + height, scaffold.getZCoord() + invmod.common.util.CoordsInt.offsetAdjZ[i]).isNormalCube()) {
					blockCount++;
				}
				if (terrainMap.getBlock(scaffold.getXCoord() + invmod.common.util.CoordsInt.offsetAdjX[i] * 2, scaffold.getYCoord() + height, scaffold.getZCoord() + invmod.common.util.CoordsInt.offsetAdjZ[i] * 2).isNormalCube()) {
					blockCount++;
				}
			}
			if (blockCount > mostBlocks) {
				highestDirectionIndex = i;
			}
		}
		scaffold.setOrientation(highestDirectionIndex);
	}

	private void addNewScaffolds(List<Scaffold> newScaffolds) {
		for (Scaffold newScaffold : newScaffolds) {
			for (Scaffold existingScaffold : this.scaffolds) {
				if ((existingScaffold.getXCoord() == newScaffold.getXCoord()) && (existingScaffold.getZCoord() == newScaffold.getZCoord())) {
					if (newScaffold.getYCoord() > existingScaffold.getYCoord()) {
						if (newScaffold.getYCoord() < existingScaffold.getYCoord() + existingScaffold.getTargetHeight()) {
							existingScaffold.setHeight(newScaffold.getYCoord() + newScaffold.getTargetHeight() - existingScaffold.getYCoord());
							break;
						}

					} else if (newScaffold.getYCoord() + newScaffold.getTargetHeight() > existingScaffold.getYCoord()) {
						existingScaffold.setPosition(newScaffold.getXCoord(), newScaffold.getYCoord(), newScaffold.getZCoord());
						existingScaffold.setHeight(existingScaffold.getYCoord() + existingScaffold.getTargetHeight() - newScaffold.getYCoord());
						break;
					}
				}

			}

			this.scaffolds.add(newScaffold);
		}
	}

	private void updateScaffolds() {
		for (int i = 0; i < this.scaffolds.size(); i++) {
			Scaffold lol = (Scaffold) this.scaffolds.get(i);
			this.nexus.getWorld().spawnParticle("heart", lol.getXCoord() + 0.2D, lol.getYCoord() + 0.2D, lol.getZCoord() + 0.2D, lol.getXCoord() + 0.5D, lol.getYCoord() + 0.5D, lol.getZCoord() + 0.5D);

			((Scaffold) this.scaffolds.get(i)).forceStatusUpdate();
			if (((Scaffold) this.scaffolds.get(i)).getPercentIntactCached() + 0.05F < 0.4F * ((Scaffold) this.scaffolds.get(i)).getPercentCompletedCached())
				this.scaffolds.remove(i);
		}
	}

	private void updateDensityData() {
		this.entityDensityData.clearMap();
		List<EntityIMLiving> mobs = this.nexus.getMobList();
		for (EntityIMLiving mob : mobs) {
			int coordHash = PathNode.makeHash(mob.getXCoord(), mob.getYCoord(), mob.getZCoord(), PathAction.NONE);
			if (this.entityDensityData.containsItem(coordHash)) {
				Integer value = (Integer) this.entityDensityData.lookup(coordHash);
				if (value.intValue() < 7) {
					this.entityDensityData.addKey(coordHash, Integer.valueOf(value.intValue() + 1));
				}
			} else {
				this.entityDensityData.addKey(coordHash, Integer.valueOf(1));
			}
		}
	}
}