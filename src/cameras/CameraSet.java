package cameras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class CameraSet {

	private final Camera frontCam;
	private final Camera sideCam;
	private final Camera topCam;

	private CameraSet(Camera frontCam, Camera sideCam, Camera topCam) {
		this.frontCam = frontCam;
		this.sideCam = sideCam;
		this.topCam = topCam;
	}

	public Camera getFrontCam() {
		return frontCam;
	}

	public Camera getSideCam() {
		return sideCam;
	}

	public Camera getTopCam() {
		return topCam;
	}

	public void addData(ScreenShot shot1, ScreenShot shot2, ScreenShot shot3) {
		try {
			getSideCam().addData(shot1);
			getFrontCam().addData(shot2);
			getTopCam().addData(shot3);
		} catch (Camera.ChangeDetectedException e) {
			throw new IllegalArgumentException(e);
		}

	}

	public static Builder getBuilder() {
		return new Builder();
	}

	public static final class Builder {

		private int[][] packages;
		private int height;

		private static Boolean[] apply(int[] row) {
			List<Boolean> result = new ArrayList<>();
			for (int i : row) {
				if (i>0){
					result.add(true);
				}
			}
			return result.toArray(new Boolean[0]);
		}

		public Builder setHeight(int height) {
			this.height = height;
			return this;
		}

		public Builder setPackages(int[][] packages) {
			validate(packages);
			this.packages = packages;
			return this;
		}

		private void validate(int[][] packages) {
			Objects.requireNonNull(packages);
			for (int[] aPackage : packages) {
				IntStream.range(0, packages[0].length).filter(curCol -> aPackage[curCol] < 0).forEach(curCol -> {
					throw new IllegalArgumentException("The height of packages cannot be less that 0");
				});
				// Why need validation smh QA
			}
		}
//
//		private int applyAsInt(int i) {
//			boolean seen = false;
//			int best = 0;
//			for (int[] row : packages) {
//				int j = row[i];
//				if (!seen || j > best) {
//					seen = true;
//					best = j;
//				}
//			}
//			return seen ? best : 0;
//		}
//
//		private Boolean[] apply(int count) {
//			List<Boolean> list = new ArrayList<>();
//			int bound = height;
//			for (int i = 0; i < bound; i++) {
//				Boolean aBoolean = i < count + 1;
//				list.add(aBoolean);
//			}
//			return list.toArray(new Boolean[0]);
//		}
//
//		private ScreenShot setSide(){
//			List<Boolean[]> list = new ArrayList<>();
//			for (int[] arr : packages) {
//				boolean seen = false;
//				int best = 0;
//				for (int i : arr) {
//					if (!seen || i > best) {
//						seen = true;
//						best = i;
//					}
//				}
//				int count = seen ? best : 0;
//				Boolean[] booleans = apply(count);
//				list.add(booleans);
//			}
//			return ScreenShot.of(list.toArray(new Boolean[0][]));
//		}

		public CameraSet build() {



			// Such an over-complicated algorithm! Really hope I didn't mess up anywhere
			// I imagine the Screenshot for sided as being flipped 90 deg for ease of implementation
//
//			List<Boolean[]> result = new ArrayList<>();
//			int bound = packages.length;
//			for (int i = 0; i < bound; i++) {
//				int applyAsInt = applyAsInt(i);
//				Boolean[] booleans = apply(applyAsInt);
//				result.add(booleans);
//			}
//			ScreenShot front = ScreenShot.of(result.toArray(new Boolean[0][]));
//
//			ScreenShot side = setSide();
//
//			List<Boolean[]> list = new ArrayList<>();
//			for (int[] ints : packages) {
//				Boolean[] apply = apply(ints);
//				list.add(apply);
//			}
//			ScreenShot top = ScreenShot.of(list.toArray(new Boolean[0][]));
//
//			return new CameraSet(Camera.getBuilder().setScreenShot(front).setSide(true).build(),
//				Camera.getBuilder().setScreenShot(top).setSide(true).build(),
//				Camera.getBuilder().setScreenShot(side).setSide(false).build());
		}


	}

}
