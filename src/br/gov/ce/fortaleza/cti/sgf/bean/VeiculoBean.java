/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Area;
import br.gov.ce.fortaleza.cti.sgf.entity.Especie;
import br.gov.ce.fortaleza.cti.sgf.entity.Modelo;
import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.UAService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;
import br.gov.ce.fortaleza.cti.sgf.util.StatusVeiculo;

@Scope("session")
@Component("veiculoBean")
public class VeiculoBean extends EntityBean<Integer, Veiculo>{

	@Autowired
	private VeiculoService service;

	@Autowired
	private UAService uaService;

	private UG ug;
	private List<UA> uas;
	private Integer searchId = 0;
	private String stringSearch = null;
	private Area area;


	protected Integer retrieveEntityId(Veiculo entity) {
		return entity.getId();
	}

	protected VeiculoService retrieveEntityService() {
		return this.service;
	}

	protected Veiculo createNewEntity() {
		Veiculo veiculo = new Veiculo();
		veiculo.setModelo(new Modelo());
		veiculo.setEspecie(new Especie());
		veiculo.setUa(new UA());
		veiculo.setPropriedade("Locado");
		veiculo.setTemSeguro(0);		
		this.stringSearch = null;
		return veiculo;
	}

	public List<Veiculo> getVeiculos(){
		return service.veiculos();
	}

	public List<Veiculo> getVeiculosRastreados(){
		return service.veiculosRastreados();
	}	

	public List<SelectItem> getVeiculoList(){
		List<Veiculo> list = service.findAll();
		List<SelectItem> result = new ArrayList<SelectItem>();
		result.add(new SelectItem(null, "Selecione"));
		for (Veiculo veiculo : list) {
			result.add(new SelectItem(veiculo.getId(), veiculo.getPlaca() +" - " + veiculo.getModelo().getDescricao()));
		}
		return result;
	}

	public List<SelectItem> getProprietarioList(){
		List<SelectItem> result = new ArrayList<SelectItem>();
		result.add(new SelectItem("PMF", "PMF"));
		result.add(new SelectItem("Locado", "Locado"));
		result.add(new SelectItem("Outro", "Outro"));
		return result;
	}

	public String save(){
		boolean valida = true;
		this.entity.setStatus(StatusVeiculo.DISPONIVEL);
		this.entity.setDataCadastro(new Date());
		this.entity.setPlaca(this.entity.getPlaca().toUpperCase());
		this.entity.setChassi(this.entity.getChassi().toUpperCase());
		
		if(service.verificaSeExistePlaca(this.entity.getPlaca())){
			valida = false;
			JSFUtil.getInstance().addErrorMessage("msg.error.placa.nomeexistente");
		} else if(service.verificaSeExisteChassi(this.entity.getChassi())){
			valida = false;
			JSFUtil.getInstance().addErrorMessage("msg.error.chassi.nomeexistente");
		} else if(service.verificaSeExisteRenavam(this.entity.getRenavam())){
			valida = false;
			JSFUtil.getInstance().addErrorMessage("msg.error.renavam.nomeexistente");
		}
		if(valida){
			return super.save();
		} else {
			return FAIL;
		}
	}

	public String prepareUpdate(){
		if(this.entity.getUa() != null){
			this.ug = this.entity.getUa().getUg();
			this.uas = uaService.retrieveByUG(this.ug.getId());
		}
		return super.prepareUpdate();
	}

	
	public String search2(){

		List<Veiculo> veiculos =  new ArrayList<Veiculo>();
		this.entities = new ArrayList<Veiculo>();
		User user = SgfUtil.usuarioLogado();

		if(this.stringSearch != null && this.stringSearch.length() > 0){

			if(SgfUtil.isAdministrador(user) || SgfUtil.isChefeTransporte(user)){
				
				try {
					switch (this.searchId) {
					case 0:
						veiculos = this.service.findByOrgaoPlacaChassiRenavam(null, this.stringSearch, null, null);
						break;
					case 1:
						veiculos = this.service.findByOrgaoPlacaChassiRenavam(null, null, this.stringSearch, null);
						break;
					case 2:
						veiculos = this.service.findByOrgaoPlacaChassiRenavam(null, null, null, this.stringSearch);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				UA ua = user.getPessoa().getUa();
				try {
					if(ua != null){
						switch (this.searchId) {
						case 0:
							veiculos = this.service.findByOrgaoPlacaChassiRenavam(ua.getUg().getId(), this.stringSearch, null, null);
							break;
						case 1:
							veiculos = this.service.findByOrgaoPlacaChassiRenavam(ua.getUg().getId(), null, this.stringSearch, null);
							break;
						case 2:
							veiculos = this.service.findByOrgaoPlacaChassiRenavam(ua.getUg().getId(), null, null,this. stringSearch);
							break;
						default:
							break;
						}
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			veiculos =  service.findAll();
		}
		this.entities = retrieveEntityService().filter(veiculos, this.stringSearch);
		Collections.sort(this.entities, new Comparator<Veiculo>() {
			public int compare(Veiculo o1, Veiculo o2) {
				return o1.getPlaca().compareTo(o2.getPlaca());
			}
		});
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
	
	public String inativarVeiculo(){
		this.entity.setStatus(StatusVeiculo.DELETADO);
		super.update();
		return super.search();
	}
	
	public String ativarVeiculo(){
		this.entity.setStatus(StatusVeiculo.DISPONIVEL);
		super.update();
		return super.search();
	}

	public String loadUas(){
		this.uas = new ArrayList<UA>();
		this.uas = uaService.retrieveByUG(this.ug.getId());
		return SUCCESS;
	}

	public Integer getSearchId() {
		return searchId;
	}

	public void setSearchId(Integer searchId) {
		this.searchId = searchId;
	}

	public String getStringSearch() {
		return stringSearch;
	}

	public void setStringSearch(String stringSearch) {
		this.stringSearch = stringSearch;
	}

	public UG getUg() {
		return ug;
	}

	public void setUg(UG ug) {
		this.ug = ug;
	}

	public List<UA> getUas() {
		return uas;
	}

	public void setUas(List<UA> uas) {
		this.uas = uas;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
