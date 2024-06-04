package gfWeb.minhasFinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gfWeb.minhasFinancas.model.entity.Lancamento;
import gfWeb.minhasFinancas.model.enums.StatusLancamento;
import gfWeb.minhasFinancas.model.repository.LancamentoRepository;
import gfWeb.minhasFinancas.service.LancamentoService;
import gfWeb.minhasFinancas.service.exception.RegraNegocioException;

@Service
public class LancamentoServiceImpl implements LancamentoService{

	private LancamentoRepository lancamentoRepository;
	
	public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
		
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());// verifica se o lancamento passado realmente existe 
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());// verifica se o lancamento passado realmente existe 
		lancamentoRepository.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true) //falando que é apenas uma transação de leitura 
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, 
							ExampleMatcher.matching()
								.withIgnoreCase()
								.withStringMatcher(StringMatcher.CONTAINING));
		
		return lancamentoRepository.findAll();
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		// TODO Auto-generated method stub
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {
		
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Infome uma Descrição válida");
		}
		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Infome uma Mês válido");
		}
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Infome uma Ano válido");
		}
		if(lancamento.getUsuario().getId() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Infome um Usuário");
		}
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Infome um Valor válido");
		}
		if(lancamento.getTipo() == null){
			throw new RegraNegocioException("Informe um tipo de lancamento");
		}
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return lancamentoRepository.findById(id);
	}

}
