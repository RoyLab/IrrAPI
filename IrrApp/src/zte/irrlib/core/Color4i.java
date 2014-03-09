package zte.irrlib.core;

/**
 * RGB��ɫ�࣬ÿ����ɫ��ȡֵ������0~255������
 * @author Roy
 *
 */
public class Color4i {
	
	/**
	 * ���췽��������ɫ
	 */
	public Color4i(){ 
		red = green = blue = alpha = 0xff;
	}
	
	/**
	 * ���췽��
	 * @param v1 ��ɫ����
	 * @param v2 ��ɫ����
	 * @param v3 ��ɫ����
	 * @param v4 ͸������
	 */
	public Color4i(int v1, int v2, int v3, int v4){
		red = v1; green = v2; blue = v3; alpha = v4;
	}
	
	/**
	 * ���غ�ɫ����
	 * @return ��ɫ������0~255��
	 */
	public int r() {
		if (red < 0) return 0;
		else if (red > 255) return 255;
		else return red;
	}
	
	/**
	 * ������ɫ����
	 * @return ��ɫ������0~255��
	 */
	public int g() {
		if (green < 0) return 0;
		else if (green > 255) return 255;
		else return green;
	}
	
	/**
	 * ������ɫ����
	 * @return ��ɫ������0~255��
	 */
	public int b() {
		if (blue < 0) return 0;
		else if (blue > 255) return 255;
		else return blue;
	}
	
	/**
	 * ����͸������
	 * @return ͸��������0~255��
	 */
	public int a() {
		if (alpha < 0) return 0;
		else if (alpha > 255) return 255;
		else return alpha;
	}
	
	private int red, green, blue, alpha;
}
