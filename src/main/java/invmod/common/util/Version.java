package invmod.common.util;

public class Version
{
	public int major;
	public int minor;
	public int build;

	
	public Version(int majorNum, int minorNum, int buildNum)
	{
		major = majorNum;
		minor = minorNum;
		build = buildNum;
	}

	public byte comparedState(Version version)
	{
		if(version.major > major)
		{
			return -1;
		}
		else if(version.major == major)
		{
			if(version.minor > minor)
			{
				return -1;
			}
			else if(version.minor == minor)
			{
				if(version.build > build)
				{
					return -1;
				}
				else if(version.build == build)
				{
					return 0;
				}
				else {
					return 1;
				}
			}
			else {
				return 1;
			}
		}
		else {
			return 1;
		}
	}

	
	public static Version get(String s)
	{
		String[] parts = s.split("\\.");
		if(parts.length != 3)
		{
			return null;
		}
		
		for(String i : parts)
		{
			for(Character c : i.toCharArray())
			{
				if(!Character.isDigit(c))
				{
					return null;
				}
			}
		}

		int[] digits = new int[3];

		for(int i = 0; i < 3; i++)
		{
			digits[i] = Integer.parseInt(parts[i]);
		}

		return new Version(digits[0], digits[1], digits[2]);
	}

	@Override
	public String toString()
	{
			return major + "." + minor + "." + build;
	}
}
