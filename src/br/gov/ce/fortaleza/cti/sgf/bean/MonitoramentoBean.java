package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.postgis.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.DownloadFileUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;
import br.gov.ce.fortaleza.cti.sgf.util.VelocityUtil;
import br.gov.ce.fortaleza.cti.sgf.util.dto.PontoDTO;

@Scope("session")
@Component("monitoramentoBean")
public class MonitoramentoBean extends EntityBean<Integer, PontoDTO>  {

	@Autowired
	private VeiculoService veiculoService;
	private PontoDTO ponto;
	private Veiculo veiculoAction;
	private List<PontoDTO> pontos;
	private List<Integer> veiculosIds;
	private Boolean autoCamera = true;
	private Boolean exibirRota = true;
	private Boolean exibirPontosRota = true;
	private Date dataInicio = DateUtil.adicionarOuDiminuir(DateUtil.getDateNow(), -4*DateUtil.HOUR_IN_MILLIS);
	private Float velocidadeMaxima = 60F;
	private String veiculo;

	@Override
	protected PontoDTO createNewEntity() {
		return new PontoDTO();
	}

	@Override
	protected Integer retrieveEntityId(PontoDTO entity) {
		throw new IllegalStateException("Não implementado");
	}

	@Override
	protected BaseService<Integer, PontoDTO> retrieveEntityService() {
		throw new IllegalStateException("Não implementado");
	}

	public boolean isTempoMonitoramento() {
		return this.pontos != null && this.pontos.size() > 0 ? true : false;
	}

	/**
	 * Busca os veículos com rastreamento para mostrar na tela de monitoramento
	 * @return
	 */
	public String searchMonitoramentoVeiculos() {

		User user = SgfUtil.usuarioLogado();
		List<Veiculo> veiculos = null;
		this.dataInicio = DateUtil.adicionarOuDiminuir(DateUtil.getDateNow(), -24*DateUtil.HOUR_IN_MILLIS);
		if(SgfUtil.isAdministrador(user)){
			veiculos = veiculoService.veiculosRastreados();
		} else {
			//veiculos = veiculoService.veiculosRastreadosByUG(user.getPessoa().getUa().getUg());
			veiculos = veiculoService.findByUG(user.getPessoa().getUa().getUg());
		}

		this.veiculosIds = new ArrayList<Integer>();
		for (Veiculo veiculo : veiculos) {
			veiculosIds.add(veiculo.getId());
		}
		this.pontos = veiculoService.searchPontosMonitoramento(veiculos, true, true, velocidadeMaxima, dataInicio);
		
		this.interval = 2000000;
		setCurrentBean(MonitoramentoBean.class.getSimpleName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
	

	/**
	 * Monta arquivo com as ultimas posições dos veículos rastreados e exporta como arquivo
	 * padrão para o google earth
	 * @return
	 * @throws Exception
	 */
	public String downloadGoogleEarthFile2() throws Exception {

		List<Integer> veiculosIds = new ArrayList<Integer>();
		for (PontoDTO p : pontos) {
			if (p.getSelecionado()) {
				veiculosIds.add(p.getId());
			}
		}
		String url = retrieveURLMonitoramento(veiculosIds);
		if (url != null) {
			byte[] bytes = VelocityUtil.merge("rotas.vm", new String[]{"url", "autoCamera"}, new Object[]{url, autoCamera});
			return DownloadFileUtil.downloadKMLFile(bytes);
		} else {
			return FAIL;
		}
	}
	
	public String downloadGoogleEarthFile() throws Exception {

		List<Integer> veiculosIds = new ArrayList<Integer>();
		for (PontoDTO pontoDTO : pontos) {
			if (pontoDTO.getSelecionado()) {
				veiculosIds.add(pontoDTO.getId());
			}
		}

		String url = retrieveURLMonitoramento(veiculosIds);
		if (url != null) {
			byte[] bytes = VelocityUtil.merge("reload.vm", new String[]{"url", "autoCamera"}, new Object[]{url, autoCamera});
			return DownloadFileUtil.downloadKMLFile(bytes);
		} else {
			return FAIL;
		}
	}

	/**
	 * Monta arquivo com as ultimas posições dos veículos rastreados e exporta como arquivo
	 * padrão para o google earth
	 * @return
	 * @throws Exception
	 */

	public Veiculo getVeiculoAction() {
		return veiculoAction;
	}

	public PontoDTO getPonto() {
		if (ponto == null) {
			ponto  = new PontoDTO();
		}
		return ponto;
	}

	/**
	 * constroi pontos para renderização na tela de monitoramento
	 * @return
	 */
	public List<PontoDTO> getPontosMonitoramento() {

		if (this.pontos == null) {
			searchMonitoramentoVeiculos();
			for (PontoDTO ponto : this.pontos) {
				ponto.setSelecionado(true);
			}
		}
		return this.pontos;
	}

	private String retrieveURLMonitoramento(List<Integer> pontos) {
		String ids = "";
		
		if( pontos.size() > 0 ){
			for (Integer id : pontos) {
				ids += id + ",";
			}
			ids = ids.substring(0, ids.length() -1);
			HttpServletRequest request = SgfUtil.getRequest();
			String url =  "?id=" + ponto.getId();
			url +=  "&di=" + DateUtil.parseTimeAsString(dataInicio);
			url +=  velocidadeMaxima != null && velocidadeMaxima != 0 ? "&vm=" + velocidadeMaxima : "&vm=60";
			url +=  exibirPontosRota ? "&epr=true" : "";
			url +=  exibirRota ? "&er=true" : "";
			url +=  autoCamera ? "&ac=true" : "";
			url +=  "&ids=" + ids;
			return "http://" + request.getLocalName() + ":" + request.getLocalPort() + request.getContextPath() + "/reload" + url;
		} else {
			return null;
		}
	}

	public boolean isMSIE(){
		return SgfUtil.getRequest().getHeader("User-Agent").contains("MSIE");
	}

	public Boolean getAutoCamera() {
		return autoCamera;
	}

	public void setAutoCamera(Boolean autoCamera) {
		this.autoCamera = autoCamera;
	}
	
	public Boolean getExibirRota() {
		return exibirRota;
	}

	public void setExibirRota(Boolean exibirRota) {
		this.exibirRota = exibirRota;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		String time = DateUtil.parseTimeAsString(dataInicio);
		this.dataInicio = DateUtil.getDateTime(DateUtil.getDateNow(), time);
	}

	public Float getVelocidadeMaxima() {
		return velocidadeMaxima;
	}

	public void setVelocidadeMaxima(Float velocidadeMaxima) {
		this.velocidadeMaxima = velocidadeMaxima;
	}

	public List<PontoDTO> getPontos() {
		return pontos;
	}

	public void setPontos(List<PontoDTO> pontos) {
		this.pontos = pontos;
	}

	public List<Integer> getVeiculosIds() {
		return veiculosIds;
	}

	public void setVeiculosIds(List<Integer> veiculosIds) {
		this.veiculosIds = veiculosIds;
	}

	public String getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}

	public void setPonto(PontoDTO ponto) {
		this.ponto = ponto;
	}
}