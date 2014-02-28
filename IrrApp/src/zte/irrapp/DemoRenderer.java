package zte.irrapp;

import java.io.IOException;

import zte.irrlib.CameraFPSWrapper;
import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.AnimateMeshSceneNode;
import zte.irrlib.scene.BillboardGroup;
import zte.irrlib.scene.BillboardSceneNode;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.LightSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;
import zte.irrlib.scene.TexMediaPlayer;

public class DemoRenderer implements Renderer {
	
	public static final String TAG = "DemoRenderer";

	public void onDrawFrame(Engine engine) {
		mPlayer.update();
		Scene scene = engine.getScene();
		
		scene.drawAllNodes();
		
		scene.drawText("fps: " + engine.getFPS() + " material: ",
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
		scene.setClearColor(new Color4i(0xff, 0xff, 0x9f, 0x3f));
		scene.setDefaultFontPath("bigfont.png");
		scene.enableLighting(false);
		
		camera = scene.addCameraSceneNode(new Vector3d(0, 0, -30), origin, true, null);
		w = new CameraFPSWrapper(camera);
		
		cube = scene.addCubeSceneNode(back, 10, null);
		cube.enableLighting(false);
		cube.setSmoothShade(false, 0);
		
		//cube.setTexture("test2.jpg");
		cube.addRotationAnimator(new Vector3d(0,0.5,0.2));
		
		/*model = scene.addAnimateMeshSceneNode("settings/settings.b3d", origin, null);
		model.addRotationAnimator(new Vector3d(0,0.5,0.0));
		model.setRotation(new Vector3d(90, 0, 0), SceneNode.TRANS_ABSOLUTE);
		model.setDiffuseColor(new Color4i(0, 0xff, 0, 0xff), 0);
		model.setAnimationSpeed(40);*/
		bill = scene.addBillboardSceneNode(origin, new Vector2d(10, 20), null);
		bill.setColor(new Color4i(0x00, 0xff, 0xff, 0xff), new Color4i(0, 0, 0, 0xff));
		bill.enableLighting(false);
		
		//bill.setTexture("test2.jpg");
		mPlayer = scene.getMediaPlayer();
		try {
			mPlayer.setDataSource("NativeMedia.ts");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cube.setMediaTexture();
		bill.setTexture("test2.jpg");
		light = scene.addLightSceneNode(new Vector3d(-30,30,-30), 100, new Color3i(0x7f,0x7f,0x7f), null);
		mPlayer.start();
		light.LightData.AmbientColor = new Color3i(0, 0, 0);		
		light.LightData.DiffuseColor = new Color3i(0, 0, 0);
		light.LightData.SpecularColor = new Color3i(0, 0, 0);
		light.LightData.DiffuseColor = new Color3i(0x7f, 0x7f, 0x7f);
		light.upLoadLightData();
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}

	private BillboardGroup group;
	private SceneNode empty, node;
	private MeshSceneNode cube;
	public AnimateMeshSceneNode model;
	private BillboardSceneNode bill;
	private LightSceneNode light;
	private int count;
	public TexMediaPlayer mPlayer;
	
	private CameraSceneNode camera;
	public CameraFPSWrapper w;
	private Vector3d origin, back, left, right;
}
