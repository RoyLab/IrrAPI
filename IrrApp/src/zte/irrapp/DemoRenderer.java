package zte.irrapp;

import zte.irrlib.CameraFPSWrapper;
import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.LightSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;
import zte.irrlib.scene.TexMediaPlayer;

public class DemoRenderer implements Renderer {
	
	public static final String TAG = "DemoRenderer";

	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		
		scene.drawAllNodes();
		
		scene.drawText("fps: " + engine.getFPS() + " material: " + model.getMaterialCount(),
				new Vector2i(0, 0),
				new Color4i(0xff, 0x40, 0x90, 0xff));
	}

	public void onCreate(Engine engine) {
		origin = new Vector3d();
		back = new Vector3d(0, 20, 0);
		left = new Vector3d(-20, 0, 0);
		right = new Vector3d(20, 0, 0);
		
		engine.setResourceDir("/storage/sdcard0/irrmedia/");
		Scene scene = engine.getScene();
		scene.setDefaultFontPath("bigfont.png");
		scene.enableLighting(true);
		
		camera = scene.addCameraSceneNode(new Vector3d(0, 0, -100), origin, true, null);
		w = new CameraFPSWrapper(camera);
		
		cube = scene.addCubeSceneNode(back, 10, null);
		cube.enableLighting(false);
		cube.setTexture("test2.jpg", 0);
		cube.addRotationAnimator(new Vector3d(0,0.5,0.2));
		
		model = scene.addMeshSceneNode("models/axis.obj", origin, null);
		model.addRotationAnimator(new Vector3d(0,0.5,0.0));
		model.setRotation(new Vector3d(90, 0, 0), SceneNode.TRANS_ABSOLUTE);
		
		light = scene.addLightSceneNode(new Vector3d(-30,30,-30), 100, new Color3i(0x7f,0x7f,0x7f), null);
		
		light.LightData.AmbientColor = new Color3i(0, 0, 0);		
		light.LightData.DiffuseColor = new Color3i(0, 0, 0);
		light.LightData.SpecularColor = new Color3i(0, 0, 0);
		light.LightData.DiffuseColor = new Color3i(0x7f, 0x7f, 0x7f);
		light.upLoadLightData();
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
	public void startMediaPlayer(){
		mPlayer.start();
	}

	private SceneNode empty, node;
	private MeshSceneNode cube, model;
	private LightSceneNode light;
	private int count;
	private TexMediaPlayer mPlayer;
	
	private CameraSceneNode camera;
	public CameraFPSWrapper w;
	private Vector3d origin, back, left, right;
}
