package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_UA", schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "UA.findBy", query = "select object(v) from Veiculo v"),
	@NamedQuery(name = "UA.findByUG", query = "select ua from UA ua where ua.ug = ?"),
	@NamedQuery(name = "UA.findByID", query = "select ua from UA ua where ua.id = ?"),
	@NamedQuery(name = "UA.findSorted", query = "select ua from UA ua order by ua.descricao")
})
public class UA implements Serializable{

	private static final long serialVersionUID = 6655568880680196547L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ua")
	@SequenceGenerator(name="seq_ua", sequenceName="sgf.codua_seq", allocationSize = 1)
	@Column(name="COD_UA", nullable = false)
	private String id;

	@Column(name="NM_UA")
	private String descricao;

	@Column(name="FLAG")
	private Integer flag;
	
	@ManyToOne
	@JoinColumn(name="COD_UG")
	private UG ug;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
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

		UA other = (UA) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
		((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}

	public void setUg(UG ug) {
		this.ug = ug;
	}

	public UG getUg() {
		return ug;
	}
}
