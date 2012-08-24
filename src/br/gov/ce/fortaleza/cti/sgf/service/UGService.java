/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

/**
 * @author Deivid
 * @since 29/12/2009
 */
@Transactional
@Repository
public class UGService extends BaseService<String, UG>{

	public UG findByID(String id){
		Query query = entityManager.createQuery("select o from UG o where o.id = ?");
		query.setParameter(1, id);
		return (UG) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<UG> findSolByUG(Date dtInicial, Date dtFinal) {

		List<UG> ugs = null;
		Query query = entityManager.createQuery("select distinct(o.veiculo.ua.ug) from SolicitacaoVeiculo o " +
		"where (o.dataHoraSaida >= :inicio and o.dataHoraRetorno <= :fim)");
		query.setParameter("inicio", dtInicial);
		query.setParameter("fim", dtFinal);
		ugs = query.getResultList();
		return ugs;
	}

	@SuppressWarnings("unchecked")
	public List<UG> findAll(){

		List<UG> ugs = new ArrayList<UG>();
		User user = SgfUtil.usuarioLogado();
		if(SgfUtil.isAdministrador(user) || SgfUtil.isCoordenador(user) || SgfUtil.isOperador(user)){
			ugs = executeResultListGenericQuery("findSorted");
		} else {
			UA ua = user.getPessoa().getUa();
			if(ua != null){
				ugs = new ArrayList<UG>();
				if(ua.getUg() != null){
					ugs.add(ua.getUg());
				}
			}
		}

		Collections.sort(ugs, new Comparator<UG>() {
			public int compare(UG o1, UG o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		return ugs;
	}

	@SuppressWarnings("unchecked")
	public List<UG> findUASorted(){
		return executeResultListGenericQuery("findSorted");
	}
}
