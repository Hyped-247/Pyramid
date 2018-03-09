package edu.westmont.cs030;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

import java.util.LinkedList;


public class Pyramid extends Drawable{
	
	/**
	 * This is a helper method that will construct a box
	 * @param texture the path name to the asset that wraps the box
	 * @param length the length of the box
	 * @param height the height of the box
	 * @param width the width of the box
	 * @param x the x position of the box
	 * @param y the y position of the box
	 * @param z the z position of the box
	 * @return a Node object representing the box that has been constructed containing the box
	 */
	private Node makeBox(String texture,
			float length, float height, float width,
			float x, float y, float z) {	
		
		// The variable to return
		Node returnNode = new Node();
		
		/* Create a new material for the side of the box*/
		Material boxMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key = new TextureKey(texture);
		key.setGenerateMips(true);
		Texture tex = assetManager.loadTexture(key);
		tex.setWrap(WrapMode.EdgeClamp);
		boxMaterial.setTexture("ColorMap", tex);
		
		/* Create box  */
		Box box = new Box(length/2.0f,height/2.0f,width/2.0f); /* Give it a size */	
		box.scaleTextureCoordinates(new Vector2f(1, 1)); /*Scale the texture */
		Geometry boxGeometry = new Geometry("Box", box);
		boxGeometry.setMaterial(boxMaterial);
		boxGeometry.setShadowMode(ShadowMode.CastAndReceive); /* Set up shadows */
		returnNode.attachChild(boxGeometry);
		returnNode.setLocalTranslation(x, y, z); /* Move it to the right place */
		return returnNode;
	}

	@Override
	public Spatial draw() {
		LinkedList<Node> nodeLinkedList = new LinkedList<>();
		Node node = new Node();
		int startingPoint = 13;
		float x  = 0;
		float y  = 0;
		float z  = 0;
		int layer = 1;
		int x_num[] = new int[2];
		int z_num[] = new int[2];

		while (layer != 90){

		x_num[0] = (int) x;
		for (int i = 0; i < startingPoint; i++) {
				Node box01 = this.makeBox("assets/box.jpg", 1.0f, 1.0f, 1.0f, x, y, z);
				Node box02 = this.makeBox("assets/box.jpg", 1.0f, 1.0f, 1.0f, x, y, startingPoint -1);
				nodeLinkedList.add(box01);
				nodeLinkedList.add(box02); // x = 1 y=0 z=0
				x_num[1] = (int) x;
				x++;

		}
		z_num[0] = (int) z;
		for (int i = 0; i < startingPoint - 1; i++) {
			Node box03 = this.makeBox("assets/box.jpg", 1.0f, 1.0f, 1.0f, x_num[1], y, z);
			Node box04 = this.makeBox("assets/box.jpg", 1.0f, 1.0f, 1.0f, x_num[0], y, z);

			nodeLinkedList.add(box03);
			nodeLinkedList.add(box04);
			z_num[1] = (int) z;
			z++;
		}
			layer++;
			y += .5f;
			x = x_num[0]+1;
			z = z_num[0]+1;
			startingPoint = startingPoint - 2;
		}

		for (int i = 0; i < nodeLinkedList.size(); i++) {
			node.attachChild(nodeLinkedList.get(i));
		}
		return node;
	}

}
