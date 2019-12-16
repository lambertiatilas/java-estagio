package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Instituicao;
import br.edu.ucv.estagio.repository.Instituicoes;

@FacesConverter(forClass = Instituicao.class)
public class InstituicaoConverter implements Converter {
	
	@Inject
	private Instituicoes instituicoes;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Instituicao retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = instituicoes.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Instituicao instituicao = (Instituicao) value;
			return instituicao.getId() == null ? null : instituicao.getId().toString();
		}
		
		return "";
	}
}