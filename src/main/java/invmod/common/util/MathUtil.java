package invmod.common.util;

public class MathUtil
{
  public static boolean floatEquals(float f1, float f2, float tolerance)
  {
    float diff = f1 - f2;
    if (diff >= 0.0F) {
      return diff < tolerance;
    }
    return -diff < tolerance;
  }

  public static double boundAnglePiRad(double angle)
  {
    angle %= 6.283185307179586D;
    if (angle >= 3.141592653589793D)
      angle -= 6.283185307179586D;
    else if (angle < -3.141592653589793D) {
      angle += 6.283185307179586D;
    }
    return angle;
  }

  public static double boundAngle180Deg(double angle)
  {
    angle %= 360.0D;
    if (angle >= 180.0D)
      angle -= 360.0D;
    else if (angle < -180.0D) {
      angle += 360.0D;
    }
    return angle;
  }

  public static float interpRotationRad(float rot1, float rot2, float t)
  {
    return interpWrapped(rot1, rot2, t, -3.141593F, 3.141593F);
  }

  public static float interpRotationDeg(float rot1, float rot2, float t)
  {
    return interpWrapped(rot1, rot2, t, -180.0F, 180.0F);
  }

  public static float interpWrapped(float val1, float val2, float t, float min, float max)
  {
    float dVal = val2 - val1;
    while (dVal < min)
    {
      dVal += max - min;
    }
    while (dVal >= max)
    {
      dVal -= max - min;
    }
    return val1 + t * dVal;
  }

  public static float unpackFloat(int i)
  {
    return Float.intBitsToFloat(i);
  }

  public static int packFloat(float f)
  {
    return Float.floatToIntBits(f);
  }

  public static int packAnglesDeg(float a1, float a2, float a3, float a4)
  {
    return packBytes((byte)(int)(a1 / 360.0F * 256.0F), (byte)(int)(a2 / 360.0F * 256.0F), (byte)(int)(a3 / 360.0F * 256.0F), (byte)(int)(a4 / 360.0F * 256.0F));
  }

  public static float unpackAnglesDeg_1(int i)
  {
    return unpackBytes_1(i) * 360.0F / 256.0F;
  }

  public static float unpackAnglesDeg_2(int i)
  {
    return unpackBytes_2(i) * 360.0F / 256.0F;
  }

  public static float unpackAnglesDeg_3(int i)
  {
    return unpackBytes_3(i) * 360.0F / 256.0F;
  }

  public static float unpackAnglesDeg_4(int i)
  {
    return unpackBytes_4(i) * 360.0F / 256.0F;
  }

  public static int packBytes(int i1, int i2, int i3, int i4)
  {
    return i1 << 24 & 0xFF000000 | i2 << 16 & 0xFF0000 | i3 << 8 & 0xFF00 | i4 & 0xFF;
  }

  public static byte unpackBytes_1(int i)
  {
    return (byte)(i >>> 24);
  }

  public static byte unpackBytes_2(int i)
  {
    return (byte)(i >>> 16 & 0xFF);
  }

  public static byte unpackBytes_3(int i)
  {
    return (byte)(i >>> 8 & 0xFF);
  }

  public static byte unpackBytes_4(int i)
  {
    return (byte)(i & 0xFF);
  }

  public static int packShorts(int i1, int i2)
  {
    return i1 << 16 | i2 & 0xFFFF;
  }

  public static short unhopackSrts_1(int i)
  {
    return (short)(i >>> 16);
  }

  public static int unpackShorts_2(int i)
  {
    return (short)(i & 0xFFFF);
  }
}