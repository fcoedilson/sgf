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
@Table(name = "TB_INFRACAO", schema = "SGF")
public class Infracao implements Serializable {

	private static final long serialVersionUID = 1031161986293985845L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_infracao")
	@SequenceGenerator(name="seq_infracao", sequenceName="sgf.codinfracao_seq", allocationSize = 1)
	@Column(name="CODINFRACAO", nullable = false)
	private Integer id;

	@Column(name="INFRACAO", nullable = false, length = 150)
	private String descricao;
	
	@Column(name="GRAVIDADE", nullable = false, length = 150)
	private String gravidade;

	@Column(name="PONTOS")
	private Integer pontuacao;

	@Column(name="VALOR")
	private Double valor;

	@Column(name="PESO")
	private Integer peso;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getPontuacao() {
		return this.pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

	public Double getValor() {
		return this.valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getGravidade() {
		return this.gravidade;
	}

	public void setGravidade(String gravidade) {
		this.gravidade = gravidade;
	}


	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public int hashCode() {

		int result = 1;
		result = 31 * result + ((id == null) ? 0 : id.hashCode());
		result = 31 * result + ((descricao == null) ? 0 : descricao.hashCode());

		return result;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		Infracao other = (Infracao) obj;

		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
		((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}

}
