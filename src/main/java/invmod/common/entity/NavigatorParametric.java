package invmod.common.entity;

import invmod.common.util.PosRotate3D;

public abstract class NavigatorParametric extends NavigatorIM
{
  protected double minMoveToleranceSq;
  protected int timeParam;

  public NavigatorParametric(EntityIMLiving entity, IPathSource pathSource)
  {
    super(entity, pathSource);
    this.minMoveToleranceSq = 21.0D;
    this.timeParam = 0;
  }

  public void onUpdateNavigation(int paramElapsed)
  {
    this.totalTicks += 1;
    if ((noPath()) || (this.waitingForNotify)) {
      return;
    }
    if ((canNavigate()) && (this.nodeActionFinished))
    {
      int pathIndex = this.path.getCurrentPathIndex();
      pathFollow(this.timeParam + paramElapsed);
      doMovementTo(this.timeParam);

      if (this.path.getCurrentPathIndex() != pathIndex)
      {
        this.ticksStuck = 0;
        if (this.activeNode.action != PathAction.NONE) {
          this.nodeActionFinished = false;
        }
      }
    }
    if (this.nodeActionFinished)
    {
      if (!isPositionClear(this.activeNode.xCoord, this.activeNode.yCoord, this.activeNode.zCoord, this.theEntity))
      {
        if (this.theEntity.onPathBlocked(this.path, this))
        {
          setDoingTaskAndHold();
        }
        else
        {
          clearPath();
        }
      }
    }
    else
    {
      handlePathAction();
    }
  }

  public void onUpdateNavigation()
  {
    onUpdateNavigation(1);
  }

  protected void doMovementTo(int param)
  {
    PosRotate3D movePos = entityPositionAtParam(param);
    this.theEntity.moveEntity(movePos.getPosX(), movePos.getPosY(), movePos.getPosZ());

    if (Math.abs(this.theEntity.getDistanceSq(movePos.getPosX(), movePos.getPosY(), movePos.getPosZ())) < this.minMoveToleranceSq)
    {
      this.timeParam = param;
      this.ticksStuck -= 1;
    }
    else
    {
      this.ticksStuck += 1;
    }
  }

  protected abstract PosRotate3D entityPositionAtParam(int paramInt);

  protected abstract boolean isReadyForNextNode(int paramInt);

  protected void pathFollow(int param)
  {
    int nextIndex = this.path.getCurrentPathIndex() + 1;
    if (isReadyForNextNode(param))
    {
      if (nextIndex < this.path.getCurrentPathLength())
      {
        this.timeParam = 0;
        this.path.setCurrentPathIndex(nextIndex);
        this.activeNode = this.path.getPathPointFromIndex(this.path.getCurrentPathIndex());
      }
    }
    else
    {
      this.timeParam = param;
    }
  }

  protected void pathFollow()
  {
    pathFollow(0);
  }
}