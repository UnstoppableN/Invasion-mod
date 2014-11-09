package invmod.common.entity;

import invmod.common.mod_Invasion;
import invmod.common.entity.ai.EntityAIAttackNexus;
import invmod.common.entity.ai.EntityAIGoToNexus;
import invmod.common.entity.ai.EntityAIKillEntity;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAISprint;
import invmod.common.entity.ai.EntityAIStoop;
import invmod.common.entity.ai.EntityAITargetOnNoNexusPath;
import invmod.common.entity.ai.EntityAITargetRetaliate;
import invmod.common.entity.ai.EntityAIWaitForEngy;
import invmod.common.entity.ai.EntityAIWanderIM;
import invmod.common.nexus.INexusAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;



public class EntityIMImp extends EntityIMMob
{
private int tier;
  public EntityIMImp(World world, INexusAccess nexus)
  {
    super(world, nexus);
    setBaseMoveSpeedStat(0.3F);
    this.attackStrength = 3;
    this.tier=1;
    setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
    setName("Imp");
    setGender(1);
    setJumpHeight(1);
    setCanClimb(true);
    
    setAI();
  }

  public EntityIMImp(World world)
  {
    this(world, null);
  }

  @Override
  public String getSpecies()
  {
    return "Imp";
  }

  @Override
  public int getTier()
  {
    return this.tier;
  }
  
  protected void setAI() 
	{
		//added entityaiswimming and increased all other tasksordernumers with 1
		this.tasks = new EntityAITasks(this.worldObj.theProfiler);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIKillEntity(this, EntityPlayer.class, 40));
		this.tasks.addTask(1, new EntityAIKillEntity(this, EntityPlayerMP.class, 40));
		this.tasks.addTask(2, new EntityAIAttackNexus(this));
		this.tasks.addTask(3, new EntityAIWaitForEngy(this, 4.0F, true));
		this.tasks.addTask(4, new EntityAIKillEntity(this, EntityLiving.class, 40));
		this.tasks.addTask(5, new EntityAIGoToNexus(this));
		this.tasks.addTask(6, new EntityAIWanderIM(this));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityIMCreeper.class, 12.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		

		this.targetTasks = new EntityAITasks(this.worldObj.theProfiler);
		this.targetTasks.addTask(0, new EntityAITargetRetaliate(this, EntityLiving.class, this.getAggroRange()));
		this.targetTasks.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
		this.targetTasks.addTask(2, new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
		this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(3, new EntityAITargetOnNoNexusPath(this, EntityIMPigEngy.class, 3.5F));
		
	}
  
  @Override
	public boolean attackEntityAsMob(Entity entity) 
	{
	  	entity.setFire(3);
		return  super.attackEntityAsMob(entity);
	}
  
  @Override
  public String toString()
  {
	  return "IMImp-T" + this.getTier();
  }
}