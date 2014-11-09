package invmod.common.nexus;

public class InvMobConstruct
{
  private int texture;
  private int tier;
  private int flavour;
  private float scaling;

  public InvMobConstruct(int texture, int tier, int flavour, float scaling)
  {
    this.texture = texture;
    this.tier = tier;
    this.flavour = flavour;
    this.scaling = scaling;
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
}