package br.gov.ce.fortaleza.cti.sgf.util;

import spatialindex.spatialindex.IData;
import spatialindex.spatialindex.INode;
import spatialindex.spatialindex.IVisitor;

public class Visitor implements IVisitor {

	public IData data;
	public void visitNode(final INode n) {}
	public void visitData(final IData d) {
		data = d;
	}
}