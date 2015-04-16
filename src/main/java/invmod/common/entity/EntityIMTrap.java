package invmod.common.entity;

import invmod.Invasion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityIMTrap extends Entity {
    public static final int TRAP_DEFAULT = 0;
    public static final int TRAP_RIFT = 1;
    public static final int TRAP_FIRE = 2;
    private static final int ARM_TIME = 60;
    private static final int META_CHANGED = 29;
    private static final int META_TYPE = 30;
    private static final int META_EMPTY = 31;
    private int trapType;
    private int ticks;
    private boolean isEmpty;
    private byte metaChanged;
    private boolean fromLoaded;

    public EntityIMTrap(World world) {
        super(world);
        setSize(0.5F, 0.28F);
        this.yOffset = 0.0F;
        this.ticks = 0;
        this.isEmpty = false;
        this.isImmuneToFire = true;
        this.trapType = 0;
        if (world.isRemote)
            this.metaChanged = 1;
        else {
            this.metaChanged = 0;
        }
        this.dataWatcher.addObject(29, Byte.valueOf(this.metaChanged));
        this.dataWatcher.addObject(30, Integer.valueOf(this.trapType));
        this.dataWatcher.addObject(31, Byte.valueOf((byte) (this.isEmpty ? 0 : 1)));
    }

    public EntityIMTrap(World world, double x, double y, double z) {
        this(world, x, y, z, 0);
    }

    public EntityIMTrap(World world, double x, double y, double z, int trapType) {
        this(world);
        this.trapType = trapType;
        this.dataWatcher.updateObject(30, Integer.valueOf(trapType));
        setLocationAndAngles(x, y, z, 0.0F, 0.0F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.ticks += 1;
        if (this.worldObj.isRemote) {
            if ((this.metaChanged != this.dataWatcher.getWatchableObjectByte(29)) || (this.ticks % 20 == 0)) {
                this.metaChanged = this.dataWatcher.getWatchableObjectByte(29);
                this.trapType = this.dataWatcher.getWatchableObjectInt(30);
                boolean wasEmpty = this.isEmpty;
                this.isEmpty = (this.dataWatcher.getWatchableObjectByte(31) == 0);
                if ((this.isEmpty) && (!wasEmpty) && (this.trapType == 1))
                    doRiftParticles();
            }
            return;
        }

        if (!isValidPlacement()) {
            EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Invasion.itemIMTrap, 1, 0));
            entityitem.delayBeforeCanPickup = 10;
            this.worldObj.spawnEntityInWorld(entityitem);
            setDead();
        }

        if ((this.worldObj.isRemote) || ((!this.isEmpty) && (this.ticks < 60))) {
            return;
        }

        List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox);
        if ((entities.size() > 0) && (!this.isEmpty)) {
            for (EntityLivingBase entity : entities) {
                if (trapEffect(entity)) {
                    setEmpty();
                    return;
                }
            }
        }
    }

    public boolean trapEffect(EntityLivingBase triggerEntity) {
        if (this.trapType == 0) {
            triggerEntity.attackEntityFrom(DamageSource.generic, 4.0F);
        } else if (this.trapType == 1) {
            triggerEntity.attackEntityFrom(DamageSource.magic, (triggerEntity instanceof EntityPlayer) ? 12.0F : 38.0F);

            List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.899999976158142D, 1.0D, 1.899999976158142D));
            for (Entity entity : entities) {
                entity.attackEntityFrom(DamageSource.magic, 8.0F);
                if ((entity instanceof EntityIMLiving)) {
                    ((EntityIMLiving) entity).stunEntity(60);
                }
            }
            this.worldObj.playSoundAtEntity(this, "random.break", 1.5F, 1.0F * (this.rand.nextFloat() * 0.25F + 0.55F));
        } else if (this.trapType == 2) {
            this.worldObj.playSoundAtEntity(this, "invmod:fireball" + 1, 1.5F, 1.15F / (this.rand.nextFloat() * 0.3F + 1.0F));
            doFireball(1.1F, 8);
        }

        return true;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityPlayer) {
        if ((!this.worldObj.isRemote) && (this.ticks > 30) && (this.isEmpty)) {
            if (entityPlayer.inventory.addItemStackToInventory(new ItemStack(Invasion.itemIMTrap, 1, 0))) {
                this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityPlayer.onItemPickup(this, 1);
                setDead();
            }
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer entityPlayer) {
        if ((this.worldObj.isRemote) || (this.isEmpty)) {
            return false;
        }
        ItemStack curItem = entityPlayer.inventory.getCurrentItem();
        if ((curItem != null) && (curItem.getItem() == Invasion.itemProbe) && (curItem.getItemDamage() >= 1)) {
            EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Invasion.itemIMTrap, 1, this.trapType));
            entityitem.delayBeforeCanPickup = 5;
            this.worldObj.spawnEntityInWorld(entityitem);
            setDead();
            return true;
        }
        return false;
    }


    public boolean isEmpty() {
        return this.isEmpty;
    }

    public int getTrapType() {
        return this.trapType;
    }

    public boolean isValidPlacement() {
        //set bool of blocknormalcubedefault to true, donno why
        return (this.worldObj.isBlockNormalCubeDefault(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ), true)) && (this.worldObj.getEntitiesWithinAABB(EntityIMTrap.class, this.boundingBox).size() < 2);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void entityInit() {
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.isEmpty = nbttagcompound.getBoolean("isEmpty");
        this.trapType = nbttagcompound.getInteger("type");
        this.dataWatcher.updateObject(31, Byte.valueOf((byte) (this.isEmpty ? 0 : 1)));
        this.dataWatcher.updateObject(30, Integer.valueOf(this.trapType));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setBoolean("isEmpty", this.isEmpty);
        nbttagcompound.setInteger("type", this.trapType);
    }

    private void setEmpty() {
        this.isEmpty = true;
        this.ticks = 0;
        this.dataWatcher.updateObject(31, Byte.valueOf((byte) (this.isEmpty ? 0 : 1)));
        this.dataWatcher.updateObject(29, Byte.valueOf((byte) (this.dataWatcher.getWatchableObjectByte(29) == 0 ? 1 : 0)));
    }

    private void doFireball(float size, int initialDamage) {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);
        int min = 0 - (int) size;
        int max = 0 + (int) size;
        for (int i = min; i <= max; i++) {
            for (int j = min; j <= max; j++) {
                for (int k = min; k <= max; k++) {
                    if ((this.worldObj.getBlock(x + i, y + j, z + k) == Blocks.air) || (this.worldObj.getBlock(x + i, y + j, z + k).getMaterial().getCanBurn())) {
                        this.worldObj.setBlock(x + i, y + j, z + k, Blocks.fire);
                    }
                }
            }
        }

        List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(size, size, size));
        for (Entity entity : entities) {
            entity.setFire(8);
            entity.attackEntityFrom(DamageSource.onFire, initialDamage);
        }
    }

    private void doRiftParticles() {
        for (int i = 0; i < 300; i++) {
            float x = this.rand.nextFloat() * 6.0F - 3.0F;
            float z = this.rand.nextFloat() * 6.0F - 3.0F;
            this.worldObj.spawnParticle("portal", this.posX + x, this.posY + 2.0D, this.posZ + z, -x / 3.0F, -2.0D, -z / 3.0F);
        }
    }
}