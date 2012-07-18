package network;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Player {
	public String name;
	public int id;
	public int hp;
	public float posX, posY,posZ;  
	public BoundingBox box;

	public Player(String xs){
		this.name = xs;
		hp = 10;
		posX = 0;
		posY = 0;
		posZ =0;
		box = new BoundingBox();
	}
	public void setBox(Vector3[] x){
		box.set(x);
	}	

}