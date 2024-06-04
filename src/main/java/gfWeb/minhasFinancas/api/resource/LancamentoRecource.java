package gfWeb.minhasFinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gfWeb.minhasFinancas.api.dto.LancamentoDto;
import gfWeb.minhasFinancas.model.entity.Lancamento;
import gfWeb.minhasFinancas.model.entity.Usuario;
import gfWeb.minhasFinancas.model.enums.TipoLancamento;
import gfWeb.minhasFinancas.service.LancamentoService;
import gfWeb.minhasFinancas.service.UsuarioService;
import gfWeb.minhasFinancas.service.exception.RegraNegocioException;
import jakarta.persistence.Entity;

@RestController
@RequestMapping("/api/lancamento")
public class LancamentoRecource {

	private LancamentoService lancamentoService;
	
	private UsuarioService usuarioService;
	
	public LancamentoRecource(LancamentoService lancamentoService) {
		this.lancamentoService = lancamentoService;
	}
	
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
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		
		return lancamento;
	}
}
