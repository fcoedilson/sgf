package br.gov.ce.fortaleza.cti.sgf.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Classe utilizada para realizar a conexão com o banco de dados do
 * patrimônio, em Oracle
 * 
 * @author lafitte
 * @since 23/04/2010
 *
 */
public class ConnectOracle {

	/**
	 * Conexão com o banco de dados Oracle do Sistema de Patrimônio
	 * Retorna a conexão do pool de conexões do container. As configurações de conexão estão no 
	 * arquivo de contexto
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public static Connection connection() throws NamingException, SQLException {

		Connection conection = null;
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource)   initContext.lookup("java:comp/env/jdbc/patrimonio");
			conection = ds.getConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conection;
	}
}
