/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Especie;
import br.gov.ce.fortaleza.cti.sgf.service.EspecieService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;

@Scope("session")
@Component("especieBean")
public class EspecieBean extends EntityBean<Integer, Especie>{

	@Autowired
	private EspecieService service;

	protected Integer retrieveEntityId(Especie entity) {
		return entity.getId();
	}

	protected EspecieService retrieveEntityService() {
		return this.service;
	}

	protected Especie createNewEntity() {
		Especie especie = new Especie();
		return especie;
	}

	public List<SelectItem> getEspecieList(){

		List<Especie> list = service.retrieveAll();
		List<SelectItem> result = new ArrayList<SelectItem>();
		for (Especie especie : list) {
			result.add(new SelectItem(especie.getId(), especie.getDescricao()));
		}
		return result;
	}

	public synchronized List<Especie> getEspecies(){
		List<Especie> list = service.retrieveAll();
		Collections.sort(list, new Comparator<Especie>() {
			public int compare(Especie o1, Especie o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		return list;
	}

	public String search(){

		super.search();
		Collections.sort(this.entities, new Comparator<Especie>() {
			public int compare(Especie o1, Especie o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		return SUCCESS;
	}
	
	
	public String update(){
		boolean valida = true;
		this.entity.setDescricao(this.entity.getDescricao().trim().toUpperCase());
		if(service.verificaSeExiste(this.entity.getDescricao())){
			JSFUtil.getInstance().addErrorMessage("msg.error.especie.nomeexistente");
			valida = false;
		}
		if(valida){
			return super.update();
		} else {
			return FAIL;
		}
	}
	
	public String save(){
		boolean valida = true;
		this.entity.setDescricao(this.entity.getDescricao().trim().toUpperCase());
		if(service.verificaSeExiste(this.entity.getDescricao())){
			JSFUtil.getInstance().addErrorMessage("msg.error.especie.nomeexistente");
			valida = false;
		}
		if(valida){
			return super.save();
		} else {
			return FAIL;
		}		
	}
}

