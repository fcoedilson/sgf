/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Deivid
 *
 */
@Entity
@Table(name = "tb_posto", schema = "SGF")
public class Posto implements Serializable {

	private static final long serialVersionUID = 2894335623640624583L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_posto")
	@SequenceGenerator(name = "seq_posto", sequenceName = "sgf.codposto_seq", allocationSize = 1)
	@Column(name="CODPOSTO", nullable = false)
	private Integer codPosto;
	
	@Column(name = "NOMEPOSTO")
	private String descricao;
	
	@OneToMany(mappedBy="posto", fetch=FetchType.LAZY)
	private List<Bomba> listaBomba;
	
	@OneToMany(mappedBy="posto")
	private List<PostoServico> postoServicos;
	
	@Transient
	private List<PostoServico> listaServicosSelecionados;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="tb_postoservico", schema = "SGF", 
				joinColumns=@JoinColumn(name="codposto"), 
				inverseJoinColumns=@JoinColumn(name="codtiposervico"))
	private List<TipoServico> servicos;
	
	@Transient
	private List<DiarioBomba> diarioBombas;
	
	public int hashCode() {

		int result = 1;
		result = 31 * result + ((codPosto == null) ? 0 : codPosto.hashCode());
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

		Posto other = (Posto) obj;

		return ((codPosto == null && other.codPosto == null) || (codPosto != null && codPosto.equals(other.codPosto))) &&
		((descricao == null && other.descricao == null) || (descricao != null && descricao.equals(other.descricao)));
	}
	
	public Integer getCodPosto() {
		return codPosto;
	}

	public void setCodPosto(Integer codPosto) {
		this.codPosto = codPosto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setListaServicosSelecionados(
			List<PostoServico> listaServicosSelecionados) {
		this.listaServicosSelecionados = listaServicosSelecionados;
	}

	public List<PostoServico> getListaServicosSelecionados() {
		return listaServicosSelecionados;
	}

	public void setListaBomba(List<Bomba> listaBomba) {
		this.listaBomba = listaBomba;
	}

	public List<Bomba> getListaBomba() {
		return listaBomba;
	}

	public void setPostoServicos(List<PostoServico> postoServicos) {
		this.postoServicos = postoServicos;
	}

	public List<PostoServico> getPostoServicos() {
		return postoServicos;
	}

	public void setServicos(List<TipoServico> servicos) {
		this.servicos = servicos;
	}

	public List<TipoServico> getServicos() {
		return servicos;
	}

	public List<DiarioBomba> getDiarioBombas() {
		return diarioBombas;
	}

	public void setDiarioBombas(List<DiarioBomba> diarioBombas) {
		this.diarioBombas = diarioBombas;
	}
}
