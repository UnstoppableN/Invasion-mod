package invmod.common.nexus;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.AttackerAI;
import invmod.common.util.IPosition;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

public abstract interface INexusAccess extends IPosition {
    public abstract void attackNexus(int paramInt);

    public abstract void registerMobDied();

    public abstract boolean isActivating();

    public abstract int getMode();

    public abstract int getActivationTimer();

    public abstract int getSpawnRadius();

    public abstract int getNexusKills();

    public abstract int getGeneration();

    public abstract int getNexusLevel();

    public abstract int getCurrentWave();

    public abstract World getWorld();

    public abstract List<EntityIMLiving> getMobList();

    public abstract AttackerAI getAttackerAI();

    public abstract void askForRespawn(EntityIMLiving paramEntityIMLiving);

    public abstract HashMap<String, Long> getBoundPlayers();
}