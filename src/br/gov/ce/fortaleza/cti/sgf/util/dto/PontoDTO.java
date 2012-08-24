package br.gov.ce.fortaleza.cti.sgf.util.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.gov.ce.fortaleza.cti.sgf.entity.Ponto;
import br.gov.ce.fortaleza.cti.sgf.util.ColorUtil;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.IndexTableDecorator;

public class PontoDTO  implements Serializable {

	private static final long serialVersionUID = 4561331928131040072L;
	
	public static String  SEARCH_ADDRESS = "SEARCH_ADDRESS";
	public static String  VIEW_MAP = "VIEW_MAP";
	public static String  RELATORIO_POSICOES = "RELATORIO_POSICOES";

	private int index;
	private int id;
	private int codigoExterno;
	private int tipo;
	private int ordem;
	private Integer ano;
	private String nome;
	private String placa;
	private String marca;
	private String modelo;
	private String cor;
	private float velocidadeMaxima;
	private float lat;
	private float lng;
	private double y;
	private double x;
	private float velocidade;
	private float temperatura;
	private float odometro;
	private float horimetro;
	private float distPontoProximo;
	private float kmAtual;
	private boolean ignicao;
	private boolean panico;
	private boolean bloqueio;
	private boolean sirene;
	private boolean escuta;
	private boolean selecionado;
	private boolean statusOdometro;
	private Boolean existeTemperatura;
	private Boolean existeOdometro;
	private String pontoProximo;
	private float distancia;
	private List<PontoDTO> rastro;
	private List<PontoDTO> rota;
	private Date dataTransmissao;
	
	public PontoDTO() {

	}

	public PontoDTO(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public boolean isStatusOdometro() {
		return statusOdometro;
	}

	public void setStatusOdometro(boolean statusOdometro) {
		this.statusOdometro = statusOdometro;
	}

	public float getKmAtual() {
		return kmAtual;
	}

	public void setKmAtual(float kmAtual) {
		this.kmAtual = kmAtual;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getCor() {
		return cor;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public float getVelocidadeMaxima() {
		return velocidadeMaxima;
	}

	public void setVelocidadeMaxima(float velocidadeMaxima) {
		this.velocidadeMaxima = velocidadeMaxima;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public float getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(float velocidade) {
		this.velocidade = velocidade;
	}

	public float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}

	public float getOdometro() {
		return odometro;
	}

	public void setOdometro(float odometro) {
		this.odometro = odometro;
	}

	public float getHorimetro() {
		return horimetro;
	}

	public void setHorimetro(float horimetro) {
		this.horimetro = horimetro;
	}

	public boolean getIgnicao() {
		return ignicao;
	}

	public void setIgnicao(boolean ignicao) {
		this.ignicao = ignicao;
	}

	public boolean getPanico() {
		return panico;
	}

	public void setPanico(boolean panico) {
		this.panico = panico;
	}

	public boolean getBloqueio() {
		return bloqueio;
	}

	public void setBloqueio(boolean bloqueio) {
		this.bloqueio = bloqueio;
	}

	public boolean getSirene() {
		return sirene;
	}

	public void setSirene(boolean sirene) {
		this.sirene = sirene;
	}

	public boolean getEscuta() {
		return escuta;
	}

	public void setEscuta(boolean escuta) {
		this.escuta = escuta;
	}

	public boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Date getDataTransmissao() {
		return dataTransmissao;
	}

	public void setDataTransmissao(Date dataTransmissao) {
		this.dataTransmissao = dataTransmissao;
	}

	public List<PontoDTO> getRastro() {
		return rastro;
	}

	public void setRastro(List<PontoDTO> rastro) {
		this.rastro = rastro;
	}

	public String getDataTransmissaoFormatada() {
		return DateUtil.parseAsString("HH:mm:ss dd/MM/yyyy", dataTransmissao);
	}

	public String getDataTransmissaoDMY() {
		return DateUtil.parseAsString("dd/MM/yyyy", dataTransmissao);
	}

	public String getColor() {
		return ColorUtil.getColor(index);
	}

	public String getRowColor() {
		return IndexTableDecorator.selectRowClass(dataTransmissao);
	}

	public boolean isExcessoVelocidade() {
		return velocidade > velocidadeMaxima;
	}

	public boolean isNaoExcessoVelocidade() {
		return !isExcessoVelocidade();
	}

	public String getDataString() {
		return id + "|" + nome + "|" + tipo + "|" + lat + "|" + lng + "|" + pontoProximo + "|" + distPontoProximo + "|" + velocidade + "|" + DateUtil.parseAsString("dd/MM/yyyy HH:mm:ss", dataTransmissao) + "|" + placa +" |||";
	}

	public String getRastroString() {
		String result = "";
		for (PontoDTO ponto : rastro) {
			result += ponto.getLat() + " " + ponto.getLng() + "|";
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
	}

	public String getRotaVeiculoString() {
		String result = "";
		if(rota != null ){
		for (PontoDTO ponto : rota) {
			result += ponto.getLat() + "|" + ponto.getLng() + "|" + ponto.getPontoProximo()+ "|" + ponto.getNome() + "|" + ponto.getOrdem() + "|" + ponto.getDataTransmissaoDMY() + "|";
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
		} else {
			return null;
		}
	}
	
	public float getDistPontoProximo() {
		return distPontoProximo;
	}

	public void setDistPontoProximo(float distPontoProximo) {
		this.distPontoProximo = distPontoProximo;
	}

	public String getPontoProximo() {
		return pontoProximo;
	}

	public void setPontoProximo(String pontoProximo) {
		this.pontoProximo = pontoProximo;
	}

	public List<PontoDTO> getRota() {
		return rota;
	}

	public void setRota(List<PontoDTO> rota) {
		this.rota = rota;
	}

	public int getCodigoExterno() {
		return codigoExterno;
	}

	public void setCodigoExterno(int codigoExterno) {
		this.codigoExterno = codigoExterno;
	}

	public Boolean getExisteTemperatura() {
		return existeTemperatura;
	}
	
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getDistancia() {
		return distancia;
	}

	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}

	public void setExisteTemperatura(Boolean existeTemperatura) {
		if (existeTemperatura == null) {
			this.existeTemperatura = false;
		} else {
			this.existeTemperatura = existeTemperatura;
		}
	}

	public Boolean getExisteOdometro() {
		return existeOdometro;
	}

	public void setExisteOdometro(Boolean existeOdometro) {
		if (existeOdometro == null) {
			this.existeOdometro = false;
		} else {
			this.existeOdometro = existeOdometro;
		}
	}

	public Boolean getHasRoute(){
		return rota.size() > 0;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PontoDTO other = (PontoDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public PontoDTO clone() {
		PontoDTO ponto = new PontoDTO();
		ponto.id = this.id;
		ponto.x = this.x;
		ponto.y = this.y;
		ponto.pontoProximo = this.pontoProximo;
		return ponto;
	}
}