/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.gov.ce.fortaleza.cti.sgf.entity.DiarioBomba;

/**
 * Classe respons�vel pelo execu��o da tarefa de fechamento autom�tico de bomba
 * @author Deivid
 * @since 29/01/2010
 */
public class JobDiarioBomba implements Job{

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("sgf");
		EntityManager entityManager = factory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		List<DiarioBomba> diarioBombas = new ArrayList<DiarioBomba>();
		Query query = entityManager.createQuery("select d from DiarioBomba d where d.status = 0");
		diarioBombas = query.getResultList();
		try{
			transaction.begin();
			for (DiarioBomba diarioBomba : diarioBombas) {
				Float valor = null;
				valor = valorDiarioPosterior(entityManager, diarioBomba);
				if(valor != null){
					diarioBomba.setValorFinal(valor);
					diarioBomba.setHoraFinal(new Date());
					diarioBomba.setStatus(1);
					entityManager.merge(diarioBomba);
				}
			}
			transaction.commit();
		}catch (Exception e) {
			transaction.rollback();
		}finally{
			entityManager.close();
		}
	}

	private Float valorDiarioPosterior(EntityManager entityManager, DiarioBomba diarioBomba) {
		DiarioBomba diarioBomba2 = null;
		Query query = entityManager.createQuery("select o from DiarioBomba o " +
		"where o.dtDia > :dia and o.bomba.id = :bombaID order by o.dtDia asc");
		query.setParameter("dia", diarioBomba.getDataCadastro());
		query.setParameter("bombaID", diarioBomba.getBomba().getId());
		query.setMaxResults(1);
		try{
			diarioBomba2 = (DiarioBomba) query.getSingleResult();
		}catch (NoResultException e) {
			return null;
		}
		return diarioBomba2.getValorInicial();
	}
}
