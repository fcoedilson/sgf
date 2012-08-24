package br.gov.ce.fortaleza.cti.sgf.util.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.gov.ce.fortaleza.cti.sgf.entity.Abastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.AtendimentoAbastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.Cota;
import br.gov.ce.fortaleza.cti.sgf.entity.DiarioBomba;
import br.gov.ce.fortaleza.cti.sgf.entity.ItemRequisicao;
import br.gov.ce.fortaleza.cti.sgf.entity.Motorista;
import br.gov.ce.fortaleza.cti.sgf.entity.Multa;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.RegistroVeiculo;
import br.gov.ce.fortaleza.cti.sgf.entity.RequisicaoManutencao;
import br.gov.ce.fortaleza.cti.sgf.entity.SolicitacaoLubrificante;
import br.gov.ce.fortaleza.cti.sgf.entity.SolicitacaoVeiculo;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;

public class RelatorioDTO implements Serializable {

	private static final long serialVersionUID = -6814076103208623949L;
	public static final String COTAS_VEICULOS = "COTAS_VEICULOS";
	public static final String TROCAS_LUBRIFICANTE = "TROCAS_LUBRIFICANTE";
	public static final String INFORMACOES_VEICULO = "INFORMACOES_VEICULO";
	public static final String PONTUACAO_MOTORISTA = "PONTUACAO_MOTORISTA";
	public static final String VEICULO_MULTA = "VEICULO_MULTA";
	public static final String DIARIO_BOMBA = "DIARIO_BOMBA";
	public static final String CONFERENCIA_ABASTECIMENTO = "CONFERENCIA_ABASTECIMENTO";
	public static final String CONSOLIDADO_MENSAL = "CONSOLIDADO_MENSAL";
	public static final String HISTORICO_VEICULO_MANUTENCAO = "HISTORICO_VEICULO_MANUTENCAO";
	public static final String HISTORICO_TROCA_PNEUS = "QUANT_TROCA_PNEUS";
	public static final String VEICULOS_EM_MANUTENCAO = "VEICULOS_EM_MANUTENCAO";
	public static final String VEICULOS_SEM_RETORNO_MANUTENCAO = "VEICULOS_SEM_RETORNO_MANUTENCAO";
	public static final String MULTAS_VEICULO = "MULTAS_VEICULO";
	public static final String SOLICITACAO_VEICULO = "SOLICITACAO_VEICULO";
	public static final String KILOMETROS_RODADOS_VEICULO = "KILOMETROS_RODADOS_VEICULO";
	public static final Integer TROCA_PNEUS = 40;
	
	public static final String SEARCH_DIARIOBOMBA = "SEARCH_DIARIOBOMBA";
	public static final String UPDATE_DIARIOBOMBA = "UPDATE_DIARIOBOMBA";
	public static final String NEW_DIARIOBOMBA = "_DIARIOBOMBA";

	private Multa  multa;
	private Veiculo veiculo;
	private Cota cotaVeiculo;
	private Motorista motorista;
	private UG orgao;
	private Posto posto;
	private DiarioBomba diarioBomba;
	private Abastecimento abastecimento;
	private AtendimentoAbastecimento atendimento;
	private RequisicaoManutencao manutencao;
	private ItemRequisicao itemManutencao;
	private List<ItemRequisicao> itensManutencao;
	private SolicitacaoVeiculo solicitacaoVeiculo;
	private RegistroVeiculo registroSolicitacao;
	private SolicitacaoLubrificante solicitacaoLubrificante;
	private Float consumo;
	private Float consumoTotal;
	private Float saldoCota;
	private Float saldoFinal;
	private Float consumoPosto;
	private Float cota;
	private Double cotaSoma;
	private Float kmRodados;
	private Float kmPorLitro;
	private Float custo;
	private Integer kmAtual;
	private Integer numeroManutencoes;
	private Integer numeroAbastecimentos;
	private Integer quantTrocaPneus;
	private Integer pontos;
	private String status;
	private String combustivel;
	private Date currentDate;
	private Date dtInicial;
	private Date dtFinal;
	private Date periodo;
	private Date duracaoManutencao;
	private Date atraso;
	private Float consumoCombustivelOrgao;
	private Float consumoGasolina = 0F;
	private Float consumoEtanol = 0F;
	private Float consumoVeiculo = 0F;
	private String horaAtendimento;
	private String dataAtendimento;

	private List<Multa> multas;
	private List<RelatorioDTO> relatorios;

	public Multa getMulta() {
		return multa;
	}

	public void setMulta(Multa multa) {
		this.multa = multa;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public UG getOrgao() {
		return orgao;
	}

	public void setOrgao(UG orgao) {
		this.orgao = orgao;
	}

	public Integer getPontos() {
		return pontos;
	}

	public void setPontos(Integer pontos) {
		this.pontos = pontos;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Multa> getMultas() {
		return multas;
	}

	public void setMultas(List<Multa> multas) {
		this.multas = multas;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public List<RelatorioDTO> getRelatorios() {
		return relatorios;
	}

	public void setRelatorios(List<RelatorioDTO> relatorios) {
		this.relatorios = relatorios;
	}

	public DiarioBomba getDiarioBomba() {
		return diarioBomba;
	}

	public void setDiarioBomba(DiarioBomba diarioBomba) {
		this.diarioBomba = diarioBomba;
	}

	public Posto getPosto() {
		return posto;
	}

	public void setPosto(Posto posto) {
		this.posto = posto;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public void setPeriodo(Date periodo) {
		this.periodo = periodo;
	}

	public Date getPeriodo() {
		return periodo;
	}

	public Float getConsumo() {
		return consumo;
	}

	public void setConsumo(Float consumo) {
		this.consumo = consumo;
	}

	public Float getSaldoCota() {
		return saldoCota;
	}

	public void setSaldoCota(Float saldoCota) {
		this.saldoCota = saldoCota;
	}

	public Float getCota() {
		return cota;
	}

	public void setCota(Float cota) {
		this.cota = cota;
	}

	public Abastecimento getAbastecimento() {
		return abastecimento;
	}

	public void setAbastecimento(Abastecimento abastecimento) {
		this.abastecimento = abastecimento;
	}

	public AtendimentoAbastecimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(AtendimentoAbastecimento atendimento) {
		this.atendimento = atendimento;
	}

	public String getCombustivel() {
		return combustivel;
	}

	public void setCombustivel(String combustivel) {
		this.combustivel = combustivel;
	}

	public Float getKmRodados() {
		return kmRodados;
	}

	public void setKmRodados(Float kmRodados) {
		this.kmRodados = kmRodados;
	}

	public Float getKmPorLitro() {
		return kmPorLitro;
	}

	public void setKmPorLitro(Float kmPorLitro) {
		this.kmPorLitro = kmPorLitro;
	}

	public Float getCusto() {
		return custo;
	}

	public void setCusto(Float custo) {
		this.custo = custo;
	}

	public Integer getNumeroAbastecimentos() {
		return numeroAbastecimentos;
	}

	public void setNumeroAbastecimentos(Integer numeroAbastecimentos) {
		this.numeroAbastecimentos = numeroAbastecimentos;
	}

	public Integer getKmAtual() {
		return kmAtual;
	}

	public void setKmAtual(Integer kmAtual) {
		this.kmAtual = kmAtual;
	}

	public RequisicaoManutencao getManutencao() {
		return manutencao;
	}

	public void setManutencao(RequisicaoManutencao manutencao) {
		this.manutencao = manutencao;
	}

	public ItemRequisicao getItemManutencao() {
		return itemManutencao;
	}

	public void setItemManutencao(ItemRequisicao itemManutencao) {
		this.itemManutencao = itemManutencao;
	}

	public void setSolicitacaoVeiculo(SolicitacaoVeiculo solicitacaoVeiculo) {
		this.solicitacaoVeiculo = solicitacaoVeiculo;
	}

	public SolicitacaoVeiculo getSolicitacaoVeiculo() {
		return solicitacaoVeiculo;
	}

	public Float getConsumoTotal() {
		if(consumoTotal != null){
			return consumoTotal;
		} else {
			return 0F;
		}
	}

	public void setConsumoTotal(Float consumoTotal) {
		this.consumoTotal = consumoTotal;
	}

	public Float getSaldoFinal() {
		return saldoFinal;
	}

	public void setSaldoFinal(Float saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public Float getConsumoPosto() {
		return consumoPosto;
	}

	public void setConsumoPosto(Float consumoPosto) {
		this.consumoPosto = consumoPosto;
	}

	public Date getDuracaoManutencao() {
		return duracaoManutencao;
	}

	public void setDuracaoManutencao(Date duracaoManutencao) {
		this.duracaoManutencao = duracaoManutencao;
	}

	public Integer getQuantTrocaPneus() {
		return quantTrocaPneus;
	}

	public void setQuantTrocaPneus(Integer quantTrocaPneus) {
		this.quantTrocaPneus = quantTrocaPneus;
	}

	public RegistroVeiculo getRegistroSolicitacao() {
		return registroSolicitacao;
	}

	public void setRegistroSolicitacao(RegistroVeiculo registroSolicitacao) {
		this.registroSolicitacao = registroSolicitacao;
	}

	public Integer getNumeroManutencoes() {
		return numeroManutencoes;
	}

	public void setNumeroManutencoes(Integer numeroManutencoes) {
		this.numeroManutencoes = numeroManutencoes;
	}

	public Date getAtraso() {
		return atraso;
	}

	public void setAtraso(Date atraso) {
		this.atraso = atraso;
	}

	public void setItensManutencao(List<ItemRequisicao> itensManutencao) {
		this.itensManutencao = itensManutencao;
	}

	public List<ItemRequisicao> getItensManutencao() {
		return itensManutencao;
	}

	public SolicitacaoLubrificante getSolicitacaoLubrificante() {
		return solicitacaoLubrificante;
	}

	public void setSolicitacaoLubrificante(
			SolicitacaoLubrificante solicitacaoLubrificante) {
		this.solicitacaoLubrificante = solicitacaoLubrificante;
	}

	public Cota getCotaVeiculo() {
		return cotaVeiculo;
	}

	public void setCotaVeiculo(Cota cotaVeiculo) {
		this.cotaVeiculo = cotaVeiculo;
	}

	public Double getCotaSoma() {
		return cotaSoma;
	}

	public void setCotaSoma(Double cotaSoma) {
		this.cotaSoma = cotaSoma;
	}

	public Float getConsumoCombustivelOrgao() {
		return consumoCombustivelOrgao;
	}

	public void setConsumoCombustivelOrgao(Float consumoCombustivelOrgao) {
		this.consumoCombustivelOrgao = consumoCombustivelOrgao;
	}

	public String getHoraAtendimento() {
		return horaAtendimento;
	}

	public void setHoraAtendimento(String horaAtendimento) {
		this.horaAtendimento = horaAtendimento;
	}

	public String getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(String dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public Float getConsumoVeiculo() {
		return consumoVeiculo;
	}

	public void setConsumoVeiculo(Float consumoVeiculo) {
		this.consumoVeiculo = consumoVeiculo;
	}

	public Float getConsumoGasolina() {
		return consumoGasolina;
	}

	public void setConsumoGasolina(Float consumoGasolina) {
		this.consumoGasolina = consumoGasolina;
	}

	public Float getConsumoEtanol() {
		return consumoEtanol;
	}

	public void setConsumoEtanol(Float consumoEtanol) {
		this.consumoEtanol = consumoEtanol;
	}
}