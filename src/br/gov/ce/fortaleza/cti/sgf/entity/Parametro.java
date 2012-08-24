package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_PARAMETRO", schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "Parametro.findByTipo", query = "select p from Parametro p where p.tipo = ?"),
	@NamedQuery(name = "Parametro.findByNome", query = "select p from Parametro p where p.nome = ?")
})
public class Parametro implements Serializable{
	
	private static final long serialVersionUID = 1659151748186365069L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID_PARAMETRO", nullable = false)
	private Integer id;
	
	@Column(name="NOME")
	private String nome;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Column(name="VALOR")
	private String valor;
	
	@Column(name="TIPO")
	private String tipo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int hashCode() {

		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		result = 31 * result + ((descricao == null) ? 0 : descricao.hashCode());

		return result;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Parametro other = (Parametro) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
		((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}

}
