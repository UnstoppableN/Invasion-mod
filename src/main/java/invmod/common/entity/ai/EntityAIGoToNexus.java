package invmod.common.entity.ai;

//NOOB HAUS:Done

import invmod.common.entity.EntityIMMob;
import invmod.common.entity.Goal;
import invmod.common.entity.Path;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.Distance;
import invmod.common.util.IPosition;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIGoToNexus extends EntityAIBase {
    private EntityIMMob theEntity;
    private IPosition lastPathRequestPos;
    private int pathRequestTimer;
    private int pathFailedCount;

    public EntityAIGoToNexus(EntityIMMob entity) {
        this.theEntity = entity;
        this.lastPathRequestPos = new CoordsInt(0, -128, 0);
        this.pathRequestTimer = 0;
        this.pathFailedCount = 0;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.theEntity.getAIGoal() == Goal.BREAK_NEXUS) {
            return true;
        }
        return false;
    }

    @Override
    public void startExecuting() {
        setPathToNexus();
    }

    @Override
    public void updateTask() {
        if (this.pathFailedCount > 1) {
            wanderToNexus();
        }
        if ((this.theEntity.getNavigatorNew().noPath()) || (this.theEntity.getNavigatorNew().getStuckTime() > 40))
            setPathToNexus();
    }

    private void setPathToNexus() {
        INexusAccess nexus = this.theEntity.getNexus();
        if ((nexus != null) && (this.pathRequestTimer-- <= 0)) {
            boolean pathSet = false;
            double distance = this.theEntity.findDistanceToNexus();
            if (distance > 2000.0D) {
                pathSet = this.theEntity.getNavigatorNew().tryMoveTowardsXZ(nexus.getXCoord(), nexus.getZCoord(), 1, 6, 4, this.theEntity.getMoveSpeedStat());
            } else if (distance > 1.5D) {
                pathSet = this.theEntity.getNavigatorNew().tryMoveToXYZ(nexus.getXCoord(), nexus.getYCoord(), nexus.getZCoord(), 1.0F, this.theEntity.getMoveSpeedStat());
            }

            if ((!pathSet) || ((this.theEntity.getNavigatorNew().getLastPathDistanceToTarget() > 3.0F) && (Distance.distanceBetween(this.lastPathRequestPos, this.theEntity) < 3.5D))) {
                this.pathFailedCount += 1;
                this.pathRequestTimer = (40 * this.pathFailedCount + this.theEntity.worldObj.rand.nextInt(10));
            } else {
                this.pathFailedCount = 0;
                this.pathRequestTimer = 20;
            }

            this.lastPathRequestPos = new CoordsInt(this.theEntity.getXCoord(), this.theEntity.getYCoord(), this.theEntity.getZCoord());
        }
    }

    private boolean pathTooShort() {
        Path path = this.theEntity.getNavigatorNew().getPath();
        if (path != null) {
            IPosition pos = path.getFinalPathPoint();
            return this.theEntity.getDistanceSq(pos.getXCoord(), pos.getYCoord(), pos.getZCoord()) < 4.0D;
        }
        return true;
    }

    protected void wanderToNexus() {
        INexusAccess nexus = this.theEntity.getNexus();
        this.theEntity.getMoveHelper().setMoveTo(nexus.getXCoord() + 0.5D, nexus.getYCoord(), nexus.getZCoord() + 0.5D, this.theEntity.getMoveSpeedStat());
    }
}