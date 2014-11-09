package invmod.common.util;

import java.util.Comparator;

import net.minecraft.entity.Entity;

public class ComparatorEntityDistanceFrom implements Comparator<Entity> {
	private double x;
	private double y;
	private double z;

	public ComparatorEntityDistanceFrom(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int compare(Entity entity1, Entity entity2) {
		double d1 = (this.x - entity1.posX) * (this.x - entity1.posX) + (this.y - entity1.posY) * (this.y - entity1.posY) + (this.z - entity1.posZ) * (this.z - entity1.posZ);
		double d2 = (this.x - entity2.posX) * (this.x - entity2.posX) + (this.y - entity2.posY) * (this.y - entity2.posY) + (this.z - entity2.posZ) * (this.z - entity2.posZ);
		if (d1 > d2)
			return -1;
		if (d1 < d2) {
			return 1;
		}
		return 0;
	}
}