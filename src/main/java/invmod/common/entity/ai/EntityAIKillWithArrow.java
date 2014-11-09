package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;

public class EntityAIKillWithArrow<T extends EntityLivingBase> extends EntityAIKillEntity<T> 
{
	private float attackRangeSq;

	public EntityAIKillWithArrow(EntityIMLiving entity, Class<? extends T> targetClass, int attackDelay, float attackRange) {
		super(entity, targetClass, attackDelay);
		this.attackRangeSq = (attackRange * attackRange);
	}

	public void updateTask() {
		super.updateTask();
		EntityLivingBase target = getTarget();
		if ((getEntity().getDistanceSq(target.posX, target.boundingBox.minY, target.posZ) < 36.0D) && (getEntity().getEntitySenses().canSee(target)))
			getEntity().getNavigatorNew().haltForTick();
	}

	protected void attackEntity(Entity target) {
		setAttackTime(getAttackDelay());
		EntityLivingBase entity = getEntity();
		EntityArrow entityarrow = new EntityArrow(entity.worldObj, entity, getTarget(), 1.1F, 12.0F);
		entity.worldObj.playSoundAtEntity(entity, "random.bow", 1.0F, 1.0F / (entity.getRNG().nextFloat() * 0.4F + 0.8F));
		entity.worldObj.spawnEntityInWorld(entityarrow);
	}

	protected boolean canAttackEntity(Entity target) {
		return (getAttackTime() <= 0) && (getEntity().getDistanceSq(target.posX, target.boundingBox.minY, target.posZ) < this.attackRangeSq) && (getEntity().getEntitySenses().canSee(target));
	}
}