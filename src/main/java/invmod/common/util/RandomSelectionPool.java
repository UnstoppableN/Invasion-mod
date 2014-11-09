package invmod.common.util;

import invmod.common.mod_Invasion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSelectionPool<EntityIMLiving> implements ISelect<EntityIMLiving> 
{
	private List<Pair<ISelect<EntityIMLiving>, Float>> pool;
	private float totalWeight;
	private Random rand;

	public RandomSelectionPool() 
	{
		this.pool = new ArrayList();
		this.totalWeight = 0.0F;
		this.rand = new Random();
	}

	public void addEntry(EntityIMLiving entry, float weight) 
	{
		SingleSelection selection = new SingleSelection(entry);
		addEntry(selection, weight);
	}

	public void addEntry(ISelect<EntityIMLiving> entry, float weight) 
	{
		this.pool.add(new Pair(entry, Float.valueOf(weight)));
		this.totalWeight += weight;
	}

	public EntityIMLiving selectNext() 
	{
		float r = this.rand.nextFloat() * this.totalWeight;
		for (Pair entry : this.pool) 
		{
			if (r < ((Float) entry.getVal2()).floatValue()) 
			{
				return (EntityIMLiving) ((ISelect) entry.getVal1()).selectNext();
			}

			r -= ((Float) entry.getVal2()).floatValue();
		}

		if (this.pool.size() > 0)
		{
			mod_Invasion.log("RandomSelectionPool invalid setup or rounding error. Failing safe.");
			return (EntityIMLiving) ((ISelect) ((Pair) this.pool.get(0)).getVal1()).selectNext();
		}
		return null;
	}

	public RandomSelectionPool<EntityIMLiving> clone() 
	{
		RandomSelectionPool clone = new RandomSelectionPool();
		for (Pair entry : this.pool) 
		{
			clone.addEntry((ISelect) entry.getVal1(), ((Float) entry.getVal2()).floatValue());
		}

		return clone;
	}

	public void reset() {
	}

	public String toString() 
	{
		String s = "RandomSelectionPool@" + Integer.toHexString(hashCode()) + "#Size=" + this.pool.size();
		for (int i = 0; i < this.pool.size(); i++) 
		{
			s = s + "\n\tEntry " + i + "   Weight: " + ((Pair) this.pool.get(i)).getVal2();
			s = s + "\n\t" + ((ISelect) ((Pair) this.pool.get(i)).getVal1()).toString();
		}
		return s;
	}
}