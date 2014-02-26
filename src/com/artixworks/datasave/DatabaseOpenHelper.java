package com.artixworks.datasave;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseOpenHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "rapiquery.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Tarjetas, String> tarjetasDao = null;
	private RuntimeExceptionDao<Tarjetas, String> TarjetasRuntimeDao = null;
	private Dao<Movimientos, String> movimientosDao = null;
	private RuntimeExceptionDao<Movimientos, String> MovimientosRuntimeDao = null;

	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource connectionSource) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, Tarjetas.class);
			TableUtils.createTableIfNotExists(connectionSource, Movimientos.class);
		} catch (SQLException e) {
			Log.e(DatabaseOpenHelper.class.getName(),
					"No es posible crear la base de datos", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource arg1,
			int oldVersion, int newVersion) {
		List<String> allSql = new ArrayList<String>();
		switch (oldVersion) {
		case 1:
			// allSql.add("alter table AdData add column `new_col` VARCHAR");
			// allSql.add("alter table AdData add column `new_col2` VARCHAR");
		}
		for (String sql : allSql) {
			db.execSQL(sql);
		}
	}

	@Override
	public void close() {
		super.close();
		tarjetasDao = null;
		movimientosDao = null;
	}

	public Dao<Tarjetas, String> getTarjetasDao() throws SQLException,
			java.sql.SQLException {
		if (null == tarjetasDao) {
			tarjetasDao = getDao(Tarjetas.class);
		}
		return tarjetasDao;
	}

	public RuntimeExceptionDao<Tarjetas, String> getTarjetasDataDao() {
		if (TarjetasRuntimeDao == null) {
			TarjetasRuntimeDao = getRuntimeExceptionDao(Tarjetas.class);
		}
		return TarjetasRuntimeDao;
	}

	public Dao<Movimientos, String> getMovimientosDao() throws SQLException,
			java.sql.SQLException {
		if (null == movimientosDao) {
			movimientosDao = getDao(Movimientos.class);
		}
		return movimientosDao;
	}

	public RuntimeExceptionDao<Movimientos, String> getMovimientosDataDao() {
		if (MovimientosRuntimeDao == null) {
			MovimientosRuntimeDao = getRuntimeExceptionDao(Movimientos.class);
		}
		return MovimientosRuntimeDao;
	}

}
