package gfWeb.minhasFinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import gfWeb.minhasFinancas.model.entity.Usuario;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario =Usuario.builder().nome("Gabriel").email("teste@teste.com").build();
		repository.save(usuario);
		
		//ação/execução
		boolean resultadoConsulta = repository.existsByEmail("teste@teste.com");
		
		//verificação
		
		Assertions.assertThat(resultadoConsulta).isTrue();
	}
}
