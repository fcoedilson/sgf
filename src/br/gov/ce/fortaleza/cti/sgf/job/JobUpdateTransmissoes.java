package br.gov.ce.fortaleza.cti.sgf.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import spatialindex.rtree.RTree;
import spatialindex.spatialindex.Point;
import spatialindex.spatialindex.SpatialIndex;
import spatialindex.storagemanager.PropertySet;
import br.gov.ce.fortaleza.cti.sgf.entity.Ponto;
import br.gov.ce.fortaleza.cti.sgf.entity.Transmissao;
import br.gov.ce.fortaleza.cti.sgf.util.GeoUtil;

public class JobUpdateTransmissoes implements Job {

	private static final Logger log = Logger.getLogger(JobUpdateTransmissoes.class);

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("sgf");
		EntityManager entityManager = factory.createEntityManager();
		EntityTransaction transaction;
		transaction = entityManager.getTransaction();
		try {
			
			transaction.begin();
			log.info("Executando de atualização de transmissões...");			
			log.info("Iniciando criação do index de busca...");
			Query query = entityManager.createQuery("SELECT p FROM Ponto p");
			List<Ponto> pontos = query.getResultList();
			
			Map<Integer, RTree> pontosMap = findPontos(pontos, 0);
			log.info("Criação do index: OK");
			
			query = entityManager.createQuery("SELECT t FROM Transmissao t WHERE t.ponto.id IS NULL ORDER BY t.dataTransmissao");
			List<Transmissao> transmissoes = query.getResultList();
			
			for (Transmissao transmissao : transmissoes) {
				GeoUtil.atualizarPontoMaisProximo(transmissao, pontosMap.get(0), pontosMap.get(0));
				entityManager.merge(transmissao);
			}
			transaction.commit();
			log.info("Término de atualização de transmissões");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			transaction.rollback();
		} finally {
			entityManager.close();
		}
	}

	
	public Map<Integer, RTree> findPontos(List<Ponto> points, Integer clientId) throws Exception {

		Map<Integer, RTree> result = new HashMap<Integer, RTree>();
		try {
			for (Ponto p : points) {
				Ponto ponto = p;
				if (!result.containsKey(clientId)) {
					PropertySet propertySet = new PropertySet();
					propertySet.setProperty("IndexCapacity", 5);
					propertySet.setProperty("LeafCapactiy", 5);
					result.put(clientId, new RTree(propertySet, SpatialIndex.createMemoryStorageManager(null)));
				}
				Point point = new Point(new double[]{ponto.getX(), ponto.getY()});
				result.get(clientId).insertData(ponto.getDescricao().getBytes(), point, ponto.getId());
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return result;
	}
}
