package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Municipio;
import br.edu.ucv.estagio.repository.Municipios;

@FacesConverter(forClass = Municipio.class)
public class MunicipioConverter implements Converter {
	
	@Inject
	private Municipios municipios;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Municipio retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = municipios.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Municipio municipio = (Municipio) value;
			return municipio.getId() == null ? null : municipio.getId().toString();
		}
		
		return "";
	}
}