package br.edu.ucv.estagio.util.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class ExecutorRelatorio {

	private String caminhoRelatorio;
	private HttpServletResponse response;
	private Map<String, Object> parametros;
	private String nomeArquivoSaida;
	private List<Object> beans;
	
	private boolean relatorioGerado;
	
	public ExecutorRelatorio(String caminhoRelatorio, HttpServletResponse response, Map<String, Object> parametros, String nomeArquivoSaida, List<Object> beans) {
		this.caminhoRelatorio = caminhoRelatorio;
		this.response = response;
		this.parametros = parametros;
		this.nomeArquivoSaida = nomeArquivoSaida;
		this.beans = beans;
		this.parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
	}
	
	public void execute() throws IOException {
		try {
			InputStream relatorioStream = this.getClass().getResourceAsStream(caminhoRelatorio);
			JasperPrint print = JasperFillManager.fillReport(relatorioStream, parametros, new JRBeanCollectionDataSource(beans));
			relatorioGerado = print.getPages().size() > 0;	
			
			if (this.relatorioGerado) {
				Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exportador = new JRPdfExporter();
				exportador.setExporterInput(new SimpleExporterInput(print));
				exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + nomeArquivoSaida);
				exportador.exportReport();
			}
		} catch (Exception e) {
			throw new IOException("Erro ao executar relatório " + caminhoRelatorio, e);
		}
	}
	
	public boolean isRelatorioGerado() {
		return relatorioGerado;
	}
}
