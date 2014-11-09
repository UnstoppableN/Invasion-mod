package invmod.common.util;

public class Triplet<T, U, V>
{
  private T val1;
  private U val2;
  private V val3;

  public Triplet(T val1, U val2, V val3)
  {
    this.val1 = val1;
    this.val2 = val2;
    this.val3 = val3;
  }

  public T getVal1()
  {
    return this.val1;
  }

  public U getVal2()
  {
    return this.val2;
  }

  public V getVal3()
  {
    return this.val3;
  }

  public void setVal1(T entry)
  {
    this.val1 = entry;
  }

  public void setVal2(U value)
  {
    this.val2 = value;
  }

  public void setVal3(V value)
  {
    this.val3 = value;
  }
}