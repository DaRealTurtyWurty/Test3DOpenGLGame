package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import models.TexturedModel;
import normalMappingRenderer.NormalMappingRenderer;
import normalMappingRenderer.NormalMappingShader;
import objects.Camera;
import objects.Entity;
import objects.Light;
import shaders.StaticShader;
import shadows.ShadowBox;
import shadows.ShadowMapMasterRenderer;
import skybox.SkyboxRenderer;
import skybox.SkyboxShader;
import terrains.Terrain;
import terrains.TerrainRenderer;
import terrains.TerrainShader;

public class MasterRenderer {

	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;

	public static final float RED = 0.6f;
	public static final float GREEN = 0.78f;
	public static final float BLUE = 0.76f;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();

	private SkyboxRenderer skyboxRenderer;
	private SkyboxShader skyboxShader = new SkyboxShader();

	private NormalMappingRenderer normalMapRenderer;
	private NormalMappingShader normalMapShader = new NormalMappingShader();

	private ShadowMapMasterRenderer shadowRenderer;

	private GuiRenderer guiRenderer;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public MasterRenderer(ModelLoader loader, Camera camera) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
		shadowRenderer = new ShadowMapMasterRenderer(camera);
		guiRenderer = new GuiRenderer(loader);
	}

	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		shader.loadShadow(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
		renderer.render(entities, shadowRenderer.getToShadowMapSpaceMatrix());
		shader.stop();
		normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadShadow(ShadowBox.SHADOW_DISTANCE, ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
		terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		terrains.clear();
		entities.clear();
		normalMapEntities.clear();
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	public void processNormalMapEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalMapEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapEntities.put(entityModel, newBatch);
		}
	}

	public void renderShadowMap(List<Entity> entityList, Light light) {
		for (Entity entity : entityList) {
			processEntity(entity);
		}
		shadowRenderer.render(entities, light);
		entities.clear();
	}

	public int getShadowMapTexture() {
		return shadowRenderer.getShadowMap();
	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		skyboxShader.cleanUp();
		normalMapRenderer.cleanUp();
		shadowRenderer.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE6);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowRenderer.getShadowMap());
	}

	private void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void renderScene(List<Entity> entities, List<Entity> normalEntities, List<Terrain> terrains,
			List<Light> lights, Camera camera, Vector4f clipPlane) {
		for (Terrain terrain : terrains) {
			processTerrain(terrain);
		}

		for (Entity entity : entities) {
			processEntity(entity);
		}

		for (Entity entity : normalEntities) {
			processNormalMapEntity(entity);
		}

		render(lights, camera, clipPlane);
	}
}
