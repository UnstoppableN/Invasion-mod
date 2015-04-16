package invmod.common.entity;


import invmod.common.util.Distance;
import invmod.common.util.MathUtil;
import invmod.common.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class NavigatorFlying extends NavigatorIM implements INavigationFlying {
    private static final int VISION_RESOLUTION_H = 30;
    private static final int VISION_RESOLUTION_V = 20;
    private static final float FOV_H = 300.0F;
    private static final float FOV_V = 220.0F;
    private final EntityIMFlying theEntity;
    private INavigationFlying.MoveType moveType;
    private boolean wantsToBeFlying;
    private float targetYaw;
    private float targetPitch;
    private float targetSpeed;
    private float visionDistance;
    private int visionUpdateRate;
    private int timeSinceVision;
    private float[][] retina;
    private float[][] headingAppeal;
    private Vec3 intermediateTarget;
    private Vec3 finalTarget;
    private boolean isCircling;
    private float circlingHeight;
    private float circlingRadius;
    private float pitchBias;
    private float pitchBiasAmount;
    private int timeLookingForEntity;
    private boolean precisionTarget;
    private float closestDistToTarget;
    private int timeSinceGotCloser;

    public NavigatorFlying(EntityIMFlying entityFlying, IPathSource pathSource) {
        super(entityFlying, pathSource);
        this.theEntity = entityFlying;
        this.moveType = INavigationFlying.MoveType.MIXED;
        this.visionDistance = 14.0F;
        this.visionUpdateRate = (this.timeSinceVision = 3);
        this.targetYaw = entityFlying.rotationYaw;
        this.targetPitch = 0.0F;
        this.targetSpeed = entityFlying.getMaxPoweredFlightSpeed();
        this.retina = new float[30][20];
        this.headingAppeal = new float[28][18];
        this.intermediateTarget = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        this.isCircling = false;
        this.pitchBias = 0.0F;
        this.pitchBiasAmount = 0.0F;
        this.timeLookingForEntity = 0;
        this.precisionTarget = false;
        this.closestDistToTarget = 0.0F;
        this.timeSinceGotCloser = 0;
    }

    public void setMovementType(INavigationFlying.MoveType moveType) {
        this.moveType = moveType;
    }

    public void enableDirectTarget(boolean enabled) {
        this.precisionTarget = enabled;
    }

    public void setLandingPath() {
        clearPath();
        this.moveType = INavigationFlying.MoveType.PREFER_WALKING;
        setWantsToBeFlying(false);
    }

    public void setCirclingPath(Entity target, float preferredHeight, float preferredRadius) {
        setCirclingPath(target.posX, target.posY, target.posZ, preferredHeight, preferredRadius);
    }

    public void setCirclingPath(double x, double y, double z, float preferredHeight, float preferredRadius) {
        clearPath();
        this.finalTarget = Vec3.createVectorHelper(x, y, z);
        this.circlingHeight = preferredHeight;
        this.circlingRadius = preferredRadius;
        this.isCircling = true;
    }

    public float getDistanceToCirclingRadius() {
        double dX = this.finalTarget.xCoord - this.theEntity.posX;
        double dY = this.finalTarget.yCoord - this.theEntity.posY;
        double dZ = this.finalTarget.zCoord - this.theEntity.posZ;
        return (float) (Math.sqrt(dX * dX + dZ * dZ) - this.circlingRadius);
    }

    public void setFlySpeed(float speed) {
        this.targetSpeed = speed;
    }

    public void setPitchBias(float pitch, float biasAmount) {
        this.pitchBias = pitch;
        this.pitchBiasAmount = biasAmount;
    }

    protected void updateAutoPathToEntity() {
        double dist = this.theEntity.getDistanceToEntity(this.pathEndEntity);
        if (dist < this.closestDistToTarget - 1.0F) {
            this.closestDistToTarget = ((float) dist);
            this.timeSinceGotCloser = 0;
        } else {
            this.timeSinceGotCloser += 1;
        }

        boolean pathUpdate = false;
        boolean needsPathfinder = false;
        if (this.path != null) {
            double dSq = this.theEntity.getDistanceSqToEntity(this.pathEndEntity);
            if (((this.moveType == INavigationFlying.MoveType.PREFER_FLYING) || ((this.moveType == INavigationFlying.MoveType.MIXED) && (dSq > 100.0D))) && (this.theEntity.canEntityBeSeen(this.pathEndEntity))) {
                this.timeLookingForEntity = 0;
                pathUpdate = true;
            } else {
                double d1 = Distance.distanceBetween(this.pathEndEntity, this.pathEndEntityLastPos);
                double d2 = Distance.distanceBetween((Entity) this.theEntity, this.pathEndEntityLastPos);
                if (d1 / d2 > 0.1D) {
                    pathUpdate = true;
                }
            }

        } else if ((this.moveType == INavigationFlying.MoveType.PREFER_WALKING) || (this.timeSinceGotCloser > 160) || (this.timeLookingForEntity > 600)) {
            pathUpdate = true;
            needsPathfinder = true;
            this.timeSinceGotCloser = 0;
            this.timeLookingForEntity = 500;
        } else if (this.moveType == INavigationFlying.MoveType.MIXED) {
            double dSq = this.theEntity.getDistanceSqToEntity(this.pathEndEntity);
            if (dSq < 100.0D) {
                pathUpdate = true;
            }

        }

        if (pathUpdate) {
            if (this.moveType == INavigationFlying.MoveType.PREFER_FLYING) {
                if (needsPathfinder) {
                    this.theEntity.setPathfindFlying(true);
                    this.path = createPath(this.theEntity, this.pathEndEntity, 0.0F);
                    if (this.path != null) {
                        setWantsToBeFlying(true);
                        setPath(this.path, this.moveSpeed);
                    }

                } else {
                    setWantsToBeFlying(true);
                    resetStatus();
                }
            } else if (this.moveType == INavigationFlying.MoveType.MIXED) {
                this.theEntity.setPathfindFlying(false);
                Path path = createPath(this.theEntity, this.pathEndEntity, 0.0F);
                if ((path != null) && (path.getCurrentPathLength() < dist * 1.8D)) {
                    setWantsToBeFlying(false);
                    setPath(path, this.moveSpeed);
                } else if (needsPathfinder) {
                    this.theEntity.setPathfindFlying(true);
                    path = createPath(this.theEntity, this.pathEndEntity, 0.0F);
                    setWantsToBeFlying(true);
                    if (path != null)
                        setPath(path, this.moveSpeed);
                    else {
                        resetStatus();
                    }
                } else {
                    setWantsToBeFlying(true);
                    resetStatus();
                }
            } else {
                setWantsToBeFlying(false);
                this.theEntity.setPathfindFlying(false);
                Path path = createPath(this.theEntity, this.pathEndEntity, 0.0F);
                if (path != null) {
                    setPath(path, this.moveSpeed);
                }
            }
            this.pathEndEntityLastPos = Vec3.createVectorHelper(this.pathEndEntity.posX, this.pathEndEntity.posY, this.pathEndEntity.posZ);
        }
    }

    public void autoPathToEntity(Entity target) {
        super.autoPathToEntity(target);
        this.isCircling = false;
    }

    public boolean tryMoveToEntity(Entity targetEntity, float targetRadius, float speed) {
        if (this.moveType != INavigationFlying.MoveType.PREFER_WALKING) {
            clearPath();
            this.pathEndEntity = targetEntity;
            this.finalTarget = Vec3.createVectorHelper(this.pathEndEntity.posX, this.pathEndEntity.posY, this.pathEndEntity.posZ);
            this.isCircling = false;
            return true;
        }

        this.theEntity.setPathfindFlying(false);
        return super.tryMoveToEntity(targetEntity, targetRadius, speed);
    }

    public boolean tryMoveToXYZ(double x, double y, double z, float targetRadius, float speed) {
        //Vec3 target = this.theEntity.worldObj.getWorldVec3Pool().getVecFromPool(x, y, z);
        Vec3 target = Vec3.createVectorHelper(x, y, z);
        if (this.moveType != INavigationFlying.MoveType.PREFER_WALKING) {
            clearPath();
            this.finalTarget = Vec3.createVectorHelper(x, y, z);
            this.isCircling = false;
            return true;
        }

        this.theEntity.setPathfindFlying(false);
        return super.tryMoveToXYZ(x, y, z, targetRadius, speed);
    }

    public boolean tryMoveTowardsXZ(double x, double z, int min, int max, int verticalRange, float speed) {
        Vec3 target = findValidPointNear(x, z, min, max, verticalRange);
        if (target != null) {
            return tryMoveToXYZ(target.xCoord, target.yCoord, target.zCoord, 0.0F, speed);
        }
        return false;
    }

    public void clearPath() {
        super.clearPath();
        this.pathEndEntity = null;
        this.isCircling = false;
    }

    public boolean isCircling() {
        return this.isCircling;
    }

    public String getStatus() {
        if (!noPath()) {
            return super.getStatus();
        }
        String s = "";
        if (isAutoPathingToEntity()) {
            s = s + "Auto:";
        }

        s = s + "Flyer:";
        if (this.isCircling) {
            s = s + "Circling:";
        } else if (this.wantsToBeFlying) {
            if (this.theEntity.getFlyState() == FlyState.TAKEOFF)
                s = s + "TakeOff:";
            else {
                s = s + "Flying:";
            }

        } else if ((this.theEntity.getFlyState() == FlyState.LANDING) || (this.theEntity.getFlyState() == FlyState.TOUCHDOWN))
            s = s + "Landing:";
        else {
            s = s + "Ground";
        }
        return s;
    }

    protected void pathFollow() {
        Vec3 vec3d = getEntityPosition();
        int maxNextLeg = this.path.getCurrentPathLength();

        float fa = this.theEntity.width * 0.5F;
        for (int j = this.path.getCurrentPathIndex(); j < maxNextLeg; j++) {
            if (vec3d.squareDistanceTo(this.path.getPositionAtIndex(this.theEntity, j)) < fa * fa)
                this.path.setCurrentPathIndex(j + 1);
        }
    }

    protected void noPathFollow() {
        if ((this.theEntity.getMoveState() != MoveState.FLYING) && (this.theEntity.getAIGoal() == Goal.CHILL)) {
            setWantsToBeFlying(false);
            return;
        }

        if (this.moveType == INavigationFlying.MoveType.PREFER_FLYING)
            setWantsToBeFlying(true);
        else if (this.moveType == INavigationFlying.MoveType.PREFER_WALKING) {
            setWantsToBeFlying(false);
        }
        if (++this.timeSinceVision >= this.visionUpdateRate) {
            this.timeSinceVision = 0;
            if ((!this.precisionTarget) || (this.pathEndEntity == null))
                updateHeading();
            else {
                updateHeadingDirectTarget(this.pathEndEntity);
            }
            this.intermediateTarget = convertToVector(this.targetYaw, this.targetPitch, this.targetSpeed);
        }
        this.theEntity.getMoveHelper().setMoveTo(this.intermediateTarget.xCoord, this.intermediateTarget.yCoord, this.intermediateTarget.zCoord, this.targetSpeed);
    }

    protected Vec3 convertToVector(float yaw, float pitch, float idealSpeed) {
        int time = this.visionUpdateRate + 20;
        double x = this.theEntity.posX + -Math.sin(yaw / 180.0F * 3.141592653589793D) * idealSpeed * time;
        double y = this.theEntity.posY + Math.sin(pitch / 180.0F * 3.141592653589793D) * idealSpeed * time;
        double z = this.theEntity.posZ + Math.cos(yaw / 180.0F * 3.141592653589793D) * idealSpeed * time;
        return Vec3.createVectorHelper(x, y, z);
    }

    protected void updateHeading() {
        float pixelDegreeH = 10.0F;
        float pixelDegreeV = 11.0F;
        for (int i = 0; i < 30; i++) {
            double nextAngleH = i * pixelDegreeH + 0.5D * pixelDegreeH - 150.0D + this.theEntity.rotationYaw;
            for (int j = 0; j < 20; j++) {
                double nextAngleV = j * pixelDegreeV + 0.5D * pixelDegreeV - 110.0D;
                double y = this.theEntity.posY + Math.sin(nextAngleV / 180.0D * 3.141592653589793D) * this.visionDistance;
                double distanceXZ = Math.cos(nextAngleV / 180.0D * 3.141592653589793D) * this.visionDistance;
                double x = this.theEntity.posX + -Math.sin(nextAngleH / 180.0D * 3.141592653589793D) * distanceXZ;
                double z = this.theEntity.posZ + Math.cos(nextAngleH / 180.0D * 3.141592653589793D) * distanceXZ;
                Vec3 target = Vec3.createVectorHelper(x, y, z);
                //Vec3 target = this.theEntity.worldObj.getWorldVec3Pool().getVecFromPool(x, y, z);
                Vec3 origin = this.theEntity.getPosition(1.0F);
                origin.yCoord += 1.0D;

                MovingObjectPosition object = this.theEntity.worldObj.rayTraceBlocks(origin, target);
//				if ((object != null) && (object.typeOfHit == EnumMovingObjectType.TILE)) {
//					double dX = this.theEntity.posX - object.blockX;
//					double dZ = this.theEntity.posY - object.blockY;
//					double dY = this.theEntity.posZ - object.blockZ;
//					this.retina[i][j] = ((float) Math.sqrt(dX * dX + dY * dY + dZ * dZ));
//				} else {
//					this.retina[i][j] = (this.visionDistance + 1.0F);
//				}

            }

        }

        for (int i = 1; i < 29; i++) {
            for (int j = 1; j < 19; j++) {
                float appeal = this.retina[i][j];
                appeal += this.retina[(i - 1)][(j - 1)];
                appeal += this.retina[(i - 1)][j];
                appeal += this.retina[(i - 1)][(j + 1)];
                appeal += this.retina[i][(j - 1)];
                appeal += this.retina[i][(j + 1)];
                appeal += this.retina[(i + 1)][(j - 1)];
                appeal += this.retina[(i + 1)][j];
                appeal += this.retina[(i + 1)][(j + 1)];
                appeal /= 9.0F;
                this.headingAppeal[(i - 1)][(j - 1)] = appeal;
            }

        }

        if (this.isCircling) {
            double dX = this.finalTarget.xCoord - this.theEntity.posX;
            double dY = this.finalTarget.yCoord - this.theEntity.posY;
            double dZ = this.finalTarget.zCoord - this.theEntity.posZ;
            double dXZ = Math.sqrt(dX * dX + dZ * dZ);

            if ((dXZ > 0.0D) && (dXZ > this.circlingRadius * 0.6D)) {
                double intersectRadius = Math.abs((this.circlingRadius - dXZ) * 2.0D) + 8.0D;
                if (intersectRadius > this.circlingRadius * 1.8D) {
                    intersectRadius = dXZ + 5.0D;
                }

                float preferredYaw1 = (float) (Math.acos((dXZ * dXZ - this.circlingRadius * this.circlingRadius + intersectRadius * intersectRadius) / (2.0D * dXZ) / intersectRadius) * 180.0D / 3.141592653589793D);
                float preferredYaw2 = -preferredYaw1;

                double dYaw = Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D;
                preferredYaw1 = (float) (preferredYaw1 + dYaw);
                preferredYaw2 = (float) (preferredYaw2 + dYaw);

                float preferredPitch = (float) (Math.atan((dY + this.circlingHeight) / intersectRadius) * 180.0D / 3.141592653589793D);

                float yawBias = (float) (1.5D * Math.abs(dXZ - this.circlingRadius) / this.circlingRadius);
                float pitchBias = (float) (1.9D * Math.abs((dY + this.circlingHeight) / this.circlingHeight));

                doHeadingBiasPass(this.headingAppeal, preferredYaw1, preferredYaw2, preferredPitch, yawBias, pitchBias);
            } else {
                float yawToTarget = (float) (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D);
                yawToTarget += 180.0F;
                float preferredPitch = (float) (Math.atan((dY + this.circlingHeight) / Math.abs(this.circlingRadius - dXZ)) * 180.0D / 3.141592653589793D);
                float yawBias = (float) (0.5D * Math.abs(dXZ - this.circlingRadius) / this.circlingRadius);
                float pitchBias = (float) (0.9D * Math.abs((dY + this.circlingHeight) / this.circlingHeight));
                doHeadingBiasPass(this.headingAppeal, yawToTarget, yawToTarget, preferredPitch, yawBias, pitchBias);
            }
        } else if (this.pathEndEntity != null) {
            double dX = this.pathEndEntity.posX - this.theEntity.posX;
            double dY = this.pathEndEntity.posY - this.theEntity.posY;
            double dZ = this.pathEndEntity.posZ - this.theEntity.posZ;
            double dXZ = Math.sqrt(dX * dX + dZ * dZ);
            float yawToTarget = (float) (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D);
            float pitchToTarget = (float) (Math.atan(dY / dXZ) * 180.0D / 3.141592653589793D);
            doHeadingBiasPass(this.headingAppeal, yawToTarget, yawToTarget, pitchToTarget, 20.6F, 20.6F);
        }

        if (this.pathEndEntity == null) {
            float dOldYaw = this.targetYaw - this.theEntity.rotationYaw;
            MathUtil.boundAngle180Deg(dOldYaw);
            float dOldPitch = this.targetPitch;
            float approxLastTargetX = dOldYaw / pixelDegreeH + 14.0F;
            float approxLastTargetY = dOldPitch / pixelDegreeV + 9.0F;
            if (approxLastTargetX > 28.0F)
                approxLastTargetX = 28.0F;
            else if (approxLastTargetX < 0.0F) {
                approxLastTargetX = 0.0F;
            }
            if (approxLastTargetY > 18.0F)
                approxLastTargetY = 18.0F;
            else if (approxLastTargetY < 0.0F) {
                approxLastTargetY = 0.0F;
            }
            float statusQuoBias = 0.4F;
            float falloffDist = 30.0F;
            for (int i = 0; i < 28; i++) {
                float dXSq = (approxLastTargetX - i) * (approxLastTargetX - i);
                for (int j = 0; j < 18; j++) {
                    float dY = approxLastTargetY - j;
                    int tmp1306_1304 = j;
                    float[] tmp1306_1303 = this.headingAppeal[i];
                    tmp1306_1303[tmp1306_1304] = ((float) (tmp1306_1303[tmp1306_1304] * (1.0F + statusQuoBias - statusQuoBias * Math.sqrt(dXSq + dY * dY) / falloffDist)));
                }
            }
        }

        if (this.pitchBias != 0.0F) {
            doHeadingBiasPass(this.headingAppeal, 0.0F, 0.0F, this.pitchBias, 0.0F, this.pitchBiasAmount);
        }

        if (!this.wantsToBeFlying) {
            Pair landingInfo = appraiseLanding();
            if (((Float) landingInfo.getVal2()).floatValue() < 4.0F) {
                if (((Float) landingInfo.getVal1()).floatValue() >= 0.9F)
                    doHeadingBiasPass(this.headingAppeal, 0.0F, 0.0F, -45.0F, 0.0F, 3.5F);
                else if (((Float) landingInfo.getVal1()).floatValue() >= 0.65F) {
                    doHeadingBiasPass(this.headingAppeal, 0.0F, 0.0F, -15.0F, 0.0F, 0.4F);
                }

            } else if (((Float) landingInfo.getVal1()).floatValue() >= 0.52F) {
                doHeadingBiasPass(this.headingAppeal, 0.0F, 0.0F, -15.0F, 0.0F, 0.8F);
            }

        }

        Pair bestPixel = chooseCoordinate();
        this.targetYaw = (this.theEntity.rotationYaw - 150.0F + (((Integer) bestPixel.getVal1()).intValue() + 1) * pixelDegreeH + 0.5F * pixelDegreeH);
        this.targetPitch = (-110.0F + (((Integer) bestPixel.getVal2()).intValue() + 1) * pixelDegreeV + 0.5F * pixelDegreeV);
    }

    protected void updateHeadingDirectTarget(Entity target) {
        double dX = target.posX - this.theEntity.posX;
        double dY = target.posY - this.theEntity.posY;
        double dZ = target.posZ - this.theEntity.posZ;
        double dXZ = Math.sqrt(dX * dX + dZ * dZ);
        this.targetYaw = ((float) (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D));
        this.targetPitch = ((float) (Math.atan(dY / dXZ) * 180.0D / 3.141592653589793D));
    }

    protected Pair<Integer, Integer> chooseCoordinate() {
        int bestPixelX = 0;
        int bestPixelY = 0;
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 18; j++) {
                if (this.headingAppeal[bestPixelX][bestPixelY] < this.headingAppeal[i][j]) {
                    bestPixelX = i;
                    bestPixelY = j;
                }
            }
        }
        return new Pair(Integer.valueOf(bestPixelX), Integer.valueOf(bestPixelY));
    }

    protected void setTarget(double x, double y, double z) {
        this.intermediateTarget = Vec3.createVectorHelper(x, y, z);
    }

    protected Vec3 getTarget() {
        return this.intermediateTarget;
    }

    protected void doHeadingBiasPass(float[][] array, float preferredYaw1, float preferredYaw2, float preferredPitch, float yawBias, float pitchBias) {
        float pixelDegreeH = 10.0F;
        float pixelDegreeV = 11.0F;
        for (int i = 0; i < array.length; i++) {
            double nextAngleH = (i + 1) * pixelDegreeH + 0.5D * pixelDegreeH - 150.0D + this.theEntity.rotationYaw;
            double dYaw1 = MathUtil.boundAngle180Deg(preferredYaw1 - nextAngleH);
            double dYaw2 = MathUtil.boundAngle180Deg(preferredYaw2 - nextAngleH);
            double yawBiasAmount = 1.0D + Math.min(Math.abs(dYaw1), Math.abs(dYaw2)) * yawBias / 180.0D;
            for (int j = 0; j < array[0].length; j++) {
                double nextAngleV = (j + 1) * pixelDegreeV + 0.5D * pixelDegreeV - 110.0D;
                double pitchBiasAmount = 1.0D + Math.abs(MathUtil.boundAngle180Deg(preferredPitch - nextAngleV)) * pitchBias / 180.0D;
                int tmp162_160 = j;
                float[] tmp162_159 = array[i];
                tmp162_159[tmp162_160] = ((float) (tmp162_159[tmp162_160] / (yawBiasAmount * pitchBiasAmount)));
            }
        }
    }

    private void setWantsToBeFlying(boolean flag) {
        this.wantsToBeFlying = flag;
        this.theEntity.getMoveHelper().setWantsToBeFlying(flag);
    }

    private Pair<Float, Float> appraiseLanding() {
        float safety = 0.0F;
        float distance = 0.0F;
        int landingResolution = 3;
        double nextAngleH = this.theEntity.rotationYaw;
        for (int i = 0; i < landingResolution; i++) {
            double nextAngleV = -90 + i * 30 / landingResolution;
            double y = this.theEntity.posY + Math.sin(nextAngleV / 180.0D * 3.141592653589793D) * 64.0D;
            double distanceXZ = Math.cos(nextAngleV / 180.0D * 3.141592653589793D) * 64.0D;
            double x = this.theEntity.posX + -Math.sin(nextAngleH / 180.0D * 3.141592653589793D) * distanceXZ;
            double z = this.theEntity.posZ + Math.cos(nextAngleH / 180.0D * 3.141592653589793D) * distanceXZ;
            //Vec3 target = this.theEntity.worldObj.getWorldVec3Pool().getVecFromPool(x, y, z);
            Vec3 target = Vec3.createVectorHelper(x, y, z);
            Vec3 origin = this.theEntity.getPosition(1.0F);
            MovingObjectPosition object = this.theEntity.worldObj.rayTraceBlocks(origin, target);
            if (object != null) {
                Block Block = this.theEntity.worldObj.getBlock(object.blockX, object.blockY, object.blockZ);
                if (!this.theEntity.avoidsBlock(Block)) {
                    safety += 0.7F;
                }
                if (object.sideHit == 1) {
                    safety += 0.3F;
                }
                double dX = object.blockX - this.theEntity.posX;
                double dY = object.blockY - this.theEntity.posY;
                double dZ = object.blockZ - this.theEntity.posZ;
                distance = (float) (distance + Math.sqrt(dX * dX + dY * dY + dZ * dZ));
            } else {
                distance += 64.0F;
            }
        }
        distance /= landingResolution;
        safety /= landingResolution;
        return new Pair(Float.valueOf(safety), Float.valueOf(distance));
    }
}
