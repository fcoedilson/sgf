package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SecondaryTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SecondaryTable(name="TB_ATENDTROCALUB")
@Table(name = "TB_SOLTROCALUBRIFICANTE", schema = "SGF")
@NamedQueries({
	@NamedQuery(name="SolicitacaoLubrificante.findByStatus", query = "select s from SolicitacaoLubrificante as s where s.status = ? and s.posto.codPosto = ? and s.status != -1 order by s.id desc"),
	@NamedQuery(name="SolicitacaoLubrificante.findByVeiculo", query = "select s from SolicitacaoLubrificante as s where s.veiculo.id = ? and s.posto.codPosto = ? and s.status != -1  order by s.id desc"),
	@NamedQuery(name="SolicitacaoLubrificante.findByVeiculoStatus", query = "select s from SolicitacaoLubrificante as s where s.veiculo.id = ? and s.status = ? and s.posto.codPosto = ? and s.status != -1 order by s.id desc"),
	@NamedQuery(name="SolicitacaoLubrificante.findUltimoByVeiculoTipoServico", 
	query = "select s from SolicitacaoLubrificante as s where s.tipoServico.codTipoServico = ? and s.veiculo.id = ? and " +
			"s.posto.codPosto = ? and s.status != -1 and s.dataAutorizacao = (select max(x.dataAutorizacao) from " +
			"SolicitacaoLubrificante as x where x.veiculo.id = ? and x.status = 2)"),
	@NamedQuery(name="SolicitacaoLubrificante.findByPosto", query = "select s from SolicitacaoLubrificante as s where s.posto.codPosto = ?  and s.posto.codPosto = ? and s.status != -1  order by s.id desc"),
	@NamedQuery(name="SolicitacaoLubrificante.findByPostoStatus", query = "select s from SolicitacaoLubrificante as s where s.posto.codPosto = ? and s.status = ? order by s.id desc"),
	@NamedQuery(name="SolicitacaoLubrificante.findBySolicitacaoDodia", query = "select s from SolicitacaoLubrificante as s order by s.dataAutorizacao desc"),
})
public class SolicitacaoLubrificante implements Serializable{

	private static final long serialVersionUID = 6254568280680196547L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_solicitacao_lubrif")
	@SequenceGenerator(name="seq_solicitacao_lubrif", sequenceName="codsoltrocalub_seq", allocationSize = 1)
	@Column(name="CODSOLTROCALUB", nullable = false)
	private Integer id;

	@ManyToOne()
	@JoinColumn(name="CODVEICULO", nullable = false)
	private Veiculo veiculo;

	@ManyToOne
	@JoinColumn(name="CODUSUARIOAUT")
	private User usuarioAutorizacao;

	@ManyToOne
	@JoinColumn(name="CODPESSOAMOTORISTA",  nullable = false)	
	private Motorista motorista;

	@ManyToOne
	@JoinColumn(name="CODPOSTO", nullable = false)
	private Posto posto;

	@ManyToOne
	@JoinColumn(name="CODTIPOSERVICO", nullable = false)	
	private TipoServico tipoServico;

	@Column(name="QUANT")
	private Double quantidade;

	@Column(name="STATUS")
	private Integer status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DTAUTORIZACAO")
	private Date dataAutorizacao;

	@Column(name="JUSTIFICATIVA",  nullable = true, table = "TB_ATENDTROCALUB")
	private String justificativa;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DTATENDIMENTO",  nullable = true, table = "TB_ATENDTROCALUB")
	private Date dataAtendimento;

	@Column(name="KMTROCAATUAL",  nullable = true, table = "TB_ATENDTROCALUB")
	private Integer kmTrocaAtual;

	@Column(name="KMTROCAANTERIOR",  nullable = true, table = "TB_ATENDTROCALUB")
	private Integer kmTrocaAnterior;

	@Column(name="KMPROXTROCA",  nullable = true, table = "TB_ATENDTROCALUB")
	private Integer kmPrevProximaTroca;

	@ManyToOne
	@JoinColumn(name="CODUSUARIOATEND", nullable = true, table = "TB_ATENDTROCALUB")
	private User usuarioAtendimento;

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

	public User getUsuarioAutorizacao() {
		return usuarioAutorizacao;
	}

	public void setUsuarioAutorizacao(User usuarioAutorizacao) {
		this.usuarioAutorizacao = usuarioAutorizacao;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataAutorizacao(Date dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public User getUsuarioAtendimento() {
		return usuarioAtendimento;
	}

	public void setUsuarioAtendimento(User usuarioAtendimento) {
		this.usuarioAtendimento = usuarioAtendimento;
	}

	public Integer getKmTrocaAtual() {
		return kmTrocaAtual;
	}

	public void setKmTrocaAtual(Integer kmTrocaAtual) {
		this.kmTrocaAtual = kmTrocaAtual;
	}

	public Integer getKmTrocaAnterior() {
		return kmTrocaAnterior;
	}

	public void setKmTrocaAnterior(Integer kmTrocaAnterior) {
		this.kmTrocaAnterior = kmTrocaAnterior;
	}

	public Integer getKmPrevProximaTroca() {
		return kmPrevProximaTroca;
	}

	public void setKmPrevProximaTroca(Integer kmPrevProximaTroca) {
		this.kmPrevProximaTroca = kmPrevProximaTroca;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
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

		SolicitacaoLubrificante other = (SolicitacaoLubrificante) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}
}
