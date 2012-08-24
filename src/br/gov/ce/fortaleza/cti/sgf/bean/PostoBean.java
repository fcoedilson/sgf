/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.service.PostoService;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

/**
 * @author Deivid
 * @since 25/11/09	
 */
@Scope("session")
@Component("postoBean")
public class PostoBean extends EntityBean<Integer, Posto>{

	@Autowired
	private PostoService service;

	@Override
	protected Posto createNewEntity() {
		return new Posto();
	}

	@Override
	protected Integer retrieveEntityId(Posto entity) {
		return entity.getCodPosto();
	}

	@Override
	protected PostoService retrieveEntityService() {
		return this.service;
	}

	public PostoService getService() {
		return service;
	}

	public void setService(PostoService service) {
		this.service = service;
	}

	public List<Posto> getPostos(){
		List<Posto> postos = new ArrayList<Posto>();
		User user = SgfUtil.usuarioLogado();
		if(SgfUtil.isChefeTransporte(user) || SgfUtil.isChefeSetor(user) || SgfUtil.isAdministrador(user) || SgfUtil.isCoordenador(user)){
			postos = service.retrieveAll();
		} else if(SgfUtil.isOperador(user)){
			postos.add(user.getPosto());
		}
		Collections.sort(postos, new Comparator<Posto>() {
			public int compare(Posto o1, Posto o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		return postos;
	}
}
