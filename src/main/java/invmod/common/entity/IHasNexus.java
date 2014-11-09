package invmod.common.entity;

import invmod.common.nexus.INexusAccess;

public abstract interface IHasNexus
{
  public abstract INexusAccess getNexus();

  public abstract void acquiredByNexus(INexusAccess paramINexusAccess);
}