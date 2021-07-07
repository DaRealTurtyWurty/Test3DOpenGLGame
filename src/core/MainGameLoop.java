package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import font.TextMaster;
import models.TexturedModel;
import objects.Camera;
import objects.Entity;
import objects.ItemEntity;
import objects.Light;
import particles.ComplexParticleSystem;
import particles.ParticleMaster;
import player.Player;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.GuiRenderer;
import renderEngine.MasterRenderer;
import renderEngine.ModelLoader;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.GuiTexture;
import textures.HotbarSlotGui;
import textures.ModelTexture;
import textures.ParticleTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import util.ItemStack;
import util.Items;
import util.Maths;
import util.Slot;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {

	public static List<ItemEntity> items = new ArrayList<ItemEntity>();

	public static void main(String[] args) throws IOException {
		DisplayManager.createDisplay();
		ModelLoader loader = new ModelLoader();
		TextMaster.init(loader);

		TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("person", loader),
				new ModelTexture(loader.loadTexture("playerTexture", -0.4f)));

		Player player = new Player(playerModel, new Vector3f(100, 5, -150), 0, 180, 0, 0.6f);
		player.getHotbar().setStackInSlot(new ItemStack(Items.STICK), 2);

		Camera camera = new Camera(player);

		MasterRenderer renderer = new MasterRenderer(loader, camera);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());

//		FontType font = new FontType(loader.loadTexture("fonts/candara", 0.0f), new File("res/fonts/candara.fnt"));
//		GUIText text = new GUIText("Lorem Ipsum", 3, font, new Vector2f(0, 0), 1f, true, true);
//		text.setColour(0.7f, 0.3f, 1.0f);
//		text.setOutlineColour(1.0f, 0.0f, 0.0f);

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2", -0.4f));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("sand", -0.4f));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassy", -0.4f));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path", -0.4f));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", 0));

		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
				new ModelTexture(loader.loadTexture("lowPolyTree", -0.4f)));
		bobble.getTexture().setHasTransparency(true);

		List<Terrain> terrains = new ArrayList<Terrain>();
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		terrains.add(terrain);

		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalEntities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 400; i++) {
			/*
			 * if (i % 3 == 0) { float x = random.nextFloat() * 800; float z =
			 * random.nextFloat() * -600; float y = terrain.getHeightOfTerrain(x, z);
			 * 
			 * entities.add(new Entity(grass, random.nextInt(4), new Vector3f(x, y, z), 0,
			 * random.nextFloat() * 360, 0, 2.5f)); } else
			 */
			if (i % 1 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				if (y > -10) {
					entities.add(new Entity(bobble, random.nextInt(4), new Vector3f(x, y, z), 0,
							random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 3f));
				}
			}
		}

		entities.add(player);

		for (ItemEntity item : items) {
			entities.add(item);
		}

		Light light = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		// lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new
		// Vector3f(1, 0.01f, 0.002f)));
		// lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new
		// Vector3f(1, 0.01f, 0.002f)));

		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();

		float index = 0.0f;
		for (Slot slot : player.getHotbar().getSlots()) {
			index += 0.0275f;
			Vector2f slotPos = new Vector2f(0.952f,
					0.6f - (((float) slot.getSlotIndex())) / Display.getPixelScaleFactor() / 8 - index);
			HotbarSlotGui slotGui = new HotbarSlotGui(player, slot.getStack(),
					!slot.getStack().isEmpty() ? loader.loadTexture(slot.getStack().getItem().getRegistryName(), 0.0f)
							: 0,
					slot.getSlotIndex(), loader.loadTexture("slot", 0.0f), loader.loadTexture("selected", 0.0f),
					slotPos, new Vector2f(0.1f, 0.1f));

			if (!slotGui.getStack().isEmpty() && slotGui.getStackTexture() != 0) {
				guiTextures.add(new GuiTexture(slotGui.getStackTexture(), slotPos, new Vector2f(0.1f, 0.1f)));
			}

			guiTextures.add(slotGui);
		}

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		Maths mousePicker = new Maths(camera, renderer.getProjectionMatrix(), terrain);

		ParticleTexture pTexture = new ParticleTexture(loader.loadTexture("shreek", 0f), 2);
		pTexture.setAdditive(false);

		// SimpleParticleSystem simpleParticleSystem = new SimpleParticleSystem(50, 25,
		// 0.3f, 4);
		ComplexParticleSystem complexParticleSystem = new ComplexParticleSystem(pTexture, 50, 25, 0.3f, 4, 2);
		complexParticleSystem.randomizeRotation();
		complexParticleSystem.setDirection(new Vector3f(0, 1, 0), random.nextFloat());
		complexParticleSystem.setLifeError(random.nextFloat());
		complexParticleSystem.setSpeedError(random.nextFloat());
		complexParticleSystem.setScaleError(random.nextFloat());

		WaterShader waterShader = new WaterShader();
		WaterFrameBuffers waterFrameBuffers = new WaterFrameBuffers();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(),
				waterFrameBuffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(400, -400, -10);
		waters.add(water);

		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo outputFbo2 = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);

		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			mousePicker.update();
			complexParticleSystem.generateParticles(new Vector3f(500, 0, -300));
			ParticleMaster.update(camera);

			for (ItemEntity item : items) {
				item.bob();
			}

			renderer.renderShadowMap(entities, light);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			waterFrameBuffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalEntities, terrains, lights, camera,
					new Vector4f(0, 1, 0, -water.getHeight() + 1f));
			camera.getPosition().y += distance;
			camera.invertPitch();

			waterFrameBuffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalEntities, terrains, lights, camera,
					new Vector4f(0, -1, 0, water.getHeight() + 1f));

			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			waterFrameBuffers.unbindCurrentFrameBuffer();

			multisampleFbo.bindFrameBuffer();
			renderer.renderScene(entities, normalEntities, terrains, lights, camera, new Vector4f(0, 0, 0, 0));
			waterRenderer.render(waters, camera, light);
			ParticleMaster.renderParticles(camera);
			multisampleFbo.unbindFrameBuffer();
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);

			PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());

			guiRenderer.render(guiTextures);
			TextMaster.render();

			DisplayManager.updateDisplay();
		}

		PostProcessing.cleanUp();
		outputFbo.cleanUp();
		outputFbo2.cleanUp();
		multisampleFbo.cleanUp();
		waterFrameBuffers.cleanUp();
		waterShader.cleanUp();
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
