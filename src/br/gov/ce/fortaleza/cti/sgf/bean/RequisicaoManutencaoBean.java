package br.gov.ce.fortaleza.cti.sgf.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.ItemRequisicao;
import br.gov.ce.fortaleza.cti.sgf.entity.Oficina;
import br.gov.ce.fortaleza.cti.sgf.entity.RequisicaoManutencao;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.ItemRequisicaoService;
import br.gov.ce.fortaleza.cti.sgf.service.RequisicaoManutencaoService;
import br.gov.ce.fortaleza.cti.sgf.service.TipoServicoService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.DownloadFileUtil;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;
import br.gov.ce.fortaleza.cti.sgf.util.StatusVeiculo;

import com.lowagie.text.DocumentException;

@Scope("session")
@Component("requisicaoManutencaoBean")
public class RequisicaoManutencaoBean extends
EntityBean<Integer, RequisicaoManutencao> {

	@Autowired
	private RequisicaoManutencaoService service;

	@Autowired
	private VeiculoService veiculoService;

	@Autowired
	private ItemRequisicaoService itemService;

	@Autowired
	private TipoServicoService tipoServicoService;

	private List<ItemRequisicao> itensRequisicao = new ArrayList<ItemRequisicao>();
	private List<TipoServico> servicos = new ArrayList<TipoServico>();
	private ItemRequisicao itemRequisicao = new ItemRequisicao(new TipoServico());
	private ItemRequisicao item2Remove = null;
	private List<Veiculo> veiculos;
	private RequisicaoManutencao requisicao;
	private Boolean validDataInicio = false;
	private String dataInicio;
	private String dataFim;
	private String placa;
	private Veiculo veiculo = new Veiculo();
	private Integer searchId = 0;
	private boolean novoItem = false;
	private UG orgao;

	protected Integer retrieveEntityId(RequisicaoManutencao entity) {
		return entity.getId();
	}

	protected RequisicaoManutencaoService retrieveEntityService() {
		return this.service;
	}

	protected RequisicaoManutencao createNewEntity() {
		RequisicaoManutencao requisicao = new RequisicaoManutencao();
		requisicao.setItensRequisicao(new ArrayList<ItemRequisicao>());
		requisicao.setVeiculo(new Veiculo());
		requisicao.setOficina(new Oficina());
		requisicao.setUsuario(SgfUtil.usuarioLogado());
		this.itensRequisicao = new ArrayList<ItemRequisicao>();
		this.itemRequisicao = new ItemRequisicao(new TipoServico());
		this.dataInicio = null;
		this.dataFim = null;
		this.orgao = SgfUtil.usuarioLogado().getPessoa().getUa().getUg();
		this.servicos = tipoServicoService.findServicosManutencao();
		return requisicao;
	}

	public String prepareSave() {
		this.veiculos = new ArrayList<Veiculo>();
		loadVeiculos();
		super.prepareSave();
		if (this.veiculo != null) {
			this.entity.setVeiculo(this.veiculo);
		}
		return SUCCESS;
	}

	public boolean isItensStatus(){
		return this.itensRequisicao.size() > 0;
	}
	
	public String searchStatus(){
		this.interval = 2000000;
		return super.searchStatus();
	}

	public String delete() {
		// this.entity.getVeiculo().setStatus(0);
		this.entity.setStatus(-1);
		this.veiculoService.update(this.entity.getVeiculo());
		super.update();
		return super.searchStatus();
	}

	public String prepareUpdate() {
		this.itensRequisicao = itemService.findByIdRequisicao(this.entity.getId());
		this.orgao = this.entity.getVeiculo().getUa().getUg();
		loadVeiculos();
		this.servicos = tipoServicoService.findServicosManutencao();
		for (ItemRequisicao i : this.getItensRequisicao()) {
			this.servicos.remove(i.getTipoServico());
		}
		return super.prepareUpdate();
	}

	@Transactional
	public String update() {
		itemService.deleteByRequisicao(this.entity.getId());
		this.entity.setItensRequisicao(new ArrayList<ItemRequisicao>(this.itensRequisicao));
		return super.update();
	}

	public String save() {
		if (!DateUtil.compareDate(this.entity.getDataInicio(), this.entity.getDataFim())) {
			JSFUtil.getInstance().addErrorMessage("msg.error.datas.inconsistentes");
			return FAIL;
		} else {
			List<RequisicaoManutencao> req = service.findByVeiculoLivre(this.entity.getVeiculo().getId(), this.entity.getDataInicio(), this.entity.getDataFim());
			if (req.size() > 0) {
				JSFUtil.getInstance().addErrorMessage("msg.error.veiculo.comManutencao");
				return FAIL;
			} else {
				this.entity.getVeiculo().setStatus(StatusVeiculo.EM_MANUTENCAO);
				this.veiculoService.update(this.entity.getVeiculo());
				this.entity.setItensRequisicao(new ArrayList<ItemRequisicao>(this.itensRequisicao));
				this.entity.setStatus(0);
				super.save();
				return super.searchStatus();
			}
		}
	}

	public String search() {
		this.entities = retrieveEntityService().findAllByStatus("id");
		this.entities = retrieveEntityService().filter(this.entities, this.filter);
		loadVeiculos();
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	@Transactional
	public String registrarSaidaManutencao() {
		this.entity.setStatus(1);
		this.entity.setDataSaida(new Date());
		super.update();
		if (!(service.findByVeiculoStatus(this.entity.getVeiculo().getId(), 0))) {
			this.entity.getVeiculo().setStatus(StatusVeiculo.EM_MANUTENCAO);
			this.veiculoService.update(this.entity.getVeiculo());
		}
		return SUCCESS;
	}

	@Transactional
	public String registrarRetornoManutencao() {
		this.entity.setStatus(-1);
		this.entity.setDataRetorno(new Date());
		super.update();
		if (!(service.findByVeiculoStatus(this.entity.getVeiculo().getId(), 0))) {
			this.entity.getVeiculo().setStatus(StatusVeiculo.DISPONIVEL);
			this.veiculoService.update(this.entity.getVeiculo());
		}
		return SUCCESS;
	}

	public String adicionarNovoItem() {
		ItemRequisicao novo = new ItemRequisicao();
		novo.setTipoServico(this.tipoServicoService.retrieve(this.itemRequisicao.getTipoServico().getCodTipoServico()));
		novo.setRequisicaoManutencao(this.entity);
		novo.setValorUnitario(this.itemRequisicao.getValorUnitario());
		novo.setQuantidade(this.itemRequisicao.getQuantidade());
		novo.setValorTotal(this.itemRequisicao.getValorUnitario()* this.itemRequisicao.getQuantidade());
		this.itensRequisicao.add(novo);
		this.servicos.remove(novo.getTipoServico());
		return SUCCESS;
	}

	public String removeItem() {
		this.getItensRequisicao().remove(item2Remove);
		// itemService.delete(item2Remove.getId());
		this.servicos.add(item2Remove.getTipoServico());
		return SUCCESS;
	}

	public String findByVeiculo() {
		Integer vid = this.veiculo != null ? this.veiculo.getId() : null;
		if (vid != null) {
			this.entities = service.findByVeiculo(vid);
		} else {
			this.entities = service.retrieveAll();
		}
		return SUCCESS;
	}

	public String loadVeiculos() {
		this.veiculos = new ArrayList<Veiculo>();
		User user = SgfUtil.usuarioLogado();
		if(SgfUtil.isAdministrador(user)){
			if(this.orgao != null){
				this.veiculos = veiculoService.findByUG(this.orgao);
			} else {
				this.veiculos = veiculoService.retrieveAll();
			}
		} else {
			this.veiculos = veiculoService.veiculos();
		}
		Collections.sort(this.veiculos, new Comparator<Veiculo>() {
			public int compare(Veiculo o1, Veiculo o2) {
				return o1.getChassi().compareTo(o2.getChassi());
			}
		});
		return SUCCESS;
	}

	public String loadList() {
		if (this.placa != "" && this.placa != null) {
			this.veiculo = veiculoService.findByPlacaSingle(this.placa.toUpperCase());
			if (this.veiculo != null) {
				this.entities = service.findByVeiculo(this.veiculo.getId());
			} else {
				this.entities = new ArrayList<RequisicaoManutencao>();
			}
		} else {
			User user = SgfUtil.usuarioLogado();
			if(SgfUtil.isAdministrador(user)){
				searchStatus();
				//this.entities = service.retrieveAll();
			} else {
				this.entities = service.findByUG(user.getPessoa().getUa().getUg().getId());
			}
		}
		return SUCCESS;
	}

	public String atualizarValorTotal() {
		if (this.itemRequisicao.getValorUnitario() != null	&& this.itemRequisicao.getQuantidade() != null) {
			this.itemRequisicao.setValorTotal(this.itemRequisicao.getValorUnitario()* this.itemRequisicao.getQuantidade());
		}
		return super.populate();
	}

	public String emitirAutorizacao() {
		String header = this.entity.getVeiculo().getModelo() == null ? "": this.entity.getVeiculo().getModelo().getDescricao() + " - " 
				+ this.entity.getVeiculo().getPlaca()+ "#"
				+ this.entity.getVeiculo().getUa().getDescricao()+ "#"
				+ this.entity.getOficina().getNome()+ "#"
				+ this.entity.getNomeMecanico()+ "#"
				+ DateUtil.parseAsString("dd/MM/yyyy", this.entity.getDataInicio())+ "#"
				+ DateUtil.parseAsString("dd/MM/yyyy", this.entity.getDataFim());
		String body = "";

		List<ItemRequisicao> currentList = this.itemService.findByIdRequisicao(this.entity.getId());

		for (int i = 0; i < currentList.size(); i++) {
			body += (i + 1) + ";"
			+ currentList.get(i).getTipoServico().getDescricao() + ";"
			+ currentList.get(i).getQuantidade() + ";"
			+ currentList.get(i).getValorUnitario() + ";"
			+ currentList.get(i).getValorTotal() + "#";
		}

		try {
			DownloadFileUtil.getAutorizacaoPDF(header, body,"autorizacao_manutencao", 1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public Boolean getValidDataInicio() {
		return validDataInicio;
	}

	public void setValidDataInicio(Boolean validDataInicio) {
		this.validDataInicio = validDataInicio;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Integer getSearchId() {
		return searchId;
	}

	public void setSearchId(Integer searchId) {
		this.searchId = searchId;
	}

	public ItemRequisicao getItemRequisicao() {
		return itemRequisicao;
	}

	public void setItemRequisicao(ItemRequisicao itemRequisicao) {
		this.itemRequisicao = itemRequisicao;
	}

	public List<ItemRequisicao> getItensRequisicao() {
		return itensRequisicao;
	}

	public void setItensRequisicao(List<ItemRequisicao> itensRequisicao) {
		this.itensRequisicao = itensRequisicao;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public boolean isNovoItem() {
		return novoItem;
	}

	public boolean isVeiculoStatus() {

		return this.veiculos.size() <= 0;
	}

	public void setNovoItem(boolean novoItem) {
		this.novoItem = novoItem;
	}

	public ItemRequisicao getItem2Remove() {
		return item2Remove;
	}

	public void setItem2Remove(ItemRequisicao item2Remove) {
		this.item2Remove = item2Remove;
	}

	public void setVeiculoService(VeiculoService veiculoService) {
		this.veiculoService = veiculoService;
	}

	public VeiculoService getVeiculoService() {
		return veiculoService;
	}

	public void setServicos(List<TipoServico> servicos) {
		this.servicos = servicos;
	}

	public List<TipoServico> getServicos() {
		return servicos;
	}

	public void setRequisicao(RequisicaoManutencao requisicao) {
		this.requisicao = requisicao;
	}

	public RequisicaoManutencao getRequisicao() {

		return requisicao;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public UG getOrgao() {
		return orgao;
	}

	public void setOrgao(UG orgao) {
		this.orgao = orgao;
	}

	public void prepareItensRequisicao() {
		this.itensRequisicao = new ArrayList<ItemRequisicao>();
		this.itensRequisicao = itemService.findByIdRequisicao(this.requisicao.getId());
	}
}
