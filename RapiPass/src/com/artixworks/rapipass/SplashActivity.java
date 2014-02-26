package com.artixworks.rapipass;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class SplashActivity extends Activity {

  private long splashDelay = 1500; //1.5 segundos

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        Intent mainIntent = new Intent().setClass(SplashActivity.this, FragTarjetasGroupActivity_.class);
        startActivity(mainIntent);
        finish();//Destruimos esta activity para prevenir que el usuario retorne aqui presionando el boton Atras.
      }
    };

    Timer timer = new Timer();
    timer.schedule(task, splashDelay);//Pasado los 6 segundos dispara la tarea
  }
  
  @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if (keyCode == KeyEvent.KEYCODE_BACK) {
		// Bloquear la tecla volver para no cerrar el app
		return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}