package br.edu.ucv.estagio.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.edu.ucv.estagio.model.Pessoa;
import br.edu.ucv.estagio.repository.Usuarios;

@FacesConverter(forClass = Pessoa.class)
public class UsuarioConverter implements Converter {
	
	@Inject
	private Usuarios usuarios;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Pessoa retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = usuarios.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Pessoa pessoa = (Pessoa) value;
			return pessoa.getId() == null ? null : pessoa.getId().toString();
		}
		
		return "";
	}
}