package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import conexion.ConexionProvider;
import tierraMedia.Atraccion;
import tierraMedia.Itinerario;
import tierraMedia.Producto;
import tierraMedia.Promocion;
import tierraMedia.TipoAtraccion;
import tierraMedia.Usuario;

public class ItinerarioDAOImpl implements ItinerarioDAO {
	
	public void insert(Producto producto, Usuario usuario) {
		try {
			if (producto.esPromo()) {
				String sql = "INSERT INTO ITINERARIO (USUARIO_ID, PROMOCION_ID) VALUES (?, ?)";
				Connection conn = ConexionProvider.getConnection();

				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setInt(1, usuario.getId());
				statement.setInt(2, producto.getId());
			}
		 else {
			String sql = "INSERT INTO ITINERARIO (USUARIO_ID, ATRACCION_ID)" + "VALUES(?, ?)";
			Connection conn = ConexionProvider.getConnection();

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, usuario.getId());
			statement.setInt(2, producto.getId());
			statement.executeUpdate();
		}

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}
	
	public List<Producto> findAll(int idUsuario, List<Producto> productos) {
		try {
			String consulta = "select promocion_id, atraccion_id from Itinerario WHERE usuario_id = ?";
			String promo = "select promocion.id, promocion.nombre from Promocion\r\n" + "INNER JOIN Itinerario on \r\n"
					+ "Promocion.id = ?;";
			String atracc = "select Atraccion.id, Atraccion.nombre from Atraccion\r\n" + "INNER JOIN Itinerario on \r\n"
					+ "Atraccion.id = ?;";

			Connection conn = ConexionProvider.getConnection();
			PreparedStatement statement = conn.prepareStatement(consulta);
			PreparedStatement statementPromo = conn.prepareStatement(promo);
			PreparedStatement statementAtrac = conn.prepareStatement(atracc);

			statement.setInt(1, idUsuario);
			ResultSet resultados = statement.executeQuery();

			List<Producto> itinerario = new ArrayList<Producto>();
			while (resultados.next()) {

				if (!(resultados.getString(1) == null)) {

					statementPromo.setInt(1, resultados.getInt(1));
					ResultSet pr = statementPromo.executeQuery();

					Producto promocion = buscarProducto(pr, productos);
					itinerario.add(promocion);
				}
				if (!(resultados.getString(2) == null)) {

					statementAtrac.setInt(1, resultados.getInt(2));
					ResultSet at = statementAtrac.executeQuery();
					Producto atraccion = buscarProducto(at, productos);
					itinerario.add(atraccion);
				}
			}

			return itinerario;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

	private Producto buscarProducto(ResultSet pr, List<Producto> productos) throws SQLException {

		for (Producto producto : productos) {
			if (producto.getNombre().equals(pr.getString(2))) {
				return producto;
			}
		}
		return null;
	}

	public Itinerario findByNombreUsuario(String nombre) {
		try {
			String sql = "SELECT (SELECT NOMBRE FROM USUARIO WHERE USUARIO.id= ITINERARIO.usuario_id) AS 'NOMBRE USUARIO', \r\n"
					+ "(SELECT NOMBRE FROM PROMOCION WHERE PROMOCION.id=ITINERARIO.promocion_id) AS 'PROMOCION COMPRADA',\r\n"
					+ "(SELECT NOMBRE FROM ATRACCION WHERE ATRACCION.id=ITINERARIO.atraccion_id) AS 'ATRACCION COMPRADA'\r\n"
					+ "FROM  Itinerario\r\n"
					+ "WHERE \"NOMBRE USUARIO\" LIKE '?'";
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
