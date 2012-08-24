package br.gov.ce.fortaleza.cti.sgf.conversores;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.postgis.Geometry;
import org.postgis.binary.BinaryParser;
import org.postgis.binary.BinaryWriter;

public class GeometryType implements UserType {

	private static final int[] SQL_TYPES = { Types.BLOB };

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		} else if (x == null || y == null) {
			return false;
		} else {
			return x.equals(y);
		}
	}

	public int hashCode(Object arg0) throws HibernateException {

		return 0;
	}

	public boolean isMutable() {
		return false;
	}

	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
		Geometry result = null;
		String geom = resultSet.getString(names[0]);
		if(geom != null ) {
			BinaryParser parser = new BinaryParser();
			result = parser.parse(geom);
		}
		return result;
	}

	public void nullSafeSet(PreparedStatement statement, 
			Object value, int index) throws HibernateException, SQLException {
		if (value == null) {
			statement.setBytes(index, null);
		} else {
			BinaryWriter bw = new BinaryWriter();

			byte[] bytes = bw.writeBinary((Geometry)value);
			statement.setBytes(index, bytes);
		}
	}


	public Object replace(Object original, Object target, 
			Object owner) throws HibernateException {
		return original;	
	}

	public Class<?> returnedClass() {
		return Geometry.class;
	}


	public int[] sqlTypes() {
		return GeometryType.SQL_TYPES;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable)value;
	}

}


