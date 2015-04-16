package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIMoveToEntity<T extends EntityLivingBase> extends EntityAIBase {
    private EntityIMLiving theEntity;
    private T targetEntity;
    private Class<? extends T> targetClass;
    private boolean targetMoves;
    private double lastX;
    private double lastY;
    private double lastZ;
    private int pathRequestTimer;
    private int pathFailedCount;

    public EntityAIMoveToEntity(EntityIMLiving entity) {
        this(entity, (Class<? extends T>) EntityLivingBase.class);
    }

    public EntityAIMoveToEntity(EntityIMLiving entity, Class<? extends T> target) {
        this.targetClass = target;
        this.theEntity = entity;
        this.targetMoves = false;
        this.pathRequestTimer = 0;
        this.pathFailedCount = 0;
        setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (--this.pathRequestTimer <= 0) {
            EntityLivingBase target = this.theEntity.getAttackTarget();
            if ((target != null) && (this.targetClass.isAssignableFrom(this.theEntity.getAttackTarget().getClass()))) {
                this.targetEntity = (T) ((EntityLivingBase) this.targetClass.cast(target));
                return true;
            }
        }
        return false;
    }

    public boolean continueExecuting() {
        EntityLivingBase target = this.theEntity.getAttackTarget();
        if ((target != null) && (target == this.targetEntity)) {
            return true;
        }
        return false;
    }

    public void startExecuting() {
        this.targetMoves = true;
        setPath();
    }

    public void resetTask() {
        this.targetMoves = false;
    }

    public void updateTask() {
        if ((--this.pathRequestTimer <= 0) && (!this.theEntity.getNavigatorNew().isWaitingForTask()) && (this.targetMoves) && (this.targetEntity.getDistanceSq(this.lastX, this.lastY, this.lastZ) > 1.8D)) {
            setPath();
        }
        if (this.pathFailedCount > 3) {
            this.theEntity.getMoveHelper().setMoveTo(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ, this.theEntity.getMoveSpeedStat());
        }
    }

    protected void setTargetMoves(boolean flag) {
        this.targetMoves = flag;
    }

    protected EntityIMLiving getEntity() {
        return this.theEntity;
    }

    protected T getTarget() {
        return this.targetEntity;
    }

    protected void setPath() {
        if (this.theEntity.getNavigatorNew().tryMoveToEntity(this.targetEntity, 0.0F, this.theEntity.getMoveSpeedStat())) {
            if (this.theEntity.getNavigatorNew().getLastPathDistanceToTarget() > 3.0F) {
                this.pathRequestTimer = (30 + this.theEntity.worldObj.rand.nextInt(10));
                if (this.theEntity.getNavigatorNew().getPath().getCurrentPathLength() > 2)
                    this.pathFailedCount = 0;
                else
                    this.pathFailedCount += 1;
            } else {
                this.pathRequestTimer = (10 + this.theEntity.worldObj.rand.nextInt(10));
                this.pathFailedCount = 0;
            }
        } else {
            this.pathFailedCount += 1;
            this.pathRequestTimer = (40 * this.pathFailedCount + this.theEntity.worldObj.rand.nextInt(10));
        }

        this.lastX = this.targetEntity.posX;
        this.lastY = this.targetEntity.posY;
        this.lastZ = this.targetEntity.posZ;
    }
}