package edu.westmont.cs030;

import java.util.ArrayList;
import java.util.List;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.ChaseCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;

public class Environment extends SimpleApplication implements AnimEventListener {
	
	Box ground;
	Material ground_mat;
	Geometry ground_geo;

	private ChaseCamera chaseCam = null;

	private List<Drawable> drawables;

	private float counter =  0.0f;
	private Node baseNode;


	//Constructor
	public Environment(List<Drawable> drawables) {
		if (drawables == null) {
			throw new IllegalArgumentException("\"drawable\" can't be null");
		}
		this.drawables = drawables;
	}

	public Environment(AppState... initialStates) {
		super(initialStates);
	}

	private void initGroundAndSky() {
		//Set up ground as a thin box
		ground_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key3 = new TextureKey("assets/map.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.MirroredRepeat);
		ground_mat.setTexture("ColorMap", tex3);

		ground = new Box(100.0f, 0.1f, 100.0f);
		ground.scaleTextureCoordinates(new Vector2f(1.0f, 1.0f));
		ground_geo = new Geometry("Ground", ground);
		ground_geo.setMaterial(ground_mat);
		ground_geo.setLocalTranslation(0.0f, -0.2f, 0.0f);
		ground_geo.setShadowMode(ShadowMode.Receive);
		rootNode.attachChild(ground_geo);

		//Set up sky
		TextureKey skyTextureKey = new TextureKey("Textures/Sky/Bright/FullskiesBlueClear03.dds");
		skyTextureKey.setGenerateMips(true);
		Texture sky = assetManager.loadTexture(skyTextureKey);

		Spatial skySpatial = SkyFactory.createSky(assetManager, sky, EnvMapType.CubeMap);
		rootNode.attachChild(skySpatial);
	}


	private void initLighting() {
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-1.0f, -1.0f, -1.0f));
		rootNode.addLight(sun);

		/* Drop shadows */
		final int SHADOWMAP_SIZE = 1024;
		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
		dlsr.setLight(sun);
		viewPort.addProcessor(dlsr);

		DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
		dlsf.setLight(sun);
		dlsf.setEnabled(true);
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		fpp.addFilter(dlsf);
		viewPort.addProcessor(fpp);
	}

	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("", FileLocator.class);
		initLighting();
		initGroundAndSky();
		
		baseNode = new Node();
		for(Drawable drawable:this.drawables) {
			drawable.setAssetManager(assetManager);
			Spatial draw = drawable.draw();
			baseNode.attachChild(draw);
		}
		
		this.counter += 0.1;
		baseNode.rotate(0, FastMath.TWO_PI * this.counter, 0);
		rootNode.attachChild(baseNode);
			
		//flyCam.setMoveSpeed(5);
		flyCam.setEnabled(false);
		
	    chaseCam = buildChaseCamera(baseNode);
	}
	
	@Override
	public void simpleUpdate(float tpf) {

		// baseNode.rotate(0, FastMath.TWO_PI * tpf/10.0f, 0);
	}

	private ChaseCamera buildChaseCamera(Node drone) {
		if (chaseCam == null) {
			ChaseCamera chaseCam = new ChaseCamera(cam, drone, inputManager);
			chaseCam.setSmoothMotion(true);
			chaseCam.setMinDistance(2f);
			chaseCam.setMaxDistance(10);
			chaseCam.setZoomSensitivity(10);
			chaseCam.setDefaultDistance(2f);
			chaseCam.setMaxVerticalRotation(FastMath.PI);
			chaseCam.setMinVerticalRotation(-1.0f * FastMath.PI);
		} else {
			chaseCam.setSpatial(drone);
		}
		return chaseCam;
	}


	
	public static void main(String[] args) {
		List<Drawable> drawables = new ArrayList<Drawable>(); 
		drawables.add(new Pyramid());
		Environment app = new Environment(drawables);
		app.start(); 
	}

	@Override
	public void onAnimChange(AnimControl arg0, AnimChannel arg1, String arg2) {
	}

	@Override
	public void onAnimCycleDone(AnimControl arg0, AnimChannel arg1, String arg2) {
	}

}
