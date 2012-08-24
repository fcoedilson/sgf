/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.AtendimentoAbastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.AtendimentoService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;


@Scope("session")
@Component("atendimentoAbastecimentoBean")
public class AtendimentoAbastecimentoBean extends EntityBean<Integer, AtendimentoAbastecimento>{
	
	@Autowired
	private AtendimentoService service;
	
	@Autowired
	private VeiculoService veiculoService;
	
	private UG orgao;
	
	private Veiculo veiculo;
	
	private Date dataInicial;
	
	private Date dataFinal;
	
	private List<Veiculo> veiculos;

	@Override
	protected AtendimentoAbastecimento createNewEntity() {
		return new AtendimentoAbastecimento();
	}

	@Override
	protected Integer retrieveEntityId(AtendimentoAbastecimento entity) {
		return entity.getId();
	}


	@Override
	protected AtendimentoService retrieveEntityService() {
		return this.service;
	}
	
	@Override
	public String update(){
		this.entities = service.findListAbastecimentosVeiculo(this.orgao, this.veiculo, this.dataInicial, this.dataFinal);
		return super.update();
	}
	
	@Override
	public String search(){
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
	
	public String populate(){
		this.veiculos = veiculoService.findByUG(this.orgao);
		this.entities = new ArrayList<AtendimentoAbastecimento>();
		return super.populate();
	}
	
	public String buscarAtendimentos(){
		
		this.dataInicial = DateUtil.getDateStartDay(this.dataInicial);
		this.dataFinal = DateUtil.getDateStartDay(this.dataFinal);
		this.entities = service.findListAbastecimentosVeiculo(this.orgao, this.veiculo, this.dataInicial, this.dataFinal);
		return SUCCESS;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public UG getOrgao() {
		return orgao;
	}

	public void setOrgao(UG orgao) {
		this.orgao = orgao;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}
}
