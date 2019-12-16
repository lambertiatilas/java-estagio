package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Clausula;
import br.edu.ucv.estagio.repository.Clausulas;

@FacesConverter(forClass = Clausula.class)
public class ClausulaConverter implements Converter {
	
	@Inject
	private Clausulas clausulas;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Clausula retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = clausulas.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Clausula clausula = (Clausula) value;
			return clausula.getId() == null ? null : clausula.getId().toString();
		}
		
		return "";
	}
}