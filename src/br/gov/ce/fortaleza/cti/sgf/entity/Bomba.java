/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Deivid
 * @since 11/12/09
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "tb_bomba" , schema = "SGF")
@NamedQueries( {
		@NamedQuery(name = "Bomba.findBombasPosto", query = "select b from Bomba b where b.posto.codPosto = ?")
})

public class Bomba implements Serializable{

	private static final long serialVersionUID = -6222342396653460579L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bomba")
	@SequenceGenerator(name = "seq_bomba", sequenceName = "sgf.codbomba_seq", allocationSize = 1)
	@Column(name="codbomba", nullable = false)
	private Integer id;
	
	@Column(name = "numero", nullable = false)
	private Integer numero;
	
	@Column(name = "limleitura", nullable = false)
	private Float limiteLeitura;
	
	@Column(name = "limalerta", nullable = false)
	private Float limiteAlerta;
	
	
	@ManyToOne
	@JoinColumn(name="CODPOSTO", nullable = false)
	private Posto posto;
	
	@OneToOne
	@JoinColumn(name="CODTIPOCOMBUSTIVEL", nullable = false)
	private TipoCombustivel combustivel;
	
	@OneToMany(mappedBy="bomba")
	private List<DiarioBomba> diarioBombas;
	
	@Transient
	private DiarioBomba diarioBomba;
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Posto getPosto() {
		return posto;
	}

	public void setPosto(Posto posto) {
		this.posto = posto;
	}

	public TipoCombustivel getCombustivel() {
		return combustivel;
	}

	public void setCombustivel(TipoCombustivel combustivel) {
		this.combustivel = combustivel;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setDiarioBombas(List<DiarioBomba> diarioBombas) {
		this.diarioBombas = diarioBombas;
	}

	public List<DiarioBomba> getDiarioBombas() {
		return diarioBombas;
	}

	public void setDiarioBomba(DiarioBomba diarioBomba) {
		this.diarioBomba = diarioBomba;
	}

	public DiarioBomba getDiarioBomba() {
		return diarioBomba;
	}
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Float getLimiteLeitura() {
		return limiteLeitura;
	}

	public void setLimiteLeitura(Float limiteLeitura) {
		this.limiteLeitura = limiteLeitura;
	}
	
	public Float getLimiteAlerta() {
		return limiteAlerta;
	}

	public void setLimiteAlerta(Float limiteAlerta) {
		this.limiteAlerta = limiteAlerta;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Bomba other = (Bomba) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id)));
	}
}
