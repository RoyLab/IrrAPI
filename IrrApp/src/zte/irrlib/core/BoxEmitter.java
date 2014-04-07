package zte.irrlib.core;

/**
 * ȱdoc
 * @author Administrator
 *
 */
public class BoxEmitter {
	
	public double BBox[]  = new double[]{-7,-7,-7,7,7,7};
	public Vector3d InitialDirection = new Vector3d(0.0,0.03,0.0);
	public int MinEmitRate=5, MaxEmitRate=10;
	public Color4i DarkestColor = new Color4i(255,0,0,0);
	public Color4i BrightestColor = new Color4i(255,255,255,255);
	public int MinLifeTime=2000, MaxLifeTime=4000;
	public int MaxAngleDegrees = 0;
	public Vector2d MinSize = new Vector2d(5.0,5.0);
	public Vector2d  MaxSize = new Vector2d(5.0,5.0);
	
}
