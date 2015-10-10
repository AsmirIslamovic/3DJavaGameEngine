package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;
	private float NEAR_ZOOM = 20;
	private float FAR_ZOOM = 200;
	
	private float resetRotation = 0;
	
	private Player player;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){
		System.out.println(angleAroundPlayer);
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float vertacalDistance = calculateVerticalDistance();
		CalculateCameraPosition(horizontalDistance,vertacalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		
		if (resetRotation == 1){
			angleAroundPlayer -= 1;
			if (angleAroundPlayer >= -5 && angleAroundPlayer <= 5){
				angleAroundPlayer = 0;
				resetRotation = 0;
			}
		}
		if (resetRotation == 2){
			angleAroundPlayer += 1;
			if (angleAroundPlayer >= -5 && angleAroundPlayer <= 5){
				angleAroundPlayer = 0;
				resetRotation = 0;
			}
		}
			
		
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void CalculateCameraPosition(float horizDistance, float verticDistance){
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;

		position.y = player.getPosition().y + verticDistance + 5;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		if (distanceFromPlayer - zoomLevel > NEAR_ZOOM && distanceFromPlayer - zoomLevel < FAR_ZOOM){
			distanceFromPlayer -= zoomLevel;
		}else if(distanceFromPlayer - zoomLevel > FAR_ZOOM){
			distanceFromPlayer = FAR_ZOOM;
		}else if (distanceFromPlayer - zoomLevel < NEAR_ZOOM){
			distanceFromPlayer = NEAR_ZOOM;
		}
		
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if (angleAroundPlayer >= 360){
			angleAroundPlayer -= 360;
		}
		if (angleAroundPlayer <= 0){
			angleAroundPlayer += 360;
		}
		if(Mouse.isButtonDown(1)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
			resetRotation = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_F)){
			if (angleAroundPlayer <= 180){
				resetRotation = 1;
			}
			else{
				resetRotation = 2;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
