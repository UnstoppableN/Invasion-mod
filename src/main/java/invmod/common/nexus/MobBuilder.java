package invmod.common.nexus;

import invmod.Invasion;
import invmod.common.entity.*;
import net.minecraft.world.World;

public class MobBuilder {
    public EntityIMLiving createMobFromConstruct(EntityConstruct mobConstruct, World world, INexusAccess nexus) {
        EntityIMLiving mob = null;
        switch (mobConstruct.getMobType()) {
            case ZOMBIE:
                EntityIMZombie zombie = new EntityIMZombie(world, nexus);
                zombie.setTexture(mobConstruct.getTexture());
                zombie.setFlavour(mobConstruct.getFlavour());
                zombie.setTier(mobConstruct.getTier());
                mob = zombie;
                break;
            case ZOMBIEPIGMAN:
                EntityIMZombiePigman zombiePigman = new EntityIMZombiePigman(world, nexus);
                zombiePigman.setTexture(mobConstruct.getTexture());
                zombiePigman.setTier(mobConstruct.getTier());
                mob = zombiePigman;
                break;
            case SPIDER:
                EntityIMSpider spider = new EntityIMSpider(world, nexus);
                spider.setTexture(mobConstruct.getTexture());
                spider.setFlavour(mobConstruct.getFlavour());
                spider.setTier(mobConstruct.getTier());
                mob = spider;
                break;
            case SKELETON:
                EntityIMSkeleton skeleton = new EntityIMSkeleton(world, nexus);
                mob = skeleton;
                break;
            case PIG_ENGINEER:
                EntityIMPigEngy pigEngy = new EntityIMPigEngy(world, nexus);
                mob = pigEngy;
                break;
            case THROWER:
                EntityIMThrower thrower = new EntityIMThrower(world, nexus);
                thrower.setTexture(mobConstruct.getTier());
                thrower.setTier(mobConstruct.getTier());
                mob = thrower;
                break;
            case BURROWER:
                EntityIMBurrower burrower = new EntityIMBurrower(world, nexus);
                mob = burrower;
                break;
            case CREEPER:
                EntityIMCreeper creeper = new EntityIMCreeper(world, nexus);
                mob = creeper;
                break;

            case IMP:
                EntityIMImp imp = new EntityIMImp(world, nexus);
                mob = imp;
                break;
            default:
                Invasion.log("Missing mob type in MobBuilder: " + mobConstruct.getMobType());
                mob = null;
        }

        return mob;
    }
}