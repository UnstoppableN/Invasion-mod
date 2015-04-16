package invmod.common.nexus;


import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.AttackerAI;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

public class DummyNexus
        implements INexusAccess {
    private World world;

    public void attackNexus(int damage) {
    }

    public void registerMobDied() {
    }

    public boolean isActivating() {
        return false;
    }

    public int getMode() {
        return 0;
    }

    public int getActivationTimer() {
        return 0;
    }

    public int getSpawnRadius() {
        return 45;
    }

    public int getNexusKills() {
        return 0;
    }

    public int getGeneration() {
        return 0;
    }

    public int getNexusLevel() {
        return 1;
    }

    public int getCurrentWave() {
        return 1;
    }

    public int getXCoord() {
        return 0;
    }

    public int getYCoord() {
        return 0;
    }

    public int getZCoord() {
        return 0;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public List<EntityIMLiving> getMobList() {
        return null;
    }

    public void askForRespawn(EntityIMLiving entity) {
    }

    public AttackerAI getAttackerAI() {
        return null;
    }

    @Override
    public HashMap<String, Long> getBoundPlayers() {
        return null;
    }
}