package br.gov.ce.fortaleza.cti.sgf.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Modelo;
import br.gov.ce.fortaleza.cti.sgf.entity.Parametro;
import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.service.ModeloService;
import br.gov.ce.fortaleza.cti.sgf.service.ParametroService;
import br.gov.ce.fortaleza.cti.sgf.service.UAService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.ConnectOracle;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;
import br.gov.ce.fortaleza.cti.sgf.util.dto.RelatorioDTO;

/**
 * Bean que gerencia a sincronização da base de dados do SGF com a base de dados
 * do Patrimônio, atualizando os veículos do SGF de acordo com o que foi
 * cadastrado no Patrimônio
 * 
 * @author lafitte
 * @since 23/04/2010
 * 
 */
@Component("sincronizaBean")
@Scope("session")
public class SincronizaBean  extends EntityBean<Integer, RelatorioDTO>{

	public static String SINCRONIZACAO = "SINCRONIZACAO";

	@Autowired
	public ParametroService parametroService;

	/**
	 * Serviço de acesso aos veículos na base do SGF
	 */
	@Autowired
	private VeiculoService veiculoService;
	/**
	 * Serviço de acesso às UAS na base do SGF
	 */
	@Autowired
	private UAService uaService;
	/**
	 * Serviço de acesso aos modelos na base do SGF
	 */
	@Autowired
	private ModeloService modeloService;
	/**
	 * Lista que armazenará os veículos cadastrados no SGF, utilizada para
	 * verificar se o veículo encontrado no Patrimônio já está cadastrado no SGF
	 */
	private List<Veiculo> veiculosSGF;
	/**
	 * veículos recuperados do Patrimônio
	 */
	private List<Veiculo> veiculos;
	/**
	 * veículo utilizado na inclusão no banco do SGF,
	 */
	private Veiculo veiculo;
	/**
	 * UA associada ao veículo que serão cadastrado
	 */
	private UA ua;
	/**
	 * UG escolhida para que sejam listadas suas UA's e selecionada uma para que
	 * seja feita a sincronização dos veículos da UA escolhida
	 */
	private UG ug;
	/**
	 * Lista de UAS listadas de acordo com a ug escolhida
	 */
	private List<UA> uas;
	/**
	 * Modelo associado ao veículo que será cadastrado
	 */
	private Modelo modelo;

	private Logger logger = Logger.getLogger(SincronizaBean.class);

	/**
	 * Construtor default
	 * 
	 * @return
	 */
	public void init() {
	}
	/**
	 * Realiza as devidas inicializações e direciona para a página
	 * de sincronização
	 * 
	 * @return
	 */
	public String sincronizacao() {
		this.ug = SgfUtil.usuarioLogado().getPessoa().getUa().getUg();
		loadUAs();
		return "SUCCESS";
	}

	/**
	 * Carrega as UA's associadas a UG selecionada
	 */
	public void loadUAs() {
		this.uas = this.uaService.retrieveByUG(this.ug.getId());
		Collections.sort(this.uas, new Comparator<UA>() {
			public int compare(UA o1, UA o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
	}

	/**
	 * Busca os veículos do Patrimônio de acordo com a UA escolhida
	 * e os insere na lista de veículos a serem litados na tela
	 * @throws SQLException 
	 */

	@SuppressWarnings("static-access")
	public void buscaVeiculosPatrimonio() throws SQLException {

		String codUAPat;
		String codUASgf;
		/*
		 * Verifica o código da UG selecionada, caso seja
		 * uma das DTE's, Zoonoses, NUCEM ou SAMU, faz o tratamento
		 * pois essas UG's são UA's no Patrimônio.
		 */
		Map<String, String> mapParam = new HashMap<String, String>();
		List<Parametro> parametros = parametroService.findByTipo("ID_UG");

		/**
		 * hashmap dos parâmetros de configuração do banco de dados
		 */
		for (Parametro p : parametros) {
			mapParam.put(p.getNome(), p.getValor());
		}


		codUAPat = this.ug.getId(); // UG SELECIONADA PELO USER
		codUASgf = mapParam.get(this.ug.getId()); // Verifica se a UA está mapeada como UA no SGF

		if(codUASgf == null){ // se codUASgf = NULL, então o mapeamento não existe
			codUAPat = this.ua.getId();
			codUASgf = codUAPat;
		} else {
			codUASgf = this.ua.getId();
		}

		/*
		 * Consulta utilizada para buscar os veículos do Patrimônio
		 * associados à ua selecionada
		 */
		String query = "select * from ("
			+ "select cd_ua_atual,cd_bem_perm,"
			+ "(select ds_car_char  from BP_ENT_CAR_BEM bb"
			+ "   where cd_caracteristica = '038'"
			+ " and bb.sq_bem_perm = v.sq_bem_perm) placa,"
			+ "(select ds_car_char"
			+ " from BP_ENT_CAR_BEM bb"
			+ " where cd_caracteristica = '039' "
			+ "and bb.sq_bem_perm = v.sq_bem_perm) chassi,"
			+ " (select ds_car_char"
			+ " from BP_ENT_CAR_BEM bb "
			+ "where cd_caracteristica = '040' "
			+ "and bb.sq_bem_perm = v.sq_bem_perm) renavam, "
			+ "(select ct.nm_caract_tab"
			+ " from BP_ENT_CAR_GEN bg, cr_caract_tab ct, BP_BEM B"
			+ " where ct.cd_caracteristica = '035'"
			+ " and bg.cd_entrada = v.cd_entrada"
			+ " and b.sq_bem_perm = v.sq_bem_perm"
			+ " and ct.cd_caracteristica = bg.cd_caracteristica"
			+ " and ct.sq_caract_tab = bg.ds_car_char"
			+ " and bg.sq_item = b.sq_item"
			+ " and bg.cd_ug = b.cd_ug) combustivel,"
			+ " (select ct.nm_caract_tab"
			+ " from BP_ENT_CAR_GEN bg, cr_caract_tab ct, BP_BEM B"
			+ " where ct.cd_caracteristica = '033'"
			+ " and bg.cd_entrada = v.cd_entrada"
			+ " and b.sq_bem_perm = v.sq_bem_perm"
			+ " and ct.cd_caracteristica = bg.cd_caracteristica"
			+ " and ct.sq_caract_tab = bg.ds_car_char"
			+ " and bg.sq_item = b.sq_item"
			+ " and bg.cd_ug = b.cd_ug) modelo,"
			+ " (select ct.nm_caract_tab"
			+ " from BP_ENT_CAR_GEN bg, cr_caract_tab ct, BP_BEM B"
			+ " where ct.cd_caracteristica = '036'"
			+ " and bg.cd_entrada = v.cd_entrada"
			+ " and b.sq_bem_perm = v.sq_bem_perm"
			+ " and ct.cd_caracteristica = bg.cd_caracteristica"
			+ " and ct.sq_caract_tab = bg.ds_car_char"
			+ " and bg.sq_item = b.sq_item"
			+ " and bg.cd_ug = b.cd_ug) ANOFABR,"
			+ " (select ct.nm_caract_tab"
			+ " from BP_ENT_CAR_GEN bg, cr_caract_tab ct, BP_BEM B"
			+ " where ct.cd_caracteristica = '037'"
			+ " and bg.cd_entrada = v.cd_entrada"
			+ " and b.sq_bem_perm = v.sq_bem_perm"
			+ " and ct.cd_caracteristica = bg.cd_caracteristica"
			+ " and ct.sq_caract_tab = bg.ds_car_char"
			+ " and bg.sq_item = b.sq_item"
			+ " and bg.cd_ug = b.cd_ug) ANOMODELO,"
			+ " (select ct.nm_caract_tab"
			+ " from BP_ENT_CAR_GEN bg, cr_caract_tab ct, BP_BEM B"
			+ " where ct.cd_caracteristica = '009'"
			+ " and bg.cd_entrada = v.cd_entrada"
			+ " and b.sq_bem_perm = v.sq_bem_perm"
			+ " and ct.cd_caracteristica = bg.cd_caracteristica"
			+ " and ct.sq_caract_tab = bg.ds_car_char"
			+ " and bg.sq_item = b.sq_item"
			+ " and bg.cd_ug = b.cd_ug) cor"
			+ " from (SELECT C.CD_ORGAO, C.SQ_BEM_PERM,"
			+ " MAX(C.CD_PATR_ANTIGO) AS CD_PATR_ANTIGO,"
			+ " MAX(C.CD_BEM_PERM) AS CD_BEM_PERM,"
			+ " MAX(C.VL_UNITARIO) AS VL_UNITARIO,"
			+ " MAX(C.VL_UFIR) AS VL_UFIR,"
			+ " MAX(C.DT_GARANTIA_INI) AS DT_GARANTIA_INI,"
			+ " MAX(C.DT_GARANTIA_FIM) AS DT_GARANTIA_FIM,"
			+ " MAX(C.CD_TP_SIT_FISICA) AS CD_TP_SIT_FISICA,"
			+ " MAX(C.CD_UG_COMPRA) AS CD_UG_COMPRA,"
			+ " MAX(C.CD_UL_ATUAL) AS CD_UL_ATUAL,"
			+ " MAX(C.CD_UA_ATUAL) AS CD_UA_ATUAL,"
			+ " MAX(C.CD_TP_STATUS) AS CD_TP_STATUS,"
			+ " MAX(C.CD_ORG_DETENTOR) AS CD_ORG_DETENTOR,"
			+ " MAX(C.CD_DETENTOR) AS CD_DETENTOR,"
			+ " MAX(C.CD_CONTA) AS CD_CONTA,"
			+ " MAX(C.CD_BEM_SERVICO) AS CD_BEM_SERVICO,"
			+ " MAX(C.CD_ENTRADA) AS CD_ENTRADA,"
			+ " MAX(C.DT_INCLUSAO) AS DT_INCLUSAO,"
			+ " MAX(C.DT_AQUISICAO) AS DT_AQUISICAO,"
			+ " MAX(C.DT_BAIXA) AS DT_BAIXA,"
			+ " MAX(C.DT_CONTABIL) AS DT_CONTABIL,"
			+ " MAX(C.CD_AGENTE) AS CD_AGENTE,"
			+ " MAX(C.CD_TP_OPERACAO) AS CD_TP_OPERACAO,"
			+ " MAX(C.CD_TP_FORMA_OP) AS CD_TP_FORMA_OP,"
			+ " MAX(C.CD_TP_BEM) AS CD_TP_BEM,"
			+ " MAX(C.CD_ORGAO_SUP) AS CD_ORGAO_SUP,"
			+ " MAX(C.SQ_BEM_PERM_SUP) AS SQ_BEM_PERM_SUP,"
			+ " MAX(C.CD_UG_ATUAL) AS CD_UG_ATUAL,"
			+ " MAX(C.CD_CENTRO_CUSTO) AS CD_CENTRO_CUSTO,"
			+ " MAX(C.DS_COMPLETA) AS DS_COMPLETA,"
			+ " MAX(C.CD_SIMOV) AS CD_SIMOV, MAX(C.VL_MERCADO) AS VL_MERCADO,"
			+ " MAX(C.NR_PROCESSO_COMODATO) AS NR_PROCESSO_COMODATO"
			+ " FROM BP_V_CONSULTA_GERAL C"
			+ " WHERE (((C.CD_BEM_SERVICO = '100001175' AND C.CD_ORGAO = '001') OR"
			+ " (C.CD_BEM_SERVICO = '100001374' AND C.CD_ORGAO = '001') OR"
			+ " (C.CD_BEM_SERVICO = '100000968' AND C.CD_ORGAO = '001') OR"
			+ " (C.CD_BEM_SERVICO = '100000255' AND C.CD_ORGAO = '001') OR"
			+ " (C.CD_BEM_SERVICO = '100001071' AND C.CD_ORGAO = '001') OR"
			+ " (C.CD_BEM_SERVICO = '100000848' AND C.CD_ORGAO = '001')))"
			+ " GROUP BY C.CD_ORGAO, C.SQ_BEM_PERM"
			+ " ORDER BY 4 ASC) v)"
			+ " where placa is not null and chassi is not null and renavam is not null and combustivel is not null"
			+ " and cd_ua_atual = '" + codUAPat + "'";
		Connection connection = null;
		Statement stmt = null;
		try {
			this.veiculos = new ArrayList<Veiculo>();
			String placaValidada;
			/**
			 * Conexao utilizada para o acesso à base do Patrimônio
			 */
			connection = ConnectOracle.connection();
			stmt = connection.createStatement();
			// Executa a consulta do par�metro query
			ResultSet rs = stmt.executeQuery(query);

			/*
			 * Itera pelo resultset que cont�m os veículos cadastrados no
			 * Patrimônio e verifica se o veículo j� est� cadastrado na base de
			 * dados do SGF, caso n�o esteja cadastra o veículo
			 */
			this.ua = uaService.retrieve(codUASgf);

			while (rs.next()) {

				placaValidada = rs.getString("placa").replace("-", "");
				placaValidada = rs.getString("placa").replace(" ", "");
				placaValidada = rs.getString("placa").replace(".", "");
				this.veiculo = veiculoService.findByPlacaSingle(placaValidada.toUpperCase()); // busca o veículo pela placa
				if (rs.getString("modelo") != null && rs.getString("modelo") != "") {
					try {
						this.modelo = modeloService.findByDesc(rs.getString("modelo"));
					} catch (Exception e) {
						this.modelo = null;
					}
				}
				if (this.veiculo == null) {
					this.veiculo = new Veiculo();
					this.veiculo.setUa(ua);
					this.veiculo.setNumeroPatrimonio(rs.getString("cd_bem_perm"));
					this.veiculo.setPlaca(placaValidada);
					this.veiculo.setChassi(rs.getString("chassi"));
					this.veiculo.setRenavam(rs.getString("renavam"));
					this.veiculo.setCombustivel(rs.getString("combustivel"));
					this.veiculo.setModelo(modelo);
					this.veiculo.setStatus(0);
					this.veiculo.setTemSeguro(0);
					if (rs.getString("anofabr") != null	&& rs.getString("anofabr") != "") {
						this.veiculo.setAnoFabricacao(Integer.parseInt(rs.getString("anofabr")));
					}
					if (rs.getString("anomodelo") != null && rs.getString("anomodelo") != "") {
						this.veiculo.setAnoModelo(Integer.parseInt(rs.getString("anomodelo")));
					}
					this.veiculo.setCor(rs.getString("cor"));
					this.veiculos.add(this.veiculo);
					this.veiculo = null;
				}
			}

			if (this.veiculos.isEmpty()) {
				JSFUtil.getInstance().addErrorMessage("msg.sinc.vazia");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JSFUtil.getInstance().addErrorMessage("msg.error.sincronizacao");
		} catch(Exception e2){
			e2.printStackTrace();
			JSFUtil.getInstance().addErrorMessage("msg.error.sincronizacao");
		} finally {
			if(connection!=null){
				connection.close();
			}
			if(stmt!=null){
				stmt.close();
			}
		}
	}
	/**
	 * Realiza a sincronização incluindo os veículos do Patrimônio no
	 * banco de dados do SGF
	 */
	public String sincronizar() throws NonUniqueObjectException {

		String placs = "\n";
		List<Veiculo> naoSincronizados = new ArrayList<Veiculo>();
		
		for (Veiculo v : veiculos) {

			try {
				this.veiculoService.save(v);

			} catch (DataIntegrityViolationException e) {
				e.printStackTrace();
				//JSFUtil.getInstance().addErrorMessage("msg.error.valorDuplicado");
				naoSincronizados.add(v);
				continue;
				//return "FAIL";
			}
			placs +=  "" + v.getPlaca() + "\n";
		}
		
		if(naoSincronizados.size() > 0){
			JSFUtil.getInstance().addErrorMessage("msg.error.valorDuplicado");
			//return FAIL;
		}

		sincronizacao();
		this.veiculos = new ArrayList<Veiculo>();
		return "SUCCESS";
	}

	/**
	 * @return the ua
	 */
	public UA getUa() {
		return ua;
	}

	/**
	 * @param ua
	 *            the ua to set
	 */
	public void setUa(UA ua) {
		this.ua = ua;
	}

	/**
	 * @return the ug
	 */
	public UG getUg() {
		return ug;
	}

	/**
	 * @param ug
	 *            the ug to set
	 */
	public void setUg(UG ug) {
		this.ug = ug;
	}

	/**
	 * @return the uas
	 */
	public List<UA> getUas() {
		return uas;
	}

	/**
	 * @param uas
	 *            the uas to set
	 */
	public void setUas(List<UA> uas) {
		this.uas = uas;
	}

	/**
	 * @return the veiculos
	 */
	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	/**
	 * @param veiculos
	 *            the veiculos to set
	 */
	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}
	@Override
	protected RelatorioDTO createNewEntity() {
		return null;
	}
	@Override
	protected Integer retrieveEntityId(RelatorioDTO entity) {
		return null;
	}
	@Override
	protected BaseService<Integer, RelatorioDTO> retrieveEntityService() {
		return null;
	}

	public String sincronizacaoVeiculos() {
		sincronizacao();
		setCurrentState(SINCRONIZACAO);
		setCurrentBean(currentBeanName());
		return SUCCESS;
	}

	public boolean isSincronizaState() {

		return SINCRONIZACAO.equals(getCurrentState());
	}
}
