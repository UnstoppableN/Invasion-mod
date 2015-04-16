package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.util.ComparatorEntityDistanceFrom;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityAISimpleTarget extends EntityAIBase {
    private final EntityIMLiving theEntity;
    private EntityLivingBase targetEntity;
    private Class<? extends EntityLivingBase> targetClass;
    private int outOfLosTimer;
    private float distance;
    private boolean needsLos;

    public EntityAISimpleTarget(EntityIMLiving entity, Class<? extends EntityLivingBase> targetType, float distance) {
        this(entity, targetType, distance, true);
    }

    public EntityAISimpleTarget(EntityIMLiving entity, Class<? extends EntityLivingBase> targetType, float distance, boolean needsLoS) {
        this.theEntity = entity;
        this.targetClass = targetType;
        this.outOfLosTimer = 0;
        this.distance = distance;
        this.needsLos = needsLoS;
        setMutexBits(1);
    }

    public EntityIMLiving getEntity() {
        return this.theEntity;
    }

    public boolean shouldExecute() {
        if (this.targetClass == EntityPlayer.class) {
            EntityPlayer entityplayer = this.theEntity.worldObj.getClosestVulnerablePlayerToEntity(this.theEntity, this.distance);
            if (isValidTarget(entityplayer)) {
                this.targetEntity = entityplayer;
                return true;
            }
        }

        List list = this.theEntity.worldObj.getEntitiesWithinAABB(this.targetClass, this.theEntity.boundingBox.expand(this.distance, this.distance / 2.0F, this.distance));
        Comparator comp = new ComparatorEntityDistanceFrom(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
        Collections.sort(list, comp);

        boolean foundEntity = false;
        while (list.size() > 0) {
            EntityLivingBase entity = (EntityLivingBase) list.remove(list.size() - 1);
            if (isValidTarget(entity)) {
                this.targetEntity = entity;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase entityliving = this.theEntity.getAttackTarget();
        if (entityliving == null) {
            return false;
        }
        if (!entityliving.isEntityAlive()) {
            return false;
        }
        if (this.theEntity.getDistanceSqToEntity(entityliving) > this.distance * this.distance) {
            return false;
        }
        if (this.needsLos) {
            if (!this.theEntity.getEntitySenses().canSee(entityliving)) {
                if (++this.outOfLosTimer > 60) {
                    return false;
                }
            } else {
                this.outOfLosTimer = 0;
            }
        }

        return true;
    }

    @Override
    public void startExecuting() {
        this.theEntity.setAttackTarget(this.targetEntity);
        this.outOfLosTimer = 0;
    }

    @Override
    public void resetTask() {
        this.theEntity.setAttackTarget(null);
    }

    public Class<? extends EntityLivingBase> getTargetType() {
        return this.targetClass;
    }

    public float getAggroRange() {
        return this.distance;
    }

    protected void setTarget(EntityLivingBase entity) {
        this.targetEntity = entity;
    }

    protected boolean isValidTarget(EntityLivingBase entity) {
        if (entity == null) {
            return false;
        }
        if (entity == this.theEntity) {
            return false;
        }
        if (!entity.isEntityAlive()) {
            return false;
        }

        //players in creative mode won't be targeted 
        if (this.targetClass == EntityPlayer.class) {
            if (((EntityPlayer) entity).capabilities.disableDamage) {
                return false;
            }
        }

        if ((this.needsLos) && (!this.theEntity.getEntitySenses().canSee(entity))) {
            return false;
        }
        return true;
    }
}