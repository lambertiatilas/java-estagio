package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Empresa;
import br.edu.ucv.estagio.repository.Empresas;

@FacesConverter(forClass = Empresa.class)
public class EmpresaConverter implements Converter {
	
	@Inject
	private Empresas empresas;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Empresa retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = empresas.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Empresa empresa = (Empresa) value;
			return empresa.getId() == null ? null : empresa.getId().toString();
		}
		
		return "";
	}
}