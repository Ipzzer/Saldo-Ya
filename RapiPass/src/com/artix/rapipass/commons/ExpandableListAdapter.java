package com.artix.rapipass.commons;

import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.artixworks.datasave.DatabaseManager;
import com.artixworks.datasave.Movimientos;
import com.artixworks.datasave.Tarjetas;
import com.artixworks.rapipass.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	
	private Activity context;
    private Map<String, List<String>> cardCollectionsInfo;
    private List<String> idCards;
    private String cardName = "";
    private String estadoTarjeta = "";
    
    public ExpandableListAdapter(Activity context, List<String> idCard, Map<String, List<String>> cardCollection) {
        this.context = context;
        this.cardCollectionsInfo = cardCollection;
        this.idCards = idCard;
        DatabaseManager.init(context);
    }

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return cardCollectionsInfo.get(idCards.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		
		final String cardInfo = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
        
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.frag_tarjetas_child, null);
        }
        
        TextView item_tarjeta = (TextView) convertView.findViewById(R.id.Txt_tarjetas_numero);
        TextView item_tcorredor = (TextView) convertView.findViewById(R.id.Txt_tarjetas_corredores);
        TextView item_transaccion = (TextView) convertView.findViewById(R.id.Txt_tarjetas_ultima_transaccion);
        Button eliminarTarjeta = (Button) convertView.findViewById(R.id.Btn_tarjetas_eliminar);
        
        Movimientos m = DatabaseManager.getInstance().getByIdMovimientos(cardName);
        item_tarjeta.setText(cardInfo);
        item_transaccion.setText(context.getString(R.string.texto_ultima_transaccion) + m.getFechas());
        item_tcorredor.setText(estadoTarjeta);
        
        final String [] chapuza = cardInfo.split(":"); // :D
        
        eliminarTarjeta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatabaseManager.getInstance().deleteTarjeta(DatabaseManager.getInstance().getByIdTarjeta(chapuza[1].trim()));
				DatabaseManager.getInstance().deleteMovimientos(DatabaseManager.getInstance().getByIdMovimientos(chapuza[1].trim()));
				Toast.makeText(context, context.getString(R.string.tarjeta_eliminada) + chapuza[1].trim(), Toast.LENGTH_SHORT).show();
			}
		});
        
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return cardCollectionsInfo.get(idCards.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return idCards.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		 return idCards.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		cardName = (String) getGroup(groupPosition);
		estadoTarjeta = "";
		
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.frag_tarjetas_group, null);
        }
        
        TextView numero_tarjeta = (TextView) convertView.findViewById(R.id.Txt_numero_tarjeta_corredor);
        TextView tipo_corredor = (TextView) convertView.findViewById(R.id.Txt_tipo_corredor);
        TextView saldo_tarjeta = (TextView) convertView.findViewById(R.id.Txt_tarjeta_saldo_corredor);
        
        numero_tarjeta.setText(cardName);
        Tarjetas t = DatabaseManager.getInstance().getByIdTarjeta(cardName);
        tipo_corredor.setText(t.getNombre());
        saldo_tarjeta.setText("$" + t.getSaldo());
        estadoTarjeta = t.getEstado();
        t = null;
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
