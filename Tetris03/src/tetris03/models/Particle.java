package tetris03.models;

import java.util.Random;

import processing.core.PVector;

public class Particle {
	public static final float PARTICLE_LIFETIME = 5;
	public float timeAlive = 0;
	
	public float width, height;
	
	public PVector position;
	public PVector velocity;
	
	//acceleration has constant magnitude in direction of destination
	public PVector acceleration;
	public float accelMagnitude = 6000;
	
	public PVector targetPosition;
	
	public int particleType;
	
	Random random = new Random();
	
	public Particle(int particleType, float x, float y, float destX, float destY) {
		this.particleType = particleType;
		position = new PVector(x, y);
		targetPosition = new PVector(destX, destY);
		
		width = random.nextFloat() * 10 + 8;
		height = width;
		
		velocity = new PVector(random.nextFloat() * 6000 - 3000, random.nextFloat() * 6000 - 3000);
		
		acceleration = new PVector();
	}
	
	public void update(float delta) {
		timeAlive += delta;
		
		acceleration.set(PVector.sub(targetPosition, position));
		acceleration.normalize();
		acceleration.mult(accelMagnitude);
		
		velocity.add(PVector.mult(acceleration, delta));
		velocity.limit(1000);
		position.add(PVector.mult(velocity, delta));
	}
}
