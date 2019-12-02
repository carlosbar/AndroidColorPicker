# AndroidColorPicker

#how to use?
#
#The color picker extends a SurfaceView, this needs to be added to some existent view, as for example, a LinearLayout
#You need to inform the current selected color on the class constructor using the R, G and B for the current color RGB channels.
#When some color is clicked in the color wheel, the callback onColorSelected is called.
#
#e.g:
#
#LinearLayout colorArea = findViewById(R.id.colorArea);
#
#ColorPicker colorPicker = new ColorPicker(this, r, g, b);
#colorPicker.setOnClickListener(new ColorPicker.OnClickListener() {
#	@Override
#	public void onColorSelected(int color) {
#		theColor = String.format(Locale.getDefault(), "#%08x", color);
#		selectedColor.setBackgroundColor(color);
#	}
#});


