package invmod.common.util;

public class Pair<T, U>
{
  private T val1;
  private U val2;

  public Pair(T val1, U val2)
  {
    this.val1 = val1;
    this.val2 = val2;
  }

  public T getVal1()
  {
    return this.val1;
  }

  public U getVal2()
  {
    return this.val2;
  }

  public void setVal1(T entry)
  {
    this.val1 = entry;
  }

  public void setVal2(U value)
  {
    this.val2 = value;
  }
}