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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Deivid
 * @since 07/12/09
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="tb_pessoamotorista" , schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "Motorista.findMotoristasBloqueados", query = "select object(o) from Motorista o where o.ativo = ?"),
	@NamedQuery(name = "Motorista.findByStatus", query = "select o from Motorista o where o.ativo = ? order by o.codMotorista desc"),
	@NamedQuery(name = "Motorista.findByNameStatus", query = "select o from Motorista o where o.pessoa.nome LIKE ? and o.ativo = ?"),
	@NamedQuery(name = "Motorista.findByUGNameStatus", query = "select o from Motorista o where o.pessoa.ua.ug.id = ? and o.pessoa.nome LIKE ? and o.ativo = ?"),
	@NamedQuery(name = "Motorista.findMotoristaByCPF", query = "select o from Motorista o where o.pessoa.cpf LIKE ?"),
	@NamedQuery(name = "Motorista.findByUG", query = "select o from Motorista o where o.pessoa.ua.ug.id = ? order by o.pessoa.nome"),
	@NamedQuery(name = "Motorista.findByUGStatus", query = "select o from Motorista o where o.pessoa.ua.ug.id = ? and o.ativo = ? order by o.codMotorista desc")
})
public class Motorista implements Serializable{

	private static final long serialVersionUID = -7384890140935452917L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_motorista")
	@SequenceGenerator(name="seq_motorista", sequenceName="sgf.codpessoamotorista_seq", allocationSize = 1)
	@Column(name="CODPESSOAMOTORISTA", nullable = false)
	private Integer codMotorista;
	
	@Column(name = "CNH")
	private String cnh; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATAVALIDADE")
	private Date dtValidade;
	
	@Column(name = "CATEGORIA")
	private String categoria;
	
	@Column(name = "ATIVO")
	private String ativo;
	
	@Column(name = "PONTOS_CNH", nullable = false)
	private Integer pontosCnh;
	
	@OneToOne
	@JoinColumn(name="CODPESSOA")
	private Pessoa pessoa;
	
	@Transient
	private String status;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((cnh == null) ? 0 : cnh.hashCode());
		result = prime * result	+ ((codMotorista == null) ? 0 : codMotorista.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Motorista other = (Motorista) obj;
		if (getCategoria() == null) {
			if (other.getCategoria() != null) {
				return false;
			}
		} else if (!getCategoria().equals(other.getCategoria())) {
			return false;
		}
		if (getCnh() == null) {
			if (other.getCnh() != null) {
				return false;
			}
		} else if (!getCnh().equals(other.getCnh())) {
			return false;
		}
		if (getCodMotorista() == null) {
			if (other.getCodMotorista() != null) {
				return false;
			}
		} else if (!getCodMotorista().equals(other.getCodMotorista())) {
			return false;
		}
		if (getPessoa() == null) {
			if (other.getPessoa() != null) {
				return false;
			}
		} else if (!getPessoa().equals(other.getPessoa())) {
			return false;
		}
		return true;
	}
	
	public Integer getCodMotorista() {
		return codMotorista;
	}

	public void setCodMotorista(Integer codMotorista) {
		this.codMotorista = codMotorista;
	}

	public String getCnh() {
		return cnh;
	}

	public void setCnh(String cnh) {
		this.cnh = cnh;
	}

	public Date getDtValidade() {
		return dtValidade;
	}

	public void setDtValidade(Date dtValidade) {
		this.dtValidade = dtValidade;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Integer getPontosCnh() {
		return pontosCnh;
	}

	public void setPontosCnh(Integer pontosCnh) {
		this.pontosCnh = pontosCnh;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
