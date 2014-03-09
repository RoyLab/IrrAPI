package zte.irrlib.core;

/**
 * RGB��ɫ�࣬ÿ����ɫ��ȡֵ������0~255������
 * @author Roy
 *
 */
public class Color3i {
	
	/**
	 * ���췽��������ɫ
	 */
	public Color3i(){
		red = green = blue = 0xff;
	}
	
	/**
	 * ���췽��
	 * @param v1 ��ɫ����
	 * @param v2 ��ɫ����
	 * @param v3 ��ɫ����
	 */
	public Color3i(int v1, int v2, int v3){
		red = v1; green = v2; blue = v3;
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
	
	private int red, green, blue;
}
