package br.gov.ce.fortaleza.cti.sgf.service;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Marca;

@Repository
@Transactional
public class MarcaService extends BaseService<Integer, Marca> {
	/**
	 * Incluir ou alterar uma Marca verificar se a mesma não já foi cadastrada
	 * @param desc
	 * @return
	 */
	@Transactional
	public Boolean verificaSeExiste(String desc, Marca marca){

		String consulta = "select m from Marca m where upper(trim(m.descricao)) like :desc";
		StringBuffer hql = new StringBuffer(consulta);

		if(marca != null){
			hql.append(" and m.id != :id");
		}

		Query query = entityManager.createQuery(hql.toString());

		query.setParameter("desc",  desc);
		if(marca != null){
			query.setParameter("id",  marca.getId());
		}
		return query.getResultList().size() > 0;
	}

}