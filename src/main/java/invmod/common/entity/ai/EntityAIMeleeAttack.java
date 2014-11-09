package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.Goal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIMeleeAttack<T extends EntityLivingBase> extends EntityAIBase {
	private EntityIMLiving theEntity;
	private Class<? extends T> targetClass;
	private float attackRange;
	private int attackDelay;
	private int nextAttack;

	public EntityAIMeleeAttack(EntityIMLiving entity, Class<? extends T> targetClass, int attackDelay) {
		this.theEntity = entity;
		this.targetClass = targetClass;
		this.attackDelay = attackDelay;
		this.attackRange = 0.6F;
		this.nextAttack = 0;
	}

	public boolean shouldExecute() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		return (target != null) && (this.theEntity.getAIGoal() == Goal.MELEE_TARGET) && (this.theEntity.getDistanceToEntity(target) < (this.attackRange + this.theEntity.width + target.width) * 4.0F) && (target.getClass().isAssignableFrom(this.targetClass));
	}

	public void updateTask() {
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (canAttackEntity(target)) {
			attackEntity(target);
		}
		setAttackTime(getAttackTime() - 1);
	}

	public Class<? extends T> getTargetClass() {
		return this.targetClass;
	}

	protected void attackEntity(EntityLivingBase target) {
		this.theEntity.attackEntityAsMob(target);
		setAttackTime(getAttackDelay());
	}

	protected boolean canAttackEntity(EntityLivingBase target) {
		if (getAttackTime() <= 0) {
			double d = this.theEntity.width + this.attackRange;
			return this.theEntity.getDistanceSq(target.posX, target.boundingBox.minY, target.posZ) < d * d;
		}
		return false;
	}

	protected int getAttackTime() {
		return this.nextAttack;
	}

	protected void setAttackTime(int time) {
		this.nextAttack = time;
	}

	protected int getAttackDelay() {
		return this.attackDelay;
	}

	protected void setAttackDelay(int time) {
		this.attackDelay = time;
	}
}