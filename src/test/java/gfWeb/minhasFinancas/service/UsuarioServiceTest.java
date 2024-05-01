package gfWeb.minhasFinancas.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import gfWeb.minhasFinancas.model.entity.Usuario;
import gfWeb.minhasFinancas.model.repository.UsuarioRepository;
import gfWeb.minhasFinancas.service.exception.RegraNegocioException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	UsuarioService service;
	
	@Test
	public void deveValidarEmail() {
		repository.deleteAll();
		
		service.validarEmail("teste@email.com");
	}
	
	@Test
	public void deveRetornarMensagemDeErroAoValidarEmail() {
		assertThrows(RegraNegocioException.class,() ->{
			Usuario usuario =Usuario.builder().nome("Gabriel").email("teste@teste.com").build();
			repository.save(usuario);
			
			service.validarEmail("teste@teste.com");
			
		});
	}
}
