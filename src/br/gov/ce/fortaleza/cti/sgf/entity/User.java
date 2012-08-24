	/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

/**
 * @author Deivid
 * @since 03/11/2009	
 */

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="TB_PESSOAUSUARIO" , schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "User.findByLogin", query = "select o from User o where o.login = ? and (o.status != ? and o.status != ?)"),
	@NamedQuery(name = "User.findByStatus", query = "select o from User o where o.status = ? or o.status = ?"),
	@NamedQuery(name = "User.findByLoginAndStatus", query = "select o from User o where o.login = ? and o.status = ?"),
	@NamedQuery(name = "User.findByUGAndLogin",   query = "select o from User o where o.pessoa.ua.ug.id = ? and o.login = ? and (o.status = ? or o.status = ?)"),
	@NamedQuery(name = "User.findByCpf", 	query = "select u from User u where u.pessoa.cpf = ?"),
	@NamedQuery(name = "User.findByUGAndCpf", 	query = "select u from User u where u.pessoa.ua.ug.id = ? and u.pessoa.cpf = ?"),
	@NamedQuery(name = "User.findByUA",     query = "select u from User u where u.pessoa.ua.id = ?"),
	@NamedQuery(name = "User.findByName", query = "select u from User u where u.pessoa.nome LIKE ? and (u.status = ? or u.status = ?)"),
	@NamedQuery(name = "User.findByUG",   query = "select u from User u where u.pessoa.ua.ug.id = ? and (u.status = ? or u.status = ?)"),
	@NamedQuery(name = "User.findByUGAndName", query="select u from User u where u.pessoa.ua.ug.id = ? and u.pessoa.nome LIKE ? and (u.status = ? or u.status = ?)")
})
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	@SequenceGenerator(name="seq_usuario", sequenceName="sgf.codpessoausuario_seq", allocationSize = 1)
	@Column(name="CODPESSOAUSUARIO", nullable = false)
	private Integer codPessoaUsuario;

	@OneToOne
	@JoinColumn(name="CODPESSOA", nullable = false)
	private Pessoa pessoa;

	@OneToOne
	@JoinColumn(name = "CODPOSTO")
	private Posto posto;

	@Column(name="LOGIN", nullable = false, length = 30)
	private String login;

	@Column(name="SENHA", nullable = false, length = 230)
	private String senha;

	@Column(name="ATIVO")
	private String status;

	@Type(type="boolean")
	@Column(name="autoriza")
	private Boolean autoriza;

	@Type(type="boolean")
	@Column(name="logado")
	private Boolean logado;

	@ManyToOne(fetch=FetchType.EAGER,  cascade=CascadeType.MERGE)
	@JoinTable(name="TB_GRUPOUSUARIO", schema = "SGF", joinColumns=@JoinColumn(name="CODPESSOAUSUARIO"), 
													   inverseJoinColumns=@JoinColumn(name="CODGRUPO"), 
													   uniqueConstraints = {@UniqueConstraint(columnNames = {"CODPESSOAUSUARIO", "CODGRUPO"})})
	private Role role;

	//	@OneToMany(mappedBy="solicitante", fetch=FetchType.LAZY)	
	//	private Set<SolicitacaoVeiculo> solicitacaoVeiculos;

	@Transient
	private GrantedAuthority[] authorities;

	@Transient
	@Override
	public GrantedAuthority[] getAuthorities() {
		if(authorities == null){
			authorities = new Role[1];
		}
		GrantedAuthority authority = role;
		authorities[0] = authority;
		return authorities;
	}

	@Transient
	private Set<String> permissoes;

	@Transient
	public Set<String> getPermissoes(){
		return this.permissoes;
	}

	@Transient
	public void setPermissoes(Set<String> permissoes){
		this.permissoes = permissoes;
	}

	@Transient
	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = authorities;
	}

	@Transient
	@Override
	public String getPassword() {
		return senha;
	}

	@Transient
	@Override
	public String getUsername() {
		return login;
	}

	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isEnabled() {
		return true;
	}

	public Integer getCodPessoaUsuario() {
		return codPessoaUsuario;
	}

	public void setCodPessoaUsuario(Integer codPessoaUsuario) {
		this.codPessoaUsuario = codPessoaUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setPosto(Posto posto) {
		this.posto = posto;
	}

	public Posto getPosto() {
		return posto;
	}

	public void setAutoriza(Boolean autoriza) {
		this.autoriza = autoriza;
	}

	public Boolean getAutoriza() {
		return autoriza;
	}

	//	public void setSolicitacaoVeiculos(Set<SolicitacaoVeiculo> solicitacaoVeiculos) {
	//		this.solicitacaoVeiculos = solicitacaoVeiculos;
	//	}
	//
	//	public Set<SolicitacaoVeiculo> getSolicitacaoVeiculos() {
	//		return solicitacaoVeiculos;
	//	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Boolean getLogado() {
		return logado;
	}

	public void setLogado(Boolean logado) {
		this.logado = logado;
	}

	public int hashCode() {

		int result = 1;
		result = 31 * result + ((codPessoaUsuario == null) ? 0 : codPessoaUsuario.hashCode());
		result = 31 * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		User other = (User) obj;

		return ((codPessoaUsuario == null && other.codPessoaUsuario == null) || (codPessoaUsuario != null && codPessoaUsuario.equals(other.codPessoaUsuario))) &&
		((login == null && other.login == null) || (login != null && login.equals(other.login)));
	}
}
