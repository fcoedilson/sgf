/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Page;
import br.gov.ce.fortaleza.cti.sgf.entity.Permissao;
import br.gov.ce.fortaleza.cti.sgf.entity.Role;
import br.gov.ce.fortaleza.cti.sgf.service.PageService;
import br.gov.ce.fortaleza.cti.sgf.service.PermissaoService;
import br.gov.ce.fortaleza.cti.sgf.service.RoleService;

@Scope("session")
@Component("roleBean")
public class RoleBean extends EntityBean<Integer, Role>{


	@Autowired
	private RoleService service;

	@Autowired
	private PermissaoService permissaoService;

	@Autowired
	private PageService pageService;

	private Permissao permissao = new Permissao();
	private List<Permissao> permissoes;
	private Page page = new Page();
	private List<Page> pages;
	private List<Page> rolePages;

	@Override
	protected Role createNewEntity() {
		this.permissoes = new ArrayList<Permissao>();
		this.pages = new ArrayList<Page>();
		this.permissoes.addAll(permissaoService.retrieveAll());
		this.pages.addAll(pageService.retrieveAll());
		this.permissao = new Permissao();
		this.page = new Page();
		return new Role();
	}

	@Override
	protected Integer retrieveEntityId(Role entity) {
		return entity.getCodGrupo();
	}

	@Override
	protected RoleService retrieveEntityService() {
		return this.service;
	}

	@Override
	public String prepareEdit() {
		this.permissao = new Permissao();
		this.page = new Page();
		this.entity = service.retrieve(this.entity.getCodGrupo());
		this.permissoes = permissaoService.retrieveAll();
		this.permissoes.removeAll(this.entity.getPermissoes());
		this.pages = pageService.retrieveAll();
		this.pages.removeAll(this.entity.getPages());
		Collections.sort(this.permissoes, new Comparator<Permissao>() {
			public int compare(Permissao o1, Permissao o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		this.rolePages = new ArrayList<Page>(this.entity.getPages());
		setCurrentBean(currentBeanName());
		setCurrentState(EDIT);
		return SUCCESS;
	}

	public String update(){
		this.entity.setPages(new HashSet<Page>(this.rolePages));
		return super.update();
	}

	public boolean isPermissaoStatus(){
		return this.permissoes.size() > 0;
	}

	public boolean isPageStatus(){
		return this.pages.size() > 0;
	}

	@Transactional(readOnly=true)
	public List<Role> getRoles(){
		return  new ArrayList<Role>(service.findAll());
	}

	public String add(){
		this.entity.getPermissoes().add(this.permissao);
		service.update(entity);
		this.permissoes.remove(this.permissao);
		return SUCCESS;
	}

	public String addPage(){
		this.rolePages.add(this.page);
		service.update(entity);
		this.pages.remove(this.page);
		return SUCCESS;
	}

	public String remove(){
		this.entity.getPermissoes().remove(this.permissao);
		this.permissoes.add(this.permissao);
		return SUCCESS;
	}

	public String removePage(){
		this.rolePages.remove(this.page);
		this.pages.add(this.page);
		return SUCCESS;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public List<Page> getRolePages() {
		return rolePages;
	}

	public void setRolePages(List<Page> rolePages) {
		this.rolePages = rolePages;
	}
}
