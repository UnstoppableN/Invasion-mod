package invmod.common.entity.ai;

import invmod.common.entity.EntityIMBird;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIWingAttack extends EntityAIMeleeAttack {
	private EntityIMBird theEntity;

	public EntityAIWingAttack(EntityIMBird entity, Class<? extends EntityLivingBase> targetClass, int attackDelay) {
		super(entity, targetClass, attackDelay);
		this.theEntity = entity;
	}

	public void updateTask() {
		if (getAttackTime() == 0) {
			this.theEntity.setAttackingWithWings(isInStartMeleeRange());
		}
		super.updateTask();
	}

	public void resetTask() {
		this.theEntity.setAttackingWithWings(false);
	}

	protected boolean isInStartMeleeRange() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (target == null) {
			return false;
		}
		double d = this.theEntity.width + this.theEntity.getAttackRange() + 3.0D;
		return this.theEntity.getDistanceSq(target.posX, target.boundingBox.minY, target.posZ) < d * d;
	}
}