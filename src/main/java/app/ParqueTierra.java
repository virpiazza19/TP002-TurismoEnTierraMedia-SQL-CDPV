package app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import daos.AtraccionDAO;
import daos.DAOFactory;
import daos.PromocionDAO;
import daos.UsuarioDAO;
import excepciones.NoHayCupoException;
import tierraMedia.Atraccion;
import tierraMedia.OfertadorDeProductos;
import tierraMedia.Producto;
import tierraMedia.Promocion;
import tierraMedia.Usuario;

public class ParqueTierra {
	
	public void comenzarPrograma() throws NoHayCupoException {
		generarUsuarios();
		generarProductos();
		ofrecerServicios (usuarios, productos);
	}

	List<Usuario> usuarios = new LinkedList<Usuario>();
	List<Producto> productos = new ArrayList<Producto>();
	OfertadorDeProductos ofertador = new OfertadorDeProductos();
	
	public ParqueTierra () {
	}

	public ParqueTierra (UsuarioDAO usuario, AtraccionDAO atraccion, PromocionDAO promocion) {
		this.usuarios = generarUsuarios();
		this.productos = generarProductos();
	}
	
	public void ofrecerServicios (List<Usuario> usuarios, List<Producto> productos) throws NoHayCupoException  {
		ofertador.sugerirProductos(this.usuarios, this.productos);
	}

	public List<Usuario> generarUsuarios() {
		UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
		usuarios = usuarioDAO.findAll();
		return usuarios;
	}

	public List<Producto> generarProductos() {
		AtraccionDAO atraccionDAO = DAOFactory.getAtraccionDAO();
		PromocionDAO promocionDAO = DAOFactory.getPromocionDAO();
		List<Atraccion> a = atraccionDAO.findAll();
		List<Promocion> p = promocionDAO.findAllPromo(a);

		for (Promocion promo : p) {
			productos.add(promo);
		}

		for (Atraccion Atraccion : a) {
			productos.add(Atraccion );
		}

		return productos;
	}
}
