package invmod.common.entity;

import invmod.common.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.MathHelper;

public class IMLookHelper extends EntityLookHelper {
	private final EntityIMLiving a;
	private float b;
	private float c;
	private boolean d = false;
	private double e;
	private double f;
	private double g;

	public IMLookHelper(EntityIMLiving entity) {
		super(entity);
		this.a = entity;
	}

	public void setLookPositionWithEntity(Entity par1Entity, float par2, float par3) {
		this.e = par1Entity.posX;

		if ((par1Entity instanceof EntityLiving)) {
			this.f = (par1Entity.posY + par1Entity.getEyeHeight());
		} else {
			this.f = ((par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0D);
		}

		this.g = par1Entity.posZ;
		this.b = par2;
		this.c = par3;
		this.d = true;
	}

	public void setLookPosition(double par1, double par3, double par5, float par7, float par8) {
		this.e = par1;
		this.f = par3;
		this.g = par5;
		this.b = par7;
		this.c = par8;
		this.d = true;
	}

	public void onUpdateLook() {
		if (this.d) {
			this.d = false;
			double d0 = this.e - this.a.posX;
			double d1 = this.f - (this.a.posY + this.a.getEyeHeight());
			double d2 = this.g - this.a.posZ;
			double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
			float yaw = (float) MathUtil.boundAngle180Deg(this.a.rotationYaw);
			float pitch = (float) MathUtil.boundAngle180Deg(this.a.rotationPitch);
			float yawHeadOffset = (float) (Math.atan2(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F - yaw;
			float pitchHeadOffset = (float) (Math.atan2(d1, d3) * 180.0D / 3.141592653589793D + 40.0D - pitch);
			float f2 = (float) MathUtil.boundAngle180Deg(yawHeadOffset);
			float yawFinal;
			if ((f2 > 100.0F) || (f2 < -100.0F))
				yawFinal = 0.0F;
			else {
				yawFinal = f2 / 6.0F;
			}

			this.a.setRotationPitchHead(updateRotation(this.a.getRotationPitchHead(), pitchHeadOffset, this.c));
			this.a.setRotationYawHeadIM(updateRotation(this.a.getRotationYawHeadIM(), yawFinal, this.b));
		}
	}

	private float updateRotation(float par1, float par2, float par3) {
		float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);

		if (f3 > par3) {
			f3 = par3;
		}

		if (f3 < -par3) {
			f3 = -par3;
		}

		return par1 + f3;
	}
}