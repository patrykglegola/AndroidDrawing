package pg.androiddrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Thread drawingThread;
    private boolean isThreadWorking = false;
    private Object lock = new Object();
    private Bitmap bitmap = null;
    private Canvas canvas = null;
    private Paint paint;
    private Path path = null;

    public DrawingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);

        path = new Path();
        paint = new Paint();
        selectDefaultPaint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        selectDefaultPaint();

    }

    public void continueDrawing() {
        drawingThread = new Thread(this);
        isThreadWorking = true;
        drawingThread.start();
    }

    public void pauseDrawing() {
        isThreadWorking = false;
    }


    //obsługa dotknięcia ekranu
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        //sekcja krytyczna - modyfikacja rysunku na wyłączność
        synchronized (lock) {
            //modyfikacja rysunku...
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //rysowanie...
                    path.moveTo(event.getX(), event.getY());
                    canvas.drawCircle(event.getX(),event.getY(), 5, paint);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //rysowanie...
                    path.lineTo(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    canvas.drawPath(path, paint);
                    path.reset();
                    canvas.drawCircle(event.getX(),event.getY(), 5, paint);

                    break;
                default:
                    return false;

            }
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
        //canvas.drawBitmap(bitmap, 0, 0, null);
        //canvas.drawPath(path, paint);
        return true;
    }

    //żeby lint nie wyświetlał ostrzeżeń - onTouchEvent i performClick trzeba implementować razem
    public boolean performClick() {
        return super.performClick();
    }


    @Override
    public void run() {
        while(isThreadWorking) {
            Canvas canvas = null;
            try {
                //sekcja krytyczna - żaden inny wątek nie może używać pojemnika
                synchronized (holder) {
                    //czy powierzchnia jest prawidłowa
                    if (!holder.getSurface().isValid())
                        continue;
                    canvas = holder.lockCanvas(null);
                    synchronized (lock) {
                        if (isThreadWorking) {
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

        //tworzenie bitmapy i związanej z nią kanwy:
        bitmap = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        clear();
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //tworzenie bitmapy i związanej z nią kanwy:
        bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        clear();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //zatrzymanie rysowania
        isThreadWorking = false;

    }

    public void selectRedPaint() {
        paint.setColor(Color.RED);
    }

    public void selectYellowPaint() {
        paint.setColor(Color.YELLOW);
    }

    public void selectBluePaint() {
        paint.setColor(Color.BLUE);
    }

    public void selectGreenPaint() {
        paint.setColor(Color.GREEN);
    }

    public void clear() {
        canvas.drawARGB(255,255,255,255);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    public void selectDefaultPaint() {
        selectRedPaint();
    }
}
