package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.Distance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;

public class NavigatorIM implements INotifyTask, INavigation {
    protected static final int XZPATH_HORIZONTAL_SEARCH = 1;
    protected static final double ENTITY_TRACKING_TOLERANCE = 0.1D;
    protected static final double MINIMUM_PROGRESS = 0.01D;
    protected final EntityIMLiving theEntity;
    protected IPathSource pathSource;
    protected Path path;
    protected PathNode activeNode;
    protected Vec3 entityCentre;
    protected Entity pathEndEntity;
    protected Vec3 pathEndEntityLastPos;
    protected float moveSpeed;
    protected float pathSearchLimit;
    protected boolean noSunPathfind;
    protected int totalTicks;
    protected Vec3 lastPos;
    protected boolean nodeActionFinished;
    protected boolean waitingForNotify;
    protected boolean actionCleared;
    protected double lastDistance;
    protected int ticksStuck;
    private Vec3 holdingPos;
    private boolean canSwim;
    private boolean maintainPosOnWait;
    private int lastActionResult;
    private boolean haltMovement;
    private boolean autoPathToEntity;

    public NavigatorIM(EntityIMLiving entity, IPathSource pathSource) {
        this.theEntity = entity;
        this.pathSource = pathSource;
        this.noSunPathfind = false;
        this.lastPos = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        this.pathEndEntityLastPos = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        this.lastDistance = 0.0D;
        this.ticksStuck = 0;
        this.canSwim = false;
        this.waitingForNotify = false;
        this.actionCleared = true;
        this.nodeActionFinished = true;
        this.maintainPosOnWait = false;
        this.haltMovement = false;
        this.lastActionResult = 0;
        this.autoPathToEntity = false;
    }

    public PathAction getCurrentWorkingAction() {
        if ((!this.nodeActionFinished) && (!noPath())) {
            return this.activeNode.action;
        }
        return PathAction.NONE;
    }

    protected boolean isMaintainingPos() {
        return this.maintainPosOnWait;
    }

    protected void setNoMaintainPos() {
        this.maintainPosOnWait = false;
    }

    protected void setMaintainPosOnWait(Vec3 pos) {
        this.holdingPos = pos;
        this.maintainPosOnWait = true;
    }

    public void setSpeed(float par1) {
        this.moveSpeed = par1;
    }

    public boolean isAutoPathingToEntity() {
        return this.autoPathToEntity;
    }

    public Entity getTargetEntity() {
        return this.pathEndEntity;
    }

    public Path getPathToXYZ(double x, double y, double z, float targetRadius) {
        if (!canNavigate()) {
            return null;
        }
        return createPath(this.theEntity, MathHelper.floor_double(x), (int) y, MathHelper.floor_double(z), targetRadius);
    }

    public boolean tryMoveToXYZ(double x, double y, double z, float targetRadius, float speed) {
        this.ticksStuck = 0;
        Path newPath = getPathToXYZ(MathHelper.floor_double(x), (int) y, MathHelper.floor_double(z), targetRadius);
        if (newPath != null) {
            return setPath(newPath, speed);
        }
        return false;
    }

    public Path getPathTowardsXZ(double x, double z, int min, int max, int verticalRange) {
        if (canNavigate()) {
            Vec3 target = findValidPointNear(x, z, min, max, verticalRange);
            if (target != null) {
                Path entityPath = getPathToXYZ(target.xCoord, target.yCoord, target.zCoord, 0.0F);
                if (entityPath != null)
                    return entityPath;
            }
        }
        return null;
    }

    public boolean tryMoveTowardsXZ(double x, double z, int min, int max, int verticalRange, float speed) {
        this.ticksStuck = 0;
        Path newPath = getPathTowardsXZ(MathHelper.floor_double(x), MathHelper.floor_double(z), min, max, verticalRange);
        if (newPath != null) {
            return setPath(newPath, speed);
        }
        return false;
    }

    public Path getPathToEntity(Entity targetEntity, float targetRadius) {
        if (!canNavigate()) {
            return null;
        }
        return createPath(this.theEntity, MathHelper.floor_double(targetEntity.posX), MathHelper.floor_double(targetEntity.boundingBox.minY), MathHelper.floor_double(targetEntity.posZ), targetRadius);
    }

    public boolean tryMoveToEntity(Entity targetEntity, float targetRadius, float speed) {
        Path newPath = getPathToEntity(targetEntity, targetRadius);
        if (newPath != null) {
            if (setPath(newPath, speed)) {
                this.pathEndEntity = targetEntity;
                return true;
            }

            this.pathEndEntity = null;
            return false;
        }

        return false;
    }

    public void autoPathToEntity(Entity target) {
        this.autoPathToEntity = true;
        this.pathEndEntity = target;
    }

    public boolean setPath(Path newPath, float speed) {
        if (newPath == null) {
            this.path = null;
            this.theEntity.onPathSet();
            return false;
        }

        this.moveSpeed = speed;
        this.lastDistance = getDistanceToActiveNode();
        this.ticksStuck = 0;
        resetStatus();

        CoordsInt size = this.theEntity.getCollideSize();
        this.entityCentre = Vec3.createVectorHelper(size.getXCoord() * 0.5D, 0.0D, size.getZCoord() * 0.5D);

        this.path = newPath;
        this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());

        if (this.activeNode.action != PathAction.NONE) {
            this.nodeActionFinished = false;
        } else if ((size.getXCoord() <= 1) && (size.getZCoord() <= 1)) {
            this.path.incrementPathIndex();
            if (!this.path.isFinished()) {
                this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
                if (this.activeNode.action != PathAction.NONE) {
                    this.nodeActionFinished = false;
                }
            }
        } else {
            //UnstoppableN Custom Code
            //changed < to > this seems to have fixed some stuffs, not sure why
            while (this.theEntity.getDistanceSq(this.activeNode.xCoord + this.entityCentre.xCoord, this.activeNode.yCoord + this.entityCentre.yCoord, this.activeNode.zCoord + this.entityCentre.zCoord) > this.theEntity.width) {
                this.path.incrementPathIndex();
                if (!this.path.isFinished()) {
                    this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
                    if (this.activeNode.action != PathAction.NONE) {
                        this.nodeActionFinished = false;
                    }
                } else {
                    //this is the part where it gets cheaty!
                    //System.out.println("Finished! : "+this.path.getCurrentPathIndex()+" / "+this.path.points.length);
                    //NEVER USE BREAKS! unless you are retarded like me and don't know how to convert a float width to double getDistance
                    break;
                }

            }

        }

        if (this.noSunPathfind) {
            removeSunnyPath();
        }

        this.theEntity.onPathSet();
        return true;
    }

    public Path getPath() {
        return this.path;
    }

    public boolean isWaitingForTask() {
        return this.waitingForNotify;
    }

    public void onUpdateNavigation() {
        this.totalTicks += 1;
        if (this.autoPathToEntity) {
            updateAutoPathToEntity();
        }

        if (noPath()) {
            noPathFollow();
            return;
        }

        if (this.waitingForNotify) {
            if (isMaintainingPos()) {
                this.theEntity.getMoveHelper().setMoveTo(this.holdingPos.xCoord, this.holdingPos.yCoord, this.holdingPos.zCoord, this.moveSpeed);
            }
            return;
        }

        if ((canNavigate()) && (this.nodeActionFinished)) {
            double distance = getDistanceToActiveNode();
            if (this.lastDistance - distance > 0.01D) {
                this.lastDistance = distance;
                this.ticksStuck -= 1;
            } else {
                this.ticksStuck += 1;
            }

            int pathIndex = this.path.getCurrentPathIndex();
            pathFollow();
            if (noPath()) {
                return;
            }
            if (this.path.getCurrentPathIndex() != pathIndex) {
                this.lastDistance = getDistanceToActiveNode();
                this.ticksStuck = 0;
                this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
                if (this.activeNode.action != PathAction.NONE) {
                    this.nodeActionFinished = false;
                }
            }
        }

        if (this.nodeActionFinished) {
            if (!isPositionClearFrom(this.theEntity.getXCoord(), this.theEntity.getYCoord(), this.theEntity.getZCoord(), this.activeNode.xCoord, this.activeNode.yCoord, this.activeNode.zCoord, this.theEntity)) {
                if (this.theEntity.onPathBlocked(this.path, this)) {
                    setDoingTaskAndHoldOnPoint();
                }

            }

            if (!this.haltMovement) {
                if ((this.pathEndEntity != null) && (this.pathEndEntity.posY - this.theEntity.posY <= 0.0D) && (this.theEntity.getDistanceSq(this.pathEndEntity.posX, this.pathEndEntity.boundingBox.minY, this.pathEndEntity.posZ) < 4.5D))
                    this.theEntity.getMoveHelper().setMoveTo(this.pathEndEntity.posX, this.pathEndEntity.boundingBox.minY, this.pathEndEntity.posZ, this.moveSpeed);
                else {
                    this.theEntity.getMoveHelper().setMoveTo(this.activeNode.xCoord + this.entityCentre.xCoord, this.activeNode.yCoord + this.entityCentre.yCoord, this.activeNode.zCoord + this.entityCentre.zCoord, this.moveSpeed);
                }
            } else {
                this.haltMovement = false;
            }

        } else if (!handlePathAction()) {
            clearPath();
        }
    }

    public void notifyTask(int result) {
        this.waitingForNotify = false;
        this.lastActionResult = result;
    }

    public int getLastActionResult() {
        return this.lastActionResult;
    }

    public boolean noPath() {
        return (this.path == null) || (this.path.isFinished());
    }

    public int getStuckTime() {
        return this.ticksStuck;
    }

    public float getLastPathDistanceToTarget() {
        if (noPath()) {
            if ((this.path != null) && (this.path.getIntendedTarget() != null)) {
                PathNode node = this.path.getIntendedTarget();
                return (float) this.theEntity.getDistance(node.xCoord, node.yCoord, node.zCoord);
            }
            return 0.0F;
        }

        return this.path.getFinalPathPoint().distanceTo(this.path.getIntendedTarget());
    }

    public void clearPath() {
        this.path = null;
        this.autoPathToEntity = false;
        resetStatus();
    }

    public void haltForTick() {
        this.haltMovement = true;
    }

    public String getStatus() {
        String s = "";
        if (this.autoPathToEntity) {
            s = s + "Auto:";
        }
        if (noPath()) {
            s = s + "NoPath:";
            return s;
        }
        s = s + "Pathing:";
        s = s + "Node[" + this.path.getCurrentPathIndex() + "/" + this.path.getCurrentPathLength() + "]:";
        if ((!this.nodeActionFinished) && (this.activeNode != null)) {
            s = s + "Action[" + this.activeNode.action + "]:";
        }
        return s;
    }

    protected Path createPath(EntityIMLiving entity, Entity target, float targetRadius) {
        return createPath(entity, MathHelper.floor_double(target.posX), (int) target.posY, MathHelper.floor_double(target.posZ), targetRadius);
    }

    protected Path createPath(EntityIMLiving entity, int x, int y, int z, float targetRadius) {
        this.theEntity.setCurrentTargetPos(new CoordsInt(x, y, z));
        IBlockAccess terrainCache = getChunkCache(entity.getXCoord(), entity.getYCoord(), entity.getZCoord(), x, y, z, 16.0F);
        INexusAccess nexus = entity.getNexus();
        if (nexus != null) {
            terrainCache = nexus.getAttackerAI().wrapEntityData(terrainCache);
        }
        float maxSearchRange = 12.0F + (float) Distance.distanceBetween(entity, x, y, z);
        if (this.pathSource.canPathfindNice(IPathSource.PathPriority.HIGH, maxSearchRange, this.pathSource.getSearchDepth(), this.pathSource.getQuickFailDepth())) {
            return this.pathSource.createPath(entity, x, y, z, targetRadius, maxSearchRange, terrainCache);
        }
        return null;
    }

    protected void pathFollow() {
        Vec3 vec3d = getEntityPosition();
        int maxNextLegIndex = this.path.getCurrentPathIndex() - 1;

        PathNode nextPoint = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
        if ((nextPoint.yCoord == (int) vec3d.yCoord) && (maxNextLegIndex < this.path.getCurrentPathLength() - 1)) {
            maxNextLegIndex++;

            boolean canConsolidate = true;
            int prevIndex = maxNextLegIndex - 2;
            if ((prevIndex >= 0) && (this.path.getPathPointFromIndex(prevIndex).action != PathAction.NONE)) {
                canConsolidate = false;
            }
            if ((canConsolidate) && (this.theEntity.canStandAt(this.theEntity.worldObj, MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ)))) {
                while ((maxNextLegIndex < this.path.getCurrentPathLength() - 1) && (this.path.getPathPointFromIndex(maxNextLegIndex).yCoord == (int) vec3d.yCoord) && (this.path.getPathPointFromIndex(maxNextLegIndex).action == PathAction.NONE)) {
                    maxNextLegIndex++;
                }
            }

        }

        float fa = this.theEntity.width * 0.5F;
        fa *= fa;
        for (int j = this.path.getCurrentPathIndex(); j <= maxNextLegIndex; j++) {
            if (vec3d.squareDistanceTo(this.path.getPositionAtIndex(this.theEntity, j)) < fa) {
                this.path.setCurrentPathIndex(j + 1);
            }
        }

        int xSize = (int) Math.ceil(this.theEntity.width);
        int ySize = (int) this.theEntity.height + 1;
        int zSize = xSize;
        int index = maxNextLegIndex;

        while (index > this.path.getCurrentPathIndex()) {
            if (isDirectPathBetweenPoints(vec3d, this.path.getPositionAtIndex(this.theEntity, index), xSize, ySize, zSize)) {
                break;
            }
            index--;
        }

        for (int i = this.path.getCurrentPathIndex() + 1; i < index; i++) {
            if (this.path.getPathPointFromIndex(i).action != PathAction.NONE) {
                index = i;
                break;
            }

        }

        if (this.path.getCurrentPathIndex() < index)
            this.path.setCurrentPathIndex(index);
    }

    protected void noPathFollow() {
    }

    protected void updateAutoPathToEntity() {
        if (this.pathEndEntity == null)
            return;
        boolean wantsUpdate;
        if (noPath()) {
            wantsUpdate = true;
        } else {
            double d1 = Distance.distanceBetween(this.pathEndEntity, this.pathEndEntityLastPos);
            double d2 = 6.0D + Distance.distanceBetween((Entity) this.theEntity, this.pathEndEntityLastPos);
            if (d1 / d2 > 0.1D)
                wantsUpdate = true;
            else {
                wantsUpdate = false;
            }
        }
        if (wantsUpdate) {
            Path newPath = getPathToEntity(this.pathEndEntity, 0.0F);
            if (newPath != null) {
                if (setPath(newPath, this.moveSpeed)) {
                    this.pathEndEntityLastPos = Vec3.createVectorHelper(this.pathEndEntity.posX, this.pathEndEntity.posY, this.pathEndEntity.posZ);
                }
            }
        }
    }

    protected double getDistanceToActiveNode() {
        if (this.activeNode != null) {
            double dX = this.activeNode.xCoord + 0.5D - this.theEntity.posX;
            double dY = this.activeNode.yCoord - this.theEntity.posY;
            double dZ = this.activeNode.zCoord + 0.5D - this.theEntity.posZ;
            return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        }
        return 0.0D;
    }

    protected boolean handlePathAction() {
        this.nodeActionFinished = true;
        return true;
    }

    protected boolean setDoingTask() {
        this.waitingForNotify = true;
        this.actionCleared = false;
        return true;
    }

    protected boolean setDoingTaskAndHold() {
        this.waitingForNotify = true;
        this.actionCleared = false;
        setMaintainPosOnWait(Vec3.createVectorHelper(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ));
        this.theEntity.setIsHoldingIntoLadder(true);
        return true;
    }

    protected boolean setDoingTaskAndHoldOnPoint() {
        this.waitingForNotify = true;
        this.actionCleared = false;
        setMaintainPosOnWait(Vec3.createVectorHelper(this.activeNode.getXCoord() + 0.5D, this.activeNode.getYCoord(), this.activeNode.getZCoord() + 0.5D));
        this.theEntity.setIsHoldingIntoLadder(true);
        return true;
    }

    protected void resetStatus() {
        setNoMaintainPos();
        this.theEntity.setIsHoldingIntoLadder(false);
        this.nodeActionFinished = true;
        this.actionCleared = true;
        this.waitingForNotify = false;
    }

    protected Vec3 getEntityPosition() {
        return Vec3.createVectorHelper(this.theEntity.posX, getPathableYPos(), this.theEntity.posZ);
    }

    protected EntityIMLiving getEntity() {
        return this.theEntity;
    }

    private int getPathableYPos() {
        if ((!this.theEntity.isInWater()) || (!this.canSwim)) {
            return (int) (this.theEntity.boundingBox.minY + 0.5D);
        }

        int i = (int) this.theEntity.boundingBox.minY;
        Block block = this.theEntity.worldObj.getBlock(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ));
        int k = 0;

        while ((block == Blocks.water) || (block == Blocks.flowing_water)) {
            i++;
            block = this.theEntity.worldObj.getBlock(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ));

            k++;
            if (k > 16) {
                return (int) this.theEntity.boundingBox.minY;
            }
        }

        return i;
    }

    protected boolean canNavigate() {
        return true;
    }

    protected boolean isInLiquid() {
        return (this.theEntity.isInWater()) || (this.theEntity.handleLavaMovement());
    }

    protected Vec3 findValidPointNear(double x, double z, int min, int max, int verticalRange) {
        double xOffset = x - this.theEntity.posX;
        double zOffset = z - this.theEntity.posZ;
        double h = Math.sqrt(xOffset * xOffset + zOffset * zOffset);

        if (h < 0.5D) {
            return null;
        }

        double distance = min + this.theEntity.getRNG().nextInt(max - min);
        int xi = MathHelper.floor_double(xOffset * (distance / h) + this.theEntity.posX);
        int zi = MathHelper.floor_double(zOffset * (distance / h) + this.theEntity.posZ);
        int y = MathHelper.floor_double(this.theEntity.posY);

        Path entityPath = null;
        for (int vertical = 0; vertical < verticalRange; vertical = vertical > 0 ? vertical * -1 : vertical * -1 + 1) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (this.theEntity.canStandAtAndIsValid(this.theEntity.worldObj, xi + i, y + vertical, zi + j)) {
                        return Vec3.createVectorHelper(xi + i, y + vertical, zi + j);
                    }
                }
            }
        }

        return null;
    }

    protected void removeSunnyPath() {
        if (this.theEntity.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.theEntity.posX), (int) (this.theEntity.boundingBox.minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ))) {
            return;
        }

        for (int i = 0; i < this.path.getCurrentPathLength(); i++) {
            PathNode pathpoint = this.path.getPathPointFromIndex(i);

            if (this.theEntity.worldObj.canBlockSeeTheSky(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord)) {
                this.path.setCurrentPathLength(i - 1);
                return;
            }
        }
    }

    protected boolean isDirectPathBetweenPoints(Vec3 pos1, Vec3 pos2, int xSize, int ySize, int zSize) {
        int x = MathHelper.floor_double(pos1.xCoord);
        int z = MathHelper.floor_double(pos1.zCoord);
        double dX = pos2.xCoord - pos1.xCoord;
        double dZ = pos2.zCoord - pos1.zCoord;
        double dXZsq = dX * dX + dZ * dZ;

        if (dXZsq < 1.0E-008D) {
            return false;
        }

        double scale = 1.0D / Math.sqrt(dXZsq);
        dX *= scale;
        dZ *= scale;
        xSize += 2;
        zSize += 2;

        if (!isSafeToStandAt(x, (int) pos1.yCoord, z, xSize, ySize, zSize, pos1, dX, dZ)) {
            return false;
        }

        xSize -= 2;
        zSize -= 2;
        double xIncrement = 1.0D / Math.abs(dX);
        double zIncrement = 1.0D / Math.abs(dZ);
        double xOffset = x * 1 - pos1.xCoord;
        double zOffset = z * 1 - pos1.zCoord;

        if (dX >= 0.0D) {
            xOffset += 1.0D;
        }

        if (dZ >= 0.0D) {
            zOffset += 1.0D;
        }

        xOffset /= dX;
        zOffset /= dZ;
        byte xDirection = (byte) (dX >= 0.0D ? 1 : -1);
        byte zDirection = (byte) (dZ >= 0.0D ? 1 : -1);
        int x2 = MathHelper.floor_double(pos2.xCoord);
        int z2 = MathHelper.floor_double(pos2.zCoord);
        int xDiff = x2 - x;

        for (int i = z2 - z; (xDiff * xDirection > 0) || (i * zDirection > 0); ) {
            if (xOffset < zOffset) {
                xOffset += xIncrement;
                x += xDirection;
                xDiff = x2 - x;
            } else {
                zOffset += zIncrement;
                z += zDirection;
                i = z2 - z;
            }

            if (!isSafeToStandAt(x, (int) pos1.yCoord, z, xSize, ySize, zSize, pos1, dX, dZ)) {
                return false;
            }
        }

        return true;
    }

    protected boolean isSafeToStandAt(int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, Vec3 entityPostion, double par8, double par10) {
        int i = xOffset - xSize / 2;
        int j = zOffset - zSize / 2;

        if (!isPositionClear(i, yOffset, j, xSize, ySize, zSize, entityPostion, par8, par10)) {
            return false;
        }

        for (int k = i; k < i + xSize; k++) {
            for (int l = j; l < j + zSize; l++) {
                double d = k + 0.5D - entityPostion.xCoord;
                double d1 = l + 0.5D - entityPostion.zCoord;

                if (d * par8 + d1 * par10 >= 0.0D) {
                    Block block = this.theEntity.worldObj.getBlock(k, yOffset - 1, l);

                    if (block == Blocks.air) {
                        return false;
                    }

                    Material material = block.getMaterial();

                    if ((material == Material.water) && (!this.theEntity.isInWater())) {
                        return false;
                    }

                    if (material == Material.lava) {
                        return false;
                    }

                    if (!material.isSolid()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean isPositionClear(int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, Vec3 entityPostion, double vecX, double vecZ) {
        for (int i = xOffset; i < xOffset + xSize; i++) {
            for (int j = yOffset; j < yOffset + ySize; j++) {
                for (int k = zOffset; k < zOffset + zSize; k++) {
                    double d = i + 0.5D - entityPostion.xCoord;
                    double d1 = k + 0.5D - entityPostion.zCoord;

                    if (d * vecX + d1 * vecZ >= 0.0D) {
                        Block block = this.theEntity.worldObj.getBlock(i, j, k);

                        if ((block != Blocks.air) && (!block.getBlocksMovement(this.theEntity.worldObj, i, j, k))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    protected boolean isPositionClearFrom(int x1, int y1, int z1, int x2, int y2, int z2, EntityIMLiving entity) {
        if (y2 > y1) {
            Block block = this.theEntity.worldObj.getBlock(x1, y1 + entity.getCollideSize().getYCoord(), z1);
            if ((block != Blocks.air) && (!block.getBlocksMovement(this.theEntity.worldObj, x1, y1 + entity.getCollideSize().getYCoord(), z1))) {
                return false;
            }
        }

        return isPositionClear(x2, y2, z2, entity);
    }

    protected boolean isPositionClear(int x, int y, int z, EntityIMLiving entity) {
        CoordsInt size = entity.getCollideSize();
        return isPositionClear(x, y, z, size.getXCoord(), size.getYCoord(), size.getZCoord());
    }

    protected boolean isPositionClear(int x, int y, int z, int xSize, int ySize, int zSize) {
        for (int i = x; i < x + xSize; i++) {
            for (int j = y; j < y + ySize; j++) {
                for (int k = z; k < z + zSize; k++) {
                    Block block = this.theEntity.worldObj.getBlock(i, j, k);

                    if ((block != Blocks.air) && (!block.getBlocksMovement(this.theEntity.worldObj, i, j, k))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    protected ChunkCache getChunkCache(int x1, int y1, int z1, int x2, int y2, int z2, float axisExpand) {
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
        return new ChunkCache(this.theEntity.worldObj, cX1, cY1, cZ1, cX2, cY2, cZ2, 0);
    }
}
