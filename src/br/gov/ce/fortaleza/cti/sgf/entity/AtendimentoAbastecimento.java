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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.ce.fortaleza.cti.sgf.util.StatusAtendimentoAbastecimento;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_ATENDABASTECIMENTO", schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "AtendimentoAbastecimento.findCota", query="select sum(o.quantidadeAbastecida) from " +
			"AtendimentoAbastecimento o where o.abastecimento.veiculo = :veiculo and month(o.data) = :mes"),
	@NamedQuery(name = "AtendimentoAbastecimento.findByVeiculo", query = "select o from AtendimentoAbastecimento o where o.abastecimento.veiculo.id = ?)"),
	@NamedQuery(name = "AtendimentoAbastecimento.findByVeiculoAndPeriodo", query = "select o from AtendimentoAbastecimento o where o.abastecimento.veiculo.id = ? " +
	"and o.data >= ? and o.data <= ?)"),
	@NamedQuery(name = "AtendimentoAbastecimento.findByPeriodo", query = "select o from AtendimentoAbastecimento o where " +
	"o.data >= ? and o.data <= ?) order by o.data asc"),
	@NamedQuery(name = "AtendimentoAbastecimento.findByMesAno", query = "select o from AtendimentoAbastecimento o where " +
	"month(o.data) = ? and year(o.data) = ?) order by o.data asc"),
	@NamedQuery(name = "AtendimentoAbastecimento.findByUGMesAno", query = "select o from AtendimentoAbastecimento o where o.abastecimento.veiculo.ua.ug.id = ? and month(o.data) = ? and year(o.data) = ?) order by o.data asc")
})
public class AtendimentoAbastecimento implements Serializable {

	private static final long serialVersionUID = -4112132350692806225L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_atendabastecimento")
	@SequenceGenerator(name = "seq_atendabastecimento", sequenceName = "sgf.atendabastecimento_seq", allocationSize = 1)
	@Column(name = "CODATENDABASTECIMENTO", nullable = false)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "CODSOLABASTECIMENTO")
	private Abastecimento abastecimento;

	@ManyToOne
	@JoinColumn(name = "CODUSUARIO")
	private User usuario;

	@ManyToOne
	@JoinColumn(name = "CODBOMBA")
	private Bomba bomba;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA")
	private Date data; // data do registro do atendimento pelo sistema

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATUS")
	private StatusAtendimentoAbastecimento status;

	@Column(name = "JUSTIFICATIVA", length = 100)
	private String justificativa;

	@Column(name = "KM_ATENDIMENTO")
	private Long quilometragem;

	@Column(name = "LITRO_ABASTECIDO")
	private Double quantidadeAbastecida;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORA_ATENDIMENTO") // DATA DO ATENDIMENTO DO ABASTECIMENTO
	private Date hora; // hora do atendimento informado pelo usuário na tela do atendimento
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HRATENDIMENTO")
	private Date horaAtendimento; // hora do atendimento informado pelo usuário na tela do atendimento

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Abastecimento getAbastecimento() {
		return abastecimento;
	}

	public void setAbastecimento(Abastecimento abastecimento) {
		this.abastecimento = abastecimento;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Bomba getBomba() {
		return bomba;
	}

	public void setBomba(Bomba bomba) {
		this.bomba = bomba;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public StatusAtendimentoAbastecimento getStatus() {
		return status;
	}

	public void setStatus(StatusAtendimentoAbastecimento status) {
		this.status = status;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Long getQuilometragem() {
		return quilometragem;
	}

	public void setQuilometragem(Long quilometragem) {
		this.quilometragem = quilometragem;
	}

	public Double getQuantidadeAbastecida() {
		return quantidadeAbastecida;
	}

	public void setQuantidadeAbastecida(Double quantidadeAbastecida) {
		this.quantidadeAbastecida = quantidadeAbastecida;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}


	public Date getHoraAtendimento() {
		return horaAtendimento;
	}

	public void setHoraAtendimento(Date horaAtendimento) {
		this.horaAtendimento = horaAtendimento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((abastecimento == null) ? 0 : abastecimento.hashCode());
		result = prime * result + ((bomba == null) ? 0 : bomba.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result	+ ((quantidadeAbastecida == null) ? 0 : quantidadeAbastecida.hashCode());
		result = prime * result	+ ((quilometragem == null) ? 0 : quilometragem.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtendimentoAbastecimento other = (AtendimentoAbastecimento) obj;
		if (getAbastecimento() == null) {
			if (other.getAbastecimento() != null)
				return false;
		} else if (!getAbastecimento().equals(other.getAbastecimento()))
			return false;
		if (getBomba() == null) {
			if (other.getBomba() != null)
				return false;
		} else if (!getBomba().equals(other.getBomba()))
			return false;
		if (getData() == null) {
			if (other.getData() != null)
				return false;
		} else if (!getData().equals(other.getData()))
			return false;
		if (getHora() == null) {
			if (other.getHora() != null)
				return false;
		} else if (!getHora().equals(other.getHora()))
			return false;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		if (getQuantidadeAbastecida() == null) {
			if (other.getQuantidadeAbastecida() != null)
				return false;
		} else if (!getQuantidadeAbastecida().equals(
				other.getQuantidadeAbastecida()))
			return false;
		if (getQuilometragem() == null) {
			if (other.getQuilometragem() != null)
				return false;
		} else if (!getQuilometragem().equals(other.getQuilometragem()))
			return false;
		if (getStatus() == null) {
			if (other.getStatus() != null)
				return false;
		} else if (!getStatus().equals(other.getStatus()))
			return false;
		if (getUsuario() == null) {
			if (other.getUsuario() != null)
				return false;
		} else if (!getUsuario().equals(other.getUsuario()))
			return false;
		return true;
	}

}
