package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import net.minecraft.entity.EntityLivingBase;

public class EntityAITargetRetaliate extends EntityAISimpleTarget {
    public EntityAITargetRetaliate(EntityIMLiving entity, Class<? extends EntityLivingBase> targetType, float distance) {
        super(entity, targetType, distance);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase attacker = getEntity().getAITarget();
        if (attacker != null) {
            if ((getEntity().getDistanceToEntity(attacker) <= getAggroRange()) && (getTargetType().isAssignableFrom(attacker.getClass()))) {
                setTarget(attacker);
                return true;
            }
        }
        return false;
    }
}