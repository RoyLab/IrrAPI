package examples;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

/**
 * 这是个测试用的模块，可以删除
 * @author Roy
 *
 */
public class TestGLRenderer implements Renderer {
	
	private float [] triangle = {
			-13, -10, -10,
			7, -10, -10,
			7, 10, -10			
	};
	
	private short [] indicesData = {
			0, 1, 2
	};
	
	private float [] coordData = {
			0.4f, 0.3f,
			0.8f, 0.3f,
			0.8f, 0.7f
	};
	
	private FloatBuffer fBuffer, tBuffer;
	private ShortBuffer indices;
	
	private Bitmap pic;
	private int mTexID = 2;
	private Context mContext;
	
	public TestGLRenderer(Context act){
		ByteBuffer buff = ByteBuffer.allocateDirect(triangle.length * 4);
		buff.order(ByteOrder.nativeOrder());
		fBuffer = buff.asFloatBuffer();
		fBuffer.put(triangle);
		fBuffer.position(0);
		
		buff = ByteBuffer.allocateDirect(coordData.length * 4);
		buff.order(ByteOrder.nativeOrder());
		tBuffer = buff.asFloatBuffer();
		tBuffer.put(coordData);
		tBuffer.position(0);
		
		buff = ByteBuffer.allocateDirect(indicesData.length * 4);
		buff.order(ByteOrder.nativeOrder());
		indices = buff.asShortBuffer();
		indices.put(indicesData);
		indices.position(0);
		
		mContext = act;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		InputStream input = null;
		try {
			input = mContext.getAssets().open("sysmedia/zte.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (input != null){
			pic = BitmapFactory.decodeStream(input);
		}
		
		gl.glClearColor(.6f, .6f, 1.f, 1.0f);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_LIGHTING);
		
		int [] texID = new int[1];
		gl.glGenTextures(1, texID, 0);
		
		mTexID = texID[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexID);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, pic, 0);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		//GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, pic, 0);
	}

	@Override
		public void onDrawFrame(GL10 gl) {
	//		angle = (angle+2)%360;
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			
			gl.glColor4f(.9f, .9f, .9f, 1.f);
			
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tBuffer);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 2);  
			
			gl.glPushMatrix();
			gl.glTranslatef(0, 0, -20);
	//		gl.glRotatef(angle, 0, 1, 1);
			gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT, indices);
			gl.glPopMatrix();
			
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			//gl.glFlush();
		}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 30.0f, (float)width/height, 1.f, 100.f);
	}
}
