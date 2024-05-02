package gfWeb.minhasFinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import gfWeb.minhasFinancas.model.entity.Usuario;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario =Usuario.builder().nome("Gabriel").email("teste@teste.com").build();
		entityManager.persist(usuario);
		
		//ação/execução
		boolean resultadoConsulta = repository.existsByEmail("teste@teste.com");
		
		//verificação
		
		Assertions.assertThat(resultadoConsulta).isTrue();
	}
	
	@Test
	public void deveRetornarFalseQuandoNaoHouverUsuarioComEmailCadastrado(){
			
		boolean resultadoConsulta = repository.existsByEmail("teste@teste.com");
		
		Assertions.assertThat(resultadoConsulta).isFalse();
	}
}
