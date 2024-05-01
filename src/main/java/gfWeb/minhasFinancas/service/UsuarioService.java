package gfWeb.minhasFinancas.service;

import gfWeb.minhasFinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario auteticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);

}
