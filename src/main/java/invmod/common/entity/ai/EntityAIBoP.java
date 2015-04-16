package invmod.common.entity.ai;

//NOOB HAUS: DONE - not certain if actually used ?

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.MoveState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIBoP extends EntityAIBase {
    private static final int PATIENCE = 500;
    private EntityIMFlying theEntity;
    private int timeWithGoal;
    private int timeWithTarget;
    private int patienceTime;
    private float lastHealth;
    private Goal lastGoal;
    private EntityLivingBase lastTarget;

    public EntityAIBoP(EntityIMFlying entity) {
        this.theEntity = entity;
        this.timeWithGoal = 0;
        this.patienceTime = 0;
        this.lastHealth = entity.getHealth();
        this.lastGoal = entity.getAIGoal();
        this.lastTarget = entity.getAttackTarget();
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public void startExecuting() {
        this.timeWithGoal = 0;
        this.patienceTime = 0;
    }

    @Override
    public void updateTask() {
        this.timeWithGoal += 1;
        if (this.theEntity.getAIGoal() != this.lastGoal) {
            this.lastGoal = this.theEntity.getAIGoal();
            this.timeWithGoal = 0;
        }

        this.timeWithTarget += 1;
        if (this.theEntity.getAttackTarget() != this.lastTarget) {
            this.lastTarget = this.theEntity.getAttackTarget();
            this.timeWithTarget = 0;
        }

        if (this.theEntity.getAttackTarget() == null) {
            if (this.theEntity.getNexus() != null) {
                if (this.theEntity.getAIGoal() != Goal.BREAK_NEXUS) {
                    this.theEntity.transitionAIGoal(Goal.BREAK_NEXUS);
                }

            } else if (this.theEntity.getAIGoal() != Goal.CHILL) {
                this.theEntity.transitionAIGoal(Goal.CHILL);
                this.theEntity.getNavigatorNew().clearPath();
                this.theEntity.getNavigatorNew().setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
                this.theEntity.getNavigatorNew().setLandingPath();
            }

        } else if ((this.theEntity.getAIGoal() == Goal.CHILL) || (this.theEntity.getAIGoal() == Goal.NONE)) {
            chooseTargetAction(this.theEntity.getAttackTarget());
        }

        if (this.theEntity.getAIGoal() != Goal.STAY_AT_RANGE) {
            if (this.theEntity.getAIGoal() == Goal.MELEE_TARGET) {
                if (this.timeWithGoal > 600) {
                    this.theEntity.transitionAIGoal(Goal.STAY_AT_RANGE);
                }
            }
        }
    }

    protected void chooseTargetAction(EntityLivingBase target) {
        if (this.theEntity.getMoveState() != MoveState.FLYING) {
            if ((this.theEntity.getDistanceToEntity(target) < 10.0F) && (this.theEntity.worldObj.rand.nextFloat() > 0.3F)) {
                this.theEntity.transitionAIGoal(Goal.MELEE_TARGET);
                return;
            }
        }
        this.theEntity.transitionAIGoal(Goal.STAY_AT_RANGE);
    }
}