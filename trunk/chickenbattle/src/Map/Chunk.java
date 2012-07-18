package Map;


import Spelet.StaticVariables;
import Spelet.VertexAttributes;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.FloatArray;


public class Chunk {
	public Voxel[][][] map;
	public Mesh chunkMesh;
	public BoundingBox bounds;
	public int x, y, z;
	float distance;

	public Chunk(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		map = new Voxel[Map.chunkSize][Map.chunkSize][Map.chunkSize];
		distance = 0;
		bounds = new BoundingBox();
		for (int x2 = 0; x2 < Map.chunkSize; x2++) {
			for (int z2 = 0; z2 < Map.chunkSize; z2++) {
				for (int y2 = 0; y2 < Map.chunkSize; y2++) {
					if (x2 == 0 || y2 == 0)
						map[x2][y2][z2] = new Voxel(Voxel.grass);
					else
						map[x2][y2][z2] = new Voxel(Voxel.nothing);	
				}
			}
		}
	}

	public int compareTo(Object arg0) {
		return (int) (distance-((Chunk) arg0).distance);
	}
	public void rebuildChunk() {
		int chunkX = x;
		int chunkY = y;
		int chunkZ = z;
		if (chunkMesh != null) {
			chunkMesh.dispose();
		}
		FloatArray fa = new FloatArray();
		for (int x = 0; x < Map.chunkSize; x++) {
			for (int y = 0; y < Map.chunkSize; y++) {
				for (int z = 0; z < Map.chunkSize; z++) { 
					if (map[x][y][z].id == Voxel.grass) {
						if (y+1 >= Map.chunkSize) {
							addTopFace(fa, x, y, z);
						} else if (map[x][y+1][z].id == Voxel.nothing) {
							addTopFace(fa, x, y, z);
						}
						if (y-1 < 0) {
							addBotFace(fa, x, y, z);
						} else if (map[x][y-1][z].id == Voxel.nothing) {
							addBotFace(fa, x, y, z);
						}
						if (z-1 < 0) {
							addBackFace(fa, x, y, z);
						} else if (map[x][y][z-1].id == Voxel.nothing) {
							addBackFace(fa, x, y, z);
						}
						if (z+1 >= Map.chunkSize) {
							addFrontFace(fa, x, y, z);
						} else if (map[x][y][z+1].id == Voxel.nothing) {
							addFrontFace(fa, x, y, z);
						}
						if (x+1 >= Map.chunkSize) {
							addRightFace(fa, x, y, z);
						} else if (map[x+1][y][z].id == Voxel.nothing) {
							addRightFace(fa, x, y, z);
						}
						if (x-1 < 0) {
							addLeftFace(fa, x, y, z);
						} else if (map[x-1][y][z].id == Voxel.nothing) {
							addLeftFace(fa, x, y, z);
						}
					}
				}
			}
		}


		if (fa.size > 0) {
			chunkMesh = new Mesh(true, fa.size, 0,
					VertexAttributes.position, 
					VertexAttributes.normal,
					VertexAttributes.textureCoords,
					VertexAttributes.occlusion);
			chunkMesh.setVertices(fa.items);
			chunkMesh.calculateBoundingBox(bounds);
			Matrix4 calcMat = StaticVariables.acquireCalcMat();
			calcMat.setToTranslation(chunkX*Map.chunkSize, chunkY*Map.chunkSize, chunkZ*Map.chunkSize);
			bounds.mul(calcMat);
			StaticVariables.releaseSema();
		}
	}
	public void addTopFace(FloatArray fa, int x, int y, int z) {
		float occlusion = 0;
		if (x-1 < 0 || y+1 >= Map.chunkSize || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (x+1 >= Map.chunkSize || y+1 >= Map.chunkSize || map[x+1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (x+1 >= Map.chunkSize || y+1 >= Map.chunkSize || map[x+1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || z-1 < 0 || map[x][y+1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue


		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || z-1 < 0 || map[x][y+1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || x-1 <0 || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (x-1 < 0 || y+1 >= Map.chunkSize || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (y+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(0); // Normal X
		fa.add(1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue
	}
	public void addBotFace(FloatArray fa, int x, int y, int z) {
		float occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || y-1 < 0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0);
		fa.add(1);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || y-1 < 0 || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(1);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || y-1 < 0 || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		fa.add(1+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || y-1 < 0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || y-1 < 0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(-1); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0);
		fa.add(1);
		fa.add(occlusion); // Occlusionvalue

	}

	public void addLeftFace(FloatArray fa, int x, int y, int z) {
		float occlusion = 0;
		if (y-1 < 0 || x-1 <0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y-1 < 0 || x-1 <0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || x-1 <0 || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(1+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0);
		fa.add(occlusion); // Occlusionvalue

		fa.add(0+x);
		fa.add(1+y);
		fa.add(1+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1);
		fa.add(0);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || x-1 <0 || map[x-1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y-1 < 0 || x-1 <0 || map[x-1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(-1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

	}

	public void addRightFace(FloatArray fa, int x, int y, int z) {
		float occlusion = 0;
		if (y-1 < 0 || x+1 >= Map.chunkSize || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y-1 < 0 || x+1 >= Map.chunkSize || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 <0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || x+1 >= Map.chunkSize || map[x+1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 <0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(0+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(1f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y+1 >= Map.chunkSize || x+1 >= Map.chunkSize || map[x+1][y+1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(1+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (y-1 < 0 || x+1 >= Map.chunkSize || map[x+1][y-1][z].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z); // z1
		fa.add(1); // Normal X
		fa.add(0); // Normal Y
		fa.add(0); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

	}
	public void addFrontFace(FloatArray fa, int x, int y, int z) {
		float occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 <0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u1
		fa.add(0.5f); // v1
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x); // x2
		fa.add(0+y); // y2
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u2
		fa.add(0.5f); // v2
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z+1 >= Map.chunkSize || map[x+1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1f+x); // x3
		fa.add(1f+y); // y2
		fa.add(1f+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u3
		fa.add(0f); // v3
		fa.add(occlusion); // Occlusionvalue

		fa.add(1f+x); // x3
		fa.add(1f+y); // y2
		fa.add(1f+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(1f); // u3
		fa.add(0f); // v3
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y+1 >= Map.chunkSize || map[x][y+1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 <0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x4
		fa.add(1+y); // y4
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u4
		fa.add(0f); // v4
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z+1 >= Map.chunkSize || y-1 < 0 || map[x][y-1][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 <0 || z+1 >= Map.chunkSize || map[x-1][y][z+1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x); // x1
		fa.add(0+y); // y1
		fa.add(1+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(1); // Normal Z
		fa.add(0.5f); // u1
		fa.add(0.5f); // v1
		fa.add(occlusion); // Occlusionvalue

	}
	public void addBackFace(FloatArray fa, int x, int y, int z) {
		float occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 < 0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y+1 >= Map.chunkSize || map[x][y+1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x-1 < 0 || z-1 < 0 || map[x-1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		fa.add(0+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(1f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue


		occlusion = 0;
		if (z-1 < 0 || y+1 >= Map.chunkSize || map[x][y+1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 < 0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(1+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0f);
		fa.add(occlusion); // Occlusionvalue

		occlusion = 0;
		if (z-1 < 0 || y-1 < 0 || map[x][y-1][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		if (x+1 >= Map.chunkSize || z-1 < 0 || map[x+1][y][z-1].id == Voxel.nothing) {
			occlusion++;
		}
		occlusion/=2;
		//if (occlusion != 0)
		//if (occlusion != 0)
		fa.add(1+x);
		fa.add(0+y);
		fa.add(0+z);
		fa.add(0); // Normal X
		fa.add(0); // Normal Y
		fa.add(-1); // Normal Z
		fa.add(0.5f);
		fa.add(0.5f);
		fa.add(occlusion); // Occlusionvalue

	}
}
