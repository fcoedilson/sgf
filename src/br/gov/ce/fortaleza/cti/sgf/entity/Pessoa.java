/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.math.BigInteger;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Deivid
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="TB_CADPESSOA" , schema = "SGF")
@NamedQueries({
	@NamedQuery(name = "Pessoa.findByCPF" , query = "select p from Pessoa p where p.cpf = ?"),
	@NamedQuery(name = "Pessoa.findByUG" , query = "select p from Pessoa p where p.ua.ug.id = ?"),
	@NamedQuery(name = "Pessoa.findByUGAndCPF" , query = "select p from Pessoa p where p.ua.ug.id = ? and p.cpf = ?")
})
public class Pessoa implements Serializable{
	
	private static final long serialVersionUID = 4698550232812151402L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pessoa")
	@SequenceGenerator(name="seq_pessoa", sequenceName="sgf.codpessoa_seq", allocationSize = 1)
	@Column(name = "CODPESSOA", nullable = false)	
	private Integer codPessoa;
	
	@ManyToOne
	@JoinColumn(name="COD_UA")
	private UA ua;
	
	@Column(name = "CPF")
	private String cpf;
	
	@Column(name = "TPPESSOA")
	private Integer tpPessoa;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "ATIVO")
	private String status;
	
	@Column(name = "MATRICULA")
	private BigInteger matricula;
	
	@Column(name = "status_rh")
	private String statusRH;
	
	public Integer getCodPessoa() {
		return codPessoa;
	}

	public void setCodPessoa(Integer codPessoa) {
		this.codPessoa = codPessoa;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Integer getTpPessoa() {
		return tpPessoa;
	}

	public void setTpPessoa(Integer tpPessoa) {
		this.tpPessoa = tpPessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigInteger getMatricula() {
		return matricula;
	}

	public void setMatricula(BigInteger matricula) {
		this.matricula = matricula;
	}

	public void setStatusRH(String statusRH) {
		this.statusRH = statusRH;
	}

	public String getStatusRH() {
		return statusRH;
	}

	public void setUa(UA uA) {
		this.ua = uA;
	}

	public UA getUa() {
		return ua;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + ((codPessoa == null) ? 0 : codPessoa.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return ((codPessoa == null && other.codPessoa == null) || (codPessoa != null && codPessoa.equals(other.codPessoa)));
	}
}
