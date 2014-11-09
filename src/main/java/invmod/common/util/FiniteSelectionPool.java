package invmod.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FiniteSelectionPool<T> implements ISelect<T> {
	private List<Pair<ISelect<T>, Integer>> currentPool;
	private List<Integer> originalPool;
	private int totalAmount;
	private int originalAmount;
	private Random rand;

	public FiniteSelectionPool() {
		this.currentPool = new ArrayList();
		this.originalPool = new ArrayList();
		this.totalAmount = 0;
		this.rand = new Random();
	}

	public void addEntry(T entry, int amount) {
		SingleSelection selection = new SingleSelection(entry);
		addEntry(selection, amount);
	}

	public void addEntry(ISelect<T> entry, int amount) {
		this.currentPool.add(new Pair(entry, Integer.valueOf(amount)));
		this.originalPool.add(Integer.valueOf(amount));
		this.originalAmount = (this.totalAmount += amount);
	}

	public T selectNext() {
		if (this.totalAmount < 1) {
			regeneratePool();
		}
		float r = this.rand.nextInt(this.totalAmount);
		for (Pair entry : this.currentPool) {
			int amountLeft = ((Integer) entry.getVal2()).intValue();
			if (r < amountLeft) {
				entry.setVal2(Integer.valueOf(amountLeft - 1));
				this.totalAmount -= 1;
				return (T) ((ISelect) entry.getVal1()).selectNext();
			}

			r -= amountLeft;
		}

		return null;
	}

	public FiniteSelectionPool<T> clone() {
		FiniteSelectionPool clone = new FiniteSelectionPool();
		for (int i = 0; i < this.currentPool.size(); i++) {
			clone.addEntry((ISelect) ((Pair) this.currentPool.get(i)).getVal1(), ((Integer) this.originalPool.get(i)).intValue());
		}

		return clone;
	}

	public void reset() {
		regeneratePool();
	}

	public String toString() {
		String s = "FiniteSelectionPool@" + Integer.toHexString(hashCode()) + "#Size=" + this.currentPool.size();
		for (int i = 0; i < this.currentPool.size(); i++) {
			s = s + "\n\tEntry " + i + "   Amount: " + this.originalPool.get(i);
			s = s + "\n\t" + ((ISelect) ((Pair) this.currentPool.get(i)).getVal1()).toString();
		}
		return s;
	}

	private void regeneratePool() {
		this.totalAmount = this.originalAmount;
		for (int i = 0; i < this.currentPool.size(); i++) {
			((Pair) this.currentPool.get(i)).setVal2(this.originalPool.get(i));
		}
	}
}