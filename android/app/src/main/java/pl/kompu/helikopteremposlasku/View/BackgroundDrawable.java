package pl.kompu.helikopteremposlasku.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kompu on 2015-09-26.
 */
public class BackgroundDrawable extends Drawable {

    private List<Paint> paints;

    public BackgroundDrawable(List<Integer> colors) {
        paints = new ArrayList<>(colors.size());
        for (int color : colors) {
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);

            paints.add(paint);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        switch (paints.size()) {
            case 1:
                drawRectangle(canvas);
                break;
            case 2:
                drawTwoTriangles(canvas);
                break;
            case 3:
                drawThreeTriangles(canvas);
                break;
            default:
                drawStrips(canvas);
                break;
        }
    }

    private void drawRectangle(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawRect(bounds, paints.get(0));
    }

    private void drawTwoTriangles(Canvas canvas) {
        Rect bounds = getBounds();
        int w = bounds.width();
        int h = bounds.height();
        Path path = new Path();
        path.lineTo(0, h);
        path.lineTo(w, h);
        path.close();
        canvas.drawPath(path, paints.get(0));

        path = new Path();
        path.lineTo(w, 0);
        path.lineTo(w, h);
        path.close();
        canvas.drawPath(path, paints.get(1));
    }

    private void drawThreeTriangles(Canvas canvas) {
        drawRectangle(canvas);
        float f = 0.7f;

        Rect bounds = getBounds();
        int w = bounds.width();
        int h = bounds.height();

        Path path = new Path();
        path.moveTo(0, (1 - f) * h);
        path.lineTo(f * w, h);
        path.lineTo(0, h);
        path.close();
        canvas.drawPath(path, paints.get(1));

        path = new Path();
        path.moveTo((1 - f) * w, 0);
        path.lineTo(w, 0);
        path.lineTo(w, f * h);
        path.close();
        canvas.drawPath(path, paints.get(2));
    }

    private void drawStrips(Canvas canvas) {
        Rect bounds = getBounds();
        int w = bounds.width() / paints.size();
        int h = bounds.height();

        int i = 0;
        for (Paint paint : paints) {
            Rect rect = new Rect(i * w, 0, (i + 1) * w, h);
            canvas.drawRect(rect, paint);
            ++i;
        }
    }

    @Override
    public void setAlpha(int i) {
        for (Paint paint : paints) {
            paint.setAlpha(i);
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
