package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Curso;
import br.edu.ucv.estagio.repository.Cursos;

@FacesConverter(forClass = Curso.class)
public class CursoConverter implements Converter {
	
	@Inject
	private Cursos cursos;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Curso retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = cursos.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Curso curso = (Curso) value;
			return curso.getId() == null ? null : curso.getId().toString();
		}
		
		return "";
	}
}