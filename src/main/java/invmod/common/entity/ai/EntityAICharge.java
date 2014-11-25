package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMZombiePigman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

	public class EntityAICharge<T extends EntityLivingBase> extends EntityAIMoveToEntity<T> 
	{
	  protected EntityCreature charger;
	  protected EntityLivingBase chargeTarget;
	  protected double chargeX;
	  protected double chargeY;
	  protected double chargeZ;
	  protected float speed;
	  protected int windup;
	  protected boolean hasAttacked;
	  protected int chargeDelay;
	  protected int runTime;
	  
		public EntityAICharge(EntityIMLiving entity, Class<? extends T> targetClass, float f)
		{
			super(entity, targetClass);
			 this.charger = entity;
			 this.speed = f;
			    this.windup = 0;
			    this.hasAttacked = false;
			    this.chargeDelay=100;
			    this.runTime=15;
		}

	  
		@Override
	  public boolean shouldExecute()
	  {
		  
		    if(chargeDelay>0){
		    	this.chargeDelay--;
		    	return false;
		    }
		  
	    this.chargeTarget = this.charger.getAttackTarget();
	    if (this.chargeTarget == null) {
	      return false;
	      
	    }
	    double distance = Math.sqrt(this.charger.getDistanceSqToEntity(this.chargeTarget));
	    if ((distance < 5.0D) || (distance > 20.0D)) {
	      return false;
	    }
	    if (!this.charger.onGround) {
	      return false;
	    }
	    Vec3 chargePos = findChargePoint(this.charger, this.chargeTarget, 6.0D);
	    if (chargePos == null){
	      return false;
	    }
	    

	    this.chargeX = chargePos.xCoord;
	    this.chargeY = chargePos.yCoord;
	    this.chargeZ = chargePos.zCoord;

	    return this.charger.getRNG().nextInt(1) == 0;
	  }
	  
		@Override
	  public void startExecuting()
	  {
		  
	    this.windup = (15 + this.charger.getRNG().nextInt(25));
	  }
	  
		@Override
	  public boolean continueExecuting()
	  {
		  if(this.windup==0&&this.runTime>0){
			 this.runTime--; 
		  }
		  
	 return (this.windup>0) || (this.runTime>0);
	  }
	  
		@Override
	  public void updateTask()
	  {
	    this.charger.getLookHelper().setLookPosition(this.chargeX, this.chargeY - 1.0D, this.chargeZ, 10.0F, this.charger.getVerticalFaceSpeed());
	    if (this.windup > 0) {
	      if (--this.windup == 0)
	      {
	        this.charger.getNavigator().tryMoveToXYZ(this.chargeX, this.chargeY, this.chargeZ, this.speed);
	      }
	      else
	      {
	        EntityCreature tmp90_87 = this.charger;
	        tmp90_87.limbSwingAmount = ((float)(tmp90_87.limbSwingAmount + 0.8D));
	        if ((this.charger instanceof EntityIMZombiePigman)) {
	          ((EntityIMZombiePigman)this.charger).setCharging(true);
	        }
	      }
	    }
	    double var1 = this.charger.width * 2.1F * this.charger.width * 2.1F;
	    if (this.charger.getDistanceSq(this.chargeTarget.posX, this.chargeTarget.boundingBox.minY, this.chargeTarget.posZ) <= var1) {
	      if (!this.hasAttacked)
	      {
	        this.hasAttacked = true;
	        this.charger.attackEntityAsMob(this.chargeTarget);
	      }
	    }
	  }
	  
		@Override
	  public void resetTask()
	  {
	    this.windup = 0;
	    this.chargeTarget = null;
	    this.hasAttacked = false;
	    this.chargeDelay=100;
	    this.runTime=15;
	    if ((this.charger instanceof EntityIMZombiePigman)) {
	      ((EntityIMZombiePigman)this.charger).setCharging(false);
	    }
	  }
	  
	  protected Vec3 findChargePoint(Entity attacker, Entity target, double overshoot)
	  {
	    double vecx = target.posX - attacker.posX;
	    double vecz = target.posZ - attacker.posZ;
	    float rangle = (float)Math.atan2(vecz, vecx);
	    
	    double distance = MathHelper.sqrt_double(vecx * vecx + vecz * vecz);

		double dx = MathHelper.cos(rangle) * (distance + overshoot);
		double dz = MathHelper.sin(rangle) * (distance + overshoot);

	    return Vec3.createVectorHelper((attacker.posX + dx), target.posY, (attacker.posZ + dz));
	  }
	}

