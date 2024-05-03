package gfWeb.minhasFinancas.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.refEq;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import gfWeb.minhasFinancas.model.entity.Usuario;
import gfWeb.minhasFinancas.model.repository.UsuarioRepository;
import gfWeb.minhasFinancas.service.exception.ErroAutenticacao;
import gfWeb.minhasFinancas.service.exception.RegraNegocioException;
import gfWeb.minhasFinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@MockBean
	UsuarioRepository repository;
	@SpyBean
	UsuarioServiceImpl service;
	
	@Test
	public void deveSalvarUmUsuario() {
		//cenário 
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).nome("teste").email("teste@email.com").senha("teste").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificacão
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("teste");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("teste@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("teste");
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		assertThrows(RegraNegocioException.class,() ->{
			String email = "teste@teste.com";
			Usuario usuario =  Usuario.builder().email(email).build();
			Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
			
			//acao
			service.salvarUsuario(usuario);
			
			//verificacao
			Mockito.verify(repository, Mockito.never()).save(usuario);
		});
	}
	
	
	@Test
	public void deveAtenticarUmUsuario() {
		String email = "teste@teste.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Usuario resultado = service.autenticar(email, senha);
		
		Assertions.assertThat(resultado).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		
	
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			Throwable exception = Assertions.catchThrowable(() ->service.autenticar("teste@email.com", "1234"));
			
			Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usúario não encontrado para o email informado");
	
	}
	
	@Test
	public void deveLanvarErroQuandoSenhaNãoBater() {
	
			String senha = "senha";
			
			Usuario usuario = Usuario.builder().email("teste@email.com").senha(senha).id(1l).build();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
			
			
			
			Throwable exception = Assertions.catchThrowable(() ->service.autenticar("teste@email.com", "1234"));
			
			Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Inválida");
			
		
	}
	
	@Test
	public void deveValidarEmail() {
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		service.validarEmail("teste@email.com");
	}
	
	@Test
	public void deveRetornarMensagemDeErroAoValidarEmail() {
		assertThrows(RegraNegocioException.class,() ->{
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
			service.validarEmail("teste@teste.com");
			
		});
	}
}
