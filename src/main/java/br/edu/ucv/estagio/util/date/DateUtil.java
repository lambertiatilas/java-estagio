package br.edu.ucv.estagio.util.date;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	
	public static Date maisUmDia(Date data) {
		if (data != null) {
			Format formatador = new SimpleDateFormat("HH:mm:ss");
			if (formatador.format(data).equals("00:00:00")) {
				Calendar c = Calendar.getInstance();
				c.setTime(data);
				c.add(Calendar.SECOND, +86399);
				data = c.getTime();
			}
		}
		return data;
	}
	
	public static Date diminuirDias(Date data, int dias) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, -dias + 1);
		return calendar.getTime();
	}
	
	public static boolean verificarPeriodoDias(Date dataDe, Date dataAte, int dias) {
		Date data = diminuirDias(dataAte, dias + 1);
		return dataDe.after(data);
	}
}