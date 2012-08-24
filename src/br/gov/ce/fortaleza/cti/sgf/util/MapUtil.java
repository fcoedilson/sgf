package br.gov.ce.fortaleza.cti.sgf.util;

import java.util.List;

import org.postgis.Point;

import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;

public class MapUtil {
	
	public static String parseVeiculosMap(List<Veiculo> veiculos){
		String geoms = new String("");
			for (Veiculo veiculo : veiculos) {
				if(veiculo.getGeometry() != null){
					Double x = ((Point)veiculo.getGeometry()).x;
					Double y = ((Point)veiculo.getGeometry()).y;
					String modelo = veiculo.getModelo() == null ? " " : veiculo.getModelo().getDescricao();
					Float vel = veiculo.getVelocidade() == null ? 0F : veiculo.getVelocidade();
					Float odometro = veiculo.getOdometro() == null ? 0F : veiculo.getOdometro();
					String pprox = veiculo.getPontoProximo() == null ? "" :   veiculo.getPontoProximo().getDescricao();
					geoms += x + "#%" + y + "#%" + modelo + "#%" + veiculo.getPlaca() + "#%" + vel + "#%" + odometro + 
					"#%" + pprox + "#%" + veiculo.getDistancia() + "#%" +DateUtil.parseAsString("dd/MM/yyyy HH:mm", veiculo.getDataTransmissao())  + "$*@";
				}
			}
		return geoms;
	}
}
