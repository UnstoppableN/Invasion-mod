package invmod.common.util;

import net.minecraft.util.Vec3;

public class PosRotate3D {
    private double posX;
    private double posY;
    private double posZ;
    private float rotX;
    private float rotY;
    private float rotZ;

    public PosRotate3D() {
        this(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
    }

    public PosRotate3D(double posX, double posY, double posZ, float rotX, float rotY, float rotZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public Vec3 getPos() {
        return Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
    }

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(double pos) {
        this.posX = pos;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosY(double pos) {
        this.posY = pos;
    }

    public double getPosZ() {
        return this.posZ;
    }

    public void setPosZ(double pos) {
        this.posZ = pos;
    }

    public float getRotX() {
        return this.rotX;
    }

    public void setRotX(float rot) {
        this.rotX = rot;
    }

    public float getRotY() {
        return this.rotY;
    }

    public void setRotY(float rot) {
        this.rotY = rot;
    }

    public float getRotZ() {
        return this.rotZ;
    }

    public void setRotZ(float rot) {
        this.rotZ = rot;
    }
}