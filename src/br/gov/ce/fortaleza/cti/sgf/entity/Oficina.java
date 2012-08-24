package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_OFICINA", schema = "SGF")
public class Oficina implements Serializable {

	private static final long serialVersionUID = 4031191986293085845L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_oficina")
	@SequenceGenerator(name="seq_oficina", sequenceName="sgf.codoficina_seq", allocationSize = 1)
	@Column(name="CODOFICINA", nullable = false)
	private Integer id;
	
	@Column(name="NOME", nullable = false, length = 80)
	private String nome;
	
	@Column(name="ENDERECO", nullable = false, length = 150)
	private String endereco;
	
	@Column(name="RESPONSAVEL", length = 100)
	private String responsavel;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
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

		Oficina other = (Oficina) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}

}
