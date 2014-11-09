package invmod.common.entity;

import invmod.common.nexus.INexusAccess;
import net.minecraft.world.World;

public abstract class EntityIMMob extends EntityIMLiving
{
  public EntityIMMob(World world)
  {
    super(world, null);
  }

  public EntityIMMob(World world, INexusAccess nexus)
  {
    super(world, nexus);
  }
}