package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TB_PAGE", schema = "SGF")
public class Page implements Serializable {

	private static final long serialVersionUID = 1031163486293985845L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CODPAGE", nullable = false)
	private Integer id;

	@Column(name="DESCPAGE", nullable = false, length = 150)
	private String descricao;
	
	@Column(name="NOMEPAGE", nullable = false, length = 50)
	private String nome;
	
	@Column(name="URLPAGE", nullable = false)
	private String urlPage;
	
	@Column(name="BEANNAME", nullable = false)
	private String beanName;
	
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
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getUrlPage() {
		return urlPage;
	}

	public void setUrlPage(String urlPage) {
		this.urlPage = urlPage;
	}
	
	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
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
		Page other = (Page) obj;
		return ((id == null && other.id == null) || (id != null && id.equals(other.id))) &&
			((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}
}
