package com.artix.rapipass.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.artixworks.datasave.Transacciones;

public class HttpCalls {
	static int i = 0;
	static List<Transacciones> listaTransacciones = new ArrayList<Transacciones>();
	static Transacciones transacciones = null;

	public static Object[] getHttpData(long tarjeta) {
		HttpURLConnection urlConnection = null;
		Object returnData [] = new Object[5];

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
					break;
				}else if (acum == 75) {
					returnData[4] = regexKSI(line); 
				}
			}
		
			return returnData;
		} catch (Exception ex) {
			returnData[0] = ex;
			return returnData;
		} finally {
			urlConnection.disconnect();
		}
	}
	
	/**
	 * Extrae la lista de los ultimos movimientos realizados con la tarjeta.
	 * 
	 * @param tarjeta ID de la tarjeta a consultarlos movimientos
	 * @return
	 */
	public static List<Transacciones> getHttpDataMove(long tarjeta) {
		HttpURLConnection urlConnection = null;
		//Número de registros que desea que el servicio retorne
		final int MAXLINE = 2;
		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault());
		
		try {
			//Identificador necesario de tarjeta para realizar esta operacion
			String KSI = (String) getHttpData(tarjeta)[4];
			StringBuilder peticion = new StringBuilder();
			peticion.append(
					"http://200.46.245.230:8080/PortalCAE-WAR-MODULE/ComercialesPortalServlet?KSI=")
					.append(KSI)
					.append("&accion=1&itemms=3000&item=2&fechalogeo=")
					.append(format.format(d)).append("&DiasMov=")
					.append(MAXLINE).append("&FechaInicioMovimientos=");
		
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
			
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
				regexSteps(line, tarjeta);
			}
			
			return listaTransacciones;
		} catch (Exception ex) {
			return null;
		} finally {
			urlConnection.disconnect();
		}
	}
	
	private static String regex(String cadena) {
		cadena.trim();
		Pattern pattern = Pattern.compile("<[^>]*>([^<]*)</[^>]*>");
		Matcher matcher = pattern.matcher(cadena);

		while (matcher.find()) {
			return matcher.group(1);
		}
	
		return null;
	}
	
	private static String regexKSI(String cadena) {
		cadena.trim();
		Pattern pattern = Pattern.compile("[A-Za-z0-9]+=([A-Z0-9]+)(.*)");
		Matcher matcher = pattern.matcher(cadena);

		if (matcher.find())
	    {
	        return matcher.group(1);
	    }
	
		return null;
	}
	
	private static void regexSteps(String cadena, long tarjeta) {
		cadena.trim();
		Pattern pattern = Pattern.compile("<td w[^>]*>&nbsp;([^<]*)</[^>]*>");
		Matcher matcher = pattern.matcher(cadena);
		
		if (matcher.find())
		{
			switch (i) {
			case 0:
				transacciones = new Transacciones();
				transacciones.setNroTransaccion(Integer.valueOf(matcher.group(1)));
				i++;
				break;
			case 1:
				transacciones.setMovimientos(matcher.group(1));
				i++;
				break;
			case 2:
				transacciones.setFecha(matcher.group(1));
				i++;
				break;
			case 3:
				transacciones.setLugar(Integer.valueOf(matcher.group(1)));
				i++;
				break;
			case 4:
				transacciones.setMonto(Double.valueOf(matcher.group(1)));
				i++;
				break;
			case 5:
				transacciones.setSaldoTarjeta(Double.valueOf(matcher.group(1)));
				transacciones.setIdTarjeta(String.valueOf(tarjeta));
				listaTransacciones.add(transacciones);
				i=0;
				break;

			default:
				break;
			}
		}
	}
}
