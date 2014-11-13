package com.example.testproject;

import android.support.v7.app.ActionBarActivity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
import android.graphics.Matrix;
//import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
//import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
import com.example.testproject.ConfirmCoordinates;



public class MainActivity extends ActionBarActivity implements OnTouchListener{
	
	private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;
    
    //control bool -- used to determine if click should register instead of movement or zoom
    private boolean isOnClick;
    
    //scroll threshold -- max amount of movement allowed to be considered a click
    private final float SCROLL_THRESHOLD = 10;

    // These matrices will be used to scale points of the image
    Matrix matrix1 = new Matrix();
    Matrix matrix2 = new Matrix();
    Matrix savedMatrix1 = new Matrix();
    Matrix savedMatrix2 = new Matrix();

    // The 3 states (events) the user is trying to perform, excluding click (could be implemented)
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView view = (ImageView) findViewById(R.id.background1);
        view.setOnTouchListener(this);
        
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
    	
        ImageView view = (ImageView) v;
        float scale;

        ImageView plupp = (ImageView) findViewById(R.id.plupp);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)plupp.getLayoutParams();
        
        view.setScaleType(ImageView.ScaleType.MATRIX);
        plupp.setScaleType(ImageView.ScaleType.MATRIX);
        
        dumpEvent(event);

        //control toast -- display when click registers
        //Context context = getApplicationContext();
        //CharSequence text = "Plupp!";
        //int duration = Toast.LENGTH_SHORT;
        //Toast controlToast = Toast.makeText(context, text, duration);
        
        /**Handling of touch events**/
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        
        	//first finger down only
            case MotionEvent.ACTION_DOWN:  
            									savedMatrix1.set(matrix1);
            									savedMatrix2.set(matrix2);
                                                start.set(event.getX(), event.getY());
                                                isOnClick = true;
                                                Log.d(TAG, "mode=DRAG"); // write to LogCat
                                                mode = DRAG;
                                                break;
                                                
            //first finger lifted
            //note that movement happens in between
            case MotionEvent.ACTION_UP: 		
            									if(isOnClick){
            										//Control message to show activation
            										lp.leftMargin = (int)event.getX();
            									    lp.topMargin = (int)event.getY();
            									    plupp.setLayoutParams(lp);
            										plupp.setVisibility(View.VISIBLE);
            										
            										//dialog box asking for confirmation of position (y/n)
            										
            										//if yes, save coordinates relative to background view
            										//how? getTop(), getRight()?
            										Log.d("MapLog", "Hejhej");
            										ConfirmCoordinates dialog = new ConfirmCoordinates();
            										dialog.show(getSupportFragmentManager(), "test");
            									}
            									
            //second finger lifted
            case MotionEvent.ACTION_POINTER_UP: 
                                                mode = NONE;
                                                Log.d(TAG, "mode=NONE");
                                                break;
                                                
            //first and second finger down
            case MotionEvent.ACTION_POINTER_DOWN: 
                                                oldDist = spacing(event);
                                                Log.d(TAG, "oldDist=" + oldDist);
                                                if (oldDist > 5f) {
                                                    savedMatrix1.set(matrix1);
                                                    midPoint(mid, event);
                                                    mode = ZOOM;
                                                    Log.d(TAG, "mode=ZOOM");
                                                }
                                                break;

            //movement (change between ACTION_DOWN and ACTION_UP)
            case MotionEvent.ACTION_MOVE:		
	            							   /*This first if-statement checks if the movement that is mostly inevitably caused upon touch
	            								*exceeds a defined threshold. If it does, then the movement is large enough that the program
	            								*shouldn't register a click, and sets the bool to false. If it doesn't exceed the threshold
	            								*then a click should register, but the small movement is still in effect because the rest of
	            								*the case is executed.*/
	            								if (isOnClick && (Math.abs(start.x - event.getX()) > SCROLL_THRESHOLD || Math.abs(start.y - event.getY()) > SCROLL_THRESHOLD))
	            									isOnClick = false;
	            								
	            								//Calculate and apply movement
	            								if (mode == DRAG) { 		//case: drag
	                                                matrix1.set(savedMatrix1);
	                                                matrix1.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
	            								} 
	                                            else if (mode == ZOOM) {  	//case: pinch zooming
	                                                float newDist = spacing(event);
	                                                Log.d(TAG, "newDist=" + newDist);
	                                                if (newDist > 5f) {
	                                                    matrix1.set(savedMatrix1);
	                                                    scale = newDist / oldDist; // setting the scaling of the
	                                                                                // matrix...if scale > 1 means
	                                                                                // zoom in...if scale < 1 means
	                                                                                // zoom out
	                                                    matrix1.postScale(scale, scale, mid.x, mid.y);
	                                                }
	                                            }
                                                break;
                                                

        }

        view.setImageMatrix(matrix1);
        /** sätta förändring applicerad på view på plupp?**/


        return true; // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event){
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
        

    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
