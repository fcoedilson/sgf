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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_NUMEROMOTOR", schema = "SGF")
public class NumeroMotor implements Serializable {

	private static final long serialVersionUID = 1031161986293985845L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_numeroMotor")
	@SequenceGenerator(name="seq_numeroMotor", sequenceName="codnumeromotor_seq", allocationSize = 1)
	@Column(name="CODNUMMOTOR", nullable = false)
	private Integer id;

	@Column(name="NUMMOTOR", nullable = false, length = 50)
	private String numeroMotor;

	@Column(name="JUSTIFICATIVA", nullable = false, length = 250)
	private String justificativa;	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATACADASTRO", nullable = false)
	private Date dataCadastro;

	@ManyToOne
	@JoinColumn(name="CODVEICULO")
	private Veiculo veiculo;

	@ManyToOne
	@JoinColumn(name="CODUSUARIO")
	private User usuario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumeroMotor() {
		return numeroMotor;
	}

	public void setNumeroMotor(String numeroMotor) {
		this.numeroMotor = numeroMotor;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
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

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public int hashCode() {

		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		result = 31 * result + ((numeroMotor == null) ? 0 : numeroMotor.hashCode());

		return result;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		NumeroMotor other = (NumeroMotor) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
		((numeroMotor == null && other.numeroMotor == null) || (numeroMotor != null && numeroMotor.equals(other.numeroMotor)));
	}

}
