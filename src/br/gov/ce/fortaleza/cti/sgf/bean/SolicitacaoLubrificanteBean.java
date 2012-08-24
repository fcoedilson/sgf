package br.gov.ce.fortaleza.cti.sgf.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Modelo;
import br.gov.ce.fortaleza.cti.sgf.entity.Motorista;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.PostoServico;
import br.gov.ce.fortaleza.cti.sgf.entity.SolicitacaoLubrificante;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;
import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.MotoristaService;
import br.gov.ce.fortaleza.cti.sgf.service.PostoService;
import br.gov.ce.fortaleza.cti.sgf.service.PostoServicoService;
import br.gov.ce.fortaleza.cti.sgf.service.SolicitacaoLubrificanteService;
import br.gov.ce.fortaleza.cti.sgf.service.TipoServicoService;
import br.gov.ce.fortaleza.cti.sgf.service.UGService;
import br.gov.ce.fortaleza.cti.sgf.service.UsuarioService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.DownloadFileUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;
import br.gov.ce.fortaleza.cti.sgf.util.StatusAbastecimento;

import com.lowagie.text.DocumentException;

@Scope("session")
@Component("solicitacaoLubrificanteBean")
public class SolicitacaoLubrificanteBean extends
		EntityBean<Integer, SolicitacaoLubrificante> {

	public static final String ATENDIMENTO = "ATENDIMENTO";

	@Autowired
	private SolicitacaoLubrificanteService service;

	@Autowired
	private MotoristaService motoristaService;

	@Autowired
	private VeiculoService veiculoService;

	@Autowired
	private PostoService postoService;

	@Autowired
	private PostoServicoService postoServicoService;

	@Autowired
	private TipoServicoService tipoServicoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UGService ugService;

	private UG orgao;

	private Posto posto = new Posto();

	private Veiculo veiculo = new Veiculo();

	private Motorista motorista = new Motorista();

	private TipoServico tipoServico = new TipoServico();

	private String dataAutorizacao;

	private String placa = "";

	private boolean showInfo = false;

	private boolean justifRequired = true;

	private Integer searchId = 1;

	private SolicitacaoLubrificante ultimaAutorizacao = new SolicitacaoLubrificante();

	private SolicitacaoLubrificante solicitacao = new SolicitacaoLubrificante();

	private List<Motorista> motoristas = null;

	private List<TipoServico> servicos = null;

	private List<Veiculo> veiculos = null;

	private boolean kmChecked = false;
	/**
	 * Per�odo inicial do filtro da busca das solicita��es de lubrificante
	 */
	private Date dtInicial;
	/**
	 * Per�odo final do filtro da busca das solicitia��es de lubrificante
	 */
	private Date dtFinal;

	protected Integer retrieveEntityId(SolicitacaoLubrificante entity) {
		return entity.getId();
	}

	protected SolicitacaoLubrificanteService retrieveEntityService() {
		return this.service;
	}
	
	@PostConstruct
	public void init() {
		this.dtInicial = DateUtil.getDateTime(DateUtil.getDateTime(new Date()), "00:00:00");
		this.dtFinal = DateUtil.getDateTime(DateUtil.getDateTime(new Date()), "23:59:59");
	}

	protected SolicitacaoLubrificante createNewEntity() {

		SolicitacaoLubrificante solicitacao = new SolicitacaoLubrificante();
		solicitacao.setVeiculo(new Veiculo());
		solicitacao.setUsuarioAutorizacao(new User());
		solicitacao.setTipoServico(new TipoServico());
		solicitacao.setMotorista(new Motorista());
		solicitacao.setPosto(new Posto());
		this.ultimaAutorizacao = new SolicitacaoLubrificante();
		this.veiculo = new Veiculo();
		this.orgao = new UG();
		this.dataAutorizacao = null;
		this.justifRequired = true;
		this.showInfo = false;
		this.motoristas = new ArrayList<Motorista>();
		this.servicos = new ArrayList<TipoServico>();
		this.veiculos = new ArrayList<Veiculo>();
		this.kmChecked = false;

		return solicitacao;
	}

	public String prepareSave() {

		User user = SgfUtil.usuarioLogado();
		this.orgao = user.getPessoa().getUa().getUg();
		loadMotoristas();
		return super.prepareSave();
	}

	public boolean isAtendimentoState() {

		return ATENDIMENTO.equals(getCurrentState());
	}

	public boolean isTipoServicoStatus() { return this.servicos.size() <= 0; }

	public boolean isMotoristaStatus() {	return this.motoristas.size() <= 0; }

	public boolean isVeiculoStatus() { return this.veiculos.size() <= 0; }

	public String prepareAtendimento() {

		this.ultimaAutorizacao = new SolicitacaoLubrificante();
		setCurrentBean(currentBeanName());
		setCurrentState(ATENDIMENTO);
		buscarUltimaSolicitacao();
		return SUCCESS;
	}

	public String save() {

		this.entity.setUsuarioAutorizacao(SgfUtil.usuarioLogado());
		this.entity.setDataAutorizacao(new Date());
		this.entity.setStatus(1);
		this.entity.setMotorista(this.motorista);
		this.entity.setPosto(this.posto);
		this.entity.setTipoServico(this.tipoServico);
		return super.save();
	}

	public String search() {

		// super.search();
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);

		// this.entities = service.findSolicitacoesDoDia();
		this.kmChecked = false;
		this.ultimaAutorizacao = new SolicitacaoLubrificante();

		loadList();
		this.interval = 2000000;
		return SUCCESS;
	}

	public String updateJustify() {

		this.justifRequired = false;
		this.entity.setKmTrocaAtual(null);
		this.entity.setKmPrevProximaTroca(null);
		return SUCCESS;
	}

	public String loadMotoristas() {

		this.motoristas = new ArrayList<Motorista>();
		User user = SgfUtil.usuarioLogado();
		if(SgfUtil.isChefeTransporte(user) || SgfUtil.isChefeSetor(user)){
			this.motoristas = motoristaService.findByUG(this.orgao.getId());
		} else if(SgfUtil.isAdministrador(user) || SgfUtil.isCoordenador(user)){
			if(this.orgao != null && this.orgao.getId() != null){
				this.motoristas = motoristaService.findByUG(this.orgao.getId());
			} else {
				this.motoristas = motoristaService.retrieveAll();
			}
		}

		Collections.sort(this.motoristas, new Comparator<Motorista>() {
			public int compare(Motorista o1, Motorista o2) {
				return o1.getPessoa().getNome().compareTo(
						o2.getPessoa().getNome());
			}
		});
		loadVeiculos();
		return SUCCESS;
	}

	public String loadServicos() {

		this.servicos = new ArrayList<TipoServico>();
		List<PostoServico> postos = new ArrayList<PostoServico>(postoServicoService.findByPosto(this.posto.getCodPosto()));

		for (PostoServico p : postos) {
			TipoServico t = p.getTipoServico();
			if (t.getManutencao() == 0 && !t.getDescricao().equals("ABASTECIMENTO")) {
				this.servicos.add(t);
			}
		}

		return SUCCESS;
	}

	@Transactional
	public void loadVeiculos() {

		this.veiculos = new ArrayList<Veiculo>();
		User user = SgfUtil.usuarioLogado();
		if(SgfUtil.isChefeTransporte(user) || SgfUtil.isChefeSetor(user) || SgfUtil.isOperador(user)){
			if(this.orgao != null && this.orgao.getId() != null){
				this.veiculos = veiculoService.findByUG(this.orgao);
			}
		} else if(SgfUtil.isAdministrador(user) || SgfUtil.isCoordenador(user)){
			if( this.orgao != null && this.orgao.getId() != null){
				this.veiculos = veiculoService.findByUG(this.orgao);
			} else {
				this.veiculos = veiculoService.retrieveAll();
			}
		}
	}

	public void buscarUltimaSolicitacao() {

		Posto posto = SgfUtil.usuarioLogado().getPosto() != null ? SgfUtil.usuarioLogado().getPosto() : new Posto();

		if (this.entity.getVeiculo().getId() != null) {

			SolicitacaoLubrificante solicitacao = 
				service.findUltimoRegistroByVeiculo2(this.entity.getVeiculo().getId(), this.tipoServico.getCodTipoServico(), posto.getCodPosto());
			if (solicitacao != null) {
				this.entity.setKmTrocaAnterior(solicitacao.getKmTrocaAtual());
				this.entity.setKmTrocaAtual(null);
				this.entity.setKmPrevProximaTroca(null);
				this.ultimaAutorizacao = solicitacao;
				this.showInfo = true;
			} else {
				this.showInfo = false;
			}
		}
	}

	public String loadList() {

		this.entities = new ArrayList<SolicitacaoLubrificante>();
		User user = SgfUtil.usuarioLogado();
		Posto posto = (user.getPosto() != null) ? user.getPosto() : new Posto();
		Veiculo veiculo = (this.placa != null && this.placa != "") ? veiculoService.findByPlacaSingle(this.placa.toUpperCase())	: new Veiculo();

		this.dtInicial = DateUtil.getDateTime(DateUtil.getDateTime(this.dtInicial), "00:00:00");
		this.dtFinal = DateUtil.getDateTime(DateUtil.getDateTime(this.dtFinal), "23:59:59");

		if (SgfUtil.isOperador(user)) {
			this.entities = service.findSolicitacoes(this.searchId, veiculo.getId(), posto.getCodPosto(), this.dtInicial, this.dtFinal, null);
		} else if (SgfUtil.isChefeTransporte(user) || SgfUtil.isChefeSetor(user)) {
			this.orgao = SgfUtil.usuarioLogado().getPessoa().getUa().getUg();
			this.entities = service.findSolicitacoes(this.searchId, veiculo	.getId(), null, this.dtInicial, this.dtFinal, this.orgao);
		} else if (SgfUtil.isAdministrador(user) || SgfUtil.isCoordenador(user)) {
			this.entities = service.findSolicitacoes(this.searchId, veiculo.getId(), null, this.dtInicial, this.dtFinal,this.orgao);
		}
		return SUCCESS;
	}

	public String prepareUpdate() {

		//this.orgao = SgfUtil.usuarioLogado().getPessoa().getUa().getUg();
		this.ultimaAutorizacao = new SolicitacaoLubrificante();
		this.veiculo = this.entity.getVeiculo();
		this.motorista = this.entity.getMotorista();
		this.posto = this.entity.getPosto();
		this.tipoServico = this.entity.getTipoServico();
		loadMotoristas();
		loadServicos();
		if (this.entity.getDataAutorizacao() != null) {
			this.dataAutorizacao = DateUtil.parseAsString("dd/MM/yyyy", this.entity.getDataAutorizacao());
		} else {
			this.dataAutorizacao = null;
		}
		return super.prepareUpdate();
	}

	public String update() {
		if (this.dataAutorizacao != null) {
			this.entity.setDataAutorizacao(DateUtil.parseStringAsDate("dd/MM/yyyy", this.dataAutorizacao));
		}
		this.kmChecked = false;
		return super.update();
	}

	public String delete() {

		this.entity.setStatus(-1);
		return super.update();
	}

	public String verificarKM() {

		if (this.entity.getKmPrevProximaTroca() != null	&& this.entity.getKmTrocaAtual() != null) {

			if (this.entity.getKmTrocaAnterior() != null) {
				if (this.entity.getKmTrocaAnterior() > this.entity.getKmTrocaAtual()) {
					this.kmChecked = true;
				}
			}
			if (this.entity.getKmPrevProximaTroca() < this.entity.getKmTrocaAtual()) {
				this.kmChecked = true;
			}
		}
		return SUCCESS;
	}

	public String atenderAutorizacao() {

		this.justifRequired = true;
		if (this.entity.getKmPrevProximaTroca() != null && this.entity.getKmTrocaAtual() != null) {
			if (this.entity.getKmTrocaAnterior() != null) {
				if (this.entity.getKmTrocaAnterior() > this.entity.getKmTrocaAtual()) {
					this.kmChecked = true;
					return FAIL;
				}
			}

			if (this.entity.getKmPrevProximaTroca() < this.entity.getKmTrocaAtual()) {
				this.kmChecked = true;
				return FAIL;
			}
		}

		this.entity.setDataAtendimento(new Date());
		this.entity.setUsuarioAtendimento(SgfUtil.usuarioLogado());
		this.entity.setStatus(2);
		return super.update();
	}

	public String negarAutorizacao() {

		Date data = new Date();
		this.entity.setDataAutorizacao(data);
		this.entity.setUsuarioAtendimento(SgfUtil.usuarioLogado());
		this.entity.setKmTrocaAtual(null);
		this.entity.setKmPrevProximaTroca(null);
		this.entity.setStatus(0);
		return super.update();
	}

	public String emitirAutorizacao() {

		String justificativa = this.entity.getJustificativa() != null && 
			this.entity.getJustificativa() != "" ? this.entity.getJustificativa() : " ";

		Modelo modelo = this.entity.getVeiculo().getModelo();
		String descricao = modelo == null ? "" : modelo.getDescricao();
		String placa = this.entity.getVeiculo().getPlaca();
		placa = placa == null || placa == "" ? "" : placa;
		UA ua = this.entity.getVeiculo().getUa();
		String descUa = ua == null ? "" : ua.getDescricao();
		String dataAut = DateUtil.parseAsString("dd/MM/yyyy HH:mm", this.entity.getDataAutorizacao());
		String header = descricao + " - " + placa + "#" + descUa + "#" +
		this.entity.getMotorista().getPessoa().getNome() + "#" +
		this.entity.getPosto().getDescricao() + "#" +
		this.entity.getTipoServico().getDescricao() + "#" +
		this.entity.getUsuarioAutorizacao().getPessoa().getNome() + "#" +
		dataAut + "#" + justificativa;

		try {
			if (header != null && header != "") {
				DownloadFileUtil.getAutorizacaoSolTrocaLubrificantePDF(header, null, "autorizacao_soltroca_lub", 1);
			}
			super.update();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataAutorizacao(String dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public Integer getSearchId() {
		return searchId;
	}

	public void setSearchId(Integer searchId) {
		this.searchId = searchId;
	}

	public SolicitacaoLubrificante getUltimaAutorizacao() {
		buscarUltimaSolicitacao();
		return ultimaAutorizacao;
	}

	public void setUltimaAutorizacao(SolicitacaoLubrificante ultimaAutorizacao) {
		this.ultimaAutorizacao = ultimaAutorizacao;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public boolean isShowInfo() {
		return showInfo;
	}

	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}

	public UG getOrgao() {
		return orgao;
	}

	public void setOrgao(UG orgao) {
		this.orgao = orgao;
	}

	public Posto getPosto() {
		return posto;
	}

	public void setPosto(Posto posto) {
		this.posto = posto;
	}

	public List<Motorista> getMotoristas() {
		return motoristas;
	}

	public void setMotoristas(List<Motorista> motoristas) {
		this.motoristas = motoristas;
	}

	public List<TipoServico> getServicos() {
		return servicos;
	}

	public void setServicos(List<TipoServico> servicos) {
		this.servicos = servicos;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public boolean isKmChecked() {
		return kmChecked;
	}

	public void setKmChecked(boolean kmChecked) {
		this.kmChecked = kmChecked;
	}

	public boolean isJustifRequired() {
		return justifRequired;
	}

	public void setJustifRequired(boolean justifRequired) {
		this.justifRequired = justifRequired;
	}

	public SolicitacaoLubrificante getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(SolicitacaoLubrificante solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}
}
