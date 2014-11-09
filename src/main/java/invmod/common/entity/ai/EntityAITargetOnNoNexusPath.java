package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.Goal;
import net.minecraft.entity.EntityLiving;

public class EntityAITargetOnNoNexusPath extends EntityAISimpleTarget {
	private final float PATH_DISTANCE_TRIGGER = 4.0F;

	public EntityAITargetOnNoNexusPath(EntityIMLiving entity, Class<? extends EntityLiving> targetType, float distance) {
		super(entity, targetType, distance);
	}

	public boolean shouldExecute() {
		if ((getEntity().getAIGoal() == Goal.BREAK_NEXUS) && (getEntity().getNavigatorNew().getLastPathDistanceToTarget() > 4.0F)) {
			return super.shouldExecute();
		}
		return false;
	}

	public boolean continueExecuting() {
		if ((getEntity().getAIGoal() == Goal.BREAK_NEXUS) && (getEntity().getNavigatorNew().getLastPathDistanceToTarget() > 4.0F)) {
			return super.continueExecuting();
		}
		return false;
	}
}