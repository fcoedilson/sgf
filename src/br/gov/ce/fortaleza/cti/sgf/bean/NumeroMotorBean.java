package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.NumeroMotor;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.NumeroMotorService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

@Scope("session")
@Component("numeroMotorBean")
public class NumeroMotorBean extends EntityBean<Integer, NumeroMotor>{

	@Autowired
	private NumeroMotorService service;

	@Autowired
	private VeiculoService veiculoService;

	private List<Veiculo> veiculos;
	private UG ug;

	protected Integer retrieveEntityId(NumeroMotor entity) {
		return entity.getId();
	}

	protected NumeroMotorService retrieveEntityService() {
		return this.service;
	}

	protected NumeroMotor createNewEntity() {
		NumeroMotor numeroMotor = new NumeroMotor();
		numeroMotor.setVeiculo(new Veiculo());
		numeroMotor.setUsuario(new User());
		return numeroMotor;
	}

	public String save(){
		this.entity.setVeiculo(veiculoService.retrieve(this.entity.getVeiculo().getId()));
		this.entity.setUsuario(SgfUtil.usuarioLogado());
		this.entity.setDataCadastro(new Date());
		return super.save();
	}

	public String search(){
		this.searchSort();
		return SUCCESS;
	}

	public String prepareUpdate(){
		this.veiculos = veiculoService.findByUG(this.entity.getVeiculo().getUa().getUg());
		return super.prepareUpdate();
	}

	public String loadVeiculos(){
		if(this.ug != null){
			this.veiculos = veiculoService.findByUG(this.ug);
		}
		return SUCCESS;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public UG getUg() {
		return ug;
	}

	public void setUg(UG ug) {
		this.ug = ug;
	}
}
