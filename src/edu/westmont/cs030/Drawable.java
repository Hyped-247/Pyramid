package edu.westmont.cs030;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

public abstract class Drawable{

	protected AssetManager assetManager;
	
	void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	abstract Spatial draw();
}
