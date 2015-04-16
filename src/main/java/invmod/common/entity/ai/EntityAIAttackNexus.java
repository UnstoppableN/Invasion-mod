package invmod.common.entity.ai;

//NOOB HAUS: File Done

import invmod.Invasion;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMZombie;
import invmod.common.entity.Goal;
import invmod.common.nexus.INexusAccess;
import invmod.common.nexus.TileEntityNexus;
import invmod.common.util.CoordsInt;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIAttackNexus extends EntityAIBase {
    private EntityIMLiving theEntity;
    private boolean attacked;

    public EntityAIAttackNexus(EntityIMLiving par1EntityLiving) {
        this.theEntity = par1EntityLiving;
        setMutexBits(3);
    }

    public boolean shouldExecute() {
        if ((this.theEntity.attackTime == 0) && (this.theEntity.getAIGoal() == Goal.BREAK_NEXUS) && (this.theEntity.findDistanceToNexus() > 4.0D)) {
            this.theEntity.attackTime = 5;
            return false;
        }

        return isNexusInRange();
    }

    public void startExecuting() {
        this.theEntity.attackTime = 40;
    }

    public boolean continueExecuting() {
        return !this.attacked;
    }

    public void updateTask() {
        if (this.theEntity.attackTime == 0) {
            if (isNexusInRange()) {
                if (this.theEntity instanceof EntityIMZombie) {
                    ((EntityIMZombie) this.theEntity).updateAnimation(true);
                }
                this.theEntity.getNexus().attackNexus(2);
            }
            this.attacked = true;
        }
    }

    public void resetTask() {
        this.attacked = false;
    }

    private boolean isNexusInRange() {
        CoordsInt size = this.theEntity.getCollideSize();
        int x = this.theEntity.getXCoord();
        int y = this.theEntity.getYCoord();
        int z = this.theEntity.getZCoord();
        for (int i = 0; i < size.getYCoord(); i++) {
            for (int j = 0; j < size.getXCoord(); j++) {
                if (this.theEntity.worldObj.getBlock(x + j, y, z - 1) == Invasion.blockNexus) {
                    if (isCorrectNexus(x + j, y, z - 1)) {
                        return true;
                    }
                }
                if (this.theEntity.worldObj.getBlock(x + j, y, z + 1 + size.getZCoord()) == Invasion.blockNexus) {
                    if (isCorrectNexus(x + j, y, z + 1 + size.getZCoord())) {
                        return true;
                    }
                }
            }

            for (int j = 0; j < size.getZCoord(); j++) {
                if (this.theEntity.worldObj.getBlock(x - 1, y, z + j) == Invasion.blockNexus) {
                    if (isCorrectNexus(x - 1, y, z + j)) {
                        return true;
                    }
                }
                if (this.theEntity.worldObj.getBlock(x + 1 + size.getXCoord(), y, z + j) == Invasion.blockNexus) {
                    if (isCorrectNexus(x + 1 + size.getXCoord(), y, z + j)) {
                        return true;
                    }
                }
            }
        }

        for (int i = 0; i < size.getXCoord(); i++) {
            for (int j = 0; j < size.getZCoord(); j++) {
                if (this.theEntity.worldObj.getBlock(x + i, y + 1 + size.getYCoord(), z + j) == Invasion.blockNexus) {
                    if (isCorrectNexus(x + i, y + 1 + size.getYCoord(), z + j)) {
                        return true;
                    }
                }
                if (this.theEntity.worldObj.getBlock(x + i, y - 1, z + j) == Invasion.blockNexus) {
                    if (isCorrectNexus(x + i, y - 1, z + j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCorrectNexus(int x, int y, int z) {
        INexusAccess nexus = (TileEntityNexus) this.theEntity.worldObj.getTileEntity(x, y, z);
        if ((nexus != null) && (nexus == this.theEntity.getNexus())) {
            return true;
        }
        return false;
    }
}