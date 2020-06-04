# AndroidColorPicker

how to use?

The color picker extends a SurfaceView, this needs to be added to some existent view, as for example, a LinearLayout (Remember to add a background color to the layout!).
You need to inform the current selected color (Android's native color format) on the class constructor.
When some color is clicked in the color wheel, the callback onColorSelected is called.

e.g:

```
LinearLayout colorArea = findViewById(R.id.colorArea);

ColorPicker colorPicker = new ColorPicker(this, color);
colorPicker.setOnClickListener(new ColorPicker.OnClickListener() {
	@Override
	public void onColorSelected(int color) {
		/* color in Android native color format */
	}
});
colorArea.addView(colorPicker);
```

The outside circle contains all colors from HSV color map. The center circle contains the variations in the saturation and value for the current selected color. Clicking in the outside circle will change the center wheel accordingly.

<p align="center"> 
<img src="colorwheel.png">
</p>

