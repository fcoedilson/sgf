package br.gov.ce.fortaleza.cti.sgf.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgis.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Ponto;
import br.gov.ce.fortaleza.cti.sgf.service.PontoService;


@Scope("session")
@Component("pontoBean")
public class PontoBean extends EntityBean<Integer, Ponto>{

	private static final Log logger = LogFactory.getLog(PontoBean.class);

	@Autowired
	private PontoService service;
	
	private Integer codcliente;

	private String points = new String("");

	private Double lat;
	private Double lng;

	@Override
	protected Integer retrieveEntityId(Ponto entity) {
		return entity.getId();
	}

	@Override
	protected PontoService retrieveEntityService() {
		return this.service;
	}

	@Override
	protected Ponto createNewEntity() {
		Ponto ponto = new Ponto();
		return ponto;
	}

	@Override
	public String prepareSave() {
		this.lat = null;
		this.lng = null;
		return super.prepareSave();
	}

	@Override
	public String prepareUpdate() {
		this.lat = ((Point)this.entity.getGeometry()).x;
		this.lng = ((Point)this.entity.getGeometry()).y;
		return super.prepareUpdate();
	}

	@Override
	public String update() {
		if(this.lat != null && this.lat != null){
			this.entity.setGeometry(new Point(this.lat, this.lng));
			this.entity.setX(this.lat);
			this.entity.setY(this.lng);
			return super.update();
		} else {
			return FAIL;
		}
	}

	public String latitudeChanged(){

		return SUCCESS;
	}

	public String save(){

		if(this.lat != null && this.lat != null){
			this.entity.setGeometry(new Point(this.lat , this.lng));
			this.entity.setX(this.lng);
			this.entity.setY(this.lat);
			this.entity.setCodcliente(0);
			return super.save();
		}
		return FAIL;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public Integer getCodcliente() {
		return codcliente;
	}

	public void setCodcliente(Integer codcliente) {
		this.codcliente = codcliente;
	}

}
