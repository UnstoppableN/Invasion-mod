package invmod.client.render.animation;

import invmod.common.util.MathUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class KeyFrame {
	private float time;
	private float rotX;
	private float rotY;
	private float rotZ;
	private float posX;
	private float posY;
	private float posZ;
	private InterpType interpType;
	private float[][] mods;
	private boolean hasPos;

	public KeyFrame(float time, float rotX, float rotY, float rotZ, InterpType interpType) {
		this(time, rotX, rotY, rotZ, 0.0F, 0.0F, 0.0F, interpType);
		this.hasPos = false;
	}

	public KeyFrame(float time, float rotX, float rotY, float rotZ, float posX, float posY, float posZ, InterpType interpType) {
		this.time = time;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.interpType = interpType;
		this.hasPos = true;
	}

	public float getTime() {
		return this.time;
	}

	public float getRotX() {
		return this.rotX;
	}

	public float getRotY() {
		return this.rotY;
	}

	public float getRotZ() {
		return this.rotZ;
	}

	public float getPosX() {
		return this.posX;
	}

	public float getPosY() {
		return this.posY;
	}

	public float getPosZ() {
		return this.posZ;
	}

	public InterpType getInterpType() {
		return this.interpType;
	}

	public boolean hasPos() {
		return this.hasPos;
	}

	public String toString() {
		return "(" + this.time + ", " + this.rotX + ", " + this.rotY + ", " + this.rotZ + ")";
	}

	public static List<KeyFrame> cloneFrames(List<KeyFrame> keyFrames) {
		return new ArrayList(keyFrames);
	}

	public static void toRadians(List<KeyFrame> keyFrames) {
		ListIterator iter = keyFrames.listIterator();
		while (iter.hasNext()) {
			float radDeg = 0.01745329F;
			KeyFrame keyFrame = (KeyFrame) iter.next();
			KeyFrame newFrame = new KeyFrame(keyFrame.getTime(), keyFrame.getRotX() * radDeg, keyFrame.getRotY() * radDeg, keyFrame.getRotZ() * radDeg, keyFrame.getPosX(), keyFrame.getPosY(), keyFrame.getPosZ(), keyFrame.getInterpType());

			newFrame.hasPos = keyFrame.hasPos;
			iter.set(newFrame);
		}
	}

	public static void mirrorFramesX(List<KeyFrame> keyFrames) {
		ListIterator iter = keyFrames.listIterator();
		while (iter.hasNext()) {
			KeyFrame keyFrame = (KeyFrame) iter.next();
			KeyFrame newFrame = new KeyFrame(keyFrame.getTime(), keyFrame.getRotX(), -keyFrame.getRotY(), -keyFrame.getRotZ(), -keyFrame.getPosX(), keyFrame.getPosY(), keyFrame.getPosZ(), keyFrame.getInterpType());

			newFrame.hasPos = keyFrame.hasPos;
			iter.set(newFrame);
		}
	}

	public static void mirrorFramesY(List<KeyFrame> keyFrames) {
		ListIterator iter = keyFrames.listIterator();
		while (iter.hasNext()) {
			KeyFrame keyFrame = (KeyFrame) iter.next();
			KeyFrame newFrame = new KeyFrame(keyFrame.getTime(), -keyFrame.getRotX(), keyFrame.getRotY(), -keyFrame.getRotZ(), keyFrame.getPosX(), -keyFrame.getPosY(), keyFrame.getPosZ(), keyFrame.getInterpType());

			newFrame.hasPos = keyFrame.hasPos;
			iter.set(newFrame);
		}
	}

	public static void mirrorFramesZ(List<KeyFrame> keyFrames) {
		ListIterator iter = keyFrames.listIterator();
		while (iter.hasNext()) {
			KeyFrame keyFrame = (KeyFrame) iter.next();
			KeyFrame newFrame = new KeyFrame(keyFrame.getTime(), -keyFrame.getRotX(), -keyFrame.getRotY(), keyFrame.getRotZ(), keyFrame.getPosX(), keyFrame.getPosY(), -keyFrame.getPosZ(), keyFrame.getInterpType());

			newFrame.hasPos = keyFrame.hasPos;
			iter.set(newFrame);
		}
	}

	public static void offsetFramesCircular(List<KeyFrame> keyFrames, float start, float end, float offset) {
		if (keyFrames.size() < 1) {
			return;
		}
		float diff = end - start;
		offset %= diff;
		float k1 = end - offset;
		List copy = cloneFrames(keyFrames);
		keyFrames.clear();
		KeyFrame currFrame = null;
		ListIterator iter = copy.listIterator();

		while (iter.hasNext()) {
			currFrame = (KeyFrame) iter.next();
			if (currFrame.getTime() >= start)
				break;
			keyFrames.add(currFrame);
		}

		List buffer = new ArrayList();
		buffer.add(currFrame);
		while (iter.hasNext()) {
			currFrame = (KeyFrame) iter.next();
			if (currFrame.getTime() >= k1) {
				break;
			}
			buffer.add(currFrame);
		}
		KeyFrame fencepostStart;
		if (!MathUtil.floatEquals(currFrame.getTime(), k1, 0.001F)) {
			iter.previous();
			KeyFrame prev = (KeyFrame) iter.previous();

			float dt = k1 - prev.getTime();
			float dtFrame = currFrame.getTime() - prev.getTime();
			float r = dt / dtFrame;
			float x = prev.getRotX() + r * (currFrame.getRotX() - prev.getRotX());
			float y = prev.getRotY() + r * (currFrame.getRotY() - prev.getRotY());
			float z = prev.getRotZ() + r * (currFrame.getRotZ() - prev.getRotZ());
			fencepostStart = new KeyFrame(start, x, y, z, InterpType.LINEAR);
		} else {
			fencepostStart = currFrame;
		}

		keyFrames.add(fencepostStart);

		while (iter.hasNext()) {
			currFrame = (KeyFrame) iter.next();
			if (currFrame.getTime() <= end) {
				float t = currFrame.getTime() + offset - diff;
				KeyFrame newFrame = new KeyFrame(t, currFrame.getRotX(), currFrame.getRotY(), currFrame.getRotZ(), currFrame.getPosX(), currFrame.getPosY(), currFrame.getPosZ(), InterpType.LINEAR);

				newFrame.hasPos = currFrame.hasPos;
				keyFrames.add(newFrame);
			} else {
				//UnstoppableN testcode, this seemed to fix some issues, not sure why
				//iter.previous();
			}

		}

		Iterator iter2 = buffer.iterator();
		while (iter2.hasNext()) {
			currFrame = (KeyFrame) iter2.next();
			float t = currFrame.getTime() + offset;
			KeyFrame newFrame = new KeyFrame(t, currFrame.getRotX(), currFrame.getRotY(), currFrame.getRotZ(), currFrame.getPosX(), currFrame.getPosY(), currFrame.getPosZ(), InterpType.LINEAR);

			newFrame.hasPos = currFrame.hasPos;
			keyFrames.add(newFrame);
		}

		keyFrames.add(new KeyFrame(end, fencepostStart.getRotX(), fencepostStart.getRotY(), fencepostStart.getRotZ(), InterpType.LINEAR));

		while (iter.hasNext()) {
			keyFrames.add((KeyFrame) iter.next());
		}
	}
}