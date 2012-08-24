/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Deivid
 *
 */
@Entity
@Table(name = "tb_postoservicoveiculo", schema = "SGF")
public class PostoServicoVeiculo implements Serializable{

	private static final long serialVersionUID = -8356972032056762191L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_postoservicoveiculo")
	@SequenceGenerator(name = "seq_postoservicoveiculo", sequenceName = "sgf.codpostoservicoveiculo_seq", allocationSize = 1)
	@Column(name="codpostoservicoveiculo", nullable = false)
	private Integer id; 
	
	@ManyToOne
	@JoinColumn(name = "CODVEICULO")
	private Veiculo veiculo;
	
	@ManyToOne
	@JoinColumn(name = "CODPOSTOSERVICO")
	private PostoServico postoServico;
	
	@Override
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

		PostoServicoVeiculo other = (PostoServicoVeiculo) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public PostoServico getPostoServico() {
		return postoServico;
	}

	public void setPostoServico(PostoServico postoServico) {
		this.postoServico = postoServico;
	}
}
