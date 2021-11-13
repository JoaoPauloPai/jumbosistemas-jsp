package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@MultipartConfig
@WebServlet(urlPatterns = { "/ServletProdutoController"})
public class ServletProdutoController extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ServletProdutoController() {
        // TODO Auto-generated constructor stub
    }



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


	}

}
