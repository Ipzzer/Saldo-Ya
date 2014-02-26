package com.artixworks.datasave;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="TARJETAS")
public class Tarjetas {

	@DatabaseField(useGetSet=true, columnName= "ID_TARJETAS", dataType= DataType.STRING, canBeNull=false, unique=true, id=true)
	private String idTarjeta;
	
	@DatabaseField(useGetSet=true, columnName= "NOMBRE", dataType= DataType.STRING)
	private String nombre;
	
	@DatabaseField(useGetSet=true, columnName= "SALDO_ACTUAL", dataType= DataType.STRING)
	private String saldo;
	
	@DatabaseField(useGetSet=true, columnName= "ESTADO", dataType= DataType.STRING)
	private String estado;


	public String getIdTarjeta() {
		return idTarjeta;
	}

	public void setIdTarjeta(String idTarjeta) {
		this.idTarjeta = idTarjeta;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
