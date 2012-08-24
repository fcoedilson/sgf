package br.gov.ce.fortaleza.cti.sgf.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
import br.gov.ce.fortaleza.cti.sgf.service.UGService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.ConnectOracle;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.VeiculoDTO;

/**
 * 
 * Bean que gerencia a verifica��o dos ve�culos existentes no patrim�nio mas que
 * ainda n�o est�o no frotas
 * 
 * @author Lafitte
 * @since 27/12/2010
 * 
 */
@Component("veiculoInconsistenteBean")
@Scope("session")
public class VeiculosInconsistentesBean extends EntityBean<Integer, VeiculoDTO> {

	/**
	 * Objeto que guarda os dados da conex�o e a realiza
	 */
	private ConnectOracle conexaoPatrimonio;
	/**
	 * Servi�o de acesso aos ve�culos na base do SGF
	 */
	@Autowired
	private VeiculoService veiculoService;
	/**
	 * Ve�culos recuperados do patrim�nio
	 */
	private List<Veiculo> veiculos;
	/**
	 * Ve�culo que ser� exibido na tela caso n�o esteja cadastrado no Frotas
	 */
	private Veiculo veiculo;

	private UA ua;
	/**
	 * UG escolhida para que sejam listadas suas UA's e selecionada uma para que
	 * seja feita a sincroniza��o dos ve�culos da UA escolhida
	 */
	private UG ug;
	/**
	 * Modelo associado ao ve�culo que ser� cadastrado
	 */
	private Modelo modelo;

	@Autowired
	public ParametroService parametroService;
	/**
	 * Servi�o de acesso �s UAS na base do SGF
	 */
	@Autowired
	private UAService uaService;

	@Autowired
	private UGService ugService;
	/**
	 * Servi�o de acesso aos modelos na base do SGF
	 */
	@Autowired
	private ModeloService modeloService;
	/**
	 * Busca os ve�culos no patrim�nio e verifica quais n�o est�o cadastrados
	 * na base de dados do frotas, aqueles que n�o estiverem ser�o exibidos
	 * na tela do sistema
	 * 
	 * @throws SQLException
	 */

	@Override
	protected BaseService<Integer, VeiculoDTO> retrieveEntityService() {
		return null;
	}

	@Override
	protected Integer retrieveEntityId(VeiculoDTO entity) {
		return null;
	}

	@Override
	protected VeiculoDTO createNewEntity() {
		return null;
	}

	@SuppressWarnings("static-access")
	public void buscaVeiculosPatrimonio() throws SQLException {

		/*
		 * Verifica o c�digo da UG selecionada, caso seja uma das DTE's,
		 * Zoonoses, NUCEM ou SAMU, faz o tratamento pois essas UG's s�o UA's no
		 * Patrim�nio.
		 */
		Map<String, String> mapParam = new HashMap<String, String>();
		List<Parametro> parametros = parametroService.findByTipo("ID_UG");

		for (Parametro p : parametros) {
			mapParam.put(p.getNome(), p.getValor());
		}

		/*
		 * Consulta utilizada para buscar os ve�culos do patrim�nio associados �
		 * ua selecionada
		 */
		String query = "select * from ("
			+ "select cd_ua_atual,cd_bem_perm,cd_ug_atual,"
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
			+ " ORDER BY 4 ASC) v)";

		Connection connection = null;
		Statement stmt = null;

		List<String> placas = new ArrayList<String>();

		this.entities = new ArrayList<VeiculoDTO>();

		try {
			this.veiculos = new ArrayList<Veiculo>();
			String placaValidada;
			this.conexaoPatrimonio = new ConnectOracle();
			/**
			 * Conex�o utilizada para o acesso � base do Patrim�nio
			 */
			connection = conexaoPatrimonio.connection();
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Executa a consulta do par�metro query
			ResultSet rs = stmt.executeQuery(query);
			/*
			 * Itera pelo resultset que cont�m os ve�culos cadastrados no
			 * patrim�nio e verifica se o ve�culo j� est� cadastrado na base de
			 * dados do SGF, caso n�o esteja exibe o ve�culo
			 */

			while (rs.next()) {
				if (rs.getString("placa") != null) {
					placaValidada = rs.getString("placa").replace("-", "");
					placaValidada = placaValidada.replace(" ", "");
					placas.add(placaValidada);
				} else {
					placaValidada = "";
				}
			}

			Map<UG, List<Veiculo>> mapVeiculos = new HashMap<UG, List<Veiculo>>();
			List<String> result = veiculoService.veiculosAusentes(placas);
			rs.first();

			while (rs.next()) {
				if(rs.getString("placa") != null){
					if (result.contains(rs.getString("placa").toUpperCase()) ){
						if (rs.getString("modelo") != null && rs.getString("modelo") != "") {
							try {
								this.modelo = modeloService.findByDesc(rs.getString("modelo"));
							} catch (Exception e) {
								this.modelo = null;
							}
						}

						this.veiculo = new Veiculo();
						this.ua = uaService.retrieve(rs.getString("cd_ua_atual"));
						if(this.ua == null){
							this.ug = ugService.retrieve(rs.getString("cd_ug_atual"));
						} else {
							this.ug = ua.getUg();
						}

						this.veiculo.setUa(ua);
						this.veiculo.setNumeroPatrimonio(rs.getString("cd_bem_perm"));
						this.veiculo.setPlaca(rs.getString("placa").toUpperCase());
						this.veiculo.setNumeroPatrimonio(rs.getString("cd_bem_perm"));
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

						if(mapVeiculos.get(this.ug) != null){
							mapVeiculos.get(this.ug).add(this.veiculo);
						} else{
							mapVeiculos.put(this.ug, new ArrayList<Veiculo>());
							mapVeiculos.get(this.ug).add(this.veiculo);
						}
						this.ug = null;
						this.ua = null;
						this.veiculo = null;
					}
				}
			}

			for (UG ug : mapVeiculos.keySet()) {
				List<Veiculo> veiculos = mapVeiculos.get(ug);
				VeiculoDTO dto = new VeiculoDTO();
				dto.setVeiculos(veiculos);
				dto.setOrgao(ug);
				this.entities.add(dto);
			}

			if (this.entities.isEmpty()) {
				JSFUtil.getInstance().addErrorMessage("msg.sinc.vazia");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			JSFUtil.getInstance().addErrorMessage("msg.error.sincronizacao");
		} catch (Exception e2) {
			e2.printStackTrace();
			JSFUtil.getInstance().addErrorMessage("msg.error.sincronizacao");
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	/**
	 * M�todo utilizado para enviar o sistema para a p�gina
	 * de exibi��o dos resultados
	 * 
	 * @return String
	 */
	public String exibeInconsistencias() {
		setCurrentBean(currentBeanName());
		try {
			buscaVeiculosPatrimonio();
			return SUCCESS;
		} catch (SQLException e) {
			e.printStackTrace();
			return FAIL;
		}
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
}
