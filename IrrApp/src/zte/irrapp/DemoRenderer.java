package zte.irrapp;

import java.io.IOException;

import android.util.Log;
import zte.irrlib.CameraFPSWrapper;
import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.core.BoxEmitter;
import zte.irrlib.scene.AnimateMeshSceneNode;
import zte.irrlib.scene.BillboardGroup;
import zte.irrlib.scene.BillboardSceneNode;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.LightSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.ParticleSystemSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;
import zte.irrlib.scene.SceneNode.E_MATERIAL_TYPE;
import zte.irrlib.scene.TexMediaPlayer;

public class DemoRenderer implements Renderer {
	
	public static final String TAG = "DemoRenderer";

	public void onDrawFrame(Engine engine) {
		//mPlayer.update();
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
		
		engine.setResourceDir("/sdcard/irrmedia/");
		Scene scene = engine.getScene();
		//scene.setClearColor(new Color4i(0xff, 0xff, 0x9f, 0x3f));
		scene.setClearColor(new Color4i(0,0,0,0));
		scene.setDefaultFontPath("bigfont.png");
		scene.enableLighting(false);
		
		camera = scene.addCameraSceneNode(new Vector3d(0, 0, -30), origin, true, null);
		w = new CameraFPSWrapper(camera);
		
		//cube.addRotationAnimator(new Vector3d(0,0.5,0.2));
		cube = scene.addCubeSceneNode(origin, 10, null);
		cube.enableLighting(false);
		cube.setSmoothShade(false, 0);
		cube.setTexture("test2.jpg");
		//cube.addRotationAnimator(new Vector3d(0,0.5,0.2));
		cube.addFlyStraightAnimator(new Vector3d(-30,0,0), new Vector3d(50,0,20), 1000, true, true);
		
		walla = scene.addCubeSceneNode(new Vector3d(-30,0,0), 10, null);
		walla.setScale(new Vector3d(0.5,5,5), 0);
		walla.setTexture("test2.jpg");
		wallb = scene.addCubeSceneNode(new Vector3d(30,0,0), 10, null);
		wallb.setTexture("test2.jpg");
		wallb.setScale(new Vector3d(0.5,5,5), 0);
		cube.addCollisionResponseAnimator(walla);
		cube.addCollisionResponseAnimator(wallb);

		bill = scene.addBillboardSceneNode(origin, new Vector2d(10, 20), null);
		bill.setColor(new Color4i(0x00, 0xff, 0xff, 0xff), new Color4i(0, 0, 0, 0xff));
		bill.enableLighting(false);
		
		ps = scene.addParticleSystemSceneNode(new Vector3d(0,10,0), true, null);
		em = new BoxEmitter();
		em.MaxEmitRate = 1000;
		em.MinEmitRate = 500;
		em.MinSize = new Vector2d(1,1);
		em.MaxSize = new Vector2d(2.0,2.0);
		em.MinLifeTime = 100;
		em.MaxLifeTime = 2000;
		em.InitialDirection = (new Vector3d(0,0.004,0.0));
		em.BBox = new double[]{-2.5,-2,-2,2.5,-1.5,2};
		ps.setBoxEmitter(em);
		ps.setTexture("fire.bmp");
		ps.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
		ps.setPosition(new Vector3d(1,-15,-5), 0);
		ps.addFadeOutAffectorParticleAffector(new Color4i(0,0,0,0), 400);
		ps.addScaleParticleAffector(new Vector2d(2,2));
		
		star = new ParticleSystemSceneNode[5];
		
		em = new BoxEmitter();
		em.MaxEmitRate = 100;
		em.MinEmitRate = 50;
		em.MinSize = new Vector2d(1,1);
		em.MaxSize = new Vector2d(1.0,1.0);
		em.MinLifeTime = 8000;
		em.MaxLifeTime = 10000;
		em.InitialDirection = (new Vector3d(0,0.0,0.0));
		em.BBox = new double[]{-2,-1,-1,2,1,1};

		star[0] = scene.addParticleSystemSceneNode(new Vector3d(0,10,0), true, null);
		star[0].setBoxEmitter(em);
		star[0].setTexture("portal1.bmp");
		star[0].setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
		star[0].setPosition(new Vector3d(-6,10,-10), 0);
		star[0].addFadeOutAffectorParticleAffector(new Color4i(0,0,0,0), 500);
		star[0].addRotationAffector(new Vector3d(0.0,0.0,50.0), new Vector3d(-6,5.0,-10.0));
		//star.addGravityAffector(new Vector3d(0.0,-0.003,0.0), 100);
		//star.addScaleParticleAffector(new Vector2d(2,2));
		star[1] = scene.addParticleSystemSceneNode(new Vector3d(0,10,0), true, null);
		star[1].setBoxEmitter(em);
		star[1].setTexture("portal1.bmp");
		star[1].setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
		star[1].setPosition(new Vector3d(0,10.0,-10), 0);
		star[1].addFadeOutAffectorParticleAffector(new Color4i(0,0,0,0), 500);
		star[1].addRotationAffector(new Vector3d(0.0,0.0,50.0), new Vector3d(0.0,5.0,-10.0));
		
		star[2] = scene.addParticleSystemSceneNode(new Vector3d(0,10,0), true, null);
		star[2].setBoxEmitter(em);
		star[2].setTexture("portal1.bmp");
		star[2].setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
		star[2].setPosition(new Vector3d(6,10.0,-10), 0);
		star[2].addFadeOutAffectorParticleAffector(new Color4i(0,0,0,0), 500);
		star[2].addRotationAffector(new Vector3d(0.0,0.0,50.0), new Vector3d(6,5.0,-10.0));
		
		star[3] = scene.addParticleSystemSceneNode(new Vector3d(0,10,0), true, null);
		star[3].setBoxEmitter(em);
		star[3].setTexture("portal1.bmp");
		star[3].setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
		star[3].setPosition(new Vector3d(-3,5.0,-10), 0);
		star[3].addFadeOutAffectorParticleAffector(new Color4i(0,0,0,0), 500);
		star[3].addRotationAffector(new Vector3d(0.0,0.0,50.0), new Vector3d(-3.0,-0.0,-10.0));
		
		star[4] = scene.addParticleSystemSceneNode(new Vector3d(0,10,0), true, null);
		star[4].setBoxEmitter(em);
		star[4].setTexture("portal1.bmp");
		star[4].setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
		star[4].setPosition(new Vector3d(3.5,5.0,-10), 0);
		star[4].addFadeOutAffectorParticleAffector(new Color4i(0,0,0,0), 500);
		star[4].addRotationAffector(new Vector3d(0.0,0.0,50.0), new Vector3d(3.0,0.0,-10.0));
		
		cube.setVisible(false);
		walla.setVisible(false);
		wallb.setVisible(false);
		bill.setVisible(false);
		//star.addAttractionParticleAffector(new Vector3d(0.0,0.0,0.0), 10, false, true, false, false);
		/*model = scene.addAnimateMeshSceneNode("settings/settings.b3d", origin, null);
		model.addRotationAnimator(new Vector3d(0,0.5,0.0));
		model.setRotation(new Vector3d(90, 0, 0), SceneNode.TRANS_ABSOLUTE);
		model.setDiffuseColor(new Color4i(0, 0xff, 0, 0xff), 0);
		model.setAnimationSpeed(40);*/
		//bill.setTexture("test2.jpg");
		/*
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
		*/
		//cube.setMediaTexture();
		bill.setTexture("test2.jpg");
		light = scene.addLightSceneNode(new Vector3d(-30,30,-30), 100, new Color3i(0x7f,0x7f,0x7f), null);
		//mPlayer.start();
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
	private MeshSceneNode cube,walla,wallb;
	public AnimateMeshSceneNode model;
	private BillboardSceneNode bill;
	private LightSceneNode light;
	private int count;
	//public TexMediaPlayer mPlayer;
	
	private CameraSceneNode camera;
	public CameraFPSWrapper w;
	private Vector3d origin, back, left, right;
	private ParticleSystemSceneNode ps, star[];
	private BoxEmitter em;
}
