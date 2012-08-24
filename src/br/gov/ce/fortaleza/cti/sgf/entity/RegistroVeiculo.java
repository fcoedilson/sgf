/**
 * 
 */
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

/**
 * @author Deivid
 * @since 20/01/2010
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_REGISTROVEICULOS", schema = "SGF")
public class RegistroVeiculo implements Serializable{

	private static final long serialVersionUID = -8327166902605307039L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_regveiculos")
	@SequenceGenerator(name = "seq_regveiculos", sequenceName = "sgf.codregistroveiculo_seq", allocationSize = 1)
	@Column(name="codregistroveiculo", nullable = false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="codpessoausuario", nullable = false)
	private User usuario;
	
	@ManyToOne
	@JoinColumn(name="codpessoamotorista", nullable = false)
	private Motorista motorista;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtsaida")
	private Date dtSaida;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtretorno")
	private Date dtRetorno;
	
	@Column(name="kmsaida")
	private Long kmSaida;
	
	@Column(name="kmretorno")
	private Long kmRetorno;
	
	@Column(name="statusregistro")
	private Integer status;
	
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

		RegistroVeiculo other = (RegistroVeiculo) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

}
