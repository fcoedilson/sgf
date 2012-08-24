/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Deivid
 * @since 09/12/09
 */
@Entity
@Table(name = "tb_tipocombustivel" , schema = "SGF")
public class TipoCombustivel implements Serializable{

	private static final long serialVersionUID = -4398341253557696360L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tipocombustivel")
	@SequenceGenerator(name = "seq_tipocombustivel", sequenceName = "sgf.codtipocombustivel_seq", allocationSize = 1)
	@Column(name="codtipocombustivel", nullable = false)
	private Integer id;
	
	@Column(name = "descservico")
	private String descricao;
	
	@OneToMany(mappedBy = "tipoCombustivel")
	private List<PostoServico> postoServicos;
	
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

		TipoCombustivel other = (TipoCombustivel) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
		((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}
	

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

	public void setPostoServicos(List<PostoServico> postoServicos) {
		this.postoServicos = postoServicos;
	}

	public List<PostoServico> getPostoServicos() {
		return postoServicos;
	}
	
}
