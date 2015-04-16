package invmod.common.entity;

import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class IMMoveHelperFlying extends IMMoveHelper {
    private EntityIMFlying a;
    private double targetFlySpeed;
    private boolean wantsToBeFlying;

    public IMMoveHelperFlying(EntityIMFlying entity) {
        super(entity);
        this.a = entity;
        this.wantsToBeFlying = false;
    }

    public void setHeading(float yaw, float pitch, float idealSpeed, int time) {
        double x = this.a.posX + Math.sin(yaw / 180.0F * 3.141592653589793D) * idealSpeed * time;
        double y = this.a.posY + Math.sin(pitch / 180.0F * 3.141592653589793D) * idealSpeed * time;
        double z = this.a.posZ + Math.cos(yaw / 180.0F * 3.141592653589793D) * idealSpeed * time;
        setMoveTo(x, y, z, idealSpeed);
    }

    public void setWantsToBeFlying(boolean flag) {
        this.wantsToBeFlying = flag;
    }

    public void onUpdateMoveHelper() {
        this.a.setMoveForward(0.0F);
        this.a.setFlightAccelerationVector(0.0F, 0.0F, 0.0F);
        if ((!this.needsUpdate) && (this.a.getMoveState() != MoveState.FLYING)) {
            this.a.setMoveState(MoveState.STANDING);
            this.a.setFlyState(FlyState.GROUNDED);
            this.a.rotationPitch = correctRotation(this.a.rotationPitch, 50.0F, 4.0F);
            return;
        }
        this.needsUpdate = false;

        if (this.wantsToBeFlying) {
            if (this.a.getFlyState() == FlyState.GROUNDED) {
                this.a.setMoveState(MoveState.RUNNING);
                this.a.setFlyState(FlyState.TAKEOFF);
            } else if (this.a.getFlyState() == FlyState.FLYING) {
                this.a.setMoveState(MoveState.FLYING);
            }

        } else if (this.a.getFlyState() == FlyState.FLYING) {
            this.a.setFlyState(FlyState.LANDING);
        }

        if (this.a.getFlyState() == FlyState.FLYING) {
            FlyState result = doFlying();
            if (result == FlyState.GROUNDED)
                this.a.setMoveState(MoveState.STANDING);
            else if (result == FlyState.FLYING) {
                this.a.setMoveState(MoveState.FLYING);
            }
            this.a.setFlyState(result);
        } else if (this.a.getFlyState() == FlyState.TAKEOFF) {
            FlyState result = doTakeOff();
            if (result == FlyState.GROUNDED)
                this.a.setMoveState(MoveState.STANDING);
            else if (result == FlyState.TAKEOFF)
                this.a.setMoveState(MoveState.RUNNING);
            else if (result == FlyState.FLYING) {
                this.a.setMoveState(MoveState.FLYING);
            }
            this.a.setFlyState(result);
        } else if ((this.a.getFlyState() == FlyState.LANDING) || (this.a.getFlyState() == FlyState.TOUCHDOWN)) {
            FlyState result = doLanding();
            if ((result == FlyState.GROUNDED) || (result == FlyState.TOUCHDOWN)) {
                this.a.setMoveState(MoveState.RUNNING);
            }
            this.a.setFlyState(result);
        } else {
            MoveState result = doGroundMovement();
            this.a.setMoveState(result);
        }
    }

    protected MoveState doGroundMovement() {
        this.a.setGroundFriction(0.6F);
        this.a.setRotationRoll(correctRotation(this.a.getRotationRoll(), 0.0F, 6.0F));
        this.targetSpeed = this.a.getMoveSpeedStat();
        this.a.rotationPitch = correctRotation(this.a.rotationPitch, 50.0F, 4.0F);
        return super.doGroundMovement();
    }

    protected FlyState doFlying() {
        this.targetFlySpeed = this.setSpeed;
        return fly();
    }

    protected FlyState fly() {
        this.a.setGroundFriction(1.0F);
        boolean isInLiquid = (this.a.isInWater()) || (this.a.handleLavaMovement());
        double dX = this.b - this.a.posX;
        double dZ = this.d - this.a.posZ;
        double dY = this.c - this.a.posY;

        double dXZSq = dX * dX + dZ * dZ;
        double dXZ = Math.sqrt(dXZSq);
        double distanceSquared = dXZSq + dY * dY;

        if (distanceSquared > 0.04D) {
            int timeToTurn = 10;
            float gravity = this.a.getGravity();
            float liftConstant = gravity;
            double xAccel = 0.0D;
            double yAccel = 0.0D;
            double zAccel = 0.0D;
            double velX = this.a.motionX;
            double velY = this.a.motionY;
            double velZ = this.a.motionZ;
            double hSpeedSq = velX * velX + velZ * velZ;
            if (hSpeedSq == 0.0D)
                hSpeedSq = 1.0E-008D;
            double horizontalSpeed = Math.sqrt(hSpeedSq);
            double flySpeed = Math.sqrt(hSpeedSq + velY * velY);

            double desiredYVelocity = dY / timeToTurn;
            double dVelY = desiredYVelocity - (velY - gravity);

            float minFlightSpeed = 0.05F;
            if (flySpeed < minFlightSpeed) {
                float newYaw = (float) (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D);
                newYaw = correctRotation(this.a.rotationYaw, newYaw, this.a.getTurnRate());
                this.a.rotationYaw = newYaw;
                if (this.a.onGround) {
                    return FlyState.GROUNDED;
                }
            } else {
                double liftForce = flySpeed / (this.a.getMaxPoweredFlightSpeed() * this.a.getLiftFactor()) * liftConstant;
                double climbForce = liftForce * horizontalSpeed / (Math.abs(velY) + horizontalSpeed);
                double forwardForce = liftForce * Math.abs(velY) / (Math.abs(velY) + horizontalSpeed);
                double turnForce = liftForce;
                double climbAccel;
                if (dVelY < 0.0D) {
                    double maxDiveForce = this.a.getMaxTurnForce() - gravity;
                    climbAccel = -Math.min(Math.min(climbForce, maxDiveForce), -dVelY);
                } else {
                    double maxClimbForce = this.a.getMaxTurnForce() + gravity;
                    climbAccel = Math.min(Math.min(climbForce, maxClimbForce), dVelY);
                }

                float minBankForce = 0.01F;
                if (turnForce < minBankForce) {
                    turnForce = minBankForce;
                }

                double desiredXZHeading = Math.atan2(dZ, dX) - 1.570796326794897D;
                double currXZHeading = Math.atan2(velZ, velX) - 1.570796326794897D;
                double dXZHeading = desiredXZHeading - currXZHeading;
                while (dXZHeading >= 3.141592653589793D)
                    dXZHeading -= 6.283185307179586D;
                while (dXZHeading < -3.141592653589793D)
                    dXZHeading += 6.283185307179586D;
                double bankForce = horizontalSpeed * dXZHeading / timeToTurn;
                double maxBankForce = Math.min(turnForce, this.a.getMaxTurnForce());
                if (bankForce > maxBankForce)
                    bankForce = maxBankForce;
                else if (bankForce < -maxBankForce) {
                    bankForce = -maxBankForce;
                }

                double bankXAccel = bankForce * -velZ / horizontalSpeed;
                double bankZAccel = bankForce * velX / horizontalSpeed;

                double totalForce = xAccel + yAccel + zAccel;

                double r = liftForce / totalForce;
                xAccel += bankXAccel;
                yAccel += climbAccel;
                zAccel += bankZAccel;
                velX += bankXAccel;
                velY += climbAccel;
                velZ += bankZAccel;

                double dYAccelGravity = yAccel - gravity;
                double middlePitch = 15.0D;
                double newPitch;
                if (velY - gravity < 0.0D) {
                    double climbForceRatio = yAccel / climbForce;
                    if (climbForceRatio > 1.0D)
                        climbForceRatio = 1.0D;
                    else if (climbForceRatio < -1.0D) {
                        climbForceRatio = -1.0D;
                    }
                    double xzSpeed = Math.sqrt(velX * velX + velZ * velZ);
                    double velPitch;
                    if (xzSpeed > 0.0D)
                        velPitch = Math.atan(velY / xzSpeed) / 3.141592653589793D * 180.0D;
                    else {
                        velPitch = -180.0D;
                    }
                    double pitchInfluence = (this.a.getMaxPoweredFlightSpeed() - Math.abs(velY)) / this.a.getMaxPoweredFlightSpeed();
                    if (pitchInfluence < 0.0D) {
                        pitchInfluence = 0.0D;
                    }
                    newPitch = velPitch + 15.0D * climbForceRatio * pitchInfluence;
                } else {
                    double pitchLimit = this.a.getMaxPitch();
                    double climbForceRatio = Math.min(yAccel / climbForce, 1.0D);
                    newPitch = middlePitch + (pitchLimit - middlePitch) * climbForceRatio;
                }
                newPitch = correctRotation(this.a.rotationPitch, (float) newPitch, 1.5F);
                double newYaw = Math.atan2(velZ, velX) * 180.0D / 3.141592653589793D - 90.0D;
                newYaw = correctRotation(this.a.rotationYaw, (float) newYaw, this.a.getTurnRate());
                this.a.setPositionAndRotation(this.a.posX, this.a.posY, this.a.posZ, (float) newYaw, (float) newPitch);
                double newRoll = 60.0D * bankForce / turnForce;
                this.a.setRotationRoll(correctRotation(this.a.getRotationRoll(), (float) newRoll, 6.0F));
                double horizontalForce;
                if (velY > 0.0D) {
                    horizontalForce = -climbAccel;
                } else {
                    horizontalForce = forwardForce;
                }
                int xDirection = velX > 0.0D ? 1 : -1;
                int zDirection = velZ > 0.0D ? 1 : -1;
                double hComponentX = xDirection * velX / (xDirection * velX + zDirection * velZ);

                double xLiftAccel = xDirection * horizontalForce * hComponentX;
                double zLiftAccel = zDirection * horizontalForce * (1.0D - hComponentX);

                double loss = 0.4D;
                xLiftAccel += xDirection * -Math.abs(bankForce * loss) * hComponentX;
                zLiftAccel += zDirection * -Math.abs(bankForce * loss) * (1.0D - hComponentX);

                xAccel += xLiftAccel;
                zAccel += zLiftAccel;
            }

            if (flySpeed < this.targetFlySpeed) {
                this.a.setThrustEffort(0.6F);
                if (!this.a.isThrustOn()) {
                    this.a.setThrustOn(true);
                }
                double desiredVThrustRatio = (dVelY - yAccel) / this.a.getThrust();
                Vec3 thrust = calcThrust(desiredVThrustRatio);
                xAccel += thrust.xCoord;
                yAccel += thrust.yCoord;
                zAccel += thrust.zCoord;
            } else if (flySpeed > this.targetFlySpeed * 1.8D) {
                this.a.setThrustEffort(1.0F);
                if (!this.a.isThrustOn()) {
                    this.a.setThrustOn(true);
                }
                double desiredVThrustRatio = (dVelY - yAccel) / (this.a.getThrust() * 10.0F);
                Vec3 thrust = calcThrust(desiredVThrustRatio);
                xAccel += -thrust.xCoord;
                yAccel += thrust.yCoord;
                zAccel += -thrust.zCoord;
            } else if (this.a.isThrustOn()) {
                this.a.setThrustOn(false);
            }

            this.a.setFlightAccelerationVector((float) xAccel, (float) yAccel, (float) zAccel);
        }
        return FlyState.FLYING;
    }

    protected FlyState doTakeOff() {
        this.a.setGroundFriction(0.98F);
        this.a.setThrustOn(true);
        this.a.setThrustEffort(1.0F);
        this.targetSpeed = this.a.getMoveSpeedStat();

        MoveState result = doGroundMovement();
        if (result == MoveState.STANDING) {
            return FlyState.GROUNDED;
        }
        if (this.a.isCollidedHorizontally) {
            this.a.getJumpHelper().setJumping();
        }
        Vec3 thrust = calcThrust(0.0D);
        this.a.setFlightAccelerationVector((float) thrust.xCoord, (float) thrust.yCoord, (float) thrust.zCoord);
        double speed = Math.sqrt(this.a.motionX * this.a.motionX + this.a.motionY * this.a.motionY + this.a.motionZ * this.a.motionZ);

        this.a.rotationPitch = correctRotation(this.a.rotationPitch, 40.0F, 4.0F);

        float gravity = this.a.getGravity();
        float liftConstant = gravity;
        double liftForce = speed / (this.a.getMaxPoweredFlightSpeed() * this.a.getLiftFactor()) * liftConstant;

        if (liftForce > gravity) {
            return FlyState.FLYING;
        }
        return FlyState.TAKEOFF;
    }

    protected FlyState doLanding() {
        this.a.setGroundFriction(0.3F);
        int x = MathHelper.floor_double(this.a.posX);
        int y = MathHelper.floor_double(this.a.posY);
        int z = MathHelper.floor_double(this.a.posZ);

        for (int i = 1; i < 5; i++) {
            if (this.a.worldObj.getBlock(x, y - i, z) != Blocks.air)
                break;
            this.targetFlySpeed = (this.setSpeed * (0.66F - (0.4F - (i - 1) * 0.133F)));
        }

        FlyState result = fly();
        this.a.setThrustOn(true);
        if (result == FlyState.FLYING) {
            double speed = Math.sqrt(this.a.motionX * this.a.motionX + this.a.motionY * this.a.motionY + this.a.motionZ * this.a.motionZ);
            if (this.a.onGround) {
                if (speed < this.a.getLandingSpeedThreshold()) {
                    return FlyState.GROUNDED;
                }

                this.a.setRotationRoll(correctRotation(this.a.getRotationRoll(), 40.0F, 6.0F));
                return FlyState.TOUCHDOWN;
            }
        }

        return FlyState.LANDING;
    }

    protected Vec3 calcThrust(double desiredVThrustRatio) {
        float thrust = this.a.getThrust();
        float rMin = this.a.getThrustComponentRatioMin();
        float rMax = this.a.getThrustComponentRatioMax();
        double vThrustRatio = desiredVThrustRatio;
        if (vThrustRatio > rMax)
            vThrustRatio = rMax;
        else if (vThrustRatio < rMin) {
            vThrustRatio = rMin;
        }
        double hThrust = (1.0D - vThrustRatio) * thrust;
        double vThrust = vThrustRatio * thrust;
        double xAccel = hThrust * -Math.sin(this.a.rotationYaw / 180.0F * 3.141592653589793D);
        double yAccel = vThrust;
        double zAccel = hThrust * Math.cos(this.a.rotationYaw / 180.0F * 3.141592653589793D);
        //Vec3 vec = this.a.worldObj.getWorldVec3Pool().getVecFromPool(xAccel, yAccel, zAccel);
        Vec3 vec = Vec3.createVectorHelper(xAccel, yAccel, zAccel);
        return vec;
    }
}