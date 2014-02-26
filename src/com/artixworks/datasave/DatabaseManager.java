package com.artixworks.datasave;

import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;

import android.content.Context;

public class DatabaseManager {
	
	 static private DatabaseManager instance;

	    static public void init(Context ctx) {
	        if (null==instance) {
	            instance = new DatabaseManager(ctx);
	        }
	    }

	    static public DatabaseManager getInstance() {
	        return instance;
	    }

	    private DatabaseOpenHelper helper;
	    private DatabaseManager(Context ctx) {
	        helper = new DatabaseOpenHelper(ctx);
	    }

	    private DatabaseOpenHelper getHelper() {
	        return helper;
	    }
	    
	    public ConnectionSource getConnectionSource(){
			return getHelper().getConnectionSource();
	    }

	    
	    //XXX: Clase Tarjetas
	    public List<Tarjetas> getAllTarjetas() {
	        List<Tarjetas> tarjetas = null;
	        	try {
					tarjetas = getHelper().getTarjetasDao().queryForAll();
				} catch (java.sql.SQLException e) {
					e.printStackTrace();
				}
	       
	        return tarjetas;
	    }
	    public List<Tarjetas> getSomeTarjeta(Map<String,Object> arg0) {
	        List<Tarjetas> Tarjetas = null;
	        Tarjetas = getHelper().getTarjetasDataDao().queryForFieldValues(arg0);
	        return Tarjetas;  
	    }

	    public Tarjetas getByIdTarjeta(String id){
	    	Tarjetas entidad;
	    	RuntimeExceptionDao<Tarjetas, String> dao = getHelper().getTarjetasDataDao();
	    	entidad = dao.queryForId(id);
	    	return entidad;
	    }
	   
		public void insertTarjeta(Tarjetas tarjetas, Movimientos mov)
		{
			RuntimeExceptionDao<Tarjetas, String> dao = getHelper().getTarjetasDataDao();
			if (dao.idExists(tarjetas.getIdTarjeta())){
				updateTarjeta(tarjetas);
				updateMovimientos(mov);
			}else{
				dao.create(tarjetas);
				insertMovimientos(mov);
			}
		}
		
		public int updateTarjeta(Tarjetas Tarjetas)
		{
			RuntimeExceptionDao<Tarjetas, String> dao = getHelper().getTarjetasDataDao();
			int i = dao.update(Tarjetas);
			return i;
		}
	    
		public int deleteTarjeta(Tarjetas Tarjetas)
		{
			RuntimeExceptionDao<Tarjetas, String> dao = getHelper().getTarjetasDataDao();
			int i = dao.delete(Tarjetas);
			return i;
		}
		
		public void deleteAllTarjeta()
		{
			RuntimeExceptionDao<Tarjetas, String> dao = getHelper().getTarjetasDataDao();
			List<Tarjetas> list = dao.queryForAll();
			dao.delete(list);
		}
		
		//XXX: Clase Movimientos
		public List<Movimientos> getAllMovimientos() {
			List<Movimientos> mov = null;
			try {
				mov = getHelper().getMovimientosDao().queryForAll();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
			
			return mov;
		}
		public List<Movimientos> getSomeMovimientos(Map<String,Object> arg0) {
			List<Movimientos> movimientos = null;
			movimientos = getHelper().getMovimientosDataDao().queryForFieldValues(arg0);
			return movimientos;  
		}
		
		public Movimientos getByIdMovimientos(String id){
			Movimientos entidad;
			RuntimeExceptionDao<Movimientos, String> dao = getHelper().getMovimientosDataDao();
			entidad = dao.queryForId(id);
			return entidad;
		}
		
		public void insertMovimientos(Movimientos mov)
		{
			RuntimeExceptionDao<Movimientos, String> dao = getHelper().getMovimientosDataDao();
			//if(dao.idExists(arg0))
			dao.create(mov);
		}
		
		public int updateMovimientos(Movimientos mov)
		{
			RuntimeExceptionDao<Movimientos, String> dao = getHelper().getMovimientosDataDao();
			int i = dao.update(mov);
			return i;
		}
		
		public int deleteMovimientos(Movimientos mov)
		{
			RuntimeExceptionDao<Movimientos, String> dao = getHelper().getMovimientosDataDao();
			int i = dao.delete(mov);
			return i;
		}
		
		public void deleteAllMovimientos()
		{
			RuntimeExceptionDao<Movimientos, String> dao = getHelper().getMovimientosDataDao();
			List<Movimientos> list = dao.queryForAll();
			dao.delete(list);
		}

}
