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
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;
import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.service.TipoServicoService;

/**
 * @author Deivid
 * @since 25/11/09
 */
@Scope("session")
@Component("tipoServicoBean")
public class TipoServicoBean extends EntityBean<Integer, TipoServico>{

	@Autowired
	private TipoServicoService service;

	@Override
	protected TipoServico createNewEntity() {
		return new TipoServico();
	}

	@Override
	protected Integer retrieveEntityId(TipoServico entity) {
		return entity.getCodTipoServico();
	}

	@Override
	protected BaseService<Integer, TipoServico> retrieveEntityService() {
		return this.service;
	}

	public TipoServicoService getService() {
		return service;
	}

	public void setService(TipoServicoService service) {
		this.service = service;
	}

	@Transactional
	public List<SelectItem> getTipoServicoList(){

		List<SelectItem> result = new ArrayList<SelectItem>();
		result.add(new SelectItem(null, "Selecione"));
		List<TipoServico> servicos = service.findByTipo(1);

		Collections.sort(servicos, new Comparator<TipoServico>() {
			public int compare(TipoServico o1, TipoServico o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		for (TipoServico t : servicos) {
			result.add(new SelectItem(t.getCodTipoServico(), t.getDescricao()));
		}
		return result;
	}

	@Transactional
	public String search(){
		
		super.search();

		List<TipoServico> list = new ArrayList<TipoServico>();

		for (TipoServico t : this.entities) {

			if( t.getManutencao() == 1 ){

				list.add(t);
			}
		}

		this.entities.clear();
		
		this.entities.addAll(list);

		setCurrentBean(currentBeanName());

		setCurrentState(SEARCH);

		return SUCCESS;
	}

	@Transactional
	public synchronized List<TipoServico> getServicos(){

		List<TipoServico> result = service.retrieveAll();
		List<TipoServico> remove = new ArrayList<TipoServico>();

		for (TipoServico t : result) {
			if( t.getManutencao() == 0 ){
				remove.add(t);
			}
		}

		result.removeAll(remove);

		Collections.sort(result, new Comparator<TipoServico>() {
			public int compare(TipoServico o1, TipoServico o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		return result;
	}

	@Transactional
	public String save(){

		this.entity.setManutencao(1);

		return super.save();
	}
}
