package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Transmissao;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.service.TransmissaoService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.dto.PontoDTO;

@Scope("session")
@Component("relatorioMonitoramentoBean")
public class RelatorioMonitoramentoBean extends EntityBean<Integer, PontoDTO>  {

	public String pontos = new String("");
	public Date start;
	public Date end;

	@Autowired
	private VeiculoService veiculoService;

	@Autowired
	private TransmissaoService transmissaoService;

	private Veiculo veiculo;

	@Override
	protected PontoDTO createNewEntity() {
		return new PontoDTO();
	}

	@Override
	protected Integer retrieveEntityId(PontoDTO entity) {
		throw new IllegalStateException("Não implementado");
	}

	@Override
	protected BaseService<Integer, PontoDTO> retrieveEntityService() {
		throw new IllegalStateException("Não implementado");
	}

	public String relatorioPosicoesVeiculo() {
		setCurrentState(PontoDTO.RELATORIO_POSICOES);
		setCurrentBean(currentBeanName());
		this.entities = null;
		return SUCCESS;
	}

	public boolean isRelatorioPosicoesVeiculo(){
		return PontoDTO.RELATORIO_POSICOES.equals(getCurrentState());
	}

	public String consultarPosicoesVeiculo(){
		this.entities = new ArrayList<PontoDTO>();
		List<PontoDTO> pontos = new ArrayList<PontoDTO>();
		this.start = DateUtil.getDateStartDay(this.start);
		this.end = DateUtil.getDateEndDay(this.end);
		
		List<Transmissao> transmissoes = transmissaoService.retrieveByVeiculo(this.veiculo.getId(), this.start, this.end);
		for (Transmissao t : transmissoes) {
			PontoDTO p = new PontoDTO();
			p.setPontoProximo(t.getPonto() != null ? t.getPonto().getDescricao() : "");
			p.setDistPontoProximo(t.getDistancia() != null ? t.getDistancia() : 0F);
			p.setIgnicao(t.getIgnicao() != null ? t.getIgnicao() : false);
			p.setDataTransmissao(t.getDataTransmissao());
			pontos.add(p);
		}
		this.entities = pontos;
		return SUCCESS;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public String getPontos() {
		return pontos;
	}

	public void setPontos(String pontos) {
		this.pontos = pontos;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}