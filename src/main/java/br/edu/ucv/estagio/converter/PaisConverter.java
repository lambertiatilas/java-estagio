package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Pais;
import br.edu.ucv.estagio.repository.Paises;

@FacesConverter(forClass = Pais.class)
public class PaisConverter implements Converter {
	
	@Inject
	private Paises paises;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Pais retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = paises.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Pais pais = (Pais) value;
			return pais.getId() == null ? null : pais.getId().toString();
		}
		
		return "";
	}
}