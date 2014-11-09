package invmod.common.entity.ai;

import invmod.common.entity.EntityIMBird;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.Path;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityAIBirdFight<T extends EntityLivingBase> extends EntityAIMeleeFight<T> 
{
	private EntityIMBird theEntity;
	private boolean wantsToRetreat;
	private boolean buffetedTarget;

	public EntityAIBirdFight(EntityIMBird entity, Class<? extends T> targetClass, int attackDelay, float retreatHealthLossPercent) 
	{
		super(entity, targetClass, attackDelay, retreatHealthLossPercent);
		this.theEntity = entity;
		this.wantsToRetreat = false;
		this.buffetedTarget = false;
	}

	public void updateTask() 
	{
		if (getAttackTime() == 0) 
		{
			this.theEntity.setAttackingWithWings(isInStartMeleeRange());
		}
		super.updateTask();
	}

	public void resetTask() 
	{
		this.theEntity.setAttackingWithWings(false);
		super.resetTask();
	}

	public void updatePath() 
	{
		INavigationFlying nav = this.theEntity.getNavigatorNew();
		Entity target = this.theEntity.getAttackTarget();
		if (target != nav.getTargetEntity()) 
		{
			nav.clearPath();
			nav.setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
			Path path = nav.getPathToEntity(target, 0.0F);
			if ((path != null) && (path.getCurrentPathLength() > 1.6D * this.theEntity.getDistanceToEntity(target))) 
			{
				nav.setMovementType(INavigationFlying.MoveType.MIXED);
			}
			nav.autoPathToEntity(target);
		}
	}

	protected void updateDisengage() 
	{
		if (!this.wantsToRetreat) 
		{
			if (shouldLeaveMelee())
				this.wantsToRetreat = true;
		} 
		else if ((this.buffetedTarget) && (this.theEntity.getAIGoal() == Goal.MELEE_TARGET)) 
		{
			this.theEntity.transitionAIGoal(Goal.LEAVE_MELEE);
		}
	}

	protected void attackEntity(EntityLivingBase target) 
	{
		this.theEntity.doMeleeSound();
		super.attackEntity(target);
		if (this.wantsToRetreat) 
		{
			doWingBuffetAttack(target);
			this.buffetedTarget = true;
		}
	}

	protected boolean isInStartMeleeRange() 
	{
		EntityLivingBase target = this.theEntity.getAttackTarget();
		if (target == null) 
		{
			return false;
		}
		double d = this.theEntity.width + this.theEntity.getAttackRange() + 3.0D;
		return this.theEntity.getDistanceSq(target.posX, target.boundingBox.minY, target.posZ) < d * d;
	}

	protected void doWingBuffetAttack(EntityLivingBase target) 
	{
		int knockback = 2;
		target.addVelocity(-MathHelper.sin(this.theEntity.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F, 0.4D, MathHelper.cos(this.theEntity.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F);
		target.worldObj.playSoundAtEntity(target, "damage.fallbig", 1.0F, 1.0F);
	}
}