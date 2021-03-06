package engineTester;

import java.util.ArrayList;
import java.util.Random;

import model.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import RenderEngine.OBJLoader;
import Terrain.Terrain;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		// ################TERRAIN TEXTURE##################

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("goodDirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//##################################################
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),fernTextureAtlas);
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(
				loader.loadTexture("grassTexture")));
		TexturedModel lowPolyTree = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(
				loader.loadTexture("lowPolyTree")));
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(
				loader.loadTexture("tree")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(
				loader.loadTexture("flower")));
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack,blendMap,"heightMap");

		
		ArrayList<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		for(int i = 0; i < 400; i++) {
			if (i % 20 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x,z);
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y ,z),0,random.nextFloat() * 360,
						0,0.9f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x,z);
				entities.add(new Entity(lowPolyTree, new Vector3f(x, y ,z),0, random.nextFloat() * 360 
						,0,random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x,z);
				entities.add(new Entity(tree, new Vector3f(x, y ,z),0, 0 ,0,
						random.nextFloat() * 1 + 4));
			}
		}
		TexturedModel dragonModel = new TexturedModel(OBJLoader.loadObjModel("Katarina", loader), new ModelTexture(
				loader.loadTexture("katarina_base_diffuse")));
		Player player = new Player(dragonModel, new Vector3f(10, 0 ,-100),0, 0,0,1.5f);
		
		Light light = new Light(new Vector3f(0, 10000, -7000), new Vector3f(1,1, 1));
		ArrayList<Light> lights = new ArrayList<Light>();
		lights.add(light);
		lights.add(new Light(new Vector3f(-200,10,-200),new Vector3f(1,1,1)));

		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer();

		
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain);
			for(Entity entity: entities){
				entity.increasePosition(0, 0, 0);
			}

			renderer.processTerrain(terrain);
			for(Entity entity: entities){
				renderer.processEntity(entity);
			}
			renderer.processEntity(player);
			renderer.render(lights, camera);
			DisplayManager.updateDisplay();

		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
