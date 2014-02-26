package com.artixworks.datasave;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MOVIMIENTOS")
public class Movimientos {

	@DatabaseField(columnName = "ID_MOVIMIENTOS", dataType = DataType.STRING, id = true)
	private String idMovimientos;
	
	@DatabaseField(columnName = "FECHAS", dataType = DataType.STRING)
	private String fechas;

	public String getIdMovimientos() {
		return idMovimientos;
	}

	public void setIdMovimientos(String idMovimientos) {
		this.idMovimientos = idMovimientos;
	}

	public String getFechas() {
		return fechas;
	}

	public void setFechas(String fechas) {
		this.fechas = fechas;
	}

}
