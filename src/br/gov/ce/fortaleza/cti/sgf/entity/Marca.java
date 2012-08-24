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
@Table(name = "TB_MARCA", schema = "SGF")
public class Marca implements Serializable {

	private static final long serialVersionUID = 1031161986293985845L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_marca")
	@SequenceGenerator(name="seq_marca", sequenceName="sgf.codmarca_seq", allocationSize = 1)
	@Column(name="CODMARCA", nullable = false)
	private Integer id;

	@Column(name="DESCMARCA", nullable = false, length = 50)
	private String descricao;

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

		Marca other = (Marca) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
			((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}

}
