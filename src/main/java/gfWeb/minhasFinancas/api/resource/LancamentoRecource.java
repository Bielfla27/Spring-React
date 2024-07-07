package gfWeb.minhasFinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gfWeb.minhasFinancas.api.dto.LancamentoDto;
import gfWeb.minhasFinancas.model.entity.Lancamento;
import gfWeb.minhasFinancas.model.entity.Usuario;
import gfWeb.minhasFinancas.model.enums.StatusLancamento;
import gfWeb.minhasFinancas.model.enums.TipoLancamento;
import gfWeb.minhasFinancas.service.LancamentoService;
import gfWeb.minhasFinancas.service.UsuarioService;
import gfWeb.minhasFinancas.service.exception.RegraNegocioException;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamento")
@RequiredArgsConstructor
public class LancamentoRecource {

	private final LancamentoService lancamentoService;
	
	private final UsuarioService usuarioService;

	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDto dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = lancamentoService.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto dto) {
		
		return lancamentoService.obterPorId(id).map(entity -> { // entity passa a ser o lançamento pego pela busca do obter por id
			
				try {
					Lancamento lancamento = converter(dto);
					lancamento.setId(entity.getId());
					lancamentoService.atualizar(lancamento);
					return ResponseEntity.ok(lancamento);
				}catch (RegraNegocioException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
				
			}).orElseGet(() -> new ResponseEntity( "Lancamento não encontrado na base de Dados." , HttpStatus.BAD_REQUEST ));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		
		return lancamentoService.obterPorId(id).map(entity -> { // entity passa a ser o lançamento pego pela busca do obter por id
			lancamentoService.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity( "Lancamento não encontrado na base de Dados." , HttpStatus.BAD_REQUEST ));
		
	}
	
	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "descricao" , required = false) String descricao,
								 @RequestParam(value = "mes" , required = false) Integer mes,
								 @RequestParam(value = "ano" , required = false) Integer ano,
								 @RequestParam("usuario") Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setData_cadastro(null);
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuário não encontrado para o id informado.");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	
	public Lancamento converter(LancamentoDto dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		Usuario usuario = usuarioService.
				obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usario não encontrado para o id informado."));
		lancamento.setUsuario(usuario);
		if(dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));			
		}
		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		return lancamento;
	}
}
