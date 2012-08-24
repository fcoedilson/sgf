package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Permissao;
import br.gov.ce.fortaleza.cti.sgf.entity.Role;
import br.gov.ce.fortaleza.cti.sgf.service.PermissaoService;
import br.gov.ce.fortaleza.cti.sgf.service.RoleService;

@Scope("session")
@Component("permissaoBean")
public class PermissaoBean extends EntityBean<Integer, Permissao> {

	private List<Role> roles = new ArrayList<Role>();

	private Role role;

	@Autowired
	private PermissaoService service = new PermissaoService();

	@Autowired
	private RoleService roleService = new RoleService();

	@Override
	public String search() {
		this.roles = roleService.retrieveAll();
		return super.searchSort();
	}

	public String save(){
		super.save();
		return searchSort();
	}

	public String update(){
		super.update();
		return searchSort();
	}

	protected Integer retrieveEntityId(Permissao entity) {
		return entity.getCodPermissao();
	}

	protected PermissaoService retrieveEntityService() {
		return this.service;
	}

	protected Permissao createNewEntity() {
		return new Permissao();
	}

	public List<Permissao> getPermissoes(){
		List<Permissao> result = new ArrayList<Permissao>(service.retrieveAll());
		return result;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}
}