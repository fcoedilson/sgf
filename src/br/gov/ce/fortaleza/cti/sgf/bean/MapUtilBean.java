package br.gov.ce.fortaleza.cti.sgf.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.util.dto.MapDTO;

@Scope("session")
@Component("mapUtilBean")
public class MapUtilBean extends EntityBean<Integer, MapDTO>  {

	@Override
	protected MapDTO createNewEntity() {
		return new MapDTO();
	}

	@Override
	protected Integer retrieveEntityId(MapDTO entity) {
		throw new IllegalStateException("N�o implementado");
	}

	@Override
	protected BaseService<Integer, MapDTO> retrieveEntityService() {
		throw new IllegalStateException("N�o implementado");
	}
	
	public String searchAddress() {
		setCurrentState(MapDTO.SEARCH_ADDRESS);
		setCurrentBean(currentBeanName());
		this.entities = null;
		return SUCCESS;
	}

	public boolean isSearchAddress() {
		return MapDTO.SEARCH_ADDRESS.equals(getCurrentState());
	}
}