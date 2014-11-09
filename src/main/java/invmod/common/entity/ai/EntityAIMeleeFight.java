package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIMeleeFight<T extends EntityLivingBase> extends EntityAIMeleeAttack<T> {
	private EntityIMLiving theEntity;
	private int time;
	private float startingHealth;
	private int damageDealt;
	private int invulnCount;
	private float retreatHealthLossPercent;

	public EntityAIMeleeFight(EntityIMLiving entity, Class<? extends T> targetClass, int attackDelay, float retreatHealthLossPercent) {
		super(entity, targetClass, attackDelay);
		this.theEntity = entity;
		this.time = 0;
		this.startingHealth = 0.0F;
		this.damageDealt = 0;
		this.invulnCount = 0;
		this.retreatHealthLossPercent = retreatHealthLossPercent;
	}

	public boolean shouldExecute() {
		Entity target = this.theEntity.getAttackTarget();
		return (this.theEntity.getAIGoal() == Goal.MELEE_TARGET) && (target != null) && (target.getClass().isAssignableFrom(getTargetClass()));
	}

	public boolean continueExecuting() {
		return ((this.theEntity.getAIGoal() == Goal.MELEE_TARGET) || (isWaitingForTransition())) && (this.theEntity.getAttackTarget() != null);
	}

	public void startExecuting() {
		this.time = 0;
		this.startingHealth = this.theEntity.getHealth();
		this.damageDealt = 0;
		this.invulnCount = 0;
	}

	public void updateTask() {
		updateDisengage();
		updatePath();
		super.updateTask();
		if ((this.damageDealt > 0) || (this.startingHealth - this.theEntity.getHealth() > 0.0F))
			this.time += 1;
	}

	public void updatePath() {
		INavigation nav = this.theEntity.getNavigatorNew();
		if (this.theEntity.getAttackTarget() != nav.getTargetEntity()) {
			nav.clearPath();
			nav.autoPathToEntity(this.theEntity.getAttackTarget());
		}
	}

	protected void updateDisengage() {
		if ((this.theEntity.getAIGoal() == Goal.MELEE_TARGET) && (shouldLeaveMelee())) {
			this.theEntity.transitionAIGoal(Goal.LEAVE_MELEE);
		}
	}

	protected boolean isWaitingForTransition() {
		return (this.theEntity.getAIGoal() == Goal.LEAVE_MELEE) && (this.theEntity.getPrevAIGoal() == Goal.MELEE_TARGET);
	}

	protected void attackEntity(EntityLivingBase target) {
		float h = target.getHealth();
		super.attackEntity(target);
		h -= target.getHealth();
		if (h <= 0.0F) {
			this.invulnCount += 1;
		}
		this.damageDealt = ((int) (this.damageDealt + h));
	}

	protected boolean shouldLeaveMelee() {
		float damageReceived = this.startingHealth - this.theEntity.getHealth();
		if ((this.time > 40) && (damageReceived > this.theEntity.getMaxHealth() * this.retreatHealthLossPercent)) {
			return true;
		}
		if ((this.time > 100) && (damageReceived - this.damageDealt > this.theEntity.getMaxHealth() * 0.66F * this.retreatHealthLossPercent)) {
			return true;
		}
		if (this.invulnCount >= 2) {
			return true;
		}
		return false;
	}
}
