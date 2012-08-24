package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.NonUniqueResultException;

import org.ajax4jsf.component.html.HtmlAjaxOutputPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Pessoa;
import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.service.MotoristaService;
import br.gov.ce.fortaleza.cti.sgf.service.PessoaService;
import br.gov.ce.fortaleza.cti.sgf.service.UAService;
import br.gov.ce.fortaleza.cti.sgf.service.UsuarioService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

/**
 * @author Deivid
 * 
 */

@Scope("session")
@Component("pessoaBean")
public class PessoaBean extends EntityBean<Integer, Pessoa>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private PessoaService service;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private MotoristaService motoristaService;

	@Autowired
	private UAService uaService;

	private UG ug;
	private UA ua;
	private List<UA> uas;
	private Boolean pessoaCadastrada;
	private Boolean pessoaNaoCadastrada;
	private Boolean usuarioCadastrado;
	private String cpf;

	//Componentes da Tela
	private HtmlAjaxOutputPanel ajaxPanelPessoa;

	@Override
	protected Pessoa createNewEntity() {
		return new Pessoa();
	}

	@Override
	protected Integer retrieveEntityId(Pessoa entity) {
		return entity.getCodPessoa();
	}

	@Override
	protected PessoaService retrieveEntityService() {
		return this.service;
	}

	public String search(){
		this.entities = new ArrayList<Pessoa>();
		User user = SgfUtil.usuarioLogado();

		if(this.cpf != "" && this.cpf != null){
			Pessoa pessoa = null;
			if(SgfUtil.isAdministrador(user)){
				pessoa = service.findByCPF(this.cpf, null);
			} else {
				pessoa = service.findByCPF(this.cpf, this.ug);
			}
			if(pessoa != null){
				this.entities.add(pessoa);
			}
		} else if(SgfUtil.isAdministrador(user)){
			this.entities = service.retrieveAll();
		} else if(this.ug != null){
			this.entities = service.findByUG(user.getPessoa().getUa().getUg().getId());
		}
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	public String prepareUpdate(){
		if(this.entity.getUa() != null){
			this.ug = this.entity.getUa().getUg();
			this.uas = uaService.retrieveByUG(ug.getId());
			this.ua = this.entity.getUa();
		}
		return super.prepareUpdate();
	}

	public String prepareSave(){
		this.entity = new Pessoa();
		return super.prepareSave();
	}

	public String update(){
		if(!SgfUtil.cpfValidaDV(this.entity.getCpf())){
			JSFUtil.getInstance().addErrorMessage("msg.error.cpf.digito.verificador.invalido");
			return FAIL;
		}
		retrieveEntityService().update(this.entity);
		setCurrentBean(currentBeanName());
		this.entity.setCpf(null);
		return search();
	}

	public String save(){
		try{
			this.entity.setStatus("true");
			if(this.entity.getCpf() != null){
				if(!SgfUtil.cpfValidaDV(this.entity.getCpf())){
					JSFUtil.getInstance().addErrorMessage("msg.error.cpf.digito.verificador.invalido");
					return FAIL;
				}
			} else {
				return FAIL;
			}
			if(this.entity.getCpf() != null){
				if(service.findPessoaByCpf(this.entity.getCpf())){
					JSFUtil.getInstance().addErrorMessage("msg.error.cpf.duplicado");
					return FAIL;
				} else {
					super.save();
					return search(); 
				}
			} else {
				return FAIL;
			}
		}catch (NonUniqueResultException e) {
			JSFUtil.getInstance().addErrorMessage("msg.error.cpf.duplicado");
			return FAIL;
		}
	}

	/**
	 * Busca por CPF simplificada
	 * @return
	 */
	public String searchByCPF(){
		this.entities = new ArrayList<Pessoa>();
		Pessoa p = service.findByCPF(this.entity.getCpf(), this.ug);
		if(p != null){
			this.entities.add(p);
		}
		return SUCCESS;
	}

	public void searchByCpf() {
		try{
			this.entity = service.findByCPF(this.entity.getCpf(), null);
			if(this.entity != null){
				this.pessoaCadastrada = true;
				this.pessoaNaoCadastrada = false;
				if(SgfUtil.isAdministrador(SgfUtil.usuarioLogado())){
					this.usuarioCadastrado = service.findByCPF(this.entity.getCpf(), null) != null;
				} else {
					this.usuarioCadastrado = service.findByCPF(this.entity.getCpf(), 
							SgfUtil.usuarioLogado().getPessoa().getUa().getUg()) != null ;
				}
				if(this.usuarioCadastrado){
					JSFUtil.getInstance().addErrorMessage("msg.error.usuario.cadastrado");
				}else if(this.usuarioCadastrado == null){
					JSFUtil.getInstance().addErrorMessage("msg.error.cpf.inexistente");
				}
			}else{
				JSFUtil.getInstance().addErrorMessage("msg.error.cpf.inexistente");
				pessoaCadastrada = false;
				pessoaNaoCadastrada = true;
			}
		}catch (NonUniqueResultException e) {
			JSFUtil.getInstance().addErrorMessage("msg.error.cpf.duplicado");
		}
	}

	public void searchMotoristaByCpf(){
		this.entity = null;
		try{
			this.entity = service.findByCPF(this.entity.getCpf(), this.ug);
			if(this.entity != null){
				this.pessoaCadastrada = true;
				this.pessoaNaoCadastrada = false;
				this.usuarioCadastrado = service.findPessoaByCpf(entity.getCpf());
				if(this.usuarioCadastrado){
					JSFUtil.getInstance().addErrorMessage("msg.error.motorista.cadastrado");
				}else if(this.usuarioCadastrado == null){
					JSFUtil.getInstance().addErrorMessage("msg.error.cpf.inexistente");
				}
			} else {
				JSFUtil.getInstance().addErrorMessage("msg.error.cpf.inexistente");
				this.pessoaCadastrada = false;
				this.pessoaNaoCadastrada = true;
			}
		}catch (NonUniqueResultException e) {
			JSFUtil.getInstance().addErrorMessage("msg.error.cpf.duplicado");
		}
	}

	public String loadUas(){
		if(this.ug != null){
			this.uas = uaService.retrieveByUG(this.ug.getId());
		} else {
			this.uas = new ArrayList<UA>();
		}
		return SUCCESS;
	}

	public void limpar(){
		this.entity = null;
	}

	public Boolean getPessoaCadastrada() {
		return pessoaCadastrada;
	}

	public void setPessoaCadastrada(Boolean pessoaCadastrada) {
		this.pessoaCadastrada = pessoaCadastrada;
	}

	public Boolean getPessoaNaoCadastrada() {
		return pessoaNaoCadastrada;
	}

	public void setPessoaNaoCadastrada(Boolean pessoaNaoCadastrada) {
		this.pessoaNaoCadastrada = pessoaNaoCadastrada;
	}

	public HtmlAjaxOutputPanel getAjaxPanelPessoa() {
		return ajaxPanelPessoa;
	}

	public void setAjaxPanelPessoa(HtmlAjaxOutputPanel ajaxPanelPessoa) {
		this.ajaxPanelPessoa = ajaxPanelPessoa;
	}

	public void setUsuarioCadastrado(Boolean usuarioCadastrado) {
		this.usuarioCadastrado = usuarioCadastrado;
	}

	public Boolean getUsuarioCadastrado() {
		return usuarioCadastrado;
	}

	public UG getUg() {
		return ug;
	}

	public void setUg(UG ug) {
		this.ug = ug;
	}

	public void setUas(List<UA> uas) {
		this.uas = uas;
	}

	public List<UA> getUas(){
		return this.uas;
	}

	public UA getUa() {
		return ua;
	}

	public void setUa(UA ua) {
		this.ua = ua;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}
