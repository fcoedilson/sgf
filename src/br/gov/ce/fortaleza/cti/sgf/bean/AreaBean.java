package br.gov.ce.fortaleza.cti.sgf.bean;

import java.util.ArrayList;
import java.util.List;

import org.postgis.LinearRing;
import org.postgis.Point;
import org.postgis.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.entity.Area;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.service.AreaService;
import br.gov.ce.fortaleza.cti.sgf.service.VeiculoService;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.MapUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

/**
 * @author Deivid
 * @since 25/11/09
 */
@Scope("session")
@Component("areaBean")
public class AreaBean extends EntityBean<Integer, Area> {

	@Autowired
	private AreaService service;

	@Autowired
	private VeiculoService veiculoService;

	private Polygon polygon;
	private Veiculo veiculo;
	private List<Veiculo> veiculos;
	private String geoms = new String("");
	private String pontos = new String("");;

	@Override
	protected Area createNewEntity() {
		return new Area();
	}

	@Override
	protected Integer retrieveEntityId(Area entity) {
		return entity.getId();
	}

	@Override
	protected AreaService retrieveEntityService() {
		return this.service;
	}

	@Override
	public String prepareUpdate() {
		this.veiculos = new ArrayList<Veiculo>();
		if (this.entity.getVeiculos() != null && this.entity.getVeiculos().size() > 0) {
			this.veiculos.addAll(this.entity.getVeiculos());
			this.geoms = MapUtil.parseVeiculosMap(this.veiculos);
		}
		this.polygon = this.entity.getGeometry();
		return super.prepareUpdate();
	}

	@Override
	public String prepareSave() {
		this.polygon = null;
		return super.prepareSave();
	}

	@Override
	public String save() {
		this.entity.setGeometry(polygon);
		return super.save();
	}

	@Override
	public String search() {
		return super.search();
	}

	@Override
	public String update() {
		this.entity.setGeometry(this.polygon);
		return super.update();
	}

	public String updateArea() {
		return super.update();
	}

	public String areaVeiculos() {

		this.veiculos = veiculoService.veiculosRastreados();
		if (this.entity.getVeiculos() != null) {
			this.veiculos.removeAll(this.entity.getVeiculos());
			for (Veiculo v : this.veiculos) {
				pontos += ((Point) v.getGeometry()).y
						+ "#%"
						+ ((Point) v.getGeometry()).x
						+ "#%"
						+ v.getId()
						+ "#%"
						+ v.getPlaca()
						+ "#%"
						+ v.getVelocidade()
						+ "#%"
						+ v.getOdometro()
						+ "#%"
						+ v.getPontoProximo().getDescricao()
						+ "#%"
						+ v.getDistancia()
						+ "#%"
						+ DateUtil.parseAsString("dd/MM/yyyy HH:mm",
								v.getDataTransmissao()) + "$####";
			}
		}

		setCurrentBean(currentBeanName());
		setCurrentState(AREA_VEICULOS);
		return SUCCESS;
	}

	public boolean isAreaVeiculosState() {
		return AREA_VEICULOS.equals(getCurrentState());
	}

	public String adicionarVeiculo() {
		this.veiculos.remove(this.veiculo);
		this.entity.getVeiculos().add(this.veiculo);
		return SUCCESS;
	}

	public String removerVeiculo() {
		this.veiculos.add(this.veiculo);
		this.entity.getVeiculos().remove(this.veiculo);
		return SUCCESS;
	}

	public String getPolygon() {
		return encodePolygon(polygon);
	}

	public void setPolygon(String polygon) {
		this.polygon = decodePolygon(polygon);
	}

	/*
	 * codifica um pol�gone em string de pontos
	 */

	private String encodePolygon(Polygon polygon) {
		if (polygon != null) {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < polygon.numPoints(); i++) {
				result.append(polygon.getPoint(i).x);
				result.append(" ");
				result.append(polygon.getPoint(i).y);

				if (i != (polygon.numPoints() - 1)) {
					result.append(",");
				}
			}

			return result.toString();
		}
		return null;
	}

	/**
	 * 
	 * decodifica uma string de pontos em um pol�gono
	 * 
	 * @param mapArea
	 * @return
	 */
	private Polygon decodePolygon(String mapArea) {
		if (mapArea != null) {
			String[] coords = mapArea.split(",");
			Point[] points = new Point[coords.length];
			for (int i = 0; i < coords.length; i++) {
				String[] latlng = coords[i].split(" ");
				Double lat = Double.valueOf(latlng[0].trim());
				Double lng = Double.valueOf(latlng[1].trim());
				points[i] = new Point(lat, lng);
			}
			if (points[0].x != points[coords.length - 1].x
					|| points[0].y != points[coords.length - 1].y) {
				Point[] newCoordinates = new Point[points.length + 1];
				System.arraycopy(points, 0, newCoordinates, 0, points.length);
				points = newCoordinates;
				points[points.length - 1] = new Point(points[0].x, points[0].y);
			}
			LinearRing linearRing = new LinearRing(points);
			LinearRing[] l = new LinearRing[1];
			l[0] = linearRing;
			return new Polygon(l);
		}
		return null;
	}

	public String mostrarVeiculoMap() {
		this.veiculos = new ArrayList<Veiculo>();
		this.geoms = new String("");
		if (SgfUtil.isAdministrador(SgfUtil.usuarioLogado())) {
			this.veiculos = veiculoService.veiculos();
			this.geoms = MapUtil.parseVeiculosMap(this.veiculos);
		} else if (this.veiculo == null) {
			this.veiculos = veiculoService.findByUG(SgfUtil.usuarioLogado()
					.getPessoa().getUa().getUg());
			this.geoms = MapUtil.parseVeiculosMap(this.veiculos);
		} else {
			this.veiculos.add(this.veiculo);
			this.geoms = MapUtil.parseVeiculosMap(this.veiculos);
		}
		return SUCCESS;
	}

	public List<Area> getAreas() {
		return service.retrieveAll();
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public String getGeoms() {
		return geoms;
	}

	public void setGeoms(String geoms) {
		this.geoms = geoms;
	}

	public String getPontos() {
		return pontos;
	}

	public void setPontos(String pontos) {
		this.pontos = pontos;
	}
}
