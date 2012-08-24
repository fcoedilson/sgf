package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = "TB_RASTREADOR", schema = "SGF")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SecondaryTables({
	@SecondaryTable(name="TB_VEICULORASTREADOR", schema = "SGF")
})
public class Rastreador implements Serializable{

	private static final long serialVersionUID = 6612568980680196547L;

	@Id
	@Column(name="IMEI", nullable = false)
	private String imei;

	@Column(name="MODELO")
	private String modelo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATACADASTRO")
	private Date dataCadastro;

	@Column(name="FABRICANTE")
	private String fabricante;

	@Column(name="SERIAL")
	private String serial;

	@Column(name="DESCCHIP")
	private String descricao;

	@Column(name="NUMERO", nullable = false)
	private String numero;

	@ManyToOne
	@JoinColumn(name="CODVEICULO", unique = true,  table = "TB_VEICULORASTREADOR")
	private Veiculo veiculo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATAATUALIZACAO", table = "TB_VEICULORASTREADOR")
	private Date dataAtualizacao;

	@Column(name="OBSERVACOES", table = "TB_VEICULORASTREADOR")
	private String observacoes;


	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imei == null) ? 0 : imei.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Rastreador other = (Rastreador) obj;
		return ((imei == null && other.imei == null) || (imei != null && imei.equals(other.imei)));
	}

}
