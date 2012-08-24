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
import br.gov.ce.fortaleza.cti.sgf.entity.Modelo;
import br.gov.ce.fortaleza.cti.sgf.service.ModeloService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;

@Scope("session")
@Component("modeloBean")
public class ModeloBean extends EntityBean<Integer, Modelo>{

	@Autowired
	private ModeloService service;

	protected Integer retrieveEntityId(Modelo entity) {
		return entity.getId();
	}

	protected ModeloService retrieveEntityService() {
		return this.service;
	}

	protected Modelo createNewEntity() {
		Modelo modelo = new Modelo();
		modelo.setMarca(new Marca());
		return modelo;
	}

	public List<SelectItem> getModeloList(){

		List<Modelo> list = service.retrieveAll();
		List<SelectItem> result = new ArrayList<SelectItem>();
		for (Modelo modelo : list) {
			result.add(new SelectItem(modelo.getId(), modelo.getDescricao()));
		}
		return result;
	}

	public synchronized List<Modelo> getModelos(){

		List<Modelo> list = service.retrieveAll();
		Collections.sort(list, new Comparator<Modelo>() {
			public int compare(Modelo o1, Modelo o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		return list;
	}

	public String search(){
		super.search();
		Collections.sort(this.entities, new Comparator<Modelo>() {
			public int compare(Modelo o1, Modelo o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		return SUCCESS;
	}

	public String save(){

		boolean valido = true;

		this.entity.setDescricao(this.entity.getDescricao().trim().toUpperCase());
		if(service.verificaSeExiste(this.entity.getDescricao(), null) ){
			valido = false;
			JSFUtil.getInstance().addErrorMessage("msg.error.modelo.nomeexistente");
		}

		if(!valido){
			return FAIL;
		} else {
			return super.save();
		}

	}

	public String update(){

		boolean valido = true;

		this.entity.setDescricao(this.entity.getDescricao().trim().toUpperCase());
		if(service.verificaSeExiste(this.entity.getDescricao(), this.entity) ){
			valido = false;
			JSFUtil.getInstance().addErrorMessage("msg.error.modelo.nomeexistente");
		}

		if(!valido){
			return FAIL;
		} else {
			return super.update();
		}

	}
}
