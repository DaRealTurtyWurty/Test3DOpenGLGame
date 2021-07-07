package terrains;

import java.util.Random;

public class HeightGenerator {

	private static final float AMPLITUDE = 70.0f;
	private Random random = new Random();
	private long seed;

	public HeightGenerator() {
		this.seed = ((long) random.nextInt(1000000000));
	}

	@SuppressWarnings("unused")
	public float generateHeight(int x, int z) {
		float hillsBiome = getInterpolatedNoise(x / 16f, z / 16f) * AMPLITUDE;
		hillsBiome += getInterpolatedNoise(x / 8f, z / 8f) * AMPLITUDE / 3f;
		hillsBiome += getInterpolatedNoise(x / 4f, z / 4f) * AMPLITUDE / 9f;

		float plainsBiome = getInterpolatedNoise(x / 16f, z / 16f) * AMPLITUDE / 2.5f;

		float mountainBiome = getInterpolatedNoise(x / 8f, z / 8f) * -AMPLITUDE * 2f;

		float testBiome = (getInterpolatedNoise(x / 32f, z / 32f) * AMPLITUDE) - 25.0f;

		return mountainBiome;
	}

	public long getSeed() {
		return seed;
	}

	protected void setSeed(long seed) {
		this.seed = seed;
	}

	private float getNoise(int x, int z) {
		random.setSeed(x * 49632 * +z * 325176 + seed);
		return random.nextFloat() * 2.0f - 1.0f;
	}

	private float getSmoothedNoise(int x, int z) {
		float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1)
				+ getNoise(x + 1, z + 1)) / 16.0f;
		float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z + 1) + getNoise(x, z - 1)) / 8.0f;
		float center = getNoise(x, z) / 4.0f;
		return corners + sides + center;
	}

	private float interpolate(float x, float z, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1.0f - Math.cos(theta)) * 0.5f;
		return x * (1.0f - f) + z * f;
	}

	private float getInterpolatedNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;
		float v1 = getSmoothedNoise(intX, intZ);
		float v2 = getSmoothedNoise(intX + 1, intZ);
		float v3 = getSmoothedNoise(intX, intZ + 1);
		float v4 = getSmoothedNoise(intX + 1, intZ + 1);
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		return interpolate(i1, i2, fracZ);
	}
}
