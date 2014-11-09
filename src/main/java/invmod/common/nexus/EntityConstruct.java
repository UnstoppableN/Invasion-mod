package invmod.common.nexus;

public class EntityConstruct
{
  private IMEntityType entityType;
  private int texture;
  private int tier;
  private int flavour;
  private int minAngle;
  private int maxAngle;
  private float scaling;

  public EntityConstruct(IMEntityType mobType, int tier, int texture, int flavour, float scaling, int minAngle, int maxAngle)
  {
    this.entityType = mobType;
    this.texture = texture;
    this.tier = tier;
    this.flavour = flavour;
    this.scaling = scaling;
    this.minAngle = minAngle;
    this.maxAngle = maxAngle;
  }

  public IMEntityType getMobType()
  {
    return this.entityType;
  }

  public int getTexture()
  {
    return this.texture;
  }

  public int getTier()
  {
    return this.tier;
  }

  public int getFlavour()
  {
    return this.flavour;
  }

  public float getScaling()
  {
    return this.scaling;
  }

  public int getMinAngle()
  {
    return this.minAngle;
  }

  public int getMaxAngle()
  {
    return this.maxAngle;
  }
}