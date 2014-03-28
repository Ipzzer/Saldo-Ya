package com.artixworks.datasave;

//import com.j256.ormlite.field.DataType;
//import com.j256.ormlite.field.DatabaseField;
//import com.j256.ormlite.table.DatabaseTable;

//@DatabaseTable(tableName = "TRANSACCIONES")
public class Transacciones {
	
//	@DatabaseField(columnName = "ID_TARJETA", dataType = DataType.STRING)
	private String idTarjeta;
	
//	@DatabaseField(columnName = "NRO_TRANSACCION", dataType = DataType.INTEGER)
	private int nroTransaccion;
	
//	@DatabaseField(columnName = "MOVIMIENTOS", dataType = DataType.STRING)
	private String movimientos;
	
//	@DatabaseField(columnName = "FECHA", dataType = DataType.STRING)
	private String fecha;
	
//	@DatabaseField(columnName = "LUGAR", dataType = DataType.INTEGER)
	private int lugar;
	
//	@DatabaseField(columnName = "MONTO", dataType = DataType.DOUBLE)
	private double monto;
	
//	@DatabaseField(columnName = "SALDO_TARJETA", dataType = DataType.DOUBLE)
	private double saldoTarjeta;
	

	public String getIdTarjeta() {
		return idTarjeta;
	}

	public void setIdTarjeta(String idTarjeta) {
		this.idTarjeta = idTarjeta;
	}

	public int getNroTransaccion() {
		return nroTransaccion;
	}

	public void setNroTransaccion(int nroTransaccion) {
		this.nroTransaccion = nroTransaccion;
	}

	public String getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(String movimientos) {
		this.movimientos = movimientos;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public int getLugar() {
		return lugar;
	}

	public void setLugar(int lugar) {
		this.lugar = lugar;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getSaldoTarjeta() {
		return saldoTarjeta;
	}

	public void setSaldoTarjeta(double saldoTarjeta) {
		this.saldoTarjeta = saldoTarjeta;
	}

}
