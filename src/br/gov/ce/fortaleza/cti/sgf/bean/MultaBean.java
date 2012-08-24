/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Infracao;
import br.gov.ce.fortaleza.cti.sgf.entity.Motorista;
import br.gov.ce.fortaleza.cti.sgf.entity.Multa;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.InfracaoService;
import br.gov.ce.fortaleza.cti.sgf.service.MotoristaService;
import br.gov.ce.fortaleza.cti.sgf.service.MultaService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

@Scope("session")
@Component("multaBean")
public class MultaBean extends EntityBean<Integer, Multa>{

	@Autowired
	private MultaService service;

	@Autowired
	private InfracaoService infracaoService;

	@Autowired
	private MotoristaService motoristaService;

	@Autowired
	private VeiculoService veiculoService;

	private String dataInfracao;
	private Integer searchId = 0;
	private String name;
	private List<Veiculo> veiculos;
	private List<Motorista> motoristas;
	private UG ug;

	protected Integer retrieveEntityId(Multa entity) {
		return entity.getId();
	}

	protected MultaService retrieveEntityService() {
		return this.service;
	}

	protected Multa createNewEntity() {
		Multa multa = new Multa();
		multa.setInfracao(new Infracao());
		multa.setVeiculo(new Veiculo());
		multa.setUsuario(new User());
		multa.setMotorista(new Motorista());
		multa.setInfracao(new Infracao());
		multa.setDataCadastro(null);
		this.dataInfracao = null;
		return multa;
	}

	public String prepareSave(){
		loadVeiculos();
		loadMotoristas();
		return super.prepareSave();
	}

	public String prepareUpdate(){
		this.dataInfracao = DateUtil.parseAsString("dd/MM/yyyy", this.entity.getDataInfracao());
		return super.prepareUpdate();
	}

	public String save(){
		Motorista motorista = motoristaService.retrieve(this.entity.getMotorista().getCodMotorista());
		this.entity.setVeiculo(veiculoService.retrieve(this.entity.getVeiculo().getId()));
		this.entity.setInfracao(infracaoService.retrieve(this.entity.getInfracao().getId()));
		motorista.setPontosCnh(motorista.getPontosCnh() + this.entity.getInfracao().getPontuacao());
		this.entity.setMotorista(motorista);
		this.entity.setUsuario(SgfUtil.usuarioLogado());
		this.entity.setDataInfracao(DateUtil.parseStringAsDate("dd/MM/yyyy", this.dataInfracao));
		this.entity.setDataCadastro(new Date());
		this.saved = true;
		return super.save();
	}

	public String update(){
		this.updated = true;
		this.entity.setMotorista(motoristaService.retrieve(this.entity.getMotorista().getCodMotorista()));
		return super.update();
	}

	public String delete(){
		this.entity.getMotorista().setPontosCnh(this.entity.getMotorista().getPontosCnh() - this.entity.getInfracao().getPontuacao());
		return super.delete();
	}

	public String search(){
		searchSort();
		return SUCCESS;
	}

	/**
	 * Retorna as multas dos últimos 6 meses
	 * 
	 */
	public String searchSort(){

		this.entities = new ArrayList<Multa>();

		Date end = new Date();

		Date start = DateUtil.adicionarOuDiminuir(end, -180*DateUtil.DAY_IN_MILLIS);

		User user = SgfUtil.usuarioLogado();

		if(SgfUtil.isAdministrador(user)){

			if(this.ug != null){

				this.entities = service.findByUG(ug, start, end);
			} else {

				this.entities = service.retrieveAll();
			}

		} else {

			this.entities = service.findByUG(user.getPessoa().getUa().getUg(), start, end);
		}
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	public String loadVeiculos(){

		User user = SgfUtil.usuarioLogado();

		this.veiculos = new ArrayList<Veiculo>();

		if(SgfUtil.isAdministrador(user) || SgfUtil.isCoordenador(user)){

			if(this.ug != null){

				this.veiculos = veiculoService.findByUG(this.ug);
			} else {

				this.veiculos = veiculoService.retrieveAll();
			}
		} else {

			this.veiculos = veiculoService.findByUG(user.getPessoa().getUa().getUg());
		}

		return loadMotoristas();
	}

	public String loadMotoristas() {

		this.motoristas = new ArrayList<Motorista>();

		User user = SgfUtil.usuarioLogado();

		if(SgfUtil.isAdministrador(user) || SgfUtil.isCoordenador(user)){

			if (this.ug != null) {

				this.motoristas = motoristaService.findByUG(this.ug.getId());
			} else {

				this.motoristas = motoristaService.retrieveAll();
			}
		} else {

			this.motoristas = motoristaService.findByUG(user.getPessoa().getUa().getUg().getId());
		}
		return SUCCESS;
	}

	public String searchByName(){

		this.entities = new ArrayList<Multa>();

		if(this.name != null && this.name.length() > 0){

			this.entities.addAll(service.findByVeiculoMotorista(this.name, searchId));
		} else {

			return searchSort();
		}

		return SUCCESS;
	}

	public synchronized List<Multa> getMultas(){

		return service.findAll("id");
	}

	public Integer getSearchId() {
		return searchId;
	}

	public void setSearchId(Integer searchId) {
		this.searchId = searchId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataInfracao() {
		return dataInfracao;
	}

	public void setDataInfracao(String dataInfracao) {
		this.dataInfracao = dataInfracao;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public UG getUg() {
		return ug;
	}

	public void setUg(UG ug) {
		this.ug = ug;
	}
}
