package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Oficina;
import br.gov.ce.fortaleza.cti.sgf.service.OficinaService;

@Scope("session")
@Component("oficinaBean")
public class OficinaBean extends EntityBean<Integer, Oficina> {

	@Autowired
	private OficinaService service;

	protected Integer retrieveEntityId(Oficina entity) {

		return entity.getId();
	}

	protected OficinaService retrieveEntityService() {

		return this.service;
	}

	protected Oficina createNewEntity() {

		Oficina oficina = new Oficina();

		return oficina;
	}

	public String search(){

		this.searchSort();

		return SUCCESS;
	}

	public List<SelectItem> getOficinaList(){

		List<SelectItem> result = new ArrayList<SelectItem>();
		List<Oficina> oficinas = service.retrieveAll();
		
		Collections.sort(oficinas, new Comparator<Oficina>() {
			public int compare(Oficina o1, Oficina o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		for (Oficina o : oficinas) {
			result.add(new SelectItem(o.getId(), o.getNome()));
		}

		return result;
	}

	public synchronized List<Oficina> getOficinas(){

		List<Oficina> oficinas = service.retrieveAll();

		Collections.sort(oficinas, new Comparator<Oficina>() {
			public int compare(Oficina o1, Oficina o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});
		return oficinas;
	}
}
