package com.artix.rapipass.commons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpCalls {

	public static Object[] getHttpData(Object [] returnData, long tarjeta) {
		HttpURLConnection urlConnection = null;

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
	
	private static String regex(String cadena) {
		cadena.trim();
		Pattern pattern = Pattern.compile("<[^>]*>([^<]*)</[^>]*>");
		Matcher matcher = pattern.matcher(cadena);

		while (matcher.find()) {
			System.out.println(matcher.group(1));
			return matcher.group(1);
		}
		return null;
	}
}
