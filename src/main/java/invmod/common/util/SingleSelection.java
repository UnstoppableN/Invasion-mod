package invmod.common.util;

public class SingleSelection<T>
  implements ISelect<T>
{
  private T object;

  public SingleSelection(T object)
  {
    this.object = object;
  }

  public T selectNext()
  {
    return this.object;
  }

  public void reset()
  {
  }

  public String toString()
  {
    return this.object.toString();
  }
}