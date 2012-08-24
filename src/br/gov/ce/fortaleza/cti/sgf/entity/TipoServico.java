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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Deivid
 * @since 23/11/09	
 */
@Entity
@Table(name = "tb_tiposervico" , schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "TipoServico.findByTipo", query = "select t from TipoServico t where t.manutencao = ?")			   
})
public class TipoServico implements Serializable{

	private static final long serialVersionUID = 2548587267806176691L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tiposervico")
	@SequenceGenerator(name = "seq_tiposervico", sequenceName = "sgf.codtiposervico_seq", allocationSize = 1)
	@Column(name="CODTIPOSERVICO", nullable = false)
	private Integer codTipoServico;  

	@Column(name = "descservico")
	private String descricao;

	@Column(name="manutencao")
	private Integer manutencao;

	@Transient
	private String cota;

	@Transient
	private TipoCombustivel tipoCombustivel;

	@OneToMany(mappedBy = "tipoServico")
	private List<PostoServico> postoServicos;

	public int hashCode() {

		int result = 1;
		result = 31 * result + ((codTipoServico == null) ? 0 : codTipoServico.hashCode());
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

		TipoServico other = (TipoServico) obj;

		return ((codTipoServico == null && other.codTipoServico == null) || (codTipoServico != null && codTipoServico.equals(other.codTipoServico))) &&
		((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}

	public Integer getCodTipoServico() {
		return codTipoServico;
	}

	public void setCodTipoServico(Integer codTipoServico) {
		this.codTipoServico = codTipoServico;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setCota(String cota) {
		this.cota = cota;
	}

	public String getCota() {
		return cota;
	}

	public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
		this.tipoCombustivel = tipoCombustivel;
	}

	public TipoCombustivel getTipoCombustivel() {
		return tipoCombustivel;
	}

	public void setPostoServicos(List<PostoServico> postoServicos) {
		this.postoServicos = postoServicos;
	}

	public List<PostoServico> getPostoServicos() {
		return postoServicos;
	}

	public Integer getManutencao() {
		return manutencao;
	}

	public void setManutencao(Integer manutencao) {
		this.manutencao = manutencao;
	}
}
