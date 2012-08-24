package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.persistence.Transient;

import org.ajax4jsf.component.html.HtmlAjaxCommandLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.gov.ce.fortaleza.cti.sgf.entity.Cota;
import br.gov.ce.fortaleza.cti.sgf.entity.Marca;
import br.gov.ce.fortaleza.cti.sgf.entity.Modelo;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.PostoServico;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.CotaService;
import br.gov.ce.fortaleza.cti.sgf.service.PostoServicoVeiculoService;
import br.gov.ce.fortaleza.cti.sgf.service.TipoServicoService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.StatusVeiculo;

/**
 * @author Deivid
 * @since 23/11/09
 */
@Scope("session")
@Component("cotaBean")
public class CotaBean extends EntityBean<Integer, Cota>{

	private static final long serialVersionUID = -30259196714728876L;

	@Autowired
	private CotaService cotaService;

	@Autowired
	private VeiculoService veiculoService;

	@Autowired
	private PostoServicoVeiculoService postoServicoVeiculoService;

	@Autowired
	private TipoServicoService servicoService;

	private List<Veiculo> veiculos = new ArrayList<Veiculo>();
	private Veiculo veiculo = new Veiculo();
	private List<PostoServico> listaPostoServicos;
	private List<TipoServico> servicos = new ArrayList<TipoServico>();
	private String placa;
	private Integer cota;

	@Transient
	private List<Cota> cotas;
	private List<Posto> postosVeiculo;

	private HtmlAjaxCommandLink botaoExcluir;
	private UISelectItems selectItemsUI;
	private List<TipoServico> servicosVeiculo;

	private String veiculoPesquisa;
	private String placaPesquisa;
	private String marcaPesquisa;
	private Double cotaAtual;

	@Override
	protected Cota createNewEntity() {
		Cota cota = new Cota();
		cota.setVeiculo(new Veiculo());
		return cota;
	}

	@Override
	protected Integer retrieveEntityId(Cota entity) {
		return entity.getId();
	}

	@Override
	protected CotaService retrieveEntityService() {
		return this.cotaService;
	}

	public String populate(){
		return super.populate();
	}


	@Override
	public String search() {
		if(StringUtils.hasText(veiculoPesquisa) || StringUtils.hasText(placaPesquisa) || StringUtils.hasText(marcaPesquisa)){
			return pesquisar();
		}
		this.entities = new ArrayList<Cota>();
		this.entities.addAll(cotaService.findcotasAllVeiculoativos());
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
	
	@Override
	public String prepareSave() {
		this.veiculos = veiculoService.executeResultListQuery("findVeiculosSemCota");
		return super.prepareSave();
	}
	
	public String prepareUpdate(){
		this.cotaAtual = this.entity.getCota();
		return super.prepareUpdate();
	}


	public String update(){
		this.entity.setCotaDisponivel(this.entity.getCotaDisponivel() + this.entity.getCota() - this.cotaAtual);
		return super.update();
	}
	
	public String atualizarCotaVeiculo(){
		if(this.entity.getCota() < (this.cotaAtual - this.entity.getCotaDisponivel())){
			JSFUtil.getInstance().addErrorMessage("msg.error.cota.utrapassada");
			return FAIL;
		} else {
			this.entity.setCotaDisponivel(this.entity.getCotaDisponivel() - (this.cotaAtual - this.entity.getCota()));
		}
		return super.update();
	}

	public String pesquisar(){
		Cota cota = new Cota();
		if(StringUtils.hasText(this.veiculoPesquisa)){
			cota.setVeiculo(new Veiculo());
			cota.getVeiculo().setModelo(new Modelo());
			cota.getVeiculo().getModelo().setDescricao(this.veiculoPesquisa);
		}
		if(StringUtils.hasText(this.placaPesquisa)){
			if(cota.getVeiculo() != null){
				cota.getVeiculo().setPlaca(this.placaPesquisa);
			} else {
				cota.setVeiculo(new Veiculo());
				cota.getVeiculo().setPlaca(this.placaPesquisa);
			}
		}

		if(StringUtils.hasText(this.marcaPesquisa)){
			if(cota.getVeiculo() != null){
				if(cota.getVeiculo().getModelo() != null){
					cota.getVeiculo().getModelo().setMarca(new Marca());
					cota.getVeiculo().getModelo().getMarca().setDescricao(this.marcaPesquisa);
				}
			} else {
				cota.setVeiculo(new Veiculo());
				cota.getVeiculo().setModelo(new Modelo());
				cota.getVeiculo().getModelo().setMarca(new Marca());
				cota.getVeiculo().getModelo().getMarca().setDescricao(this.marcaPesquisa);
			}
		}
		this.entities = new ArrayList<Cota>();
		this.entities = cotaService.pesquisar(cota);
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	@Override
	@Transactional
	public String save() {
		TipoServico servico = servicoService.retrieve(1);
		this.entity.setTipoServico(servico);
		this.entity.setCotaDisponivel(this.entity.getCota());
		return super.save();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String delete(){
		if(this.entity.getVeiculo().getStatus() == StatusVeiculo.LOCADO){
			this.botaoExcluir.setOncomplete("alert('Não é permitido excluir cotas de veículo ativo!')");
			return FAIL;
		} else if(this.entity.getVeiculo().getStatus() == StatusVeiculo.DISPONIVEL){
			return super.delete();
		}
		return FAIL;
	}

	public String veiculoSemCotaPorPlaca(){
		if(this.placa != null && this.placa != ""){
			if(cotaService.findByPlacaVeiculo(this.placa) == null){
				this.veiculos = veiculoService.findByPlaca(this.placa);
			}
		} else {
			this.veiculos = new ArrayList<Veiculo>();
		}
		return SUCCESS;
	}

	public CotaService getCotaService() {
		return cotaService;
	}

	public void setCotaService(CotaService cotaService) {
		this.cotaService = cotaService;
	}

	public HtmlAjaxCommandLink getBotaoExcluir() {
		return botaoExcluir;
	}

	public void setBotaoExcluir(HtmlAjaxCommandLink botaoExcluir) {
		this.botaoExcluir = botaoExcluir;
	}

	public void setPostosVeiculo(List<Posto> postosVeiculo) {
		this.postosVeiculo = postosVeiculo;
	}

	public List<Posto> getPostosVeiculo() {
		return postosVeiculo;
	}

	public List<TipoServico> getServicosVeiculo() {
		return servicosVeiculo;
	}

	public void setServicosVeiculo(List<TipoServico> servicosVeiculo) {
		this.servicosVeiculo = servicosVeiculo;
	}

	public void setCotas(List<Cota> cotas) {
		this.cotas = cotas;
	}

	public List<Cota> getCotas() {
		return cotas;
	}

	public void setListaPostoServicos(List<PostoServico> listaPostoServicos) {
		this.listaPostoServicos = listaPostoServicos;
	}

	public List<PostoServico> getListaPostoServicos() {
		return listaPostoServicos;
	}

	public void setCota(Integer cota) {
		this.cota = cota;
	}

	public Integer getCota() {
		return cota;
	}

	public void setServicos(List<TipoServico> servicos) {
		this.servicos = servicos;
	}

	public List<TipoServico> getServicos() {
		return servicos;
	}

	public String getVeiculoPesquisa() {
		return veiculoPesquisa;
	}

	public void setVeiculoPesquisa(String veiculoPesquisa) {
		this.veiculoPesquisa = veiculoPesquisa;
	}

	public String getPlacaPesquisa() {
		return placaPesquisa;
	}

	public void setPlacaPesquisa(String placaPesquisa) {
		this.placaPesquisa = placaPesquisa;
	}

	public String getMarcaPesquisa() {
		return marcaPesquisa;
	}

	public void setMarcaPesquisa(String marcaPesquisa) {
		this.marcaPesquisa = marcaPesquisa;
	}

	public void setSelectItemsUI(UISelectItems selectItemsUI) {
		this.selectItemsUI = selectItemsUI;
	}

	public UISelectItems getSelectItemsUI() {
		return selectItemsUI;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
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
