package br.gov.ce.fortaleza.cti.sgf.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connetions {


	public static Connection connectMysql() throws Exception {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection( "jdbc:mysql://localhost:3306/fsdfh", "root", "123");
			con.setAutoCommit(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static Connection connectPostgres() throws Exception {

		Connection con = null;
		try {
			Class.forName("org.postgresql.Driver");
			con  = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sgf", "sgf", "sgf");
			con.setAutoCommit(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}

	public static void close(Connection connection) {

		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception e) {}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception e) {}
		}
	}

//	public static Map<Integer, RTree> findPontos(Connection conn) throws Exception {
//
//		Map<Integer, RTree> result = new HashMap<Integer, RTree>();
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try {
//			stmt = conn.prepareStatement(Util.FIND_PONTOS);
//			rs = stmt.executeQuery();
//			while (rs.next()) {
//				int clienteId = rs.getInt("SEQ_CLIENTE");
//				Ponto ponto = new Ponto();
//				ponto.setPontoId(rs.getInt("SEQ_PONTO"));
//				ponto.setX(rs.getFloat("X"));
//				ponto.setY(rs.getFloat("Y"));
//				ponto.setDescricao(rs.getString("DSC_PONTO"));
//				if (!result.containsKey(clienteId)) {
//					PropertySet propertySet = new PropertySet();
//					propertySet.setProperty("IndexCapacity", 5);
//					propertySet.setProperty("LeafCapactiy", 5);
//					result.put(clienteId, new RTree(propertySet, SpatialIndex.createMemoryStorageManager(null)));
//				}
//				Point point = new Point(new double[]{ponto.getX(), ponto.getY()});
//				result.get(clienteId).insertData(ponto.getDescricao().getBytes(), point, ponto.getPontoId());
//			}
//
//		} finally {
//			Connetions.close(rs);
//			Connetions.close(stmt);
//		}
//		return result;
//	}

//	public static Map<Integer, Integer> findAllVeiculos(Connection conn) throws Exception {
//
//		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try {
//			stmt = conn.prepareStatement(Util.FIND_VEICULOS);
//			rs = stmt.executeQuery();
//			while (rs.next()) {
//				result.put(rs.getInt(1), rs.getInt(2));
//			}
//		} finally {
//			Connetions.close(rs);
//			Connetions.close(stmt);
//		}
//		return result;
//	}

//
//	public static List<Transmissao> findSemTransmissoes(Connection conn) throws Exception {
//		List<Transmissao> result = new ArrayList<Transmissao>();
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try {
//			stmt = conn.prepareStatement(Util.FIND_VEICULOS_SEM_TRANSMISSAO);
//			rs = stmt.executeQuery();
//			while (rs.next()) {
//				Transmissao transmissao = new Transmissao();
//				transmissao.setVeiculoId(rs.getInt("SEQ_VEICULO"));
//				transmissao.setDataTransmissao(rs.getTimestamp("DAT_TRANSMISSAO"));
//				transmissao.setDescricaoPonto(rs.getString("DSC_PONTO"));
//				transmissao.setVeiculoNome(rs.getString("DSC_VEICULO"));
//				transmissao.setDistPontoTransmissao(rs.getFloat("NUM_DIST_PONTO"));
//				transmissao.setDataVerificacaoIni(rs.getTimestamp("HORA_VERIFICACAO_INI"));
//				transmissao.setDataVerificacaoFim(rs.getTimestamp("HORA_VERIFICACAO_FIM"));
//				transmissao.setCriacaoOcorrencia(rs.getBoolean("STA_OCORRENCIA"));
//				result.add(transmissao);
//			}
//		} finally {
//			Connetions.close(rs);
//			Connetions.close(stmt);
//		}
//		return result;
//	}

//	public static boolean updateTransmissao(Connection conn, Transmissao transmissao) throws Exception {
//
//		PreparedStatement stmt = null;
//		try {
//			stmt = conn.prepareStatement(Util.UPDATE_TRANSMISSAO);
//			stmt.setInt(1, transmissao.getPontoMaisProximo().getPontoId());
//			stmt.setInt(2, (int) transmissao.getPontoMaisProximo().getDist());
//			stmt.setInt(3, transmissao.getTransmissaoId());
//			return stmt.execute();
//		} finally {
//			Connetions.close(stmt);
//		}
//	}
//	
//	public static void pontosFromMysq() throws Exception{
//		
//		String query = "SELECT * FROM PONTO";
//		String insert = "INSERT INTO VIEWCAR.PONTO(descricao, geomponto) VALUES(?, GEOMETRYFROMTEXT(?))";
//		 
//		Connection pg = Connetions.getPgConexao();
//		Connection my = Connetions.getConexao();
//		PreparedStatement stmt = my.prepareStatement(query);
//		PreparedStatement stmt2 = my.prepareStatement(insert);
//		ResultSet rs = stmt.executeQuery();
//		
//		while(rs.next()){
//			
//			System.out.println(rs.getString(3));
//		}
//		
//	}

//	public static boolean insertTransmissao(Connection conn, Transmissao t) throws Exception {
//		
//		PreparedStatement stmt = null;
//		try {
//			stmt = conn.prepareStatement(Util.INSERT_TRANSM);
//			stmt.setInt(1, t.getVeiculoId());
//			//stmt.setString(2, "(POINT(" + transmissao.getYTransmissao() + " " + transmissao.getXTransmissao() + "))");
//			//stmt.setObject(2, GeometryFromtext("POINT(" + t.getYTransmissao() + " " + t.getXTransmissao() + ")"));
//			stmt.setDouble(2, t.getYTransmissao());
//			stmt.setDouble(3, t.getXTransmissao());
//			stmt.setBoolean(4, t.getPanico());
//			stmt.setBoolean(5, t.getIgnicao());
//			stmt.setBoolean(6, t.getEntrada2());
//			stmt.setBoolean(7, t.getEntrada3());
//			stmt.setBoolean(8, t.getEntrada4());
//			stmt.setInt(9, t.getCount1());
//			stmt.setInt(10, t.getCount2());
//			stmt.setInt(11, t.getCount3());
//			stmt.setFloat(12, t.getVelocidadeTransmissao());
//			stmt.setTimestamp(13, new Timestamp(t.getDataTransmissao().getTime()));
//			return stmt.execute();
//
//		} finally {
//			Connetions.close(stmt);
//		}
//	}

//	public static List<Transmissao> findTransmissoes(Connection conn) throws Exception {
//
//		List<Transmissao> result = new ArrayList<Transmissao>();
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try {
//			stmt = conn.prepareStatement(Util.fIND_TRANSMISSOES);
//			rs = stmt.executeQuery();
//			while (rs.next()) {
//				Transmissao transmissao = new Transmissao();
//				transmissao.setTransmissaoId(rs.getInt("SEQ_TRANSMISSAO"));
//				transmissao.setVeiculoId(rs.getInt("SEQ_VEICULO"));
//				transmissao.setDataTransmissao(rs.getTimestamp("DAT_TRANSMISSAO"));
//				transmissao.setClienteId(rs.getInt("SEQ_CLIENTE"));
//				transmissao.setXTransmissao(rs.getFloat("X"));
//				transmissao.setYTransmissao(rs.getFloat("Y"));
//				result.add(transmissao);
//			}
//
//		} finally {
//			Connetions.close(rs);
//			Connetions.close(stmt);
//		}
//		return result;
//	}

//	public static List<Transmissao> findTransmissoesOcorrencia(Connection conn, List<Transmissao> transmissoes) throws Exception {
//
//		List<Transmissao> result = new ArrayList<Transmissao>();
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try {
//			String tmp = "";
//			for (Transmissao transmissao : transmissoes) {
//				tmp += transmissao.getTransmissaoId() + ",";
//			}
//
//			if (tmp.length() > 0) {
//				tmp =  tmp.substring(0, tmp.length() - 1);
//				stmt = conn.prepareStatement(Util.fIND_TRANSMISSOES_OCORRENCIA.replaceAll("#####", tmp));
//				rs = stmt.executeQuery();
//				while (rs.next()) {
//					Transmissao transmissao = new Transmissao();
//					transmissao.setTransmissaoId(rs.getInt("SEQ_TRANSMISSAO"));
//					transmissao.setVeiculoId(rs.getInt("SEQ_VEICULO"));
//					transmissao.setVeiculoNome(rs.getString("DSC_VEICULO"));
//					transmissao.setDescricaoPonto(rs.getString("DSC_PONTO"));
//					transmissao.setDistPontoTransmissao(rs.getFloat("NUM_DIST_PONTO"));
//					transmissao.setDataTransmissao(rs.getTimestamp("DAT_TRANSMISSAO"));
//					transmissao.setPanicoAcinado(rs.getBoolean("STA_PANICO"));
//					transmissao.setVelocidadeTransmissao(rs.getInt("NUM_VELOCIDADE"));
//					transmissao.setClienteId(rs.getInt("SEQ_CLIENTE"));
//					transmissao.setXTransmissao(rs.getFloat("X"));
//					transmissao.setYTransmissao(rs.getFloat("Y"));
//					transmissao.setVelocidadeMinima(rs.getFloat("NUM_VELOCIDADE_MIN"));
//					transmissao.setVelocidadeMaxima(rs.getFloat("NUM_VELOCIDADE_MAX"));
//					transmissao.setTemperaturaMim(rs.getFloat("NUM_TEMPERATURA_MIN"));
//					transmissao.setTemperaturaMax(rs.getFloat("NUM_TEMPERATURA_MAX"));
//					transmissao.setTemperaturaDataIni(rs.getTimestamp("HORA_TEMPERATURA_INI"));
//					transmissao.setTemperaturaDataFim(rs.getTimestamp("HORA_TEMPERATURA_FIM"));
//					transmissao.setMovimentacaoDataIni(rs.getTimestamp("HORA_MOVIMENTACAO_INI"));
//					transmissao.setMovimentacaoDataFim(rs.getTimestamp("HORA_MOVIMENTACAO_FIM"));
//					transmissao.setDistanciaMovimentacao(rs.getInt("NUM_DISTANCIA_MAXIMA"));
//					transmissao.setMinutosAvisoParada(rs.getInt("MINUTOS_AVISO_PARADA"));
//					transmissao.setMinutosAvisoArea(rs.getInt("MINUTOS_AVISO_AREA"));
//					transmissao.setStatusEnvioPanico(rs.getBoolean("STA_EMAIL_PANICO"));
//					transmissao.setMinutosEnvioEmail(rs.getInt("MINUTOS_ENVIO_EMAIL"));
//					transmissao.setEmail(rs.getString("DSC_EMAIL"));
//					transmissao.setDataInicioViolacaoArea(rs.getTimestamp("DAT_VIOLACAO_AREA"));
//					transmissao.setDataInicioAreaRisco(rs.getTimestamp("DAT_AREA_RISCO"));
//					transmissao.setDataInicioParada(rs.getTimestamp("DAT_PARADA"));
//					transmissao.setDataVerificacaoIni(rs.getTimestamp("HORA_VERIFICACAO_INI"));
//					transmissao.setDataVerificacaoFim(rs.getTimestamp("HORA_VERIFICACAO_FIM"));
//					transmissao.setCriacaoOcorrencia(rs.getBoolean("STA_OCORRENCIA"));
//					result.add(transmissao);
//				}
//			}
//
//		} finally {
//			Connetions.close(rs);
//			Connetions.close(stmt);
//		}
//		return result;
//	}

//	public static boolean insertOcorrencia(Connection conn, Integer tipo, Integer veiculoId, Date dataOcorrencia, String descricao) throws Exception {
//
//		PreparedStatement stmt = null;
//		try {
//			stmt = conn.prepareStatement(Util.INSERT_OCORRENCIA);
//			stmt.setInt(1, veiculoId);
//			stmt.setString(2, descricao);
//			stmt.setInt(3, tipo);
//			stmt.setTimestamp(4, new Timestamp(dataOcorrencia.getTime()));
//			return stmt.execute();
//		} catch (SQLException e) {
//			return false;
//		} finally {
//			Connetions.close(stmt);
//		}
//	}
}