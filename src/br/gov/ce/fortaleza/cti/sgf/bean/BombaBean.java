/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.gov.ce.fortaleza.cti.sgf.entity.Bomba;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoCombustivel;
import br.gov.ce.fortaleza.cti.sgf.service.BombaService;
import br.gov.ce.fortaleza.cti.sgf.service.PostoService;
import br.gov.ce.fortaleza.cti.sgf.service.TipoCombustivelService;

/**
 * @author Deivid
 * @since 11/12/09
 */
@Scope("session")
@Component("bombaBean")
public class BombaBean extends EntityBean<Integer, Bomba>{

	@Autowired
	private BombaService bombaService;
	
	@Autowired
	private PostoService postoService;
	
	@Autowired
	private TipoCombustivelService combustivelService;
	
	private String numeroPesquisa;
	private Posto posto;
	private TipoCombustivel combustivel;
		
	private Posto postoSelecionado;
	private TipoCombustivel combustivelSelecionado;
	
	@Override
	public String prepareUpdate() {
		return super.prepareUpdate();
	}
	
	@Override
	public String prepareSave() {
		return super.prepareSave();
	}
	
	@Override
	public String save() {
		boolean existeTipoCombustivel = bombaService.findPostoCombustivel(this.entity);  
		if(!existeTipoCombustivel){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("posto", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Este posto n�o oferece esse tipo de combustivel."));
			return FAIL;
		}
		boolean existe = bombaService.findBombaPosto(this.entity);
		if(existe){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("numero", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Bomba j� cadastrada neste posto."));
			return FAIL;
		}
		return super.save();
	}
	
	@Override
	public String search() {
		Bomba bomba = new Bomba();
		if(StringUtils.hasText(numeroPesquisa)){
			bomba.setNumero(Integer.valueOf(numeroPesquisa));
		}
		bomba.setPosto(posto);
		bomba.setCombustivel(combustivel);
		this.entities = bombaService.pesquisar(bomba);
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
	
	@Override
	protected Bomba createNewEntity() {
		Bomba bomba = new Bomba();
		return new Bomba();
	}

	@Override
	protected Integer retrieveEntityId(Bomba entity) {
		return entity.getId();
	}

	@Override
	protected BombaService retrieveEntityService() {
		return this.bombaService;
	}
	

	public Posto getPostoSelecionado() {
		return postoSelecionado;
	}

	public void setPostoSelecionado(Posto postoSelecionado) {
		this.postoSelecionado = postoSelecionado;
	}

	public TipoCombustivel getCombustivelSelecionado() {
		return combustivelSelecionado;
	}

	public void setCombustivelSelecionado(TipoCombustivel combustivelSelecionado) {
		this.combustivelSelecionado = combustivelSelecionado;
	}

	public BombaService getBombaService() {
		return bombaService;
	}

	public void setBombaService(BombaService bombaService) {
		this.bombaService = bombaService;
	}

	public PostoService getPostoService() {
		return postoService;
	}

	public void setPostoService(PostoService postoService) {
		this.postoService = postoService;
	}

	public TipoCombustivelService getCombustivelService() {
		return combustivelService;
	}

	public void setCombustivelService(TipoCombustivelService combustivelService) {
		this.combustivelService = combustivelService;
	}

	public String getNumeroPesquisa() {
		return numeroPesquisa;
	}

	public void setNumeroPesquisa(String numeroPesquisa) {
		this.numeroPesquisa = numeroPesquisa;
	}

	public Posto getPosto() {
		return posto;
	}

	public void setPosto(Posto posto) {
		this.posto = posto;
	}

	public TipoCombustivel getCombustivel() {
		return combustivel;
	}

	public void setCombustivel(TipoCombustivel combustivel) {
		this.combustivel = combustivel;
	}
}
