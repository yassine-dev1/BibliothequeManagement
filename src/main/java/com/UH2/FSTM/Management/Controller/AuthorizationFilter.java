package com.UH2.FSTM.Management.Controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import io.jsonwebtoken.Claims;

import com.UH2.FSTM.Configuration.JWT.JWTUtil;



/**
 * Servlet Filter implementation class AuthorizationFilter
 */
@WebFilter("/HomeAdmin/*")
public class AuthorizationFilter extends HttpFilter implements Filter ,  Serializable {

	private static final long serialVersionUID = 1L;

	public AuthorizationFilter() {
        super();
    }

	public void destroy() {
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		System.out.println("---- filter ---------") ;
		HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    HttpSession session = req.getSession(false);
	    
		String token = (session != null ) ? (String) session.getAttribute("AUTH_TOKEN") : null ;
		
		try
		{
		   if(token != null)
		   {
			   Claims claim = JWTUtil.validateToken(token) ;
			   String role = (String) claim.get("role");
			   Date expiration = claim.getExpiration() ;
			   Date currentDate = new Date() ;
			  
			   if(expiration != null  && currentDate.before(expiration) && "admin".equals( role.toLowerCase() ) )
			   {
					System.out.println("Accès autorisé : redirection vers showEmprunts");
				   chain.doFilter(request, response);
				   return ;
			   }else
				   System.out.print("token est expiré");
		   }else
		   {
			   System.out.print("token est nulle");
		   }
		   
		}catch(Exception e)
		{
			System.err.println("Erreur validation Token: " + e.getMessage());
		}
        
		System.out.println("Accès refusé : redirection vers login");
	    res.sendRedirect(req.getContextPath() + "/login.xhtml");
	}


	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
