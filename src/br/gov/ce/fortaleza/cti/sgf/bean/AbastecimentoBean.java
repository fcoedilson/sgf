/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Abastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.AtendimentoAbastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.Bomba;
import br.gov.ce.fortaleza.cti.sgf.entity.Cota;
import br.gov.ce.fortaleza.cti.sgf.entity.Motorista;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoCombustivel;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;
import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.AbastecimentoService;
import br.gov.ce.fortaleza.cti.sgf.service.AtendimentoService;
import br.gov.ce.fortaleza.cti.sgf.service.BombaService;
import br.gov.ce.fortaleza.cti.sgf.service.CotaService;
import br.gov.ce.fortaleza.cti.sgf.service.MotoristaService;
import br.gov.ce.fortaleza.cti.sgf.service.PostoService;
import br.gov.ce.fortaleza.cti.sgf.service.TipoServicoService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;
import br.gov.ce.fortaleza.cti.sgf.util.StatusAbastecimento;
import br.gov.ce.fortaleza.cti.sgf.util.StatusAtendimentoAbastecimento;

/**
 * @author Joel
 * @since 11/12/09
 */
@Scope("session")
@Component("abastecimentoBean")
public class AbastecimentoBean extends EntityBean<Integer, Abastecimento> {

	@Autowired
	private AbastecimentoService service;

	@Autowired
	private AtendimentoService atendimentoService;

	@Autowired
	private VeiculoService veiculoService;

	@Autowired
	private MotoristaService motoristaService;

	@Autowired
	private TipoServicoService tipoServicoService;

	@Autowired
	private PostoService postoService;

	@Autowired
	private BombaService bombaService;

	@Autowired
	private CotaService cotaService;

	private List<Veiculo> veiculos = new ArrayList<Veiculo>();
	private List<TipoServico> tiposServico = new ArrayList<TipoServico>();
	private List<Motorista> motoristas = new ArrayList<Motorista>();
	private List<Posto> postos = new ArrayList<Posto>();
	private List<Bomba> bombas = new ArrayList<Bomba>();
	private List<TipoCombustivel> combustiveis = new ArrayList<TipoCombustivel>();

	private User usuario;
	private Date dtInicial;
	private Date dtFinal;
	private UG orgaoCadSelecionado;
	private UG orgaoSelecionado;
	private UA uaSelecionada;
	private UG ug;
	private Boolean autorizar;
	private Boolean atendimento;
	private Boolean atender;
	private Boolean negar;
	private Boolean start;
	private Boolean mostrarPosto;
	private Long kmAtendimento;
	private Double quantidadeAbastecida;
	private Long ultimaKilometragem;
	private Bomba bomba;
	private StatusAbastecimento status = StatusAbastecimento.AUTORIZADO;
	private String placa;
	private Double saldoAtual;
	private boolean vasilhame = false;
	private boolean kmValido = true;
	private boolean confirmAtender = false;
	private Date horaAbastecimento;

	@Override
	protected Abastecimento createNewEntity() {
		Abastecimento abastecimento = new Abastecimento();
		abastecimento.setCombustivel(new TipoCombustivel());
		abastecimento.setPosto(new Posto());
		setDtInicial(DateUtil.getDateTime(new Date(), "00:00:00"));
		setDtFinal(DateUtil.getDateTime(new Date(), "23:59:59"));
		horaAbastecimento = null;
		this.mostrarPosto = false;
		this.bomba = new Bomba();
		this.ultimaKilometragem = null;
		return abastecimento;
	}

	@Override
	protected Integer retrieveEntityId(Abastecimento entity) {
		return entity.getId();
	}

	@Override
	protected AbastecimentoService retrieveEntityService() {
		return this.service;
	}

	@PostConstruct
	public void init() {

		this.autorizar = false;
		this.atender = false;
		this.atendimento = false;
		this.mostrarPosto = false;
		this.ultimaKilometragem = null;
		this.entities = new ArrayList<Abastecimento>();
		this.dtInicial = DateUtil.getDateTime(DateUtil.getDateTime(new Date()));
		this.dtFinal = DateUtil.getDateTime(DateUtil.getDateTime(new Date()));
		this.status = StatusAbastecimento.AUTORIZADO;

		if(SgfUtil.isChefeTransporte(SgfUtil.usuarioLogado()) || SgfUtil.isChefeSetor(SgfUtil.usuarioLogado())){

			this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial,this.dtFinal, 
					SgfUtil.usuarioLogado().getPessoa().getUa().getUg(), this.status);
		} else if(SgfUtil.isAdministrador(SgfUtil.usuarioLogado())){

			if(this.orgaoSelecionado != null){
				this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial,this.dtFinal, this.orgaoSelecionado, this.status);
			} else {
				this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial,this.dtFinal, 
						SgfUtil.usuarioLogado().getPessoa().getUa().getUg(), this.status);
			}
		}
	}

	@Override
	public String prepareSave() {
		this.autorizar = false;
		this.atender = false;
		this.atendimento = false;
		this.mostrarPosto = false;
		this.ultimaKilometragem = null;
		refreshLists();
		return super.prepareSave();
	}

	@Override
	public String prepareUpdate() {
		this.postos = new ArrayList<Posto>();
		this.orgaoSelecionado = this.entity.getVeiculo().getUa().getUg();
		loadVeiculos();
		postoPorCombustivel();
		return super.prepareUpdate();
	}

	public String postoPorCombustivel(){
		this.postos = new ArrayList<Posto>();
		if (this.entity.getCombustivel() != null) {
			this.postos = postoService.findByCombustivel(this.entity.getCombustivel());
			this.mostrarPosto = true;
		} else {
			this.mostrarPosto = false;
		}
		return SUCCESS;
	}

	/**
	 * pesquisa os abastecimentos efetuados no período por perfil de usuario, período e orgão
	 * @return
	 */
	public String pesquisarAbastecimentosPorPeriodoPorOrgao() {

		User usuarioLogado = SgfUtil.usuarioLogado();
		this.entities = new ArrayList<Abastecimento>();
		this.dtInicial = DateUtil.getDateStartDay(this.dtInicial);
		this.dtFinal = DateUtil.getDateEndDay(this.dtFinal);

		if (DateUtil.compareDate(this.dtInicial, this.dtFinal)) {

			this.placa = this.placa.toUpperCase();

			if (SgfUtil.isOperador(usuarioLogado)) {
				if (this.placa != null && this.placa != "") {
					this.entities = service.pesquisarAbastecimentoVeiculoPorPlaca(this.dtInicial,this.dtFinal, this.orgaoSelecionado, this.placa,this.status);
				} else {
					this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial,this.dtFinal, this.orgaoSelecionado, this.status);
					//this.entities = service.findByPeriodoAndPosto(usuarioLogado.getPosto().getCodPosto(), this.dtInicial,this.dtFinal, status);
				}
			} else if(SgfUtil.isChefeTransporte(usuarioLogado) || SgfUtil.isChefeSetor(usuarioLogado)){
				if (this.placa != null && this.placa != "") {
					this.entities = service.pesquisarAbastecimentoVeiculoPorPlaca(this.dtInicial,this.dtFinal, 
							usuarioLogado.getPessoa().getUa().getUg(), this.placa,this.status);
				} else {
					this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial,this.dtFinal, 
							usuarioLogado.getPessoa().getUa().getUg(), this.status);
				}
			} else if (SgfUtil.isAdministrador(usuarioLogado) || SgfUtil.isCoordenador(usuarioLogado)) {

				if(this.orgaoSelecionado != null && this.orgaoSelecionado.getId() != null){
					if (this.placa != null && this.placa != "") {
						this.entities = service.pesquisarAbastecimentoVeiculoPorPlaca(this.dtInicial,this.dtFinal, this.orgaoSelecionado, this.placa,this.status);
					} else {
						this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial,this.dtFinal, this.orgaoSelecionado, this.status);
					}
				} else {
					//this.entities = service.pesquisarAbastecimentos(this.dtInicial,this.dtFinal, this.status);
					this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial,this.dtFinal, null, this.status);
				}
			}
			return SUCCESS;
		} else {
			JSFUtil.getInstance().addErrorMessage("msg.error.datas.inconsistentes");
			return FAIL;
		}
	}

	/**
	 * O m�todo loadVeiculos, povoa a lista de ve�culos do �rg�o selecionado e chama o m�todo para 
	 * povoar a lista de motoristas.
	 */
	public void loadVeiculos() {
		this.veiculos = new ArrayList<Veiculo>();

		if (this.orgaoSelecionado != null) {
			this.veiculos.addAll(veiculoService.veiculosAtivoscomcota(this.orgaoSelecionado));
		}
		Collections.sort(this.veiculos, new Comparator<Veiculo>() {
			public int compare(Veiculo p1, Veiculo p2) {
				return p1.getPlaca().compareTo(p2.getPlaca());
			}
		});
		loadMotoristas();
	}

	/**
	 * O m�todo loadMotoristas, povoa a lista de motoristas do �rg�o selecionado 
	 */
	public void loadMotoristas() {
		this.motoristas = new ArrayList<Motorista>();
		if (this.orgaoSelecionado != null) {
			this.motoristas = motoristaService.findByUG(this.orgaoSelecionado.getId());
		}
		Collections.sort(this.motoristas, new Comparator<Motorista>() {
			public int compare(Motorista p1, Motorista p2) {
				return p1.getPessoa().getNome().compareTo(p2.getPessoa().getNome());
			}
		});
	}

	private void refreshLists() {
		this.veiculos.clear();
		this.motoristas.clear();
		this.tiposServico.clear();
		if (this.entity != null) {
			if (entity.getQuilometragem() != null) {
				this.ultimaKilometragem = entity.getQuilometragem();
			}
		}
		//Veiculo vasilhame = veiculoService.findByPlacaSingle("VASILHA");
		//this.veiculos.add(vasilhame);
		if (this.orgaoSelecionado != null) {
			this.veiculos.addAll(veiculoService.findByUG(this.orgaoSelecionado));
			this.motoristas.addAll(motoristaService.findByUG(this.orgaoSelecionado.getId()));
		} else {
			this.veiculos.addAll(veiculoService.findByUG(SgfUtil.usuarioLogado().getPessoa().getUa().getUg()));
			this.motoristas.addAll(motoristaService.findByUG(SgfUtil.usuarioLogado().getPessoa().getUa().getUg().getId()));
		}
		this.tiposServico.add(tipoServicoService.retrieve(1));
		if (this.entity.getPosto() != null) {
			if(this.entity.getPosto().getListaBomba() != null){
				setBombas(this.entity.getPosto().getListaBomba());
			}
		}
	}

	/**
	 * valida kilometragem e a cota informados no atendimento do abastecimento
	 */
	private boolean validaQuilometragem() {

		if (this.atendimento) {
			Abastecimento last = service.executeSingleResultQuery("findLast", this.entity.getVeiculo().getId());
			if (last != null) {
				this.ultimaKilometragem = last.getQuilometragem();
				if (this.ultimaKilometragem != null) {
					if (this.ultimaKilometragem > this.kmAtendimento) {
						JSFUtil.getInstance().addErrorMessage("msg.error.quilometragem.inconsistente");
						return false;
					}
				}
			}
		}

		return validarQuantidadeAbastecimento();
	}
	/**
	 * Valida a cota dispon�vel para o ve�culo durante o atendimento
	 * do abastecimento
	 * 
	 * @return Boolean
	 */
	public Boolean validaCota() {
		return this.entity.getVeiculo().getCota().getCotaDisponivel() > 0 ? true : false;
	}

	/**
	 * valida a kilometragem informada em relação ao abastecimento anterior. Caso exista inconsistencia, o sistema retorna para
	 * a tela do cadastro e solicitar confirmação do operador, caso não exista, o sistema salva os dados do atendimento
	 * @return
	 */
	public String validarKilometragemInformada(){
		
		Abastecimento ultimoAbastecimento = service.executeSingleResultQuery("findLast", this.entity.getVeiculo().getId());
		
		if(!this.vasilhame && ultimoAbastecimento != null){
		
			if(ultimoAbastecimento.getQuilometragem() == null){
				ultimoAbastecimento.setQuilometragem(0L);
			}

			if(ultimoAbastecimento != null &&  this.kmAtendimento < ultimoAbastecimento.getQuilometragem()){
				this.kmValido = false;
				JSFUtil.getInstance().addErrorMessage("msg.error.quilometragem.inconsistente");
			} else {
				return update();
			}
		} else {
			this.kmAtendimento = null;
			return update();
		}
		return SUCCESS;
	}
	/**
	 * validar a quantidade a ser abastecida pela operador da bomba
	 * @return
	 */
	public Boolean validarQuantidadeAbastecimento(){

		Cota cota = this.entity.getVeiculo().getCota();
		this.saldoAtual = cota.getCotaDisponivel();
		if(this.saldoAtual > 0){
			if(this.quantidadeAbastecida > this.saldoAtual){
				JSFUtil.getInstance().addErrorMessage("msg.error.cota.utrapassada");
				return false;
			} else {
				return true;
			}
		} else {
			JSFUtil.getInstance().addErrorMessage("msg.error.cota.utrapassada");
			return false;
		}
	}

	/**
	 * faz validações de autorizaçao existente, se veículo é vasilhame
	 * se veículo possui cota
	 * se veículo já possui autorizacao de abastecimento na data solicitada
	 * @return
	 */
	public boolean validarAutorizacao() {

		Veiculo veiculo = this.entity.getVeiculo();
		boolean vasilhame = false;
		boolean existeAutorizacao = false;

		if(veiculo != null){

			if(this.entity.getDataAutorizacao() == null){
				existeAutorizacao = service.validarAutorizacaoVeiculo(veiculo, DateUtil.getDateTime(new Date()), this.entity.getCombustivel().getId());
			} else {
				existeAutorizacao = service.validarAutorizacaoVeiculo(veiculo, this.entity.getDataAutorizacao(), this.entity.getCombustivel().getId());
			}
			if(existeAutorizacao){
				JSFUtil.getInstance().addErrorMessage("msg.error.abastecimento.autoriazacaoExistente");
				return false;
			}

			if (veiculo.getCota() != null || vasilhame) {
				if (veiculo.getCota().getCotaDisponivel() > 0) {
					return true;
				} else {
					JSFUtil.getInstance().addErrorMessage("msg.error.cota.utrapassada");
					return false;
				}
			} else {
				JSFUtil.getInstance().addErrorMessage("msg.error.cota.indisponivel");
				return false;
			}
		}
		return false;
	}

	public String populate() {
		this.placa = null;
		return SUCCESS;
	}


	@Override
	public String search() {
		User usuarioLogado = SgfUtil.usuarioLogado();
		Set<Abastecimento> filtro = new HashSet<Abastecimento>(0);
		this.entities = new ArrayList<Abastecimento>();
		this.tiposServico.add(tipoServicoService.retrieve(1));
		if (SgfUtil.isChefeTransporte(usuarioLogado)  || SgfUtil.isChefeSetor(usuarioLogado)) {
			this.orgaoSelecionado = usuarioLogado.getPessoa().getUa().getUg();
		}
		this.autorizar = false;
		this.atender = false;
		this.atendimento = false;

		this.dtInicial = DateUtil.getDateStartDay(this.dtInicial);//DateUtil.getDateTime(this.dtInicial);
		this.dtInicial = DateUtil.setMilisecondsIndate(this.dtInicial);

		this.dtFinal = DateUtil.getDateEndDay(this.dtFinal);
		this.dtFinal = DateUtil.setMilisecondsIndate(this.dtFinal);

		if (SgfUtil.isAdministrador(usuarioLogado) || SgfUtil.isCoordenador(usuarioLogado)) {

			if(this.orgaoSelecionado == null){
				this.entities = service.pesquisarAbastecimentos(this.dtInicial, this.dtFinal, this.status);
			} else {
				this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial, this.dtFinal, this.orgaoSelecionado, this.status);
			}

		} else if (SgfUtil.isOperador(SgfUtil.usuarioLogado())) {
			if(this.orgaoSelecionado == null){
				this.entities = service.findByPeriodoAndPosto(null, usuarioLogado.getPosto().getCodPosto(), this.dtInicial, this.dtFinal, this.status);
			} else {
				this.entities = service.findByPeriodoAndPosto(this.orgaoSelecionado.getId(), usuarioLogado.getPosto().getCodPosto(), this.dtInicial, this.dtFinal, this.status);
			}

		} else if (SgfUtil.isChefeTransporte(usuarioLogado)) {
			this.entities = service.pesquisarAbastecimentosPorPeriodo(this.dtInicial, this.dtFinal, usuarioLogado.getPessoa().getUa().getUg(), this.status);
		}

		for (Abastecimento abastecimento : this.entities) {

			if (SgfUtil.isAdministrador(usuarioLogado) || SgfUtil.isCoordenador(usuarioLogado)) {
				this.autorizar = true;
			} else if (usuarioLogado.getAutoriza() != null) {
				this.autorizar = usuarioLogado.getAutoriza();
			}

			if (!this.atendimento) {
				this.atendimento = SgfUtil.isOperador(usuarioLogado) && (abastecimento.getStatus().equals(StatusAbastecimento.AUTORIZADO));
			}

			if (this.autorizar || this.atendimento) {
				this.negar = false;
				this.atender = false;
				filtro.add(abastecimento);
			}
		}

		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		this.interval = 2000000;
		return SUCCESS;
	}

	@SuppressWarnings("finally")
	@Override
	public String save() {
		
			if (validarAutorizacao()) {
				if(this.entity.getDataAutorizacao() == null){
					this.entity.setDataAutorizacao(DateUtil.getDateTime(DateUtil.getDateStartDay(new Date())));
				} else {
					this.entity.setDataAutorizacao(DateUtil.getDateTime(DateUtil.getDateStartDay(this.entity.getDataAutorizacao())));
					this.entity.setDataAutorizacao(DateUtil.adicionarOuDiminuir(this.entity.getDataAutorizacao(), DateUtil.SECOND_IN_MILLIS));
				}
				this.entity.setAutorizador(SgfUtil.usuarioLogado());

				try{
					 retrieveEntityService().save(this.entity);
					 return search();
				} catch (Exception e) {
					JSFUtil.getInstance().addErrorMessage("msg.error.abastecimento.autoriazacaoExistente");
					return FAIL;
				} finally{
					return FAIL;
				}
				
		}
		return FAIL;
	}

	/**pages/home.jsf
	 * Atualiza o cadastro do  atendimento, veificando se o abastecimento não se refere ao abastecimento 
	 * de um vasilhame(veículo de modelo com código = 75)
	 */
	@Override
	public String update() {

		try {
			boolean vasilhame = false;

			if (getCurrentState().equals(VIEW) && this.entity.getStatus().equals(StatusAbastecimento.AUTORIZADO)) {

				if(this.entity.getVeiculo().getModelo() != null){
					vasilhame = this.entity.getVeiculo().getModelo().getId() == 75;
				}

				Date currentdate = new Date();

				if(vasilhame){
					this.entity.setQuilometragem(0L);
					this.entity.setStatus(StatusAbastecimento.ATENDIDO);

					AtendimentoAbastecimento atendimento = new AtendimentoAbastecimento();
					atendimento.setBomba(this.bomba);
					atendimento.setData(this.entity.getDataAutorizacao());
					atendimento.setHora(currentdate);
					atendimento.setHoraAtendimento(DateUtil.setHourMinuteSecond(currentdate, this.horaAbastecimento.getHours(), this.horaAbastecimento.getMinutes()));
					atendimento.setQuantidadeAbastecida(quantidadeAbastecida);
					atendimento.setQuilometragem(0L);
					atendimento.setUsuario(SgfUtil.usuarioLogado());
					atendimento.setStatus(StatusAtendimentoAbastecimento.ATENDIDO);
					atendimento.setAbastecimento(this.entity);
					atendimentoService.save(atendimento);
					this.entity.setAtendimentoAbastecimento(atendimento);
					return super.update();
				} else {
					Double cotaAtualizada = 0.0;
					this.entity.setQuilometragem(kmAtendimento);
					this.entity.setStatus(StatusAbastecimento.ATENDIDO);
					AtendimentoAbastecimento atendimento = new AtendimentoAbastecimento();
					atendimento.setBomba(this.bomba);
					atendimento.setData(this.entity.getDataAutorizacao()); // data da autorização do abastecimento
					atendimento.setHora(currentdate); // data e hora atual
					atendimento.setHoraAtendimento(
						DateUtil.setHourMinuteSecond(currentdate, this.horaAbastecimento.getHours(), this.horaAbastecimento.getMinutes())); // data e hora informada pelo operador
					atendimento.setQuantidadeAbastecida(quantidadeAbastecida);
					cotaAtualizada = this.entity.getVeiculo().getCota().getCotaDisponivel() - this.quantidadeAbastecida;
					this.entity.getVeiculo().getCota().setCotaDisponivel(cotaAtualizada);
					atendimento.setQuilometragem(kmAtendimento);
					atendimento.setUsuario(SgfUtil.usuarioLogado());
					atendimento.setStatus(StatusAtendimentoAbastecimento.ATENDIDO);
					atendimento.setAbastecimento(this.entity);
					this.cotaService.update(this.entity.getVeiculo().getCota());
					atendimentoService.save(atendimento);
					this.entity.setAtendimentoAbastecimento(atendimento);
					return super.update();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			setCurrentBean(currentBeanName());
			setCurrentState(SEARCH);
			return FAIL;
		}

		this.horaAbastecimento = null;
		this.bomba = new Bomba();

		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	/**
	 * Negação do abastecimento
	 * @return
	 */
	public String negarAbastecimento() {
		Date now = new Date();
		AtendimentoAbastecimento atendimento = new AtendimentoAbastecimento();
		atendimento.setData(now);
		atendimento.setHora(now);
		atendimento.setUsuario(SgfUtil.usuarioLogado());
		atendimento.setStatus(StatusAtendimentoAbastecimento.NEGADO);
		atendimento.setAbastecimento(this.entity);
		atendimentoService.save(atendimento);
		this.entity.setStatus(StatusAbastecimento.NEGADO);
		return SUCCESS;
	}

	public String confirmarNegacaoAbastecimento(){
		service.update(this.entity);
		return search();
	}

	/**
	 * Prepara tela para efetuar o atendimento do abastecimento
	 * @return
	 */
	public String atenderAbastecimento() {
		this.kmValido = true;
		this.vasilhame = false;
		this.horaAbastecimento = null;

		if(this.entity.getVeiculo().getModelo() != null && this.entity.getVeiculo().getModelo().getId() == 75){
			this.vasilhame = true;
		}

		if(!this.vasilhame){
			Abastecimento ultimaKilometragem = service.executeSingleResultQuery("findLast", this.entity.getVeiculo().getId());
			if(ultimaKilometragem != null){
				this.ultimaKilometragem = ultimaKilometragem.getQuilometragem();
			} else {
				this.ultimaKilometragem = null;
			}
		}


		this.orgaoSelecionado = this.entity.getVeiculo().getUa().getUg();
		Cota cota = this.entity.getVeiculo().getCota();
		this.saldoAtual = cota.getCotaDisponivel();

		this.bombas = new ArrayList<Bomba>();
		this.kmAtendimento = null;
		this.quantidadeAbastecida = null;
		this.veiculos = new ArrayList<Veiculo>();
		this.tiposServico = new ArrayList<TipoServico>();
		this.tiposServico.add(tipoServicoService.retrieve(1));

		User usuarioLogado = SgfUtil.usuarioLogado();
		loadVeiculos();

		if (usuarioLogado.getPosto() != null) {
			this.bombas.addAll(bombaService.findBombaByPosto(usuarioLogado.getPosto().getCodPosto()));
		}
		this.atendimento = true;
		return super.prepareView();
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public List<TipoServico> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServico> tiposServico) {
		this.tiposServico = tiposServico;
	}

	public List<Motorista> getMotoristas() {
		return motoristas;
	}

	public void setMotoristas(List<Motorista> motoristas) {
		this.motoristas = motoristas;
	}

	public List<Posto> getPostos() {
		return postos;
	}

	public void setPostos(List<Posto> postos) {
		this.postos = postos;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public boolean isAutorizar() {
		return autorizar;
	}

	public void setAutorizar(boolean autorizar) {
		this.autorizar = autorizar;
	}

	public void setAtendimento(Boolean atendimento) {
		this.atendimento = atendimento;
	}

	public boolean isAtendimento() {
		return atendimento;
	}

	public boolean isAtender() {
		return atender;
	}

	public void setAtender(boolean atender) {
		this.atender = atender;
	}

	public void setBombas(List<Bomba> bombas) {
		this.bombas = bombas;
	}

	public List<Bomba> getBombas() {
		return bombas;
	}

	public Long getKmAtendimento() {
		return kmAtendimento;
	}

	public void setKmAtendimento(Long kmAtendimento) {
		this.kmAtendimento = kmAtendimento;
	}

	public Double getQuantidadeAbastecida() {
		return quantidadeAbastecida;
	}

	public void setQuantidadeAbastecida(Double quantidadeAbastecida) {
		this.quantidadeAbastecida = quantidadeAbastecida;
	}

	public Bomba getBomba() {
		return bomba;
	}

	public void setBomba(Bomba bomba) {
		this.bomba = bomba;
	}

	public void setNegar(Boolean negar) {
		this.negar = negar;
	}

	public Boolean getNegar() {
		return negar;
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

	public void setCombustiveis(List<TipoCombustivel> combustiveis) {
		this.combustiveis = combustiveis;
	}

	public List<TipoCombustivel> getCombustiveis() {
		return combustiveis;
	}

	public Long getUltimaKilometragem() {
		return ultimaKilometragem;
	}

	public void setUltimaKilometragem(Long ultimaKilometragem) {
		this.ultimaKilometragem = ultimaKilometragem;
	}

	public void setStart(Boolean start) {
		this.start = start;
	}

	public boolean isStart() {
		return start;
	}

	public void setOrgaoSelecionado(UG orgaoSelecionado) {
		this.orgaoSelecionado = orgaoSelecionado;
	}

	public UG getOrgaoSelecionado() {
		return orgaoSelecionado;
	}

	public UG getUg() {
		return ug;
	}

	public void setUg(UG ug) {
		this.ug = ug;
	}

	public void setMostrarPosto(Boolean mostrarPosto) {
		this.mostrarPosto = mostrarPosto;
	}

	public Boolean getMostrarPosto() {
		return mostrarPosto;
	}

	public UG getOrgaoCadSelecionado() {
		return orgaoCadSelecionado;
	}

	public void setOrgaoCadSelecionado(UG orgaoCadSelecionado) {
		this.orgaoCadSelecionado = orgaoCadSelecionado;
	}

	public UA getUaSelecionada() {
		return uaSelecionada;
	}

	public void setUaSelecionada(UA uaSelecionada) {
		this.uaSelecionada = uaSelecionada;
	}

	public StatusAbastecimento getStatus() {
		return status;
	}

	public void setStatus(StatusAbastecimento status) {
		this.status = status;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Double getSaldoAtual() {
		return saldoAtual;
	}

	public void setSaldoAtual(Double saldoAtual) {
		this.saldoAtual = saldoAtual;
	}

	public boolean isVasilhame() {
		return vasilhame;
	}

	public void setVasilhame(boolean vasilhame) {
		this.vasilhame = vasilhame;
	}

	public boolean isKmValido() {
		return kmValido;
	}

	public void setKmValido(boolean kmValido) {
		this.kmValido = kmValido;
	}

	public Date getHoraAbastecimento() {
		return horaAbastecimento;
	}

	public void setHoraAbastecimento(Date horaAbastecimento) {
		this.horaAbastecimento = horaAbastecimento;
	}

	public boolean isConfirmAtender() {
		return confirmAtender;
	}

	public void setConfirmAtender(boolean confirmAtender) {
		this.confirmAtender = confirmAtender;
	}
}
