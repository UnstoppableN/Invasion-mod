package invmod.common.entity;

import invmod.common.IBlockAccessExtended;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class EntityIMFlying extends EntityIMLiving {
    private static final int META_TARGET_X = 29;
    private static final int META_TARGET_Y = 30;
    private static final int META_TARGET_Z = 31;
    private static final int META_THRUST_DATA = 28;
    private static final int META_FLYSTATE = 27;
    private final NavigatorFlying navigatorFlying;
    private final IMMoveHelperFlying i;
    private final IMLookHelper h;
    private final IMBodyHelper bn;
    private FlyState flyState;
    private float liftFactor;
    private float maxPoweredFlightSpeed;
    private float thrust;
    private float thrustComponentRatioMin;
    private float thrustComponentRatioMax;
    private float maxTurnForce;
    private float optimalPitch;
    private float landingSpeedThreshold;
    private float maxRunSpeed;
    private float flightAccelX;
    private float flightAccelY;
    private float flightAccelZ;
    private boolean thrustOn;
    private float thrustEffort;
    private boolean flyPathfind;
    private boolean debugFlying;

    public EntityIMFlying(World world) {
        this(world, null);
    }

    public EntityIMFlying(World world, INexusAccess nexus) {
        super(world, nexus);
        this.debugFlying = true;
        this.flyState = FlyState.GROUNDED;
        this.maxPoweredFlightSpeed = 0.28F;
        this.liftFactor = 0.4F;
        this.thrust = 0.08F;
        this.thrustComponentRatioMin = 0.0F;
        this.thrustComponentRatioMax = 0.1F;
        this.maxTurnForce = (getGravity() * 3.0F);
        this.optimalPitch = 52.0F;
        this.landingSpeedThreshold = (getMoveSpeedStat() * 1.2F);
        this.maxRunSpeed = 0.45F;
        this.thrustOn = false;
        this.thrustEffort = 1.0F;
        this.flyPathfind = true;

        this.i = new IMMoveHelperFlying(this);
        this.h = new IMLookHelper(this);
        this.bn = new IMBodyHelper(this);
        IPathSource pathSource = getPathSource();
        pathSource.setSearchDepth(800);
        pathSource.setQuickFailDepth(200);
        this.navigatorFlying = new NavigatorFlying(this, pathSource);

        this.dataWatcher.addObject(29, Integer.valueOf(0));
        this.dataWatcher.addObject(30, Integer.valueOf(0));
        this.dataWatcher.addObject(31, Integer.valueOf(0));
        this.dataWatcher.addObject(28, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(27, Integer.valueOf(this.flyState.ordinal()));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            if (this.debugFlying) {
                Vec3 target = this.navigatorFlying.getTarget();
                float oldTargetX = MathUtil.unpackFloat(this.dataWatcher.getWatchableObjectInt(29));
                float oldTargetY = MathUtil.unpackFloat(this.dataWatcher.getWatchableObjectInt(30));
                float oldTargetZ = MathUtil.unpackFloat(this.dataWatcher.getWatchableObjectInt(31));

                if ((!MathUtil.floatEquals(oldTargetX, (float) target.xCoord, 0.1F)) || (!MathUtil.floatEquals(oldTargetY, (float) target.yCoord, 0.1F)) || (!MathUtil.floatEquals(oldTargetZ, (float) target.zCoord, 0.1F))) {
                    this.dataWatcher.updateObject(29, Integer.valueOf(MathUtil.packFloat((float) target.xCoord)));
                    this.dataWatcher.updateObject(30, Integer.valueOf(MathUtil.packFloat((float) target.yCoord)));
                    this.dataWatcher.updateObject(31, Integer.valueOf(MathUtil.packFloat((float) target.zCoord)));
                }
            }

            byte thrustData = this.dataWatcher.getWatchableObjectByte(28);
            int oldThrustOn = thrustData & 0x1;
            int oldThrustEffortEncoded = thrustData >> 1 & 0xF;
            int thrustEffortEncoded = (int) (this.thrustEffort * 15.0F);
            if (this.thrustOn == oldThrustOn > 0) {
                if (thrustEffortEncoded == oldThrustEffortEncoded)
                    ;
            } else {
                this.dataWatcher.updateObject(28, Byte.valueOf((byte) (thrustEffortEncoded << 1 | oldThrustOn)));
            }

        } else {
            if (this.debugFlying) {
                float x = MathUtil.unpackFloat(this.dataWatcher.getWatchableObjectInt(29));
                float y = MathUtil.unpackFloat(this.dataWatcher.getWatchableObjectInt(30));
                float z = MathUtil.unpackFloat(this.dataWatcher.getWatchableObjectInt(31));
                this.navigatorFlying.setTarget(x, y, z);
            }

            this.flyState = FlyState.values()[this.dataWatcher.getWatchableObjectInt(27)];

            byte thrustData = this.dataWatcher.getWatchableObjectByte(28);
            this.thrustOn = ((thrustData & 0x1) > 0);
            this.thrustEffort = ((thrustData >> 1 & 0xF) / 15.0F);
        }
    }

    public FlyState getFlyState() {
        return this.flyState;
    }

    protected void setFlyState(FlyState flyState) {
        this.flyState = flyState;
        if (!this.worldObj.isRemote)
            this.dataWatcher.updateObject(27, Integer.valueOf(flyState.ordinal()));
    }

    public boolean isThrustOn() {
        return this.dataWatcher.getWatchableObjectByte(28) != 0;
    }

    protected void setThrustOn(boolean flag) {
        this.thrustOn = flag;
    }

    public float getThrustEffort() {
        return this.thrustEffort;
    }

    protected void setThrustEffort(float effortFactor) {
        this.thrustEffort = effortFactor;
    }

    public Vec3 getFlyTarget() {
        return this.navigatorFlying.getTarget();
    }

    @Override
    public INavigationFlying getNavigatorNew() {
        return this.navigatorFlying;
    }

    @Override
    public IMMoveHelperFlying getMoveHelper() {
        return this.i;
    }

    @Override
    public IMLookHelper getLookHelper() {
        return this.h;
    }

    public IMBodyHelper getBodyHelper() {
        return this.bn;
    }

    @Override
    public void moveEntityWithHeading(float x, float z) {
        if (isInWater()) {
            double y = this.posY;
            moveFlying(x, z, isAIEnabled() ? 0.04F : 0.02F);
            moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.8D;
            this.motionY *= 0.8D;
            this.motionZ *= 0.8D;
            this.motionY -= 0.02D;
            if ((this.isCollidedHorizontally) && (isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6D - this.posY + y, this.motionZ)))
                this.motionY = 0.3D;
        } else if (handleLavaMovement()) {
            double y = this.posY;
            moveFlying(x, z, isAIEnabled() ? 0.04F : 0.02F);
            moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
            this.motionY -= 0.02D;
            if ((this.isCollidedHorizontally) && (isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6D - this.posY + y, this.motionZ)))
                this.motionY = 0.3D;
        } else {
            float groundFriction = 0.9995F;

            if (this.onGround) {
                groundFriction = getGroundFriction();

                float maxRunSpeed = getMaxRunSpeed();
                if (this.motionX * this.motionX + this.motionZ * this.motionZ < maxRunSpeed * maxRunSpeed) {
                    float landMoveSpeed = getAIMoveSpeed();
                    landMoveSpeed *= 0.162771F / (groundFriction * groundFriction * groundFriction);
                    moveFlying(x, z, landMoveSpeed);
                }
            } else {
                moveFlying(x, z, 0.01F);
            }

            this.motionX += this.flightAccelX;
            this.motionY += this.flightAccelY;
            this.motionZ += this.flightAccelZ;

            moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionY -= getGravity();
            this.motionY *= getAirResistance();
            this.motionX *= groundFriction * getAirResistance();
            this.motionZ *= groundFriction * getAirResistance();
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double dX = this.posX - this.prevPosX;
        double dZ = this.posZ - this.prevPosZ;
        float limbEnergy = MathHelper.sqrt_double(dX * dX + dZ * dZ) * 4.0F;

        if (limbEnergy > 1.0F) {
            limbEnergy = 1.0F;
        }

        this.limbSwingAmount += (limbEnergy - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    public boolean hasFlyingDebug() {
        return this.debugFlying;
    }

    protected void setPathfindFlying(boolean flag) {
        this.flyPathfind = flag;
    }

    public float getMaxPoweredFlightSpeed() {
        return this.maxPoweredFlightSpeed;
    }

    protected void setMaxPoweredFlightSpeed(float speed) {
        this.maxPoweredFlightSpeed = speed;
        getNavigatorNew().setFlySpeed(speed);
    }

    protected float getLiftFactor() {
        return this.liftFactor;
    }

    protected void setLiftFactor(float liftFactor) {
        this.liftFactor = liftFactor;
    }

    protected float getThrust() {
        return this.thrust;
    }

    protected void setThrust(float thrust) {
        this.thrust = thrust;
    }

    protected float getThrustComponentRatioMin() {
        return this.thrustComponentRatioMin;
    }

    protected void setThrustComponentRatioMin(float ratio) {
        this.thrustComponentRatioMin = ratio;
    }

    protected float getThrustComponentRatioMax() {
        return this.thrustComponentRatioMax;
    }

    protected void setThrustComponentRatioMax(float ratio) {
        this.thrustComponentRatioMax = ratio;
    }

    protected float getMaxTurnForce() {
        return this.maxTurnForce;
    }

    protected void setMaxTurnForce(float maxTurnForce) {
        this.maxTurnForce = maxTurnForce;
    }

    protected float getMaxPitch() {
        return this.optimalPitch;
    }

    protected float getLandingSpeedThreshold() {
        return this.landingSpeedThreshold;
    }

    protected void setLandingSpeedThreshold(float speed) {
        this.landingSpeedThreshold = speed;
    }

    protected float getMaxRunSpeed() {
        return this.maxRunSpeed;
    }

    protected void setMaxRunSpeed(float speed) {
        this.maxRunSpeed = speed;
    }

    protected void setFlightAccelerationVector(float xAccel, float yAccel, float zAccel) {
        this.flightAccelX = xAccel;
        this.flightAccelY = yAccel;
        this.flightAccelZ = zAccel;
    }

    protected void setOptimalPitch(float pitch) {
        this.optimalPitch = pitch;
    }

    @Override
    protected void fall(float par1) {
    }

    @Override
    protected void updateFallState(double par1, boolean par3) {
    }

    @Override
    protected void calcPathOptions(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        if (!this.flyPathfind)
            super.calcPathOptions(terrainMap, currentNode, pathFinder);
        else
            calcPathOptionsFlying(terrainMap, currentNode, pathFinder);
    }

    protected void calcPathOptionsFlying(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        if ((currentNode.yCoord <= 0) || (currentNode.yCoord > 255)) {
            return;
        }

        if (getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord) > 0) {
            pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.NONE);
        }

        if (getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord - 1, currentNode.zCoord) > 0) {
            pathFinder.addNode(currentNode.xCoord, currentNode.yCoord - 1, currentNode.zCoord, PathAction.NONE);
        }

        for (int i = 0; i < 4; i++) {
            if (getCollide(terrainMap, currentNode.xCoord + invmod.common.util.CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + invmod.common.util.CoordsInt.offsetAdjZ[i]) > 0) {
                pathFinder.addNode(currentNode.xCoord + invmod.common.util.CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + invmod.common.util.CoordsInt.offsetAdjZ[i], PathAction.NONE);
            }
        }
        if (canSwimHorizontal()) {
            for (int i = 0; i < 4; i++) {
                if (getCollide(terrainMap, currentNode.xCoord + invmod.common.util.CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + invmod.common.util.CoordsInt.offsetAdjZ[i]) == -1)
                    pathFinder.addNode(currentNode.xCoord + invmod.common.util.CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + invmod.common.util.CoordsInt.offsetAdjZ[i], PathAction.SWIM);
            }
        }
    }

    @Override
    protected float calcBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        float multiplier = 1.0F;
        if ((terrainMap instanceof IBlockAccessExtended)) {
            int mobDensity = ((IBlockAccessExtended) terrainMap).getLayeredData(node.xCoord, node.yCoord, node.zCoord) & 0x7;
            multiplier += mobDensity * 3;
        }

        for (int i = -1; i > -6; i--) {
            Block block = terrainMap.getBlock(node.xCoord, node.yCoord + i, node.zCoord);
            if (block != Blocks.air) {
                int blockType = getBlockType(block);
                if (blockType != 1) {
                    multiplier += 1.0F - -i * 0.2F;
                    if ((blockType != 2) || (i < -2))
                        break;
                    multiplier = (float) (multiplier + (6.0D - -i * 2.0D));
                    break;
                }

            }

        }

        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= 2; j++) {
                Block block = terrainMap.getBlock(node.xCoord + invmod.common.util.CoordsInt.offsetAdjX[i] * j, node.yCoord, node.zCoord + invmod.common.util.CoordsInt.offsetAdjZ[i] * j);
                int blockType = getBlockType(block);
                if (blockType != 1) {
                    multiplier += 1.5F - j * 0.5F;
                    if ((blockType != 2) || (i < -2))
                        break;
                    multiplier += 6.0F - j * 2.0F;
                    break;
                }

            }

        }

        if (node.action == PathAction.SWIM) {
            multiplier *= ((node.yCoord <= prevNode.yCoord) && (terrainMap.getBlock(node.xCoord, node.yCoord + 1, node.zCoord) != Blocks.air) ? 3.0F : 1.0F);
            return prevNode.distanceTo(node) * 1.3F * multiplier;
        }

        Block block = terrainMap.getBlock(node.xCoord, node.yCoord, node.zCoord);
        if (EntityIMLiving.blockCosts.containsKey(block)) {
            return prevNode.distanceTo(node) * ((Float) EntityIMLiving.blockCosts.get(block)).floatValue() * multiplier;
        }
        if (block.isCollidable()) {
            return prevNode.distanceTo(node) * 3.2F * multiplier;
        }

        return prevNode.distanceTo(node) * 1.0F * multiplier;
    }
}