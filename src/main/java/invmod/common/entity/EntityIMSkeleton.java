package invmod.common.entity;

import invmod.Invasion;
import invmod.common.entity.ai.*;
import invmod.common.nexus.INexusAccess;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityIMSkeleton extends EntityIMMob {
    private static final ItemStack defaultHeldItem = new ItemStack(Items.bow, 1);
    private int tier;

    public EntityIMSkeleton(World world) {
        this(world, null);
    }

    public EntityIMSkeleton(World world, INexusAccess nexus) {
        super(world, nexus);
        this.tier = 1;
        //setBurnsInDay(true);

        setMaxHealthAndHealth(Invasion.getMobHealth((EntityIMLiving) this));
        setName("Skeleton");
        setGender(0);
        setBaseMoveSpeedStat(0.21F);

        setAI();

    }

    private void setAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIKillWithArrow(this, EntityPlayer.class, 65, 16.0F));
        this.tasks.addTask(1, new EntityAIKillWithArrow(this, EntityPlayerMP.class, 65, 16.0F));
        //this.tasks.addTask(1, new EntityAIRallyBehindEntity(this, EntityIMCreeper.class, 4.0F));
        this.tasks.addTask(2, new EntityAIKillWithArrow(this, EntityLiving.class, 65, 16.0F));
        this.tasks.addTask(3, new EntityAIAttackNexus(this));
        this.tasks.addTask(4, new EntityAIGoToNexus(this));
        this.tasks.addTask(5, new EntityAIWanderIM(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityIMCreeper.class, 12.0F));

        this.targetTasks.addTask(0, new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected String getLivingSound() {
        return "mob.skeleton.say";
    }

    @Override
    protected String getHurtSound() {
        return "mob.skeleton.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
    }

    @Override
    public String getSpecies() {
        return "Skeleton";
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public String toString() {
        return "IMSkeleton-T" + this.getTier();
    }

    @Override
    protected void dropFewItems(boolean flag, int bonus) {
        super.dropFewItems(flag, bonus);
        int i = this.rand.nextInt(3);
        for (int j = 0; j < i; j++) {
            dropItem(Items.arrow, 1);
        }

        i = this.rand.nextInt(3);
        for (int k = 1; k < i; k++) {
            dropItem(Items.bone, 1);
        }
    }

    @Override
    public ItemStack getHeldItem() {
        return defaultHeldItem;
    }
}