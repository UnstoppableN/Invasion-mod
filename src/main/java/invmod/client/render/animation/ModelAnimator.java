package invmod.client.render.animation;

import invmod.common.util.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.model.ModelRenderer;

public class ModelAnimator<T extends Enum<T>> {
	private List<Triplet<ModelRenderer, Integer, List<KeyFrame>>> parts;
	private float animationPeriod;

	public ModelAnimator() {
		this(1.0F);
	}

	public ModelAnimator(float animationPeriod) {
		this.animationPeriod = animationPeriod;
		this.parts = new ArrayList(1);
	}

	public ModelAnimator(Map<T, ModelRenderer> modelParts, Animation<T> animation) {
		this.animationPeriod = animation.getAnimationPeriod();
		this.parts = new ArrayList(((Enum[]) animation.getSkeletonType().getEnumConstants()).length);
		for (Map.Entry entry : modelParts.entrySet()) {
			List keyFrames = animation.getKeyFramesFor((T) entry.getKey());
			if (keyFrames != null) {
				this.parts.add(new Triplet(entry.getValue(), Integer.valueOf(0), keyFrames));
			}
		}
	}

	public void addPart(ModelRenderer part, List<KeyFrame> keyFrames) {
		if (validate(keyFrames)) {
			this.parts.add(new Triplet(part, Integer.valueOf(0), keyFrames));
		}
	}

	public void clearParts() {
		this.parts.clear();
	}

	public void updateAnimation(float newTime) {
		for (Triplet entry : this.parts) {
			int prevIndex = ((Integer) entry.getVal2()).intValue();
			List keyFrames = (List) entry.getVal3();
			KeyFrame prevFrame = (KeyFrame) keyFrames.get(prevIndex++);
			KeyFrame nextFrame = null;

			if (prevFrame.getTime() <= newTime) {
				for (; prevIndex < keyFrames.size(); prevIndex++) {
					KeyFrame keyFrame = (KeyFrame) keyFrames.get(prevIndex);
					if (newTime < keyFrame.getTime()) {
						nextFrame = keyFrame;
						prevIndex--;
						break;
					}

					prevFrame = keyFrame;
				}

				if (prevIndex >= keyFrames.size()) {
					prevIndex = keyFrames.size() - 1;
					nextFrame = (KeyFrame) keyFrames.get(0);
				}
			} else {
				for (prevIndex = 0; prevIndex < keyFrames.size(); prevIndex++) {
					KeyFrame keyFrame = (KeyFrame) keyFrames.get(prevIndex);
					if (newTime < keyFrame.getTime()) {
						nextFrame = keyFrame;
						prevIndex--;
						prevFrame = (KeyFrame) keyFrames.get(prevIndex);
						break;
					}
				}
			}
			entry.setVal2(Integer.valueOf(prevIndex));
			interpolate(prevFrame, nextFrame, newTime, (ModelRenderer) entry.getVal1());
		}
	}

	private void interpolate(KeyFrame prevFrame, KeyFrame nextFrame, float time, ModelRenderer part) {
		if (prevFrame.getInterpType() == InterpType.LINEAR) {
			float dtPrev = time - prevFrame.getTime();
			float dtFrame = nextFrame.getTime() - prevFrame.getTime();
			if (dtFrame < 0.0F) {
				dtFrame += this.animationPeriod;
			}

			float r = dtPrev / dtFrame;
			part.rotateAngleX = (prevFrame.getRotX() + r * (nextFrame.getRotX() - prevFrame.getRotX()));
			part.rotateAngleY = (prevFrame.getRotY() + r * (nextFrame.getRotY() - prevFrame.getRotY()));
			part.rotateAngleZ = (prevFrame.getRotZ() + r * (nextFrame.getRotZ() - prevFrame.getRotZ()));

			if (prevFrame.hasPos()) {
				if (nextFrame.hasPos()) {
					part.rotationPointX = (prevFrame.getPosX() + r * (nextFrame.getPosX() - prevFrame.getPosX()));
					part.rotationPointY = (prevFrame.getPosY() + r * (nextFrame.getPosY() - prevFrame.getPosY()));
					part.rotationPointZ = (prevFrame.getPosZ() + r * (nextFrame.getPosZ() - prevFrame.getPosZ()));
				} else {
					part.rotationPointX = prevFrame.getPosX();
					part.rotationPointY = prevFrame.getPosY();
					part.rotationPointZ = prevFrame.getPosZ();
				}
			}
		}
	}

	private boolean validate(List<KeyFrame> keyFrames) {
		if (keyFrames.size() < 2) {
			return false;
		}
		if (((KeyFrame) keyFrames.get(0)).getTime() != 0.0F) {
			return false;
		}
		int prevTime = 0;
		for (int i = 1; i < keyFrames.size(); i++) {
			if (((KeyFrame) keyFrames.get(i)).getTime() <= prevTime) {
				return false;
			}
		}
		return true;
	}
}