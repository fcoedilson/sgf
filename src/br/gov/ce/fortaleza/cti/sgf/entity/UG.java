/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Deivid
 * @since 29/12/2009
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="tb_ug", schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "UG.findByID", query = "select ug from UG ug where ug.id = ?"),
	@NamedQuery(name = "UG.findSorted", query = "select ug from UG ug order by ug.descricao")
})
public class UG implements Serializable{

	private static final long serialVersionUID = -8966558699798206692L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name="cod_ug", nullable = false)
	private String id;
	
	@Column(name="nm_ug")
	private String descricao;
	
	@Column(name="flag")
	private Integer flag;
	
	@OneToMany(mappedBy="ug", fetch=FetchType.LAZY)
	private List<UA> uas;
	
	@Transient
	private List<Motorista> motoristas;
	
	@Transient
	private List<Veiculo> veiculos;
	
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UG other = (UG) obj;
		return ((id == null && other.id == null) || (id != null && id.equals(other.id)) &&
				(descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao))&&
				(flag == null && other.flag == null) || (flag != null && flag.equals(other.flag)));
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		result = 31 * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = 31 * result + ((flag == null) ? 0 : flag.hashCode());
		return result;
	}

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

	public List<UA> getUas() {
		return uas;
	}

	public void setUas(List<UA> uas) {
		this.uas = uas;
	}

	public void setMotoristas(List<Motorista> motoristas) {
		this.motoristas = motoristas;
	}

	public List<Motorista> getMotoristas() {
		return motoristas;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}
	
}
