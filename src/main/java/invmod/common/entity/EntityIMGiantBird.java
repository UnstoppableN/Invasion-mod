package invmod.common.entity;

import invmod.common.mod_Invasion;
import invmod.common.entity.ai.EntityAIBirdFight;
import invmod.common.entity.ai.EntityAIBoP;
import invmod.common.entity.ai.EntityAICircleTarget;
import invmod.common.entity.ai.EntityAIFlyingStrike;
import invmod.common.entity.ai.EntityAIFlyingTackle;
import invmod.common.entity.ai.EntityAIPickUpEntity;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAIStabiliseFlying;
import invmod.common.entity.ai.EntityAISwoop;
import invmod.common.entity.ai.EntityAIWatchTarget;
import invmod.common.nexus.INexusAccess;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityIMGiantBird extends EntityIMBird
{
  private static final float PICKUP_OFFSET_X = 0.0F;
  private static final float PICKUP_OFFSET_Y = 0.2F;
  private static final float PICKUP_OFFSET_Z = -0.92F;
  private static final float MODEL_ROTATION_OFFSET_Y = 1.9F;
  private static final byte TRIGGER_SQUAWK = 10;
  private static final byte TRIGGER_SCREECH = 10;
  private static final byte TRIGGER_DEATHSOUND = 10;
  private int tier;
  
  public EntityIMGiantBird(World world)
  {
    this(world, null);
  }

  public EntityIMGiantBird(World world, INexusAccess nexus)
  {
    super(world, nexus);
    setName("Bird");
    setGender(2);
    this.attackStrength = 5;

    this.tier=1;
    setSize(1.9F, 2.8F);
    setGravity(0.03F);
    setThrust(0.028F);
    setMaxPoweredFlightSpeed(0.9F);
    setLiftFactor(0.35F);
    setThrustComponentRatioMin(0.0F);
    setThrustComponentRatioMax(0.5F);
    setMaxTurnForce(getGravity() * 8.0F);
    setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
    setBaseMoveSpeedStat(0.4F);
    setAI();
    setDebugMode(1);
  }

  @Override
  public void onUpdate()
  {
    super.onUpdate();
    if ((getDebugMode() == 1) && (!this.worldObj.isRemote))
    {
      setRenderLabel(getAIGoal() + "\n" + getNavString());
    }
  }

  @Override
  public boolean canDespawn()
  {
    return false;
  }

  @Override
  public void updateRiderPosition()
  {
    if (this.riddenByEntity != null)
    {
      double x = 0.0D;
      double y = getMountedYOffset() - 1.899999976158142D;
      double z = -0.9200000166893005D;

      double dAngle = this.rotationPitch / 180.0F * 3.141592653589793D;
      double sinF = Math.sin(dAngle);
      double cosF = Math.cos(dAngle);
      double tmp = z * cosF - y * sinF;
      y = y * cosF + z * sinF;
      z = tmp;

      dAngle = this.rotationYaw / 180.0F * 3.141592653589793D;
      sinF = Math.sin(dAngle);
      cosF = Math.cos(dAngle);
      tmp = x * cosF - z * sinF;
      z = z * cosF + x * sinF;
      x = tmp;

      y += 1.899999976158142D + this.riddenByEntity.getYOffset();

      this.riddenByEntity.lastTickPosX = (this.lastTickPosX + x);
      this.riddenByEntity.lastTickPosY = (this.lastTickPosY + y);
      this.riddenByEntity.lastTickPosZ = (this.lastTickPosZ + z);
      this.riddenByEntity.setPosition(this.posX + x, this.posY + y, this.posZ + z);
      this.riddenByEntity.rotationYaw = (getCarriedEntityYawOffset() + this.rotationYaw);
    }
  }

  @Override
  public boolean shouldRiderSit()
  {
    return false;
  }

  @Override
  public double getMountedYOffset()
  {
    return -0.2000000029802322D;
  }

  @Override
  public void doScreech()
  {
    if (!this.worldObj.isRemote)
    {
      this.worldObj.playSoundAtEntity(this, "invmod:v_screech"+(rand.nextInt(2)+(Integer)1), 6.0F, 1.0F + (this.rand.nextFloat() * 0.2F - 0.1F));
      this.worldObj.setEntityState(this, (byte)10);
    }
    else
    {
      setBeakState(35);
    }
  }

  @Override
  public void doMeleeSound()
  {
    doSquawk();
  }

  @Override
  protected void doHurtSound()
  {
    doSquawk();
  }

  @Override
  protected void doDeathSound()
  {
    if (!this.worldObj.isRemote)
    {
      this.worldObj.playSoundAtEntity(this, "invmod:v_death1", 1.9F, 1.0F + (this.rand.nextFloat() * 0.2F - 0.1F));
      this.worldObj.setEntityState(this, (byte)10);
    }
    else
    {
      setBeakState(25);
    }
  }

  @Override
  protected void onDebugChange()
  {
    if (getDebugMode() == 1)
    {
      setShouldRenderLabel(true);
    }
    else
    {
      setShouldRenderLabel(false);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void handleHealthUpdate(byte b)
  {
    super.handleHealthUpdate(b);
    if (b == 10)
    {
      doSquawk();
    }
    else if (b == 10)
    {
      doScreech();
    }
    else if (b == 10)
    {
      doDeathSound();
    }
  }

  private void doSquawk()
  {
    if (!this.worldObj.isRemote)
    {
      this.worldObj.playSoundAtEntity(this, "invmod:v_squawk"+(rand.nextInt(3)+(Integer)1), 1.9F, 1.0F + (this.rand.nextFloat() * 0.2F - 0.1F));
      this.worldObj.setEntityState(this, (byte)10);
    }
    else
    {
      setBeakState(10);
    }
  }

  private String getNavString()
  {
    return getNavigatorNew().getStatus();
  }

  private void setAI()
  {
    this.tasks = new EntityAITasks(this.worldObj.theProfiler);

    this.tasks.addTask(0, new EntityAISwoop(this));

    this.tasks.addTask(3, new EntityAIBoP(this));
    this.tasks.addTask(4, new EntityAIFlyingStrike(this));
    this.tasks.addTask(4, new EntityAIFlyingTackle(this));
    this.tasks.addTask(4, new EntityAIPickUpEntity(this, 0.0F, 0.2F, 0.0F, 1.5F, 1.5F, 20, 45.0F, 45.0F));
    this.tasks.addTask(4, new EntityAIStabiliseFlying(this, 35));
    this.tasks.addTask(4, new EntityAICircleTarget(this, 300, 16.0F, 45.0F));
    this.tasks.addTask(4, new EntityAIBirdFight(this, EntityZombie.class, 25, 0.4F));
    this.tasks.addTask(4, new EntityAIWatchTarget(this));

    this.targetTasks = new EntityAITasks(this.worldObj.theProfiler);

    this.targetTasks.addTask(2, new EntityAISimpleTarget(this, EntityZombie.class, 58.0F, true));
  }
  
  @Override
  public int getTier()
  {
	  return this.tier;
  }
  
  @Override
  public String toString()
  {
	  return "IMVulture-T" + this.getTier();
  }
}