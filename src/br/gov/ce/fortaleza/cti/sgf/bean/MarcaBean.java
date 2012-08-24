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

import br.gov.ce.fortaleza.cti.sgf.entity.Marca;
import br.gov.ce.fortaleza.cti.sgf.service.MarcaService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;

@Scope("session")
@Component("marcaBean")
public class MarcaBean extends EntityBean<Integer, Marca>{

	@Autowired
	private MarcaService service;

	protected Integer retrieveEntityId(Marca entity) {
		return entity.getId();
	}

	protected MarcaService retrieveEntityService() {
		return this.service;
	}

	protected Marca createNewEntity() {
		Marca marca = new Marca();
		return marca;
	}

	public List<SelectItem> getMarcaList(){

		List<Marca> list = service.retrieveAll();
		List<SelectItem> result = new ArrayList<SelectItem>();
		for (Marca marca : list) {
			result.add(new SelectItem(marca.getId(), marca.getDescricao()));
		}
		return result;
	}

	public List<Marca> getMarcas(){
		List<Marca> list = service.retrieveAll();
		Collections.sort(list, new Comparator<Marca>() {
			public int compare(Marca o1, Marca o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		return list;
	}

	public String search(){
		super.search();
		Collections.sort(this.entities, new Comparator<Marca>() {
			public int compare(Marca o1, Marca o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		return SUCCESS;
	}
	
	public String update(){
		
		boolean valido = true;
		this.entity.setDescricao(this.entity.getDescricao().trim().toUpperCase());
		if(service.verificaSeExiste(this.entity.getDescricao(), this.entity)){
			valido = false;
			JSFUtil.getInstance().addErrorMessage("msg.error.marca.nomeexistente");
		}

		if(!valido){
			return FAIL;
		} else {
			return super.update();
		}
	}



	public String save(){

		boolean valido = true;

		this.entity.setDescricao(this.entity.getDescricao().trim().toUpperCase());
		if(service.verificaSeExiste(this.entity.getDescricao(), null) ){
			valido = false;
			JSFUtil.getInstance().addErrorMessage("msg.error.marca.nomeexistente");
		}

		if(!valido){
			return FAIL;
		} else {
			return super.save();
		}

	}

}
