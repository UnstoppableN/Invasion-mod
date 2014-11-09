package invmod.common.entity;

import invmod.common.mod_Invasion;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityIMEgg extends EntityIMLiving
{
  private static int META_HATCHED = 30;
  private int hatchTime;
  private int ticks;
  private boolean hatched;
  private Entity parent;
  private Entity[] contents;
  
  public EntityIMEgg(World world)
  {
    super(world);
    getDataWatcher().addObject(META_HATCHED, Byte.valueOf((byte)0));
  }

  public EntityIMEgg(Entity parent, Entity[] contents, int hatchTime)
  {
    super(parent.worldObj);
    this.parent = parent;
    this.contents = contents;
    this.hatchTime = hatchTime;
    this.setBurnsInDay(false);
    this.hatched = false;
    this.ticks = 0;
    setBaseMoveSpeedStat(0.01F);

    getDataWatcher().addObject(META_HATCHED, Byte.valueOf((byte)0));

    setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
    setName("Spider Egg");
    setGender(0);
    setPosition(parent.posX, parent.posY, parent.posZ);
    setSize(0.5F, 0.8F);
  }

  @Override
  public String getSpecies()
  {
    return null;
  }

  @Override
  public int getTier()
  {
    return 0;
  }

  @Override
  public boolean isHostile()
  {
    return false;
  }

  @Override
  public boolean isNeutral()
  {
    return false;
  }

  @Override
  public boolean isThreatTo(Entity entity)
  {
    if ((entity instanceof EntityPlayer)) {
      return true;
    }
    return false;
  }

  @Override
  public Entity getAttackingTarget()
  {
    return null;
  }

  @Override
  public void onEntityUpdate()
  {
    super.onEntityUpdate();
    if (!this.worldObj.isRemote)
    {
      this.ticks += 1;
      if (this.hatched)
      {
        if (this.ticks > this.hatchTime + 40)
          setDead();
      }
      else if (this.ticks > this.hatchTime)
      {
        hatch();
      }
    }
    else if ((!this.hatched) && (getDataWatcher().getWatchableObjectByte(META_HATCHED) == 1))
    {
      this.worldObj.playSoundAtEntity(this, "invmod:egghatch"+(rand.nextInt(1)+(Integer)1), 1.0F, 1.0F);
      this.hatched = true;
    }
  }

  private void hatch()
  {
    this.worldObj.playSoundAtEntity(this, "invmod:egghatch"+(rand.nextInt(1)+(Integer)1), 1.0F, 1.0F);
    this.hatched = true;
    if (!this.worldObj.isRemote)
    {
      getDataWatcher().updateObject(META_HATCHED, Byte.valueOf((byte)1));
      if (this.contents != null)
      {
        for (Entity entity : this.contents)
        {
          entity.setPosition(this.posX, this.posY, this.posZ);
          this.worldObj.spawnEntityInWorld(entity);
        }
      }
    }
  }
  
  @Override
  public String toString()
  {
	  return "IMSpider-egg";
  }
}