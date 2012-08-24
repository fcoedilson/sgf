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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_MULTA", schema = "SGF")
@NamedQueries({
	@NamedQuery(name="Multa.findByPlacaVeiculo", query = "select m from Multa as m where m.veiculo.placa = ?"),
	@NamedQuery(name="Multa.findByNomeMotorista", query = "select m from Multa as m where m.motorista.pessoa.nome like ?"),
	@NamedQuery(name="Multa.findByVeiculo", query = "select m from Multa as m where m.veiculo.id = ? and m.dataCadastro between ? and ?"),
	@NamedQuery(name="Multa.findByMotorista", query = "select m from Multa as m where m.motorista.codMotorista = ?"),
	@NamedQuery(name="Multa.findByUG", query = "select m from Multa m where m.veiculo.ua.ug.id = ? and m.dataCadastro between ? and ?"),
	@NamedQuery(name="Multa.findByPeriodo", query = "select m from Multa m where m.dataCadastro between ? and ?"),
})
public class Multa implements Serializable {

	private static final long serialVersionUID = 1031161986293985845L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_multa")
	@SequenceGenerator(name="seq_multa", sequenceName="sgf.codmulta_seq", allocationSize = 1)
	@Column(name="CODMULTA", nullable = false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATACADASTRO", nullable = false)
	private Date dataCadastro;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATAINFRACAO")
	private Date dataInfracao;

	@ManyToOne
	@JoinColumn(name="CODINFRACAO")
	private Infracao infracao;

	@ManyToOne
	@JoinColumn(name="CODVEICULO")
	private Veiculo veiculo;
	
	@ManyToOne
	@JoinColumn(name="CODUSUARIO")
	private User usuario;

	@ManyToOne
	@JoinColumn(name="CODPESSOAMOTORISTA")
	private Motorista motorista;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataCadastro() {
		return this.dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Veiculo getVeiculo() {
		return this.veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	public Date getDataInfracao() {
		return dataInfracao;
	}

	public void setDataInfracao(Date dataInfracao) {
		this.dataInfracao = dataInfracao;
	}

	public Infracao getInfracao() {
		return infracao;
	}

	public void setInfracao(Infracao infracao) {
		this.infracao = infracao;
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

		Multa other = (Multa) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}

}
