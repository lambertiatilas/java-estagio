package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Bairro;
import br.edu.ucv.estagio.repository.Bairros;

@FacesConverter(forClass = Bairro.class)
public class BairroConverter implements Converter {
	
	@Inject
	private Bairros bairros;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Bairro retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = bairros.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Bairro bairro = (Bairro) value;
			return bairro.getId() == null ? null : bairro.getId().toString();
		}
		
		return "";
	}
}