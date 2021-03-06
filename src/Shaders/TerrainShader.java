package Shaders;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;
import entities.Camera;
import entities.Light;

public class TerrainShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 4;

	private static final String VERTEX_FILE = "src/Shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/Shaders/terrainFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int loaction_backgroundTexture;
	private int loaction_rTexture;
	private int loaction_gTexture;
	private int loaction_bTexture;
	private int loaction_blendMap;



	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0,"position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		loaction_backgroundTexture = super.getUniformLocation("backgroundTexture");
		loaction_rTexture = super.getUniformLocation("rTexture");
		loaction_gTexture = super.getUniformLocation("gTexture");
		loaction_bTexture = super.getUniformLocation("bTexture");
		loaction_blendMap = super.getUniformLocation("blendMap");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		for(int i=0;i<MAX_LIGHTS;i++){
			location_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			location_lightColour[i] = super.getUniformLocation("lightColour["+i+"]");
		}
	}
	
	public void connectTextureUnits(){
		super.loadInt(loaction_backgroundTexture,0);
		super.loadInt(loaction_rTexture, 1);
		super.loadInt(loaction_gTexture, 2);
		super.loadInt(loaction_bTexture, 3);
		super.loadInt(loaction_blendMap, 4);
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadLights(ArrayList<Light> lights){
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
			}else{
				super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector(location_lightColour[i], new Vector3f(0,0,0));	
			}
		}
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.creatViewMatrix(camera);
		super.loadMatrix(location_viewMatrix,viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix,projection);
	}

}
