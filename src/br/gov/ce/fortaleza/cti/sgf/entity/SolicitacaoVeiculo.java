/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.ce.fortaleza.cti.sgf.util.StatusSolicitacaoVeiculo;

/**
 * @author Deivid
 * @since 22/12/2009
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SecondaryTable(name="TB_REGISTROVEICULOS", schema = "SGF")
@Table(name = "TB_SOLVEICULOS", schema = "SGF")
@NamedQueries( {
		@NamedQuery(name = "SolicitacaoVeiculo.findByStatus", query = "select s from SolicitacaoVeiculo s where s.status = ? order by s.dataHoraSaida desc"),
		@NamedQuery(name = "SolicitacaoVeiculo.findByUsuarioStatus", query = "select s from SolicitacaoVeiculo s where "
				+ "s.solicitante.codPessoaUsuario = ? and s.status = ? and s.veiculo.status != 4"),
		@NamedQuery(name = "SolicitacaoVeiculo.veiculosIndisponiveis", query = "select o from SolicitacaoVeiculo o where "
				+ "(o.dataHoraSaida <= :saida or o.dataHoraSaida <= :retorno) and (o.dataHoraRetorno >= :saida or o.dataHoraRetorno >= :retorno)") })
public class SolicitacaoVeiculo implements Serializable {

	private static final long serialVersionUID = -3178774870117420419L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_solveiculos")
	@SequenceGenerator(name = "seq_solveiculos", sequenceName = "sgf.codsolveiculo_seq", allocationSize = 1)
	@Column(name = "CODSOLVEICULO", nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "CODUSUARIOSOL")
	private User solicitante;

	@ManyToOne
	@JoinColumn(name = "CODUSUARIOAUT")
	private User autorizador;

	@ManyToOne
	@JoinColumn(name = "CODVEICULO")
	private Veiculo veiculo;

	@ManyToOne
	@JoinColumn(name = "CODVEICULOAUT")
	private Veiculo veiculoAut;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATAINI")
	private Date dataHoraSaida;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATAFIM")
	private Date dataHoraRetorno;

	@Column(name = "DESTINO")
	private String destino;

	@Column(name = "JUSTIFICATIVA")
	private String justificativa;

	@Column(name = "JUSTIFICATIVA_NEG")
	private String justificativaNeg;

	@Column(name = "JUSTIFICATIVA_RETORNO")
	private String justificativaRetorno;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATUSSOL", nullable = false)
	private StatusSolicitacaoVeiculo status;

	@Transient
	private String imagemURL;

	/**
	 * 
	 * Mapeamento do atendimento da solicita��o de veiculo
	 */

	@ManyToOne
	@JoinColumn(name="codpessoausuario", table="TB_REGISTROVEICULOS")
	private User usuario;
	
	@ManyToOne
	@JoinColumn(name="codpessoamotorista", table="TB_REGISTROVEICULOS")
	private Motorista motorista;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtsaida", table="TB_REGISTROVEICULOS")
	private Date dtSaida;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtretorno", table="TB_REGISTROVEICULOS")
	private Date dtRetorno;
	
	@Column(name="kmsaida", table="TB_REGISTROVEICULOS")
	private Long kmSaida;
	
	@Column(name="kmretorno", table="TB_REGISTROVEICULOS")
	private Long kmRetorno;
	
	@Column(name="statusregistro", table="TB_REGISTROVEICULOS")
	private Integer statusAtendimento;

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

		SolicitacaoVeiculo other = (SolicitacaoVeiculo) obj;

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

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getJustificativaNeg() {
		return justificativaNeg;
	}

	public void setJustificativaNeg(String justificativaNeg) {
		this.justificativaNeg = justificativaNeg;
	}

	public Veiculo getVeiculoAut() {
		return veiculoAut;
	}

	public void setVeiculoAut(Veiculo veiculoAut) {
		this.veiculoAut = veiculoAut;
	}

	public User getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(User solicitante) {
		this.solicitante = solicitante;
	}

	public User getAutorizador() {
		return autorizador;
	}

	public void setAutorizador(User autorizador) {
		this.autorizador = autorizador;
	}

	public void setImagemURL(String imagemURL) {
		this.imagemURL = imagemURL;
	}

	public String getImagemURL() {
		return imagemURL;
	}

	public void setJustificativaRetorno(String justificativaRetorno) {
		this.justificativaRetorno = justificativaRetorno;
	}

	public String getJustificativaRetorno() {
		return justificativaRetorno;
	}

	public void setStatus(StatusSolicitacaoVeiculo status) {
		this.status = status;
	}

	public StatusSolicitacaoVeiculo getStatus() {
		return status;
	}

	public Date getDataHoraSaida() {
		return dataHoraSaida;
	}

	public void setDataHoraSaida(Date dataHoraSaida) {
		this.dataHoraSaida = dataHoraSaida;
	}

	public Date getDataHoraRetorno() {
		return dataHoraRetorno;
	}

	public void setDataHoraRetorno(Date dataHoraRetorno) {
		this.dataHoraRetorno = dataHoraRetorno;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public Date getDtSaida() {
		return dtSaida;
	}

	public void setDtSaida(Date dtSaida) {
		this.dtSaida = dtSaida;
	}

	public Date getDtRetorno() {
		return dtRetorno;
	}

	public void setDtRetorno(Date dtRetorno) {
		this.dtRetorno = dtRetorno;
	}

	public Long getKmSaida() {
		return kmSaida;
	}

	public void setKmSaida(Long kmSaida) {
		this.kmSaida = kmSaida;
	}

	public Long getKmRetorno() {
		return kmRetorno;
	}

	public void setKmRetorno(Long kmRetorno) {
		this.kmRetorno = kmRetorno;
	}

	public Integer getStatusAtendimento() {
		return statusAtendimento;
	}

	public void setStatusAtendimento(Integer statusAtendimento) {
		this.statusAtendimento = statusAtendimento;
	}
}
