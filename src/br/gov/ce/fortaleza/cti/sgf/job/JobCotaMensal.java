package br.gov.ce.fortaleza.cti.sgf.job;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.gov.ce.fortaleza.cti.sgf.entity.Cota;
/**
 * Classe respons�vel pela atualiza��o mensal das cotas de abastecimento dispon�veis para os ve�culos
 * 
 * @author Lafitte
 * @since 30/07/2010
 *
 */
public class JobCotaMensal implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<Cota> cotas = new ArrayList<Cota>();
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("sgf");
		EntityManager entityManager = factory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		Query query = entityManager.createQuery("SELECT c FROM Cota c");
		cotas = query.getResultList();
		try{
			transaction.begin();
			for (Cota cota : cotas) {
				cota.setCotaDisponivel(cota.getCota());
			}
			transaction.commit();
		}catch (Exception e) {
			transaction.rollback();
		}finally{
			entityManager.close();
		}
	}

}
