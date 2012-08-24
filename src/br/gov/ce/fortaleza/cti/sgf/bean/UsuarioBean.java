/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.NonUniqueResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Pessoa;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.Role;
import br.gov.ce.fortaleza.cti.sgf.entity.UA;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.service.PessoaService;
import br.gov.ce.fortaleza.cti.sgf.service.RoleService;
import br.gov.ce.fortaleza.cti.sgf.service.UAService;
import br.gov.ce.fortaleza.cti.sgf.service.UsuarioService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

/**
 * @author Deivid
 * @since 01/11/2009
 */
@Scope("session")
@Component("usuarioBean")
public class UsuarioBean extends EntityBean<Integer, User>{

	//Componentes da Tela
	private HtmlCommandButton botaoGravar;

	@Autowired
	private UsuarioService service;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private UAService uaService;

	@Autowired
	private RoleService roleService;

	private List<UG> ugs = new ArrayList<UG>();
	private List<UA> uas = new ArrayList<UA>();

	private Boolean mostrarPosto;
	private Boolean mostrarUA;
	private Boolean searchName = false;
	private Boolean status = true;
	private Boolean pessoaCadastrada;
	private Boolean pessoaNaoCadastrada;
	private Boolean usuarioCadastrado;
	private String confirmaSenha;
	private String grupo;
	private String cpf;

	private Pessoa pessoa;
	private Role role;
	private UG ug = new UG();
	private UA ua = new UA();

	@Override
	protected User createNewEntity() {
		User user = new User();
		user.setPosto(new Posto());
		user.setRole(new Role());
		user.setPessoa(new Pessoa());
		user.setAutoriza(false);
		this.mostrarPosto = false;
		this.pessoaCadastrada = false;
		this.usuarioCadastrado = false;
		this.confirmaSenha = null;
		this.role = new Role();
		this.ua = new UA();
		this.ug = new UG();
		return user;
	}

	protected Integer retrieveEntityId(User entity) {
		return entity.getCodPessoaUsuario().intValue();
	}

	protected UsuarioService retrieveEntityService() {
		return this.service;
	}

	public String populate(){
		if(this.searchName == true){
			this.status = null;
			this.filter = "";
		}
		return super.populate();
	}
	
	public String buscarUsuario(){
		if(this.filter != null){
			this.entities = service.findByLogin(this.filter, this.status.toString());
		}
		
		return SUCCESS;
	}

	public String prepareUpdate() {

		if(this.entity.getPessoa().getUa() != null){
			this.ua = this.entity.getPessoa().getUa();
			this.ug = this.ua.getUg();
			this.uas = uaService.retrieveByUG(this.ug.getId()); //new ArrayList<UA>(entity.getPessoa().getUa().getUg().getUas());
		} else {
			this.ua = null;
			this.ug = null;
		}
		this.mostrarUA = true;
		if(this.entity.getRole() != null){
			if(this.entity.getRole().getCodGrupo() == 5){
				this.mostrarPosto = true;
			} else {
				this.mostrarPosto = false;
			}
		} else {
			this.mostrarPosto = false;
		}
		this.confirmaSenha = this.entity.getSenha();
		setCurrentBean(currentBeanName());
		setCurrentState(UPDATE);
		return SUCCESS;
	}

	/**
	 * O status do usuário é a string TRUE ou FALSE
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String save() {
		// se login já existe, o retorno será TRUE
		Boolean hasLoginUser = service.loginExistente(this.entity.getLogin(), null);
		if(validar()){
			this.entity.setStatus("TRUE");
			this.entity.getPessoa().setUa(this.ua);
			this.entity.setSenha(SgfUtil.md5(this.entity.getSenha()));
			if(hasLoginUser){
				this.entity.setSenha(null);
				this.confirmaSenha = null;
				JSFUtil.getInstance().addErrorMessage("msg.error.login.existente");
				return FAIL;
			} else {
				this.pessoaService.update(this.entity.getPessoa());
				return super.save();
			}
		} else {
			return FAIL;
		}
	}

	@Transactional
	public String update() {
		boolean hasLoginUser = service.loginExistente(this.entity.getLogin(), this.entity.getCodPessoaUsuario());
		if(hasLoginUser){
			this.entity.setSenha(null);
			this.confirmaSenha = null;
			JSFUtil.getInstance().addErrorMessage("msg.error.login.existente");
			return FAIL;
		} else {
			if(validar()){
				this.entity.getPessoa().setUa(this.ua);
				if(!this.entity.getRole().getAuthority().equals("ROLE_OPERADOR")){
					this.entity.setPosto(null);
				}
				this.entity.setSenha(SgfUtil.md5(this.entity.getSenha()));
				pessoaService.update(this.entity.getPessoa());
				service.update(this.entity);
			} else {
				return FAIL;
			}
		}
		return search();
	}

	@Override
	public String search() {
		User user = SgfUtil.usuarioLogado();
		this.entities = new ArrayList<User>();
		if(SgfUtil.isAdministrador(user) ||  SgfUtil.isCoordenador(user)){
			this.entities = service.findByStatus("TRUE");
		} else {
			UA ua = SgfUtil.usuarioLogado().getPessoa().getUa();
			if(ua != null){
				this.entities.add(service.retrieve(user.getCodPessoaUsuario()));
			}
		}
		Collections.sort(this.entities, new Comparator<User>() {
			public int compare(User o1, User o2) {
				return o1.getPessoa().getNome().compareTo(o2.getPessoa().getNome());
			}
		});
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	public String searchUsuarios(){
		this.entities = new ArrayList<User>();
		User user = SgfUtil.usuarioLogado();
		if(SgfUtil.isAdministrador(user) || SgfUtil.isCoordenador(user)){
			String str = this.status == true ? "TRUE" : "FALSE";
			this.entities = service.findByUGAndLogin(null, this.filter, str);
		} else {
			UA ua = user.getPessoa().getUa();
			if(ua != null){
				String str = this.status == true ? "TRUE" : "FALSE";
				this.entities = service.findByUGAndLogin(ua.getUg().getId(), this.filter, str);
			}
		}
		Collections.sort(this.entities, new Comparator<User>() {
			public int compare(User o1, User o2) {
				return o1.getPessoa().getNome().compareTo(o2.getPessoa().getNome());
			}
		});
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	/**
	 * 
	 * Pesquisa uma pessoa pelo seu CPF
	 * @return
	 */
	public String searchByCpf(){
		try{
			Pessoa pessoa;
			User user = SgfUtil.usuarioLogado();
			if(SgfUtil.isAdministrador(user)){
				pessoa = pessoaService.findByCPF(this.cpf, null);
			} else {
				pessoa = pessoaService.findByCPF(this.cpf, user.getPessoa().getUa().getUg());
			}
			if(pessoa != null){
				this.pessoaCadastrada = true;
				this.pessoaNaoCadastrada = false;

				this.entity.setPessoa(pessoa);
				this.ua = pessoa.getUa();
				this.mostrarUA = false;
				if(this.ua != null){
					this.ug = this.ua.getUg();
					this.uas = uaService.retrieveByUG(this.ug.getId());
					this.mostrarUA = true;
				}

				if(SgfUtil.isAdministrador(user)){
					this.usuarioCadastrado = service.findUserByCpf(pessoa.getCpf(), null);
				} else {
					this.usuarioCadastrado = service.findUserByCpf(pessoa.getCpf(), user.getPessoa().getUa().getUg());
				}

				if(this.usuarioCadastrado){
					JSFUtil.getInstance().addErrorMessage("msg.error.usuario.cadastrado");
				} else if(usuarioCadastrado == null){
					JSFUtil.getInstance().addErrorMessage("msg.error.cpf.inexistente");
				}
			} else {
				JSFUtil.getInstance().addErrorMessage("msg.error.cpf.inexistente");
				this.pessoaCadastrada = false;
				this.pessoaNaoCadastrada = true;
			}
		} catch (NonUniqueResultException e) {
			JSFUtil.getInstance().addErrorMessage("msg.error.cpf.duplicado");
		}
		return SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String selecionarGrupoUsuario(){
		this.entity.setRole(roleService.retrieve(this.entity.getRole().getCodGrupo()));
		if (this.entity.getRole().getCodGrupo() == 5) {
			this.mostrarPosto = true;
		} else {
			this.entity.setPosto(null);
			this.mostrarPosto = false;
		}
		return SUCCESS;
	}

	public List<SelectItem> getUserList(){
		List<SelectItem> result = new ArrayList<SelectItem>();
		result.add(new SelectItem(null, "Selecione"));
		List<User> users = service.retrieveAll();
		for (User user : users) {
			result.add(new SelectItem(user.getCodPessoaUsuario(), user.getPessoa().getNome()));
		}
		return result;
	}

	public List<User> getUsuarios(){
		UA ua = SgfUtil.usuarioLogado().getPessoa().getUa();
		List<User> usuarios = new ArrayList<User>();
		if(ua != null){
			usuarios = service.findByUA(ua.getId());
		}
		return usuarios;
	}

	/**
	 * 
	 * @return
	 */
	public String loadUas(){
		this.uas = new ArrayList<UA>();
		this.mostrarUA = true;
		this.uas = uaService.retrieveByUG(this.ug.getId());
		Collections.sort(uas, new Comparator<UA>() {
			public int compare(UA o1, UA o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		return SUCCESS;
	}

	/**
	 * 
	 * valida a confirmação de senha do usuário
	 * @return
	 */
	public Boolean validar() {
		if(!this.entity.getSenha().equals(this.confirmaSenha)){
			FacesContext.getCurrentInstance().addMessage("confirmaSenha", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "msg.error.senha.nao.conferem"));
			return false;
		}
		return true;
	}

	public void bloquear(){
		service.bloquear(this.entity.getCodPessoaUsuario());
		search();
	}

	public void desbloquear(){
		service.desbloquear(this.entity.getCodPessoaUsuario());
		search();
	}

	public HtmlCommandButton getBotaoGravar() {
		return botaoGravar;
	}

	public void setBotaoGravar(HtmlCommandButton botaoGravar) {
		this.botaoGravar = botaoGravar;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getGrupo() {
		return grupo;
	}

	public List<UG> getUgs() {
		return ugs;
	}

	public void setUgs(List<UG> ugs) {
		this.ugs = ugs;
	}

	public List<UA> getUas() {
		return uas;
	}

	public void setUas(List<UA> uas) {
		this.uas = uas;
	}

	public Boolean getSearchName() {
		return searchName;
	}

	public void setSearchName(Boolean searchName) {
		this.searchName = searchName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public UG getUg() {
		return ug;
	}

	public void setUg(UG ug) {
		this.ug = ug;
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

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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

	public Boolean getUsuarioCadastrado() {
		return usuarioCadastrado;
	}

	public void setUsuarioCadastrado(Boolean usuarioCadastrado) {
		this.usuarioCadastrado = usuarioCadastrado;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Boolean getMostrarPosto() {
		return mostrarPosto;
	}

	public void setMostrarPosto(Boolean mostrarPosto) {
		this.mostrarPosto = mostrarPosto;
	}

	public Boolean getMostrarUA() {
		return mostrarUA;
	}

	public void setMostrarUA(Boolean mostrarUA) {
		this.mostrarUA = mostrarUA;
	}
}
