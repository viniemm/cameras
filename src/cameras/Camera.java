package cameras;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Camera {

	private final List<ScreenShot> data;
	private final Boolean isSide;

	public ScreenShot getTrueScreenshot() {
		return trueScreenshot;
	}

	private final ScreenShot trueScreenshot;
	private int diffCounter;

	private final static int MAX_DIFF = 1;

	private Camera(ScreenShot trueScreenshot, boolean isSide) {
		this.trueScreenshot = trueScreenshot;
		this.data = new ArrayList<>(List.of(this.trueScreenshot));
		this.isSide = isSide;
		diffCounter = 0;
	}

	public boolean isSide() {
		return isSide;
	}

	public List<ScreenShot> getData() {
		return new ArrayList<>(data);
	}

	public void addData(ScreenShot newS) throws ChangeDetectedException {
		Objects.requireNonNull(newS);
		newS.validate();
		ScreenShot filteredS = newS;
		data.add(newS);
		if (isSide()) {
			filteredS = ScreenShot.removeFloat(filteredS);
		}

		if (ScreenShot.isShifted(trueScreenshot, filteredS)) {
			diffCounter = 0;
//			changed diffCounter++ to ++dffCounter because increment was needed before checking conditional.
		} else if (++diffCounter > MAX_DIFF) {
			throw Camera.getExceptionBuilder()
				.setBefore(trueScreenshot)
				.setAfter(newS)
				.build();
		}

	}

	public static Builder getBuilder() {
		return new Builder();
	}

	public static final class Builder {
		private ScreenShot data = null;
		private Boolean isSide = null;

		public Builder setScreenShot(ScreenShot data) {
			// No validation needed. Sure that the user did the right thing
			// Validation definitely needed. Added requireNonNull and package-private validate method
			if (Objects.isNull(data)) {
				throw new IllegalArgumentException();
			}
			data.validate();
			this.data = data;
			return this;
		}

		public Builder setSide(Boolean side) {
			Objects.requireNonNull(side);
			isSide = side;
			return this;
		}

		public Camera build() {
			Objects.requireNonNull(data);
			Objects.requireNonNull(isSide);
			return new Camera(data, isSide);
		}
	}

	static ChangeDetectedException.Builder getExceptionBuilder() {
		return new ChangeDetectedException.Builder();
	}

	public static final class ChangeDetectedException extends Exception {

		private final ScreenShot before;
		private final ScreenShot after;

		private ChangeDetectedException(ScreenShot before, ScreenShot after) {
			super("Error: changes detected from the data: \n" + before.toString() + "\n to " + after.toString() + "\n");
			this.before = before;
			this.after = after;
		}

		public ScreenShot getBefore() {
			return before;
		}

		public ScreenShot getAfter() {
			return after;
		}

		static final class Builder {

			private ScreenShot before;
			private ScreenShot after;

			public Builder setBefore(ScreenShot before) {
				this.before = before;
				return this;
			}

			public Builder setAfter(ScreenShot after) {
				this.after = after;
				return this;
			}

			public ChangeDetectedException build() {
				return new ChangeDetectedException(this.after, this.before);
			}
		}


	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Camera) {
			return this.getTrueScreenshot().equals(((Camera) other).getTrueScreenshot())
				&& this.isSide() == ((Camera) other).isSide();
		} else return false;
	}

}