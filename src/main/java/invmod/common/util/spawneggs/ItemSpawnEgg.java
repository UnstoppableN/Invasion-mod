package invmod.common.util.spawneggs;

import invmod.Invasion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ItemSpawnEgg extends Item {

    private IIcon overlay;

    public ItemSpawnEgg() {
        setHasSubtypes(true);
        setCreativeTab(Invasion.tabInvmod);
        setUnlocalizedName("monsterPlacer");
    }

    public static Entity spawnCreature(World world, ItemStack stack, double x, double y, double z) {
        SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short) stack.getItemDamage());

        if (info == null)
            return null;

        String mobID = info.mobID;
        NBTTagCompound spawnData = info.spawnData;

        if (stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound.hasKey("mobID"))
                mobID = compound.getString("mobID");
            if (compound.hasKey("spawnData"))
                spawnData = compound.getCompoundTag("spawnData");
        }

        Entity entity = null;

        entity = EntityList.createEntityByName(mobID, world);

        if (entity != null) {
            if (entity instanceof EntityLiving) {
                EntityLiving entityliving = (EntityLiving) entity;
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                entityliving.onSpawnWithEgg(null);
                if (!spawnData.hasNoTags())
                    addNBTData(entity, spawnData);
                world.spawnEntityInWorld(entity);
                entityliving.playLivingSound();
                spawnRiddenCreatures(entity, world, spawnData);
            }
        }

        return entity;
    }

    private static void spawnRiddenCreatures(Entity entity, World world, NBTTagCompound cur) {
        while (cur.hasKey("Riding")) {
            cur = cur.getCompoundTag("Riding");
            Entity newEntity = EntityList.createEntityByName(cur.getString("id"), world);
            if (newEntity != null) {
                addNBTData(newEntity, cur);
                newEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                world.spawnEntityInWorld(newEntity);
                entity.mountEntity(newEntity);
            }
            entity = newEntity;
        }
    }

    @SuppressWarnings("unchecked")
    private static void addNBTData(Entity entity, NBTTagCompound spawnData) {
        NBTTagCompound newTag = new NBTTagCompound();
        entity.writeToNBTOptional(newTag);

        for (String name : (Set<String>) spawnData.func_150296_c())
            newTag.setTag(name, spawnData.getTag(name).copy());

        entity.readFromNBT(newTag);
    }

    public static String attemptToTranslate(String key, String _default) {
        String result = StatCollector.translateToLocal(key);
        return (result.equals(key)) ? _default : result;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = ("" + StatCollector.translateToLocal(getUnlocalizedName() + ".name")).trim();
        SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short) stack.getItemDamage());

        if (info == null)
            return name;

        String mobID = info.mobID;
        String displayName = info.displayName;

        if (stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound.hasKey("mobID"))
                mobID = compound.getString("mobID");
            if (compound.hasKey("displayName"))
                displayName = compound.getString("displayName");
        }

        if (displayName == null)
            name += ' ' + attemptToTranslate("entity." + mobID + ".name", mobID);
        else
            name += ' ' + attemptToTranslate("eggdisplay." + displayName, displayName);

        return name;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int par2) {
        SpawnEggInfo info = SpawnEggRegistry.getEggInfo((short) stack.getItemDamage());

        if (info == null)
            return 16777215;

        int color = (par2 == 0) ? info.primaryColor : info.secondaryColor;

        if (stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();
            if (par2 == 0 && compound.hasKey("primaryColor"))
                color = compound.getInteger("primaryColor");
            if (par2 != 0 && compound.hasKey("secondaryColor"))
                color = compound.getInteger("secondaryColor");
        }

        return color;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
        if (world.isRemote)
            return true;

        Block block = world.getBlock(x, y, z);
        x += Facing.offsetsXForSide[par7];
        y += Facing.offsetsYForSide[par7];
        z += Facing.offsetsZForSide[par7];
        double d0 = 0.0D;

        if (par7 == 1 && block != null && block.getRenderType() == 11)
            d0 = 0.5D;

        Entity entity = spawnCreature(world, stack, x + 0.5D, y + d0, z + 0.5D);

        if (entity != null) {
            if (entity instanceof EntityLiving && stack.hasDisplayName())
                ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
            if (!player.capabilities.isCreativeMode)
                --stack.stackSize;
        }
        return true;

    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote)
            return stack;

        MovingObjectPosition trace = getMovingObjectPositionFromPlayer(world, player, true);

        if (trace == null)
            return stack;

        if (trace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = trace.blockX;
            int y = trace.blockY;
            int z = trace.blockZ;

            if (!world.canMineBlock(player, x, y, z) || !player.canPlayerEdit(x, y, z, trace.sideHit, stack))
                return stack;

            if (world.getBlock(x, y, z) instanceof BlockLiquid) {
                Entity entity = spawnCreature(world, stack, x, y, z);

                if (entity != null) {
                    if (entity instanceof EntityLiving && stack.hasDisplayName())
                        ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
                    if (!player.capabilities.isCreativeMode)
                        --stack.stackSize;
                }
            }
        }

        return stack;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
        return par2 > 0 ? overlay : super.getIconFromDamageForRenderPass(par1, par2);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List list) {
        for (SpawnEggInfo info : SpawnEggRegistry.getEggInfoList())
            list.add(new ItemStack(item, 1, info.eggID));
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon("spawn_egg");
        overlay = iconRegister.registerIcon("spawn_egg_overlay");
    }
}