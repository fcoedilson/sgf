package br.gov.ce.fortaleza.cti.sgf.util;

import java.io.Serializable;
import java.util.List;

import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;

public class VeiculoDTO  implements Serializable {

	private List<Veiculo> veiculos;
	private UG orgao;
	public List<Veiculo> getVeiculos() {
		return veiculos;
	}
	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}
	public UG getOrgao() {
		return orgao;
	}
	public void setOrgao(UG orgao) {
		this.orgao = orgao;
	}
}
