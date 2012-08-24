/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Abastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.Cota;
import br.gov.ce.fortaleza.cti.sgf.entity.Motorista;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.entity.TipoServico;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.AbastecimentoService;
import br.gov.ce.fortaleza.cti.sgf.service.BaseService;
import br.gov.ce.fortaleza.cti.sgf.service.MotoristaService;
import br.gov.ce.fortaleza.cti.sgf.service.PostoService;
import br.gov.ce.fortaleza.cti.sgf.service.TipoServicoService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

/**
 * @author Joel
 * @since 11/12/09
 */
@Scope("session")
@Component("autorizacaoAbastecimentoBean")
public class AutorizacaoAbastecimentoBean extends EntityBean<Integer, Abastecimento> {

	@Autowired
	private AbastecimentoService abastecimentoService;

	@Autowired
	private VeiculoService veiculoService;

	@Autowired
	private TipoServicoService tipoServicoService;

	@Autowired
	private MotoristaService motoristaService;

	@Autowired
	private PostoService postoService;

	private List<Veiculo> veiculos = new ArrayList<Veiculo>();

	private List<TipoServico> tiposServico = new ArrayList<TipoServico>();

	private List<Motorista> motoristas = new ArrayList<Motorista>();

	private List<Posto> postos = new ArrayList<Posto>();

	@Override
	public String prepareSave() {
		refreshLists();
		return super.prepareSave();
	}

	@Override
	public String prepareUpdate() {
		refreshLists();
		return super.prepareUpdate();
	}

	private void refreshLists() {
		veiculos.clear();
		veiculos.addAll(veiculoService.retrieveAll());

		tiposServico.clear();
		tiposServico.addAll(tipoServicoService.retrieveAll());

		motoristas.clear();
		motoristas.addAll(motoristaService.retrieveAll());

		postos.clear();
		postos.addAll(postoService.retrieveAll());

	}

	@Override
	protected Abastecimento createNewEntity() {
		return new Abastecimento();
	}

	@Override
	protected Integer retrieveEntityId(Abastecimento entity) {
		Integer id = null;
		if (entity != null && entity.getId() != null) {
			id = entity.getId().intValue();
		}
		return id;
	}

	@Override
	public String save() {
		String saved = FAIL;
		if (isValid()) {
			User usuario = SgfUtil.usuarioLogado();
			saved = super.save();
		}
		return saved;
	}

	private boolean isValid() {
		boolean valid = false;
		Double total = abastecimentoService.pesquisarTotalAbastecidoMes(entity
				.getVeiculo());
		Veiculo veiculo = entity.getVeiculo();
		Cota cota = veiculo.getCota();
		
		if (cota != null) {
			if (total < cota.getCota()) {
				valid = true;
			}
		} else {
			valid = false;
			JSFUtil.getInstance().addErrorMessage("msg.error.cota.indisponivel");
		}

		return valid;
	}

	@Override
	public String update() {
		if (entity.getMotorista() == null) {
			FacesContext.getCurrentInstance().addMessage(
					"motorista",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
							"Cota excedida!"));
		}
		return super.update();
	}

	@Override
	protected BaseService<Integer, Abastecimento> retrieveEntityService() {
		return this.abastecimentoService;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public List<TipoServico> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServico> tiposServico) {
		this.tiposServico = tiposServico;
	}

	public List<Motorista> getMotoristas() {
		return motoristas;
	}

	public void setMotoristas(List<Motorista> motoristas) {
		this.motoristas = motoristas;
	}

	public List<Posto> getPostos() {
		return postos;
	}

	public void setPostos(List<Posto> postos) {
		this.postos = postos;
	}
}
