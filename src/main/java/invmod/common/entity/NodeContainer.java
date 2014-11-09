package invmod.common.entity;

public class NodeContainer {
	private PathNode[] pathPoints;
	private int count;

	public NodeContainer() {
		this.pathPoints = new PathNode[1024];
		this.count = 0;
	}

	public PathNode addPoint(PathNode pathpoint) {
		if (pathpoint.index >= 0) {
			throw new IllegalStateException("OW KNOWS!");
		}
		if (this.count == this.pathPoints.length) {
			PathNode[] apathpoint = new PathNode[this.count << 1];
			System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
			this.pathPoints = apathpoint;
		}
		this.pathPoints[this.count] = pathpoint;
		pathpoint.index = this.count;
		sortBack(this.count++);
		return pathpoint;
	}

	public void clearPath() {
		this.count = 0;
	}

	public PathNode dequeue() {
		PathNode pathpoint = this.pathPoints[0];
		this.pathPoints[0] = this.pathPoints[(--this.count)];
		this.pathPoints[this.count] = null;
		if (this.count > 0) {
			sortForward(0);
		}
		pathpoint.index = -1;
		return pathpoint;
	}

	public void changeDistance(PathNode pathpoint, float f) {
		float f1 = pathpoint.distanceToTarget;
		pathpoint.distanceToTarget = f;
		if (f < f1) {
			sortBack(pathpoint.index);
		} else {
			sortForward(pathpoint.index);
		}
	}

	private void sortBack(int i) {
		PathNode pathpoint = this.pathPoints[i];
		float f = pathpoint.distanceToTarget;

		while (i > 0) {
			int j = i - 1 >> 1;
			PathNode pathpoint1 = this.pathPoints[j];
			if (f >= pathpoint1.distanceToTarget) {
				break;
			}
			this.pathPoints[i] = pathpoint1;
			pathpoint1.index = i;
			i = j;
		}

		this.pathPoints[i] = pathpoint;
		pathpoint.index = i;
	}

	private void sortForward(int i) {
		PathNode pathpoint = this.pathPoints[i];
		float f = pathpoint.distanceToTarget;
		while (true) {
			int j = 1 + (i << 1);
			int k = j + 1;
			if (j >= this.count) {
				break;
			}
			PathNode pathpoint1 = this.pathPoints[j];
			float f1 = pathpoint1.distanceToTarget;
			float f2;
			PathNode pathpoint2;
			if (k >= this.count) {
				pathpoint2 = null;
				f2 = (1.0F / 1.0F);
			} else {
				pathpoint2 = this.pathPoints[k];
				f2 = pathpoint2.distanceToTarget;
			}
			if (f1 < f2) {
				if (f1 >= f) {
					break;
				}
				this.pathPoints[i] = pathpoint1;
				
				pathpoint1.index = i;
				i = j;
			} else {
				
				if (f2 >= f) {
					break;
				}
				//Unstoppable Custom Testcode
				//this seems to temp fix mobs not being able to spawn.
				if(pathpoint2==null){
					break;
				}
				//end Unstoppable Custom Testcode
				this.pathPoints[i] = pathpoint2;
				pathpoint2.index = i;
				i = k;
			}
		}
		this.pathPoints[i] = pathpoint;
		pathpoint.index = i;
	}

	public boolean isPathEmpty() {
		return this.count == 0;
	}
}