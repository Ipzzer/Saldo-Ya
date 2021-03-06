package com.artixworks.rapipass;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.artix.rapipass.commons.ExpandableListAdapter;
import com.artix.rapipass.commons.Utils;
import com.artixworks.datasave.DatabaseManager;
import com.artixworks.datasave.Tarjetas;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.frag_tarjetas)
public class FragTarjetasGroupActivity extends Activity {
	
	private List<String> groupList;
	private List<String> childList;
	private Map<String, List<String>> tarjetCollection;
	@ViewById ExpandableListView Expandable_tarjet_list;
	@ViewById TextView textView1;
	private final static int RESULT_ADD_CARD = 1;
	
	@AfterViews
	void mostrarDatosLista() {
		textView1.setTypeface(Utils.loadFont(getApplicationContext()));
		DatabaseManager.init(getApplicationContext());
		createGroupList();
		createCollection();
		final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, groupList, tarjetCollection);
		Expandable_tarjet_list.setAdapter(expListAdapter);
	}
	
    private void createGroupList() {
    	List<Tarjetas> lTarjetas = DatabaseManager.getInstance().getAllTarjetas();
    	groupList = new ArrayList<String>();
    	
    	for(Tarjetas t : lTarjetas)
    		groupList.add(t.getIdTarjeta());
    }
    
    private void createCollection() {
        tarjetCollection = new LinkedHashMap<String, List<String>>();
 
        for(String str : groupList) {
        	loadChild("N�mero de tarjeta : " + str);
        	tarjetCollection.put(str, childList);
        }
    }
    
    private void loadChild(String childStr) {
        childList = new ArrayList<String>();
        childList.add(childStr);
    }
    
    public void addCard(View v){
    	//Se muestra la ventana de agregar nueva tarjeta
    	Intent i = new Intent(this, HomeActivity_.class);
    	startActivityForResult(i, RESULT_ADD_CARD);
    }
    
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case RESULT_ADD_CARD://Espera el resultado de la pantalla de configuracion
        	mostrarDatosLista();
            break;
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
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
