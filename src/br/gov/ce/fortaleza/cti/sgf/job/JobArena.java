package br.gov.ce.fortaleza.cti.sgf.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.TransactionException;
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
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.ArenaService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.GeoUtil;

public class JobArena implements Job {

	private static final Logger log = Logger.getLogger(JobArena.class);

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("sgf");
		EntityManager entityManager = factory.createEntityManager();

		Query query = entityManager.createQuery("SELECT v FROM Veiculo v WHERE v.status != -1 and v.codigoVeiculoArena is not null");
		List<Veiculo> result = query.getResultList();

		try {
			for (Veiculo v : result) {
				insertTransmissoes(entityManager, v.getId(), v.getCodigoVeiculoArena());
			}

			updateTransmissoes(entityManager);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			entityManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public static void updateTransmissoes(EntityManager entityManager){

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			Map<Integer, RTree> pontosMap = null;
			log.info("Iniciando atualização de transmissões...");
			Query query = entityManager.createQuery("SELECT t FROM Transmissao t WHERE t.ponto.id IS NULL ORDER BY t.dataTransmissao");
			List<Transmissao> transmissoesToUpdate = query.getResultList();

			if(transmissoesToUpdate != null && transmissoesToUpdate.size() > 0){
				log.info("Executando de atualização de transmissões...");
				log.info("Iniciando criação do index de busca...");
				query = entityManager.createQuery("SELECT p FROM Ponto p");
				List<Ponto> pontos = query.getResultList();
				pontosMap = findPontos(pontos, 0);
				for (Transmissao transmissao : transmissoesToUpdate) {
					GeoUtil.atualizarPontoMaisProximo(transmissao, pontosMap.get(0), pontosMap.get(0));
					entityManager.merge(transmissao);
				}
				log.info(" " + transmissoesToUpdate.size()+ " transmissões atualizadas...");
				log.info("Criação do index: OK");
			} else {
				log.info("Nenhuma transmissão atualiazada...");
			}
			transaction.commit();
		} catch (TransactionException e) {
			e.printStackTrace();
			log.info("ERRO:");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("ERRO:");
		}
	}

	@Transactional
	public static void insertTransmissoes(EntityManager entityManager, Integer codveiculo, Integer codveiculoArena) throws Exception{

		try {
			EntityTransaction transaction = entityManager.getTransaction();
			List<Transmissao> transmissoes;

			transaction.begin();

			log.info("Iniciando conexão Arena...");
			ArenaService arena = ArenaService.login();
			log.info("Conexão Arena: OK");
			Date fim = DateUtil.getDateNow();
			Date ini;
			Query query = entityManager.createQuery("SELECT max(t.dataTransmissao) FROM Transmissao t WHERE t.veiculoId = ?");
			query.setParameter(1, codveiculo);
			Date dataUltimaTransmissao =  (Date) query.getSingleResult();

			if(dataUltimaTransmissao != null){
				ini = DateUtil.adicionarOuDiminuir(dataUltimaTransmissao, DateUtil.SECOND_IN_MILLIS);
			} else {
				ini = DateUtil.adicionarOuDiminuir(fim, -5*DateUtil.DAY_IN_MILLIS);
			}

			transmissoes = arena.retrieveTransmissions(ini, fim, codveiculoArena, codveiculo);
			for (Transmissao transmissao : transmissoes) {
				entityManager.persist(transmissao);
			}

			log.info("Terminou inserção de transmissões...");
			transaction.commit();
		} catch (TransactionException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<Integer, RTree> findPontos(List<Ponto> points, Integer clientId) throws Exception {

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
