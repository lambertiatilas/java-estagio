package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Estagio;
import br.edu.ucv.estagio.repository.Estagios;

@FacesConverter(forClass = Estagio.class)
public class EstagioConverter implements Converter {
	
	@Inject
	private Estagios estagios;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Estagio retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = estagios.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Estagio estagio = (Estagio) value;
			return estagio.getId() == null ? null : estagio.getId().toString();
		}
		
		return "";
	}
}