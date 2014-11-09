package invmod.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;

public class PathNavigateAdapter extends PathNavigate
{
  private NavigatorIM navigator;

  public PathNavigateAdapter(NavigatorIM navigator)
  {
    super(navigator.getEntity(), navigator.getEntity().worldObj);
    this.navigator = navigator;
  }

  public void onUpdateNavigation()
  {
    this.navigator.onUpdateNavigation();
  }

  public boolean noPath()
  {
    return this.navigator.noPath();
  }

  public void clearPathEntity()
  {
    this.navigator.clearPath();
  }

  public void setSpeed(double speed)
  {
    this.navigator.setSpeed((float)speed);
  }

  public boolean tryMoveToXYZ(double x, double y, double z, double movespeed)
  {
    return this.navigator.tryMoveToXYZ(x, y, z, 0.0F, (float)movespeed);
  }

  public boolean tryMoveToEntityLiving(Entity entity, double movespeed)
  {
    return this.navigator.tryMoveToEntity(entity, 0.0F, (float)movespeed);
  }

  public boolean setPath(Path entity, float movespeed)
  {
    return this.navigator.setPath(entity, movespeed);
  }

  public boolean setPath(PathEntity entity, double movespeed)
  {
    return false;
  }

  public PathEntity getPathToXYZ(double x, double y, double z)
  {
    return null;
  }

  public void setAvoidsWater(boolean par1)
  {
  }

  public boolean getAvoidsWater()
  {
    return false;
  }

  public void setBreakDoors(boolean par1)
  {
  }

  public void setEnterDoors(boolean par1)
  {
  }

  public boolean getCanBreakDoors()
  {
    return false;
  }

  public void setAvoidSun(boolean par1)
  {
  }

  public void setCanSwim(boolean par1)
  {
  }

  public PathEntity getPathToEntityLiving(Entity par1EntityLiving)
  {
    return null;
  }

  public PathEntity getPath()
  {
    return null;
  }
}