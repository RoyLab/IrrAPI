package zte.irrlib.core;

public class Vector3d {
	 
	public Vector3d(){
		
	}	
	
	public Vector3d(Vector3d v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vector3d(double x, double y, double z){
		this.x = x; this.y = y; this.z = z;
	}
	
	public Vector3d plus(Vector3d v){
		return new Vector3d(x+v.x, y+v.y, z+v.z);
	}
	
	public Vector3d minus(Vector3d v){
		return new Vector3d(x-v.x, y-v.y, z-v.z);
	}
	
	public Vector3d multi(Vector3d v){
		return new Vector3d(x*v.x, y*v.y, z*v.z);
	}
	
	public Vector3d multi(double k){
		return new Vector3d(x*k, y*k, z*k);
	}
	
	public double distanceSquare(Vector3d v){
		return (v.x-x)*(v.x-x) + (v.y-y)*(v.y-y) + (v.z-z)*(v.z-z);
	}
	
	public Vector3d copy(Vector3d v){
		x = v.x; y = v.y; z = v.z;
		return this;
	}
	
	public Vector3d reverse(){
		x = -x; y = -y; z = -z;
		return this;
	}
	
	public double dot(Vector3d v){
		return v.x*x + v.y*y + v.z*z;
	}
	
	public Vector3d cross(Vector3d v){
		return new Vector3d(
				y*v.z-z*v.y,
				z*v.x-x*v.z,
				x*v.y-y*v.x
				);
	}
	
	public Vector3d normalize(){
		double d = Math.sqrt(x*x+y*y+z*z);
		x/= d; y/=d; z/=d;
		return this;
	}
	
	public boolean isZero(){
		return ((Math.abs(x) < ZERO) && (Math.abs(y) < ZERO) && (Math.abs(z) < ZERO));
	}
	
	public boolean isEqual(Vector3d v){
		return Math.abs(v.dot(this)) < ZERO;
	}
	
	public double x, y, z;
	
	public static final double ZERO = 1E-9;
}
