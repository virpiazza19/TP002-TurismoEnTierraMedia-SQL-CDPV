package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import conexion.ConexionProvider;
import tierraMedia.Atraccion;
import tierraMedia.Itinerario;
import tierraMedia.TipoAtraccion;

public class ItinerarioDAOImpl implements ItinerarioDAO {
	
	public int insert(Itinerario itinerario) {
		try {
			String sql = "INSERT INTO ITINERARIO (USUARIO_ID, ATRACCION_ID, PROMOCION_ID) VALUES (?, ?, ?)";
			Connection conn = ConexionProvider.getConnection();

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, itinerario.getUsuario());
			statement.setString(2, itinerario.getAtraccion());
			statement.setString(3, itinerario.getPromocion());
			int rows = statement.executeUpdate();

			return rows;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

	public int update(Itinerario itinerario) {
		try {
			String sql = "UPDATE ITINERARIO SET ATRACCION_ID= ?, PROMOCION_ID= ? WHERE USUARIO_ID = ?";
			Connection conn = ConexionProvider.getConnection();

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, itinerario.getUsuario());
			int rows = statement.executeUpdate();

			return rows;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}
	
	public List<Itinerario> findAll() {
		try {
			String sql = "SELECT ATRACCION.id, ATRACCION.nombre, ATRACCION.costo, ATRACCION.duracion, ATRACCION.cupo, Tipo_atraccion.nombre AS 'tipo_atraccion'\r\n"
					+ "FROM ATRACCION\r\n"
					+ "JOIN Tipo_atraccion ON Tipo_atraccion.id = ATRACCION.tipo\r\n"
					+ "";
			Connection conn = ConexionProvider.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultados = statement.executeQuery();

			List<Itinerario> itinerario = new LinkedList<Itinerario>();
			while (resultados.next()) {
				itinerario.add(toItinerario(resultados));
			}

			return itinerario;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}
	
	private Itinerario toItinerario(ResultSet results) throws SQLException {
		return new Itinerario(results.getInt(1), results.getString(2), results.getInt(3), results.getDouble(4), 
				results.getInt(5), TipoAtraccion.valueOf(results.getString(6)));
	}

	public int countAll() {
		try {
			String sql = "SELECT COUNT(1) AS TOTAL FROM ITINERARIO";
			Connection conn = ConexionProvider.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet resultados = statement.executeQuery();

			resultados.next();
			int total = resultados.getInt("TOTAL");

			return total;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}
	
	public Itinerario findByNombreUsuario(String nombre) {
		try {
			String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
			Connection conn = ConexionProvider.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, nombre);
			ResultSet resultados = statement.executeQuery();

			Itinerario itinerario = null;

			if (resultados.next()) {
				itinerario = toItinerario(resultados);
			}

			return itinerario;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

}
