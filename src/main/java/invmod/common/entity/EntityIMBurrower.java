package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.PosRotate3D;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

//NOOB HAUS: This one is done I think...
public class EntityIMBurrower extends EntityIMMob implements ICanDig {
    public static final int NUMBER_OF_SEGMENTS = 16;
    private final NavigatorBurrower bo;
    private final PathNavigateAdapter oldNavAdapter;
    protected float prevRotX;
    protected float prevRotY;
    protected float prevRotZ;
    private TerrainModifier terrainModifier;
    private TerrainDigger terrainDigger;
    private EntityAITasks goals;
    private PosRotate3D[] segments3D;
    private PosRotate3D[] segments3DLastTick;
    private float rotX;
    private float rotY;
    private float rotZ;

    public EntityIMBurrower(World world) {
        this(world, null);
    }

    public EntityIMBurrower(World world, INexusAccess nexus) {
        super(world, nexus);

        IPathSource pathSource = getPathSource();
        pathSource.setSearchDepth(800);
        pathSource.setQuickFailDepth(400);
        this.bo = new NavigatorBurrower(this, pathSource, 16, -4);
        this.oldNavAdapter = new PathNavigateAdapter(this.bo);

        this.terrainModifier = new TerrainModifier(this, 2.0F);
        this.terrainDigger = new TerrainDigger(this, this.terrainModifier, 1.0F);

        setName("Burrower");
        setGender(0);
        this.setSize(0.5F, 0.5F);
        setJumpHeight(0);
        setCanClimb(true);
        setDestructiveness(2);
        this.maxDestructiveness = 2;
        this.blockRemoveSpeed = 0.5F;

        this.segments3D = new PosRotate3D[16];
        this.segments3DLastTick = new PosRotate3D[16];

        PosRotate3D zero = new PosRotate3D();
        for (int i = 0; i < 16; i++) {
            this.segments3D[i] = zero;
            this.segments3DLastTick[i] = zero;
        }
    }

    @Override
    public String toString() {
        return "EntityIMBurrower#u-u-u";
    }

    @Override
    public IBlockAccess getTerrain() {
        return this.worldObj;
    }

    public float getBlockPathCost(PathPoint prevNode, PathPoint node, IBlockAccess worldMap) {
        Block block = worldMap.getBlock(node.xCoord, node.yCoord, node.zCoord);

        float penalty = 0.0F;
        int enclosedLevelSide = 0;
        //donno what last parameter does, but I've set it to true anyway!
        if (!this.worldObj.isBlockNormalCubeDefault(node.xCoord, node.yCoord - 1, node.zCoord, true))
            penalty += 0.3F;
        if (!this.worldObj.isBlockNormalCubeDefault(node.xCoord, node.yCoord + 1, node.zCoord, true))
            penalty += 2.0F;
        if (!this.worldObj.isBlockNormalCubeDefault(node.xCoord + 1, node.yCoord, node.zCoord, true))
            enclosedLevelSide++;
        if (!this.worldObj.isBlockNormalCubeDefault(node.xCoord - 1, node.yCoord, node.zCoord, true))
            enclosedLevelSide++;
        if (!this.worldObj.isBlockNormalCubeDefault(node.xCoord, node.yCoord, node.zCoord + 1, true))
            enclosedLevelSide++;
        if (!this.worldObj.isBlockNormalCubeDefault(node.xCoord, node.yCoord, node.zCoord - 1, true))
            enclosedLevelSide++;

        if (enclosedLevelSide > 2) {
            enclosedLevelSide = 2;
        }
        penalty += enclosedLevelSide * 0.5F;

        if (block == Blocks.air) {
            return prevNode.distanceTo(node) * 1.0F * penalty;
        }
        if (EntityIMLiving.blockCosts.containsKey(block)) {
            return prevNode.distanceTo(node) * 1.0F * 1.3F * penalty;
        }
        if (block.isCollidable()) {
            return prevNode.distanceTo(node) * 1.0F * 1.3F * penalty;
        }

        return prevNode.distanceTo(node) * 1.0F * penalty;
    }

    @Override
    public float getBlockRemovalCost(int x, int y, int z) {
        return getBlockStrength(x, y, z) * 20.0F;
    }

    @Override
    public boolean canClearBlock(int x, int y, int z) {
        Block block = this.worldObj.getBlock(x, y, z);
        return (block == Blocks.air) || (isBlockDestructible(this.worldObj, x, y, z, block));
    }

    @Override
    public String getSpecies() {
        return "";
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public PathNavigateAdapter getNavigator() {
        return this.oldNavAdapter;
    }

    @Override
    public INavigation getNavigatorNew() {
        return this.bo;
    }

    protected boolean onPathBlocked(int x, int y, int z, INotifyTask notifee) {
        if (this.terrainDigger.askClearPosition(x, y, z, notifee, 1.0F)) {
            return true;
        }
        return false;
    }

    public float getRotX() {
        return this.rotX;
    }

    public float getRotY() {
        return this.rotY;
    }

    public float getRotZ() {
        return this.rotZ;
    }

    public float getPrevRotX() {
        return this.prevRotX;
    }

    public float getPrevRotY() {
        return this.prevRotY;
    }

    public float getPrevRotZ() {
        return this.prevRotZ;
    }

    public PosRotate3D[] getSegments3D() {
        return this.segments3D;
    }

    public PosRotate3D[] getSegments3DLastTick() {
        return this.segments3DLastTick;
    }

    public void setSegment(int index, PosRotate3D pos) {
        if (index < this.segments3D.length) {
            this.segments3DLastTick[index] = this.segments3D[index];
            this.segments3D[index] = pos;
        }
    }

    public void setHeadRotation(PosRotate3D pos) {
        this.prevRotX = this.rotX;
        this.prevRotY = this.rotY;
        this.prevRotZ = this.rotZ;
        this.rotX = pos.getRotX();
        this.rotY = pos.getRotY();
        this.rotZ = pos.getRotZ();
    }

    @Override
    public void moveEntityWithHeading(float x, float z) {
        if (isInWater()) {
            double y = this.posY;
            moveFlying(x, z, 0.02F);
            moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.8D;
            this.motionY *= 0.8D;
            this.motionZ *= 0.8D;
            this.motionY -= 0.02D;
            if ((this.isCollidedHorizontally) && (isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6D - this.posY + y, this.motionZ)))
                this.motionY = 0.3D;
        } else if (handleLavaMovement()) {
            double y = this.posY;
            moveFlying(x, z, 0.02F);
            moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
            this.motionY -= 0.02D;
            if ((this.isCollidedHorizontally) && (isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6D - this.posY + y, this.motionZ)))
                this.motionY = 0.3D;
        } else {
            float groundFriction = 1.0F;
            if (this.onGround) {
                groundFriction = 0.546F;
                Block block = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
                if (block != Blocks.air) {
                    groundFriction = block.slipperiness * 0.91F;
                }
            }
            if (isOnLadder()) {
                float maxLadderXZSpeed = 0.15F;
                if (this.motionX < -maxLadderXZSpeed)
                    this.motionX = (-maxLadderXZSpeed);
                if (this.motionX > maxLadderXZSpeed)
                    this.motionX = maxLadderXZSpeed;
                if (this.motionZ < -maxLadderXZSpeed)
                    this.motionZ = (-maxLadderXZSpeed);
                if (this.motionZ > maxLadderXZSpeed) {
                    this.motionZ = maxLadderXZSpeed;
                }
                this.fallDistance = 0.0F;
                if (this.motionY < -0.15D) {
                    this.motionY = -0.15D;
                }
                if ((isSneaking()) && (this.motionY < 0.0D)) {
                    this.motionY = 0.0D;
                }
            }
            moveEntity(this.motionX, this.motionY, this.motionZ);
            if ((this.isCollidedHorizontally) && (isOnLadder())) {
                this.motionY = 0.2D;
            }
            float airResistance = 0.98F;
            float gravityAcel = 0.0F;
            this.motionY -= gravityAcel;
            this.motionY *= airResistance;
            this.motionX *= airResistance;
            this.motionZ *= airResistance;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double dX = this.posX - this.prevPosX;
        double dZ = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt_double(dX * dX + dZ * dZ) * 4.0F;

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.terrainModifier.onUpdate();
    }

    @Override
    public void updateAITick() {
        super.updateAITick();
    }

    @Override
    public void onBlockRemoved(int paramInt1, int paramInt2, int paramInt3,
                               Block block) {
        // TODO Auto-generated method stub

    }
}