package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "tb_permissao", schema = "SGF")
public class Permissao implements Serializable {

	private static final long serialVersionUID = -1460863496492190140L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_permissao")
	@SequenceGenerator(name = "seq_permissao", sequenceName = "sgf.codpermissao_seq", allocationSize = 1)
	@Column(name="CODPERMISSAO", nullable = false)
	private Integer codPermissao;

	@Column(name="DESCPERMISSAO", nullable = false, length = 250)
	private String descricao;

	@Column(name="NOMEPERMISSAO", nullable = false, length = 150)
	private String nome;

	public Integer getCodPermissao() {
		return codPermissao;
	}

	public void setCodPermissao(Integer codPermissao) {
		this.codPermissao = codPermissao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + ((codPermissao == null) ? 0 : codPermissao.hashCode());
		result = 31 * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Permissao other = (Permissao) obj;

		return ((codPermissao == null && other.codPermissao == null) || (codPermissao != null && codPermissao.equals(other.codPermissao))  &&
				(nome == null && other.nome == null) || (nome != null && nome.equals(other.nome)));
	}
}