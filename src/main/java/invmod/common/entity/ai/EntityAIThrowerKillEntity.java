package invmod.common.entity.ai;

import invmod.common.entity.EntityIMThrower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class EntityAIThrowerKillEntity<T extends EntityLivingBase> extends EntityAIKillEntity<T> {
    private final EntityIMThrower theEntity;
    private boolean melee;
    private float attackRangeSq;
    private float launchSpeed;
    private Random rand;
    private int maxBoulderAmount;

    public EntityAIThrowerKillEntity(EntityIMThrower entity, Class<? extends T> targetClass, int attackDelay, float throwRange, float launchSpeed) {
        super(entity, targetClass, attackDelay);
        this.rand = new Random();
        this.attackRangeSq = (throwRange * throwRange);
        this.launchSpeed = launchSpeed;
        this.theEntity = entity;
        maxBoulderAmount = 3;
    }

    @Override
    protected void attackEntity(Entity target) {
        if (this.melee) {
            setAttackTime(getAttackDelay());
            super.attackEntity(target);
        } else {
            setAttackTime(getAttackDelay() * 2);
            int distance = Math.round((float) this.theEntity.getDistance(target.posX, target.posY, target.posZ));
            int missDistance = Math.round((float) Math.ceil(distance / 10));

            for (int i = 1; i <= this.rand.nextInt(maxBoulderAmount); i++) {
                double x = (target.posX - missDistance) + (double) this.rand.nextInt((missDistance + 1) * 2);
                double y = (target.posY - missDistance + 1) + (double) this.rand.nextInt((missDistance + 1) * 2);
                double z = (target.posZ - missDistance) + (double) this.rand.nextInt((missDistance + 1) * 2);

                if (this.theEntity.getTier() == 1) {
                    this.theEntity.throwBoulder(x, y, z);
                } else {
                    this.theEntity.throwTNT(x, y, z);
                }
            }
        }
    }

    @Override
    protected boolean canAttackEntity(Entity target) {
        this.melee = super.canAttackEntity(target);
        if (this.melee) {
            return true;
        }
        if ((!this.theEntity.canThrow())) {
            return false;
        }

        double dX = this.theEntity.posX - target.posX;
        double dZ = this.theEntity.posZ - target.posZ;
        double dXY = MathHelper.sqrt_double(dX * dX + dZ * dZ);
        return (getAttackTime() <= 0) && (this.theEntity.getEntitySenses().canSee(target)) && (0.025D * dXY / (this.launchSpeed * this.launchSpeed) <= 1.0D);
    }
}