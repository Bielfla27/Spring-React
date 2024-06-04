package gfWeb.minhasFinancas.service;

import java.util.List;
import java.util.Optional;

import gfWeb.minhasFinancas.model.entity.Lancamento;
import gfWeb.minhasFinancas.model.entity.Usuario;
import gfWeb.minhasFinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar (Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	
	Optional<Lancamento> obterPorId(Long id);
}
