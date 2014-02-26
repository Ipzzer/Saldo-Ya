package com.artix.rapipass.commons;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {

	/**
	 * Carga una fuente custom a partir de un fichero .otf o .ttf ubicado en la carpeta
	 * assets
	 * @param ctx Contexto de la aplicacion para poder acceder a los assets
	 * @return Typeface a partir del fichero de fuente custom
	 */
	public static Typeface loadFont(Context ctx) {
		return Typeface.createFromAsset(ctx.getAssets(), "svenings.ttf");
	}
}
