package com.artix.rapipass.commons;

import java.net.SocketTimeoutException;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;
import com.artixworks.rapipass.R;

public class Utils {

	private static ProgressDialog ringProgressDialog;
	
	/**
	 * Carga una fuente custom a partir de un fichero .otf o .ttf ubicado en la carpeta
	 * assets
	 * @param ctx Contexto de la aplicacion para poder acceder a los assets
	 * @return Typeface a partir del fichero de fuente custom
	 */
	public static Typeface loadFont(Context ctx) {
		return Typeface.createFromAsset(ctx.getAssets(), "svenings.ttf");
	}
	
	
	public static void presentarProgressBar(Context context) {
		ringProgressDialog = ProgressDialog.show(context, context.getString(R.string.msg_progress_1), context.getString(R.string.msg_progress_2), true);
		ringProgressDialog.setIcon(R.drawable.ic_action_refresh);
		ringProgressDialog.setCancelable(true);
	}
	
	public static void ocultarProgressBar() {
		ringProgressDialog.dismiss();
	}
	
	public static void mostrarErrorConexion(Context context, Exception e) {
		if(e instanceof SocketTimeoutException) {
			Toast.makeText(context, context.getString(R.string.time_out), Toast.LENGTH_LONG).show();
		}else {
			Toast.makeText(context, context.getString(R.string.sin_conexion), Toast.LENGTH_LONG).show();
		}
		Utils.ocultarProgressBar();
	}
}
