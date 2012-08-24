package br.gov.ce.fortaleza.cti.sgf.util;

import spatialindex.rtree.RTree;
import spatialindex.spatialindex.Point;
import spatialindex.spatialindex.Region;
import br.gov.ce.fortaleza.cti.sgf.entity.Ponto;
import br.gov.ce.fortaleza.cti.sgf.entity.Transmissao;


public class GeoUtil {
	
	public static void atualizarPontoMaisProximo(Transmissao transmissao, RTree client, RTree points) {

		Ponto ponto = transmissao.getPontoMaisProximo();
		Point point = new Point(new double[]{ponto.getX(), ponto.getY()});
		Visitor visitor = new Visitor();
		Ponto pontoProximo = new Ponto();
		pontoProximo.setDistancia(Float.MAX_VALUE);
		if (client != null) {
			client.nearestNeighborQuery(1, point, visitor);
			pontoProximo.setId(visitor.data.getIdentifier());
			pontoProximo.setDistancia(distanciaEntrePontos(point, (Region) visitor.data.getShape()));
		}
		visitor = new Visitor();
		if (pontoProximo.getDistancia() > 50) {
			points.nearestNeighborQuery(1, point, visitor);
			float distancia = distanciaEntrePontos(point, (Region) visitor.data.getShape());
			if (distancia < pontoProximo.getDistancia()) {
				pontoProximo.setId(visitor.data.getIdentifier());
				pontoProximo.setDistancia(distancia);
			}
		}
		transmissao.setPonto(pontoProximo);
		transmissao.setDistancia((float)(pontoProximo.getDistancia()));
	}
	
	private static Float distanciaEntrePontos(Point pointA, Region  pointB) {
		double lat1 = pointA.getCoord(0) / (180D / (22D / 7D));
		double lng1 = pointA.getCoord(1) / (180D / (22D / 7D));
		double lat2 = pointB.getCenter()[0] / (180D / (22D / 7D));
		double lng2 = pointB.getCenter()[1] / (180D / (22D / 7D));
		return (float) (long) (6378800D * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1)));
	}
}