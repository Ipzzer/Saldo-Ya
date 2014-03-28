package com.artixworks.rapipass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.artix.rapipass.commons.HttpCalls;
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
	@ViewById TextView Txt_title_frame;
	AlertDialog.Builder builder = null;
	private String[] returnData = new String[5];
	
	
	@AfterViews
	void inicializarVistas() {
		Edt_nombre_tarjeta.setTypeface(Utils.loadFont(getApplicationContext()));
		Edt_consulta_saldo.setTypeface(Utils.loadFont(getApplicationContext()));
		Txt_nombre_tarjeta.setTypeface(Utils.loadFont(getApplicationContext()));
		Btn_consulta_saldo.setTypeface(Utils.loadFont(getApplicationContext()));
		Txt_numero_tarjeta.setTypeface(Utils.loadFont(getApplicationContext()));
		Txt_title_frame.setTypeface(Utils.loadFont(getApplicationContext()));
		DatabaseManager.init(getApplicationContext());
	}

	@Background
	void connectItem(long tarjeta) {
		Object objectReturn [] = new Object[5];
		presentarProgressBar();//Abre el progressDialog
		objectReturn = HttpCalls.getHttpData(tarjeta);
		
		if(objectReturn != null) {
			if(objectReturn[0] instanceof Exception)
				mostrarErrorConexion((Exception) objectReturn[0]);
			else if(objectReturn[0] instanceof String) {
				
				for(int i = 0; i < objectReturn.length; i++) {
					returnData[i] = (String) objectReturn[i];
				}
				
				actualizarEstadoDescarga();
			}
		}
	}

	/**
	 * OnPreExecute, este metodo se ejecuta justo antes del proceso en background
	 */
	@UiThread
	void presentarProgressBar() {
		Utils.presentarProgressBar(this);
	}

	/**
	 * OnPostExecute, este metodo se ejecuta despues de terminar el proceso en background
	 */
	@UiThread
	void actualizarEstadoDescarga() {
		Utils.ocultarProgressBar();
		builder = new AlertDialog.Builder(HomeActivity.this);
		final String idCard = returnData[0];
		final String status = returnData[1];
		final String credit = returnData[2];
		final String date = returnData[3];
		
		System.out.println("Retornando idCard desde la web " + idCard);
		if (idCard != null) {
			builder.setMessage(getString(R.string.mensaje, credit, date));
			builder.setIcon(R.drawable.ic_action_accept);
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
			builder.setIcon(R.drawable.ic_action_cancel);
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
		Utils.mostrarErrorConexion(getApplicationContext(), e);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.about_screen:
	        	Intent i = new Intent(this, AboutActivity_.class);
	        	startActivity(i);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        } 
	}
}
