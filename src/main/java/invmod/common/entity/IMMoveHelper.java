package invmod.common.entity;

import invmod.common.util.IPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class IMMoveHelper extends EntityMoveHelper {
	protected EntityIMLiving a;
	protected double b;
	protected double c;
	protected double d;
	protected double setSpeed;
	protected double targetSpeed;
	protected boolean needsUpdate;
	protected boolean isRunning;

	public IMMoveHelper(EntityIMLiving par1EntityLiving) {
		super(par1EntityLiving);
		this.needsUpdate = false;
		this.a = par1EntityLiving;
		this.b = par1EntityLiving.posX;
		this.c = par1EntityLiving.posY;
		this.d = par1EntityLiving.posZ;
		this.setSpeed = (this.targetSpeed = 0.0D);
	}

	public boolean isUpdating() {
		return this.needsUpdate;
	}

	public double getSpeed() {
		return this.setSpeed;
	}

	public void setMoveSpeed(float speed) {
		this.setSpeed = speed;
	}

	public void setMoveTo(IPosition pos, float speed) {
		setMoveTo(pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), speed);
	}

	public void setMoveTo(double x, double y, double z, double speed) {
		this.b = x;
		this.c = y;
		this.d = z;
		this.setSpeed = speed;
		this.needsUpdate = true;
	}

	public void onUpdateMoveHelper() {
		if (!this.needsUpdate) {
			this.a.setMoveForward(0.0F);
			this.a.setMoveState(MoveState.STANDING);
			return;
		}

		MoveState result = doGroundMovement();
		this.a.setMoveState(result);
	}

	protected MoveState doGroundMovement() {
		this.needsUpdate = false;
		this.targetSpeed = this.setSpeed;
		boolean isInLiquid = (this.a.isInWater()) || (this.a.handleLavaMovement());
		double dX = this.b - this.a.posX;
		double dZ = this.d - this.a.posZ;
		double dY = this.c - (!isInLiquid ? MathHelper.floor_double(this.a.boundingBox.minY + 0.5D) : this.a.posY);

		float newYaw = (float) (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D) - 90.0F;
		int ladderPos = -1;
		if ((Math.abs(dX) < 0.8D) && (Math.abs(dZ) < 0.8D) && ((dY > 0.0D) || (this.a.isHoldingOntoLadder()))) {
			ladderPos = getClimbFace(this.a.posX, this.a.posY, this.a.posZ);
			if (ladderPos == -1) {
				ladderPos = getClimbFace(this.a.posX, this.a.posY + 1.0D, this.a.posZ);
			}

			switch (ladderPos) {
			case 0:
				newYaw = (float) (Math.atan2(dZ, dX + 1.0D) * 180.0D / 3.141592653589793D) - 90.0F;
				break;
			case 1:
				newYaw = (float) (Math.atan2(dZ, dX - 1.0D) * 180.0D / 3.141592653589793D) - 90.0F;
				break;
			case 2:
				newYaw = (float) (Math.atan2(dZ + 1.0D, dX) * 180.0D / 3.141592653589793D) - 90.0F;
				break;
			case 3:
				newYaw = (float) (Math.atan2(dZ - 1.0D, dX) * 180.0D / 3.141592653589793D) - 90.0F;
			}
		}

		double dXZSq = dX * dX + dZ * dZ;
		double distanceSquared = dXZSq + dY * dY;
		if ((distanceSquared < 0.01D) && (ladderPos == -1)) {
			return MoveState.STANDING;
		}

		if ((dXZSq > 0.04D) || (ladderPos != -1)) {
			this.a.rotationYaw = correctRotation(this.a.rotationYaw, newYaw, this.a.getTurnRate());
			double moveSpeed;
			if ((distanceSquared >= 0.064D) || (this.a.isSprinting()))
				moveSpeed = this.targetSpeed;
			else {
				moveSpeed = this.targetSpeed * 0.5D;
			}
			if ((this.a.isInWater()) && (moveSpeed < 0.6D)) {
				moveSpeed = 0.6000000238418579D;
			}
			this.a.setAIMoveSpeed((float) moveSpeed);
		}

		double w = Math.max(this.a.width * 0.5F + 1.0F, 1.0D);
		w = this.a.width * 0.5F + 1.0F;
		if ((dY > 0.0D) && ((dX * dX + dZ * dZ <= w * w) || (isInLiquid))) {
			this.a.getJumpHelper().setJumping();
			if (ladderPos != -1)
				return MoveState.CLIMBING;
		}
		return MoveState.RUNNING;
	}

	protected float correctRotation(float currentYaw, float newYaw, float turnSpeed) {
		float dYaw = newYaw - currentYaw;
		while (dYaw < -180.0F)
			dYaw += 360.0F;
		while (dYaw >= 180.0F)
			dYaw -= 360.0F;
		if (dYaw > turnSpeed)
			dYaw = turnSpeed;
		if (dYaw < -turnSpeed) {
			dYaw = -turnSpeed;
		}
		return currentYaw + dYaw;
	}

	protected int getClimbFace(double x, double y, double z) {
		int mobX = MathHelper.floor_double(x);
		int mobY = MathHelper.floor_double(y);
		int mobZ = MathHelper.floor_double(z);

		Block block = this.a.worldObj.getBlock(mobX, mobY, mobZ);
		if (block == Blocks.ladder) {
			int meta = this.a.worldObj.getBlockMetadata(mobX, mobY, mobZ);
			if (meta == 2)
				return 2;
			if (meta == 3)
				return 3;
			if (meta == 4)
				return 0;
			if (meta == 5)
				return 1;
		} else if (block == Blocks.vine) {
			int meta = this.a.worldObj.getBlockMetadata(mobX, mobY, mobZ);
			if (meta == 1)
				return 2;
			if (meta == 4)
				return 3;
			if (meta == 2)
				return 1;
			if (meta == 8)
				return 0;
		}
		return -1;
	}
}