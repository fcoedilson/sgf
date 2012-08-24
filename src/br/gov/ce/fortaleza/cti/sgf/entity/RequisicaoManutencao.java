package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/* EXEMPLO:
select rm.veiculo.id, rm.dataInicio, rm.dataFim from RequisicaoManutencao rm
where rm.veiculo.id = 8  and 
(rm.dataFim > '2010-02-10 00:00:00' or  rm.dataFim < '2010-02-15 23:59:59')
and 
(rm.dataInicio > '2010-02-10 00:00:00'  or  rm.dataInicio < '2010-02-15 23:59:59') */

@Entity
@Table(name = "TB_REQUISICAOMANUTENCAO", schema = "SGF")
@NamedQueries({
	@NamedQuery(name="RequisicaoManutencao.findByVeiculo", query = "select r from RequisicaoManutencao as r where r.veiculo.id = ?"),
	@NamedQuery(name="RequisicaoManutencao.findByVeiculoMesAno", query = "select r from RequisicaoManutencao as r where r.veiculo.id = ? and (month(r.dataInicio) >= ? and month(r.dataFim) <= ?) and year(r.dataInicio) = ? and  year(r.dataFim) = ?"),
	@NamedQuery(name="RequisicaoManutencao.findByMesAno", query = "select r from RequisicaoManutencao as r where month(r.dataInicio) >= ? and month(r.dataFim) <= ? and year(r.dataInicio) = ? and  year(r.dataFim) = ?"),
	@NamedQuery(name="RequisicaoManutencao.findByVeiculoLivre", query = "select r from RequisicaoManutencao as r where r.veiculo.id = ? and " +
	"((r.dataInicio < ? and r.dataFim >= ?) or (r.dataInicio >= ? and r.dataFim <= ?) or (r.dataInicio <= ? and r.dataFim > ?))")
})

public class RequisicaoManutencao implements Serializable{

	private static final long serialVersionUID = 1031161986293985845L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CODREQMANUTENCAO", nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="CODOFICINA")
	private Oficina oficina;

	@ManyToOne
	@JoinColumn(name="CODVEICULO")
	private Veiculo veiculo;

	@ManyToOne
	@JoinColumn(name="CODUSUARIO")
	private User usuario;

	@Column(name="MECANICO", nullable = false)
	private String nomeMecanico;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATAINI")
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATAFIM")
	private Date dataFim;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_SAIDA")
	private Date dataSaida;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_RETORNO")
	private Date dataRetorno;

	@Column(name="STATUS")
	private Integer status;

	@OneToMany(mappedBy = "requisicaoManutencao", cascade=CascadeType.ALL)
	@JoinColumn(name="CODREQMANUTENCAO")
	private List<ItemRequisicao> itensRequisicao;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<ItemRequisicao> getItensRequisicao() {
		return itensRequisicao;
	}

	public void setItensRequisicao(List<ItemRequisicao> itensRequisicao) {
		this.itensRequisicao = itensRequisicao;
	}

	public void setNomeMecanico(String nomeMecanico) {
		this.nomeMecanico = nomeMecanico;
	}

	public String getNomeMecanico() {
		return nomeMecanico;
	}

	public Date getDataRetorno() {
		return dataRetorno;
	}

	public void setDataRetorno(Date dataRetorno) {
		this.dataRetorno = dataRetorno;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
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

		RequisicaoManutencao other = (RequisicaoManutencao) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}
	
}
