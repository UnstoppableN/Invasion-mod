package invmod.common.entity.ai;

//NOOB HAUS: Done 

import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMWolf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class EntityAICreeperIMSwell extends EntityAIBase {
    EntityIMCreeper theEntity;
    EntityLivingBase targetEntity;

    public EntityAICreeperIMSwell(EntityIMCreeper par1EntityCreeper) {
        this.theEntity = par1EntityCreeper;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entityliving = this.theEntity.getAttackTarget();

        return (this.theEntity.getCreeperState() > 0) || ((entityliving != null) && (this.theEntity.getDistanceSqToEntity(entityliving) < 9.0D) && ((entityliving.getClass() == EntityPlayer.class) || (entityliving.getClass() == EntityIMWolf.class) || (entityliving.getClass() == EntityPlayerMP.class)));
    }

    @Override
    public void startExecuting() {
        this.theEntity.getNavigatorNew().clearPath();
        this.targetEntity = this.theEntity.getAttackTarget();
    }

    @Override
    public void resetTask() {
        this.targetEntity = null;
    }

    @Override
    public void updateTask() {
        if (this.targetEntity == null) {
            this.theEntity.setCreeperState(-1);
            return;
        }

        if (this.theEntity.getDistanceSqToEntity(this.targetEntity) > 49.0D) {
            this.theEntity.setCreeperState(-1);
            return;
        }

        if (!this.theEntity.getEntitySenses().canSee(this.targetEntity)) {
            this.theEntity.setCreeperState(-1);
            return;
        }
        this.theEntity.setCreeperState(1);
    }
}