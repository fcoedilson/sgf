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

import br.gov.ce.fortaleza.cti.sgf.entity.Infracao;
import br.gov.ce.fortaleza.cti.sgf.service.InfracaoService;

@Scope("session")
@Component("infracaoBean")
public class InfracaoBean extends EntityBean<Integer, Infracao>{

	@Autowired
	private InfracaoService service;

	protected Integer retrieveEntityId(Infracao entity) {

		return entity.getId();
	}

	protected InfracaoService retrieveEntityService() {

		return this.service;
	}

	protected Infracao createNewEntity() {

		Infracao infracao = new Infracao();

		this.saved = false;

		this.updated = false;

		return infracao;
	}

	public List<SelectItem> getInfracaoList(){

		List<Infracao> list = service.retrieveAll();
		List<SelectItem> result = new ArrayList<SelectItem>();
		
		Collections.sort(list, new Comparator<Infracao>() {
			public int compare(Infracao o1, Infracao o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		for (Infracao infracao : list) {
			result.add(new SelectItem(infracao.getId(), infracao.getDescricao()));
		}

		return result;
	}

	public synchronized List<Infracao> getInfracoes(){

		List<Infracao> result = service.retrieveAll();
		
		Collections.sort(result, new Comparator<Infracao>() {
			public int compare(Infracao o1, Infracao o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		return result;
	}

	public List<SelectItem> getGravidadeInfracao(){

		List<SelectItem> result = new ArrayList<SelectItem>();
		result.add(new SelectItem("Leve"));
		result.add(new SelectItem("Média"));
		result.add(new SelectItem("Grave"));
		result.add(new SelectItem("Gravíssima"));
		return result;
	}

	public String save(){

		this.saved = true;

		return super.save();
	}

	public String search(){

		this.searchSort();

		return SUCCESS;
	}
}
