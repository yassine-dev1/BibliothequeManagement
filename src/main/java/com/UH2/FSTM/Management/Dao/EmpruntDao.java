package com.UH2.FSTM.Management.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.UH2.FSTM.Management.Model.EmpruntModel;
import com.UH2.FSTM.ConnexionDB.ConnectDB;


public class EmpruntDao {

    private static final String _INSERT_EMPRUNT = "INSERT INTO EMPRUNTS (ID_USER, ISBN, DATE_RETOUR_MAX) VALUES (?, ?, DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY))";
    private static final String _UPDATE_RETOUR = "UPDATE EMPRUNTS SET DATE_RETOUR_REELLE = CURRENT_DATE, STATUT = 'RENDU' WHERE ID_EMPRUNT = ?";
    private static final String _SELECT_EN_COURS = "SELECT e.*, u.firstname, u.lastname, l.TITRE FROM EMPRUNTS e " +
                                           "JOIN USERS u ON e.ID_USER = u.ID " +
                                           "JOIN LIVRES l ON e.ISBN = l.ISBN " +
                                           "WHERE e.STATUT = 'EN_COURS' ORDER BY e.DATE_RETOUR_MAX ASC";
    private static final String _SELECT_ALL_BY_ADHERON = "SELECT e.*, u.firstname, u.lastname, l.TITRE FROM EMPRUNTS e " +
            								  "JOIN USERS u ON e.ID_USER = u.ID " +
            								  "JOIN LIVRES l ON e.ISBN = l.ISBN " +
            								  "WHERE ID_USER = ? " +
            								  "ORDER BY e.DATE_SORTIE ASC";
    private static final String _SELECT_ALL = "SELECT e.*, u.firstname, u.lastname, l.TITRE FROM EMPRUNTS e " +
    													 "JOIN USERS u ON e.ID_USER = u.ID " +
    													 "JOIN LIVRES l ON e.ISBN = l.ISBN " +
    													 "ORDER BY e.DATE_SORTIE ASC";
    private static final String _DELETE_EMPRUNT = "DELETE FROM EMPRUNTS WHERE ID_EMPRUNT = ? ";
    private static final String VERIFY_ADHERON_EMPRUNT = "SELECT COUNT(*) FROM EMPRUNTS WHERE ID_USER = ? AND STATUT='EN_COURS' " ; 
    private static final String VERIFY_BOOK_EMPRUNT = "SELECT COUNT(*) FROM EMPRUNTS WHERE ISBN = ? AND STATUT='EN_COURS' " ; 
    private static final String _FIND_BY_ID = "SELECT * FROM EMPRUNTS WHERE ID_EMPRUNT = ? ";
    private static  Connection conn ;

    // --- ENREGISTRER UN EMPRUNT ---
    public static boolean emprunterLivre(int idUser, String isbn) {
    	conn = ConnectDB.getConnection() ;
        try {
            conn.setAutoCommit(false); // Début de la transaction
            try (PreparedStatement psEmp = conn.prepareStatement(_INSERT_EMPRUNT)) {
                psEmp.setInt(1, idUser);
                psEmp.setString(2, isbn);
                int rowEmp = psEmp.executeUpdate();

                    if (rowEmp > 0 && LivreDao.decrementDispLivre(conn , isbn)) {
                        conn.commit(); // Valider les deux opérations
                        return true;
                    }
                
            }
            conn.rollback(); 
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) {}
        }
        return false;
    }

    
    // --- ENREGISTRER UN RETOUR ---
    public static boolean retournerLivre(int idEmprunt, String isbn) {
    	conn = ConnectDB.getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psRet = conn.prepareStatement(_UPDATE_RETOUR)) {
                psRet.setInt(1, idEmprunt);
                int rowRet = psRet.executeUpdate();


                    if (rowRet > 0 && LivreDao.incrementeDispLivre(conn ,isbn)) {
                        conn.commit();
                        return true;
                    }
                
            }
            conn.rollback();
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) {}
        }
        return false;
    }
    
          // --- findById ---------------
    public static EmpruntModel findById(int idEmprunt)
    {
    	try
    	{
    		PreparedStatement pSt = conn.prepareStatement(_FIND_BY_ID);
    		pSt.setInt(1, idEmprunt);
    		ResultSet rs = pSt.executeQuery();
            if (rs.next()) {
                EmpruntModel dto = new EmpruntModel();
                dto.setIdEmprunt(rs.getInt("ID_EMPRUNT"));
                dto.setIdEmprunt(rs.getInt("ID_USER"));
                dto.setIsbn(rs.getString("ISBN"));
                dto.setDateSortie(rs.getDate("DATE_SORTIE"));
                dto.setDateRetourMax(rs.getDate("DATE_RETOUR_MAX"));
                dto.setStatut(rs.getString("STATUT"));
                return dto ;
            }
    	}catch(Exception e)
    	{
    		e.printStackTrace(); 
    	}
    	return null ;
    }
     
          // --- Delete Emprunt By ser ---
    public static boolean deleteEmpruntById(int idEmprunt) throws SQLException
    {
    	try
    	{  
    		conn.setAutoCommit(false);
    		
    		EmpruntModel dto = findById(idEmprunt) ;
    		if (dto == null) return false;
    		
    		   // transacion for update book disponibility
    		if(dto.getStatut().equals("EN_COURS"))
    		{
    			boolean updateDisp = LivreDao.incrementeDispLivre(conn, dto.getIsbn());
                if (!updateDisp) {
                    conn.rollback(); 
                    return false;
                }
    		}
    		
    		// transaction for delete book
            try (PreparedStatement pSt = conn.prepareStatement(_DELETE_EMPRUNT)) {
                pSt.setInt(1, idEmprunt);
                pSt.executeUpdate();
            }
            
            conn.commit();
            return true;
            
    	}catch (Exception e) {
            if (conn != null) conn.rollback(); 
            e.printStackTrace();
            return false;
            
        } finally {
            if (conn != null) conn.setAutoCommit(true) ;
        }
    }

    // --- LISTER LES EMPRUNTS ACTIFS ---
    public static List<EmpruntModel> getEmpruntsEnCours() {
    	conn = ConnectDB.getConnection();
        List<EmpruntModel> liste = new ArrayList<>();
        try (
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(_SELECT_EN_COURS)) {
            
            while (rs.next()) {
                EmpruntModel dto = new EmpruntModel();
                dto.setIdEmprunt(rs.getInt("ID_EMPRUNT"));
                dto.setIsbn(rs.getString("ISBN"));
                dto.setLivreTitre(rs.getString("TITRE"));
                dto.setUserName(rs.getString("firstname") + " " + rs.getString("lastname"));
                dto.setDateSortie(rs.getDate("DATE_SORTIE"));
                dto.setDateRetourMax(rs.getDate("DATE_RETOUR_MAX"));
                dto.setStatut(rs.getString("STATUT"));
                liste.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    
    // --- LISTER LES EMPRUNTS d'une Adhéron ---
    public static List<EmpruntModel> getAllEmpruntsByUserId(int idUser) {
    	conn = ConnectDB.getConnection();
        List<EmpruntModel> liste = new ArrayList<>();
        try {
             PreparedStatement st = conn.prepareStatement(_SELECT_ALL_BY_ADHERON) ;
        	 st.setInt(1, idUser);
             ResultSet rs = st.executeQuery() ;
            
            while (rs.next()) {
                EmpruntModel dto = new EmpruntModel();
                dto.setIdEmprunt(rs.getInt("ID_EMPRUNT"));
                dto.setIsbn(rs.getString("ISBN"));
                dto.setLivreTitre(rs.getString("TITRE"));
                dto.setUserName(rs.getString("firstname") + " " + rs.getString("lastname"));
                dto.setDateSortie(rs.getDate("DATE_SORTIE"));
                dto.setDateRetourMax(rs.getDate("DATE_RETOUR_MAX"));
                dto.setStatut(rs.getString("STATUT"));
                liste.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    
    // --- LISTER LES EMPRUNTS d'une Adhéron ---
    public static List<EmpruntModel> getAllEmprunts() {
    	conn = ConnectDB.getConnection();
        List<EmpruntModel> liste = new ArrayList<>();
        try {
             Statement st = conn.createStatement() ;
             ResultSet rs = st.executeQuery(_SELECT_ALL) ;
            
            while (rs.next()) {
                EmpruntModel dto = new EmpruntModel();
                dto.setIdEmprunt(rs.getInt("ID_EMPRUNT"));
                dto.setIsbn(rs.getString("ISBN"));
                dto.setLivreTitre(rs.getString("TITRE"));
                dto.setUserName(rs.getString("firstname") + " " + rs.getString("lastname"));
                dto.setDateSortie(rs.getDate("DATE_SORTIE"));
                dto.setDateRetourMax(rs.getDate("DATE_RETOUR_MAX"));
                dto.setStatut(rs.getString("STATUT"));
                liste.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    
    
    // --- verify  Adheron already Emprunt Book for not delete this adheron ---
    
    public static boolean verifyAdheronEmprunt(int Id_user)
    {
    	try
    	{
    		PreparedStatement pSt = conn.prepareStatement(VERIFY_ADHERON_EMPRUNT);
    		pSt.setInt(1, Id_user);
    		ResultSet rSet = pSt.executeQuery() ;
    		
    		while(rSet.next())
    		{
    			int countEmprunt = rSet.getInt(1);
    			return countEmprunt >  0 ;
    		}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return false ;
    }
    
    // --- verify  Adheron already Emprunt Book for not delete this adheron ---
    
    public static boolean verifyBookEmprunt(String  isbn)
    {
    	try
    	{
    		PreparedStatement pSt = conn.prepareStatement(VERIFY_BOOK_EMPRUNT);
    		pSt.setString(1, isbn);
    		ResultSet rSet = pSt.executeQuery() ;
    		
    		while(rSet.next())
    		{
    			int countEmprunt = rSet.getInt(1);
    			return countEmprunt >  0 ;
    		}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return false ;
    }
}