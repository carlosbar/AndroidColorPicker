
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class ColorPicker extends SurfaceView implements SurfaceHolder.Callback {
	private static final int colorsHeight = 100;
	private int selectedColor;
	private OnClickListener onClickListener;
	private int width, height;

	public interface OnClickListener {
		void onColorSelected(int color);
	}

	private boolean isPointInCircle(double centerX, double centerY, double x, double y, int radius) {
		double square_dist = Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2);
		return square_dist <= Math.pow(radius, 2);
	}

	private double distanceFromPoint(double x1, double y1, double x2, double y2) {
		double v1 = Math.pow(x2 - x1, 2);
		double v2 = Math.pow(y2 - y1, 2);
		return Math.sqrt(v1 + v2);
	}

	@SuppressLint("ClickableViewAccessibility")
	public ColorPicker(final Context context, int r, int g, int b) {
		super(context);
		this.setClickable(true);
		float [] hsv = new float[3];
		Color.RGBToHSV(r, g, b, hsv);
		selectedColor = (int) hsv[0];

		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN && onClickListener != null) {
					if (width == 0 || height == 0) {
						return true;
					}

					int centerX = width / 2;
					int centerY = height / 2;
					int radius = Math.min(width, height) / 2;

					float x = event.getX(), y = event.getY();

					if (!isPointInCircle(centerX, centerY, x, y, radius)) {
						return false;
					}

					radius -= colorsHeight;

					float[] hsv = {0, 0, 0};

					if (!isPointInCircle(centerX, centerY, x, y, radius)) {
						selectedColor = (int) ((360 + Math.atan2(y - centerY, x - centerX) / Math.PI * 180) % 360);
						hsv[0] = selectedColor;
						hsv[1] = 1.0f;
						hsv[2] = 1.0f;
						drawPicker();
					} else {
						float angle = (float) (360 + Math.atan2(y - centerY, x - centerX) / Math.PI * 180) % 360;
						float distance = (float) distanceFromPoint(centerX, centerY, x, y);
						hsv[0] = selectedColor;
						hsv[1] = distance / radius;
						hsv[2] = angle / 360;
					}
					onClickListener.onColorSelected(Color.HSVToColor(hsv));
				}
				return true;
			}
		});

		getHolder().addCallback(this);

	}

	public void setOnClickListener(OnClickListener listener) {
		onClickListener = listener;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawPicker();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		drawPicker();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	private void drawPicker() {

		Canvas canvas = getHolder().lockCanvas();

		if (canvas == null) {
			return;
		}

		width = getWidth();
		height = getHeight();

		int centerX = width / 2;
		int centerY = height / 2;
		int radius = Math.min(width, height) / 2 - colorsHeight;

		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setDither(true);
		p.setColor(Color.BLACK);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(10);

		/* colors circle */
		canvas.drawCircle(centerX, centerY, radius + colorsHeight, p);

		for (int x = 0; x < 360; x++) {
			float[] hsv = {x, 1.0f, 1.0f};

			float sx = (float) (centerX + Math.cos(Math.toRadians(x)) * radius);
			float sy = (float) (centerY + Math.sin(Math.toRadians(x)) * radius);

			float ex = (float) (centerX + Math.cos(Math.toRadians(x)) * (radius + colorsHeight));
			float ey = (float) (centerY + Math.sin(Math.toRadians(x)) * (radius + colorsHeight));

			p.setColor(Color.HSVToColor(hsv));
			canvas.drawLine(sx, sy, ex, ey, p);
		}

		int[] colors = new int[radius];

		/* draw color variation circle */
		p.setColor(Color.WHITE);
		canvas.drawCircle(centerX, centerY, radius, p);

		for (int x = 0; x < 360; x++) {

			for (int y = 0; y < radius; y++) {
				float[] hsv = {selectedColor, (float) y / radius, (float) x / 360.0f};

				colors[y] = Color.HSVToColor(hsv);
			}

			float cx = (float) (centerX + Math.cos(Math.toRadians(x)) * (radius + 5));
			float cy = (float) (centerY + Math.sin(Math.toRadians(x)) * (radius + 5));

			p.setShader(new LinearGradient(centerX, centerY, cx, cy, colors, null, Shader.TileMode.REPEAT));
			canvas.drawLine(centerX, centerY, cx, cy, p);
		}

		getHolder().unlockCanvasAndPost(canvas);
	}
}
