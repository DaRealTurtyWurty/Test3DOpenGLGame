package particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import objects.Camera;
import renderEngine.ModelLoader;
import textures.ParticleTexture;

public class ParticleMaster {

	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private static ParticleRenderer renderer;

	public static void init(ModelLoader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}

	public static void update(Camera camera) {
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			Entry<ParticleTexture, List<Particle>> entry = mapIterator.next();
			List<Particle> list = entry.getValue();
			Iterator<Particle> iterator = list.iterator();
			while (iterator.hasNext()) {
				Particle particle = iterator.next();
				boolean alive = particle.update(camera);
				if (!alive) {
					iterator.remove();
					if (list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
			if(!entry.getKey().isAdditive()) {
				InsertionSort.sortHighToLow(list);
			}
		}
	}

	public static void renderParticles(Camera camera) {
		renderer.render(particles, camera);
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

	public static void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getTexture());
		if (list == null) {
			list = new ArrayList<Particle>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}
}
