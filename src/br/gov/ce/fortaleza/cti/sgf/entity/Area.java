package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.postgis.Polygon;


@Entity
@Table(name = "TB_AREA", schema = "SGF")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Area implements Serializable{

	private static final long serialVersionUID = 1182756761912025569L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CODAREA", nullable = false)
	private Integer id;

	@Column(name="DESCAREA", length =255)
	private String descricao;


//	@Type(type="org.hibernatespatial.GeometryUserType")
	@Type(type="br.gov.ce.fortaleza.cti.sgf.conversores.GeometryType")
	@Column(name = "GEOMAREA", nullable = false)
	private Polygon geometry;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "TB_VEICULO_AREA", schema = "SGF", joinColumns=@JoinColumn(name="CODAREA"), inverseJoinColumns=@JoinColumn(name="CODVEICULO"))
	private List<Veiculo> veiculos;
	
	private transient Integer numeroVeiculos;

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

	public Polygon getGeometry() {
		return geometry;
	}

	public void setGeometry(Polygon geometry) {
		this.geometry = geometry;
	}
	
	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}
	
	public Integer getNumeroVeiculos() {
		return this.veiculos.size();
	}

	public void setNumeroVeiculos(Integer numeroVeiculos) {
		this.numeroVeiculos = numeroVeiculos;
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Area other = (Area) obj;
		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}
}
