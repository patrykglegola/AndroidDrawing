package pg.androiddrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by student on 05.05.17.
 */
public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Thread drawingThread;
    private boolean threadIsWorking = false;
    private Object lock = new Object();

    public DrawingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
    }

    public void continueDrawing() {
        drawingThread = new Thread(this);
        threadIsWorking = true;
        drawingThread.start();
    }

    public void pauseDrawing() {
        threadIsWorking = false;
    }


    //obsługa dotknięcia ekranu
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        //sekcja krytyczna - modyfikacja rysunku na wyłączność
        synchronized (lock) {
            //modyfikacja rysunku...
        }
        return true;
    }

    //żeby lint nie wyświetlał ostrzeżeń - onTouchEvent i performClick trzeba implementować razem
    public boolean performClick() {
        return super.performClick();
    }


    @Override
    public void run() {
        while(threadIsWorking) {
            Canvas canvas = null;
            try {
                //sekcja krytyczna - żaden inny wątek nie może używać pojemnika
                synchronized (holder) {
                    //czy powierzchnia jest prawidłowa
                    if (!holder.getSurface().isValid())
                        continue;
                    canvas = holder.lockCanvas(null);
                    synchronized (lock) {
                        if (threadIsWorking) {
                            //rysowanie na lokalnej kanwie
                        }
                    }
                }

            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(1000 / 25);
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //zatrzymanie rysowania
        threadIsWorking = false;

    }
}
