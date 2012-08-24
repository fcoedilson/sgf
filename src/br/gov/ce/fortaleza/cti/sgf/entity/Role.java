/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.GrantedAuthority;

@Entity
@Table(name = "tb_grupo" , schema = "SGF")
@NamedQuery(name = "Role.findById", query = "select object(o) from Role o where o.codGrupo = ?")
public class Role implements Serializable, GrantedAuthority{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_grupo")
	@SequenceGenerator(name = "seq_grupo", sequenceName = "sgf.codgrupo_seq", allocationSize = 1)
	@Column(name="CODGRUPO", nullable = false)
	private Integer codGrupo;

	@Column(name="NOMEGRUPO", nullable = false)
	private String nome;

	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="tb_grupopermissao", schema = "SGF", joinColumns=@JoinColumn(name="codgrupo"), 
			inverseJoinColumns=@JoinColumn(name="codpermissao"))
	private List<Permissao> permissoes;

	@Column(name="AUTORIDADEGRUPO", nullable = true)
	private String authority;

	@OneToMany(mappedBy="role", fetch=FetchType.LAZY)
	@JoinTable(name="tb_grupousuario", schema = "SGF", joinColumns=@JoinColumn(name="codgrupo"), 
			inverseJoinColumns=@JoinColumn(name="codpessoausuario"))
	private Set<User> users;
	
	
	@ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name = "tb_grupopage", schema = "SGF", joinColumns=@JoinColumn(name="codgrupo"), 
			inverseJoinColumns=@JoinColumn(name="codpage"))
	private Set<Page> pages;

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Transient
	@Override
	public int compareTo(Object o) {
		if (o != null) {
			Role other = (Role) o;
			if (this.authority == null && other.authority != null){
				return -1;
			} else if (this.authority != null && other.authority == null) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		return getAuthority();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getCodGrupo() {
		return codGrupo;
	}

	public void setCodGrupo(Integer codGrupo) {
		this.codGrupo = codGrupo;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public Set<Page> getPages() {
		return pages;
	}

	public void setPages(Set<Page> pages) {
		this.pages = pages;
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + ((this.codGrupo == null) ? 0 : this.codGrupo.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return ((this.codGrupo == null && other.codGrupo == null) || (this.codGrupo != null && this.codGrupo.equals(other.codGrupo)));
	}
}
