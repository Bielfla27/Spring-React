package gfWeb.minhasFinancas.api.resource;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gfWeb.minhasFinancas.api.dto.UsuarioDto;
import gfWeb.minhasFinancas.model.entity.Usuario;
import gfWeb.minhasFinancas.service.UsuarioService;
import gfWeb.minhasFinancas.service.exception.RegraNegocioException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	private UsuarioService service;
	
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity salvar (@RequestBody UsuarioDto dto) {
		
		Usuario usuario = Usuario.builder()
									.nome(dto.getNome())
									.email(dto.getEmail())
									.senha(dto.getSenha())
									.build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
