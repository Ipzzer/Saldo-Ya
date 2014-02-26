package com.artixworks.rapipass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.artix.rapipass.commons.Utils;
import com.artixworks.datasave.DatabaseManager;
import com.artixworks.datasave.Movimientos;
import com.artixworks.datasave.Tarjetas;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.frag_consulta)
public class HomeActivity extends Activity {

	@ViewById EditText Edt_nombre_tarjeta;
	@ViewById EditText Edt_consulta_saldo;
	@ViewById Button Btn_consulta_saldo;
	@ViewById TextView Txt_nombre_tarjeta;
	@ViewById TextView Txt_numero_tarjeta;
	AlertDialog.Builder builder = null;
	private String[] returnData = new String[4];
	private ProgressDialog ringProgressDialog;
	
	
	@AfterViews
	void inicializarVistas() {
		Edt_nombre_tarjeta.setTypeface(Utils.loadFont(getApplicationContext()));
		Edt_consulta_saldo.setTypeface(Utils.loadFont(getApplicationContext()));
		Txt_nombre_tarjeta.setTypeface(Utils.loadFont(getApplicationContext()));
		Btn_consulta_saldo.setTypeface(Utils.loadFont(getApplicationContext()));
		Txt_numero_tarjeta.setTypeface(Utils.loadFont(getApplicationContext()));
		DatabaseManager.init(getApplicationContext());
	}

	@Background
	void connectItem(long tarjeta) {
		
		presentarProgressBar();//Abre el progressDialog
		HttpURLConnection urlConnection = null;

		try {
			StringBuilder peticion = new StringBuilder();
			peticion.append(
					"http://200.46.245.230:8080/PortalCAE-WAR-MODULE/SesionPortalServlet?accion=6&NumDistribuidor=99&NomUsuario=usuInternet&NomHost=AFT&NomDominio=aft.cl&Trx=&RutUsuario=0&NumTarjeta=")
					.append(tarjeta).append("&bloqueable=");

			URL url = new URL(peticion.toString());
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setReadTimeout(10000);
			urlConnection.setConnectTimeout(10000);

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.117 Safari/537.36");

			OutputStreamWriter request = new OutputStreamWriter(urlConnection.getOutputStream());
			request.flush();
			request.close();

			InputStream inputStream = urlConnection.getInputStream();

			BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			int acum = 0;

			while ((line = rd.readLine()) != null) {
				acum = acum + 1;

				if (acum == 264) {
					returnData[0] = regex(line);
				} else if (acum == 266) {
					returnData[1] = regex(line);
				} else if (acum == 272) {
					returnData[2] = regex(line);
				} else if (acum == 274) {
					returnData[3] = regex(line);
				}
			}
			actualizarEstadoDescarga();//Cierra el progressDialog y actualiza la interfaz con el resultado
		} catch (Exception ex) {
			mostrarErrorConexion(ex);
		} finally {
			urlConnection.disconnect();
		}
	}

	/**
	 * OnPreExecute, este metodo se ejecuta justo antes del proceso en background
	 */
	@UiThread
	void presentarProgressBar() {
		ringProgressDialog = ProgressDialog.show(HomeActivity.this, getString(R.string.msg_progress_1), getString(R.string.msg_progress_2), true);
		ringProgressDialog.setCancelable(true);
	}

	/**
	 * OnPostExecute, este metodo se ejecuta despues de terminar el proceso en background
	 */
	@UiThread
	void actualizarEstadoDescarga() {
		ringProgressDialog.dismiss();
		builder = new AlertDialog.Builder(HomeActivity.this);
		final String idCard = returnData[0];
		final String status = returnData[1];
		final String credit = returnData[2];
		final String date = returnData[3];

		if (status != null) {
			builder.setMessage(getString(R.string.mensaje, credit, date));
			builder.setPositiveButton(getString(R.string.btn_guardar),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Tarjetas t = new Tarjetas();
							Movimientos m = new Movimientos();

							t.setIdTarjeta(idCard);
							t.setEstado(status);
							t.setSaldo(credit);
							t.setNombre(Edt_nombre_tarjeta.getText().toString());
							m.setIdMovimientos(idCard);
							m.setFechas(date);
							DatabaseManager.getInstance().insertTarjeta(t, m);
							dialog.dismiss();
							Toast.makeText(getApplicationContext(), getString(R.string.tarjeta_almacenada), Toast.LENGTH_LONG).show();
							limpiarCampos();
						}
					});
		} else {
			builder.setMessage(getString(R.string.numero_invalido));
		}
		builder.setCancelable(false);
		builder.setTitle(getString(R.string.saldo_disponible));

		builder.setNegativeButton(getString(R.string.btn_aceptar),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						limpiarCampos();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private String regex(String cadena) {
		cadena.trim();
		Pattern pattern = Pattern.compile("<[^>]*>([^<]*)</[^>]*>");
		Matcher matcher = pattern.matcher(cadena);

		while (matcher.find()) {
			System.out.println(matcher.group(1));
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * Evento del boton Consultar Saldo
	 * @param v
	 */
	public void llamarServidor(View v) {
		long tarjeta = Long.valueOf(Edt_consulta_saldo.getText().toString()
				.length() == 0 ? "0" : Edt_consulta_saldo.getText().toString());
		if (tarjeta == 0)
			Toast.makeText(getApplicationContext(), getString(R.string.ingrese_num_tarjeta), Toast.LENGTH_LONG).show();
		else
			connectItem(tarjeta);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(1);//Se retorna el estado a la actividad anterior para que actualice 
	}
	
	public void limpiarCampos() {
		Edt_nombre_tarjeta.setText("");
		Edt_consulta_saldo.setText("");
	}
	
	@UiThread
	void mostrarErrorConexion(Exception e) {
		if(e instanceof SocketTimeoutException) {
			Toast.makeText(getApplicationContext(), getString(R.string.time_out), Toast.LENGTH_LONG).show();
		}else {
			Toast.makeText(getApplicationContext(), getString(R.string.sin_conexion), Toast.LENGTH_LONG).show();
		}
		ringProgressDialog.dismiss();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.about_screen:
	        	Intent i = new Intent(this, AboutActivity.class);
	        	startActivity(i);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        } 
	}
}
