/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.annotations.Index;

/**
 * @author Deivid
 * @since 25/11/09
 */
@Entity
@Table(name = "tb_cotaabastecimento" , schema = "SGF")
@NamedQueries( {
		@NamedQuery(name = "Cota.findByVeiculoServico", query = "select o from Cota o where o.veiculo = :veiculo and o.tipoServico = :tipoServico  and (o.veiculo.status > -1)"),
		@NamedQuery(name = "Cota.findCotasByVeiculo", query = "select o from Cota o where o.veiculo = ?  and (o.veiculo.status > -1)"),
		@NamedQuery(name = "Cota.findByPlacaVeiculo", query = "select o from Cota o where o.veiculo.placa = ? and (o.veiculo.status > -1)"),
		@NamedQuery(name = "Cota.findCotasVeiculosAtivos", query = "select o from Cota o where o.veiculo.status > -1")
})

public class Cota implements Serializable {

	private static final long serialVersionUID = -6483162443460126425L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cota")
	@SequenceGenerator(name = "seq_cota", sequenceName = "sgf.codcotaabastecimento_seq", allocationSize = 1)
	@Column(name = "codcotaabastecimento", nullable = false)
	private Integer id;

	@Column(name = "COTA")
	private Double cota;

	@OneToOne
	@JoinColumn(name = "CODVEICULO")
	private Veiculo veiculo;

	@ManyToOne
	@JoinColumn(name = "CODTIPOSERVICO", nullable = true)
	private TipoServico tipoServico;
	/**
	 * Cota de abastecimento dispon�vel no m�s
	 */
	@Column(name = "COTA_DISPONIVEL_MES")
	private Double cotaDisponivel;

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

		Cota other = (Cota) obj;

		return ((id == null && other.id == null) || (id != null && id
				.equals(other.id)));
	}

	public Double getCota() {
		return cota;
	}

	public void setCota(Double cota) {
		this.cota = cota;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Double getCotaDisponivel() {
		return cotaDisponivel;
	}

	public void setCotaDisponivel(Double cotaDisponivel) {
		this.cotaDisponivel = cotaDisponivel;
	}

}
