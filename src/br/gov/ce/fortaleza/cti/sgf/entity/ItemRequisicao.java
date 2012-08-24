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
import javax.persistence.Table;

@Entity
@Table(name = "TB_REQUISICAOITEM", schema = "SGF")
@NamedQueries({
	@NamedQuery(name="ItemRequisicao.findByIdRequisicao", query = "select r from ItemRequisicao as r where r.requisicaoManutencao.id = ?"),
	@NamedQuery(name="ItemRequisicao.findByVeiculoTipoServico", query = "select i from ItemRequisicao i where i.tipoServico.codTipoServico = ? and i.requisicaoManutencao.veiculo.id = ?"),
	@NamedQuery(name="ItemRequisicao.findByTipoServico", query = "select i from ItemRequisicao i where i.tipoServico.codTipoServico = ?"),
})
public class ItemRequisicao implements Serializable{

	private static final long serialVersionUID = 1031161986293985845L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CODREQITEM", nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="CODREQMANUTENCAO")
	private RequisicaoManutencao requisicaoManutencao;

	@ManyToOne
	@JoinColumn(name="CODTIPOSERVICO")
	private TipoServico tipoServico;

	@Column(name="QTDE", nullable = false)	
	private Integer quantidade;

	@Column(name="VALORUNT", nullable = false)
	private Float valorUnitario;

	@Column(name="VALORTOT")
	private Float valorTotal;
	
	public ItemRequisicao(TipoServico tipo){
		this.tipoServico = tipo;
	}	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RequisicaoManutencao getRequisicaoManutencao() {
		return requisicaoManutencao;
	}

	public void setRequisicaoManutencao(RequisicaoManutencao requisicaoManutencao) {
		this.requisicaoManutencao = requisicaoManutencao;
	}

	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Float getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Float valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public ItemRequisicao(){
		
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

		ItemRequisicao other = (ItemRequisicao) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}
}
