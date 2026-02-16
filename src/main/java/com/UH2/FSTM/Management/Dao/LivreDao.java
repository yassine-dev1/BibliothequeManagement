package com.UH2.FSTM.Management.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.UH2.FSTM.Management.Model.LivreModel;
import com.UH2.FSTM.ConnexionDB.ConnectDB;
import com.UH2.FSTM.Management.Dto.LivreDTO;

public class LivreDao {

    // --- Requete ---
    private static final String _INSERT_SQL = "INSERT INTO LIVRES (ISBN, TITRE, AUTEUR, CATEGORIE, EXEMPLAIRES, DISPONIBLES, IMAGE_COUVERTURE) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String _UPDATE_SQL = "UPDATE LIVRES SET TITRE=?, AUTEUR=?, CATEGORIE=?, EXEMPLAIRES=?, DISPONIBLES=?, IMAGE_COUVERTURE=? WHERE ISBN=?";
    private static final String _DELETE_SQL = "DELETE FROM LIVRES WHERE ISBN=?";
    private static final String _SELECT_ALL_SQL = "SELECT * FROM LIVRES";
    private static final String _SELECT_BY_CATEGORY_SQL = "SELECT * FROM LIVRES WHERE CATEGORIE = ?";
    private static final String _SELECT_DISTINCT_CATEGORIES = "SELECT DISTINCT CATEGORIE FROM LIVRES WHERE CATEGORIE IS NOT NULL ORDER BY CATEGORIE" ;
    private static final String _DECREMENT_DISP_LIVRE = "UPDATE LIVRES SET DISPONIBLES = DISPONIBLES - 1 WHERE ISBN = ? AND DISPONIBLES > 0";
    private static final String _INCREMENT_DISP_LIVRE = "UPDATE LIVRES SET DISPONIBLES = DISPONIBLES + 1 WHERE ISBN = ?";
    private static final String _SELECT_DISP_LIVRES = "SELECT IMAGE_COUVERTURE , ISBN , TITRE , CATEGORIE FROM LIVRES WHERE DISPONIBLES > 0";
    //private final String SELECT_BY_ISBN_SQL = "SELECT * FROM LIVRES WHERE ISBN = ?";
    private static Connection _conn = ConnectDB.getConnection() ; ;
    
    
    //--- create ---
    public static boolean create(LivreModel livre) {
        try (PreparedStatement ps = _conn.prepareStatement(_INSERT_SQL)) {
            ps.setString(1, livre.getIsbn());
            ps.setString(2, livre.getTitre());
            ps.setString(3, livre.getAuteur());
            ps.setString(4, livre.getCategorie());
            ps.setInt(5, livre.getExemplaires());
            ps.setInt(6, livre.getDisponibles());
            ps.setString(7, livre.getImage());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Get All ---
    public  static List<LivreModel> findAll() {
        List<LivreModel> livres = new ArrayList<>();
        try (Statement st = _conn.createStatement(); 
             ResultSet rs = st.executeQuery(_SELECT_ALL_SQL)) {
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    // --- update ---
    public static  boolean update(LivreModel livre) {
        try (PreparedStatement ps = _conn.prepareStatement(_UPDATE_SQL)) {
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getCategorie());
            ps.setInt(4, livre.getExemplaires());
            ps.setInt(5, livre.getDisponibles());
            ps.setString(6, livre.getImage());
            ps.setString(7, livre.getIsbn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- delete ---
    public static boolean delete(String isbn) {
        try (PreparedStatement ps = _conn.prepareStatement(_DELETE_SQL)) {
            ps.setString(1, isbn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Find  BY CATEGORY ---
    public static List<LivreModel> findByCategory(String categorie) {
        List<LivreModel> livres = new ArrayList<>();
        try (PreparedStatement ps = _conn.prepareStatement(_SELECT_BY_CATEGORY_SQL)) {
            ps.setString(1, categorie);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    livres.add(mapResultSetToLivre(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }
    
    // --- get All Distinct Categories  ---
    public static List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        try (PreparedStatement ps = _conn.prepareStatement(_SELECT_DISTINCT_CATEGORIES);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("CATEGORIE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public static boolean  incrementeDispLivre(Connection connection , String isbn)
    {
    	try
    	{
    		  PreparedStatement psLivre = connection.prepareStatement(_INCREMENT_DISP_LIVRE);
              psLivre.setString(1, isbn);
              return  psLivre.executeUpdate() > 0;	
    	}catch(Exception e)
    	{
    		 e.printStackTrace();
    	}
    	return false;
    }
    
    public static boolean decrementDispLivre(Connection connection , String isbn)
    {
    	try
    	{
    		  PreparedStatement psLivre = connection.prepareStatement(_DECREMENT_DISP_LIVRE);
              psLivre.setString(1, isbn);
              return  psLivre.executeUpdate() > 0;	
    	}catch(Exception e)
    	{
    		 e.printStackTrace();
    	}
    	return false;
    }
    
    public static List<LivreDTO> findDispLivres()
    {
    	List<LivreDTO> livres = new ArrayList<>();
    	try
    	{
    	    PreparedStatement prepSt = _conn.prepareStatement(_SELECT_DISP_LIVRES);
    	    ResultSet rSet = prepSt.executeQuery() ;
    	    
    	    while(rSet.next())
    	    {
    	    	String v_isbn = rSet.getString("ISBN");
    	    	String v_titre = rSet.getString("TITRE");
    	    	String v_categorie = rSet.getString("Categorie");
    	    	String v_image = rSet.getString("IMAGE_COUVERTURE");
    	    	
    	    	livres.add(new LivreDTO(v_isbn , v_titre , v_categorie , v_image));
    	    }
    	    
    	    return livres ;
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return null ;
    }

    private static LivreModel mapResultSetToLivre(ResultSet rs) throws SQLException {
        LivreModel livre = new LivreModel();
        livre.setIsbn(rs.getString("ISBN"));
        livre.setTitre(rs.getString("TITRE"));
        livre.setAuteur(rs.getString("AUTEUR"));
        livre.setCategorie(rs.getString("CATEGORIE"));
        livre.setExemplaires(rs.getInt("EXEMPLAIRES"));
        livre.setDisponibles(rs.getInt("DISPONIBLES"));
        livre.setImage(rs.getString("IMAGE_COUVERTURE"));
        livre.setDateAjout(rs.getDate("DATE_AJOUT"));
        return livre;
    }
}