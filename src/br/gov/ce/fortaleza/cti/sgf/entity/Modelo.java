package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_MODELO", schema = "SGF")
@NamedQuery(name="Modelo.findByDesc",query="SELECT m FROM Modelo m WHERE m.descricao LIKE :descricao")
public class Modelo implements Serializable{

	private static final long serialVersionUID = 6536095417453972946L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_modelo")
	@SequenceGenerator(name="seq_modelo", sequenceName="sgf.codmodelo_seq", allocationSize = 1)
	@Column(name="CODMODELO", nullable = false)
	private Integer id;

	@Column(name="DESCMODELO", nullable = false, length = 120)
	private String descricao;

	@ManyToOne
	@JoinColumn(name="CODMARCA")
	private Marca marca;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Marca getMarca() {
		return this.marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
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

		Modelo other = (Modelo) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
		((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}

}
