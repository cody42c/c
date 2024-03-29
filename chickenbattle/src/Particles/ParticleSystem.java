package Particles;

import Screens.Application;
import Spelet.VertexAttributes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class ParticleSystem {
	public Array<Particle> particles;
	public float timer;
	public static Mesh quad;
	public static Texture midExplo;
	Vector3 temp = new Vector3();
	Vector3 temp2 = new Vector3();
	Matrix4 rot = new Matrix4();
	public ParticleSystem() {
		midExplo = new Texture(Gdx.files.internal("data/Particles/midexplosion.bmp"));
		particles = new Array<Particle>();
		initiateMesh();

	}
	public void addParticle(Particle p) {
		particles.add(p);
	}
//	public void addParticle(float px, float py, float pz, float vx, float vy, float vz, float w, float h, float d, float dw,float dh, float dd, float steps, boolean grav, boolean emitts, boolean phys, boolean isSmoke) {
//		particles.add(new Particle(px, py,pz, vx, vy, vz, w, h, d, dw,dh, dd, steps, grav, emitts, phys, isSmoke));
//	}
	public void update(Application app) {
		System.out.println(particles.size);
		timer += Gdx.graphics.getDeltaTime()*1000;
		float stepSpeed = 16;
		if (timer >= stepSpeed) {
			for (int i = particles.size-1; i >= 0; i--) {
				particles.get(i).step(app,this);
				if (particles.get(i).steps > particles.get(i).stepsAlive) {
					particles.removeIndex(i);
				}
			}
			timer -= stepSpeed;
		}
		sort(app);
	}
	public void sort(Application app) {
		for (int i = 0; i < particles.size; i++) {
			particles.get(i).distance = particles.get(i).position.dst2(app.cam.position);
		}
		particles.sort(ParticleComparator.getInstance());
	}
	public void addExplosion(float cx, float cy, float cz) {
	
		// Core fire
		Particle p = new Particle(cx,cy,cz,
				10,10,10,
				100,false,false,false);
		p.setSizeChange(-0.01f, -0.01f, -0.01f);
		p.setColor(1,0.5f,0.2f);
		addParticle(p);
		
		// Main smoke
		for (int i = 0; i < 10; i++) {
			temp.set(cx,cy,cz);
			temp.add(MathUtils.random(-5f,5f),MathUtils.random(-3f,0f),MathUtils.random(-5f,5f));
			p = new Particle(temp.x,temp.y,temp.z,
					10,10,10,
					200,false,false,true);
			p.setVelocity(0,0.3f,0);
			p.setColor(0.6f,0.6f,0.6f);
			addParticle(p);	
		}
		
		// Blast fire
		p = new Particle(cx,cy,cz,
				10,10,10,
				30,false,false,false);
		p.setColor(1,0.5f,0.2f);
		p.setSizeChange(3,3,3);
		addParticle(p);
		p = new Particle(cx,cy,cz,
				10,10,10,
				30,false,false,false);
		p.setColor(1,0.5f,0.2f);
		p.setSizeChange(3,3,3);
		addParticle(p);

		for (int i = 0; i < 30; i++) {
			temp.set(1,0,0);
			rot.setToRotation(0,0,1,MathUtils.random(0f,90f));
			temp.rot(rot);
			rot.setToRotation(0,1,0, MathUtils.random(360f));
			temp.rot(rot);
			temp.nor();
			temp.mul(MathUtils.random(0.3f,0.4f));
			p = new Particle(cx,cy,cz,
					5,5,5,
					50,true,true,false);
			p.setEmitter(true);
			p.setEmitterProperties(false,0,0.05f,0,2.5f,4f,4f);
			p.setColor(1,0.5f,0.2f);
			temp.mul(2);
			p.setVelocity(temp.x,temp.y,temp.z);
			addParticle(p);
		}
		
//		// Upwards fire
//		for (int i = 0; i < 30; i++) {
//			temp.set(1,0,0);
//			rot.setToRotation(0,0,1,MathUtils.random(70f,90f));
//			temp.rot(rot);
//			rot.setToRotation(0,1,0, MathUtils.random(360f));
//			temp.rot(rot);
//			temp.nor();
//			temp.mul(MathUtils.random(0.3f,0.4f));
//			p = new Particle(cx,cy,cz,
//					5,5,5,
//					100,true,true,false);
//			p.setEmitter(true);
//			p.setEmitterProperties(false,0,0.05f,0,2.5f,4f,4f);
//			p.setColor(1,0.5f,0.2f);
//			temp.mul(2);
//			p.setVelocity(temp.x,temp.y,temp.z);
//			addParticle(p);
//		}
//		for (int i = 0; i < 10; i++) {
//			temp.set(1,0,0);
//			rot.setToRotation(0,0,1,MathUtils.random(20f,60f));
//			temp.rot(rot);
//			rot.setToRotation(0,1,0, MathUtils.random(360f));
//			temp.rot(rot);
//			temp.nor();
//			temp.mul(0.4f);
//			for (int j = 0; j < MathUtils.random(3,5); j++) {
//				temp2.set(temp);
//				temp2.add(MathUtils.random(-1f,1f)/16, MathUtils.random(-1f,1f)/16,MathUtils.random(-1f,1f)/16);
//				p = new Particle(cx,cy,cz,
//						5,5,5,
//						100,true,true,false);
//				p.setEmitter(true);
//				p.setEmitterProperties(false,0,0.05f,0,4f,4f,4f);
//				temp2.mul(2);
//				p.setVelocity(temp2.x,temp2.y,temp2.z);
//				p.setColor(1,0.5f,0.2f);
//				addParticle(p);
//			}
//		}
	}



	public static void initiateMesh() {
		float[] list = new float[48];
		int i = 0;
		list[i++] = -0.5f; // x1
		list[i++] = -0.5f; // y1
		list[i++] = 0;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 0; // u1
		list[i++] = 0.25f; // v1

		list[i++] = 0.5f; // x2
		list[i++] = -0.5f; // y2
		list[i++] = 0f;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 0.25f; // u2
		list[i++] = 0.25f; // v2

		list[i++] = 0.5f; // x3
		list[i++] = 0.5f; // y2
		list[i++] = 0f;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 0.25f; // u3
		list[i++] = 0; // v3

		list[i++] = 0.5f; // x3
		list[i++] = 0.5f; // y2
		list[i++] = 0f;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 0.25f; // u3
		list[i++] = 0; // v3

		list[i++] = -0.5f; // x4
		list[i++] = 0.5f; // y4
		list[i++] = 0f;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 0; // u4
		list[i++] = 0; // v4

		list[i++] = -0.5f; // x1
		list[i++] = -0.5f; // y1
		list[i++] = 0f;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 5;
		list[i++] = 0; // u1
		list[i++] = 0.25f; // v1
		quad = new Mesh(true, 6, 0,
				VertexAttributes.position,
				VertexAttributes.normal,
				VertexAttributes.textureCoords);
		quad.setVertices(list);
	}
}
