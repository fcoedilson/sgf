
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * @author Deivid
 * @since 10/12/09
 */
@Entity
@Table(name = "tb_postoservico" , schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "PostoServico.findByPostoServico", query = "select ps from PostoServico as ps where ps.posto = ?"),
	@NamedQuery(name = "PostoServico.findByPostoServicoCombustivel", query = "select ps from PostoServico as ps where ps.posto = ? " +
			"and ps.tipoServico = ? and ps.tipoCombustivel = ?"),
	@NamedQuery(name = "PostoServico.findByPosto", query = "select distinct(ps) from PostoServico as ps where ps.posto.codPosto = ?"),
})
public class PostoServico implements Serializable{

	private static final long serialVersionUID = 8620647491964276451L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_postoservico")
	@SequenceGenerator(name = "seq_postoservico", sequenceName = "sgf.codpostoservico_seq", allocationSize = 1)
	@Column(name="codpostoservico", nullable = false)
	private Integer id;  
	
	@ManyToOne
	@JoinColumn(name = "CODPOSTO")
	private Posto posto;
	
	@ManyToOne
	@JoinColumn(name = "CODTIPOSERVICO")
	private TipoServico tipoServico;
	
	@ManyToOne
	@JoinColumn(name = "CODTIPOCOMBUSTIVEL")
	private TipoCombustivel tipoCombustivel;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "postoServico", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	private Set<PostoServicoVeiculo> postosServicosVeiculos;
	
	@Override
	public int hashCode() {

		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		PostoServico other = (PostoServico) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getPosto().getDescricao() +""+ this.getTipoServico().getDescricao();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Posto getPosto() {
		return posto;
	}

	public void setPosto(Posto posto) {
		this.posto = posto;
	}

	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	public TipoCombustivel getTipoCombustivel() {
		return tipoCombustivel;
	}

	public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
		this.tipoCombustivel = tipoCombustivel;
	}

	public void setPostosServicosVeiculos(Set<PostoServicoVeiculo> postosServicosVeiculos) {
		this.postosServicosVeiculos = postosServicosVeiculos;
	}

	public Set<PostoServicoVeiculo> getPostosServicosVeiculos() {
		return postosServicosVeiculos;
	}

}
