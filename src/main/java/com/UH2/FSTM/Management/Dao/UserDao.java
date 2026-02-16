package com.UH2.FSTM.Management.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.UH2.FSTM.Management.Model.User;
import com.UH2.FSTM.Management.Dto.LoginDTO; 
import com.UH2.FSTM.Management.Dto.UserDTO; 
import com.UH2.FSTM.ConnexionDB.ConnectDB;

public class UserDao {

    private static final String _queryInsert			= "INSERT INTO USERS (firstname, lastname, username, email, password, role) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String _querySelectAll 		= "SELECT * FROM USERS";
    private static final String _querySelectByEmail 	= "SELECT * FROM USERS WHERE email =? and ID !=? ";
    private static final String _querySelectByUserName 	= "SELECT * FROM USERS WHERE username =? and ID !=?";
    private static final String _queryUpdate 			= "UPDATE USERS SET firstname = ?, lastname = ? , email = ? WHERE ID = ?";
    private static final String _queryDelete 			= "DELETE FROM USERS WHERE ID = ?";
    private static final String _querySelectAdherons    = "SELECT ID , LASTNAME , FIRSTNAME ,USERNAME , EMAIL FROM USERS WHERE ROLE != 'admin'" ;
    
    private static String hashPassword(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // --- CREATE ---
    public static boolean create(User user) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(_queryInsert)) {
            
            ps.setString(1, user.getFirstname());
            ps.setString(2, user.getLastname());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getEmail());
            ps.setString(5, hashPassword(user.getPassword()));
            ps.setString(6, "user");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // --- READ ADHERONS ---
    public static List<UserDTO> FindAllAherons()
    {
    	List<UserDTO> users = new ArrayList<>();
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(_querySelectAdherons)) {
        	
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToDto(rs));
                }
                return users ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;   	
    }

    
    // --- READ (Find by Email) ---
    public static List<UserDTO> getAll() {
    	
    	List<UserDTO> users = new ArrayList<>();
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(_querySelectAll)) {
        	
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToDto(rs));
                }
                return users ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    // --- READ (Find by Email) ---
    public static UserDTO findByUserName(int id , String userName) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(_querySelectByUserName)) {
            
            ps.setString(1, userName);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDto(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // --- READ (Find by Email) ---
    public static LoginDTO findByEmail(int id , String email) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(_querySelectByEmail)) {
            
            ps.setString(1, email);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	
					return new LoginDTO( rs.getInt("id"),
				             			 rs.getString("username") , 
				                         rs.getString("email"),
				                         rs.getString("Role"),
				                         rs.getString("password")
				                        );
                }
                
                return null ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- UPDATE ---
    public static boolean update(User user) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(_queryUpdate)) {
            
            ps.setString(1, user.getFirstname());
            ps.setString(2, user.getLastname());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- DELETE ---
    public static boolean deleteById(int id) {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(_queryDelete)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static UserDTO mapResultSetToDto(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setFirstName(rs.getString("firstname"));
        user.setLastName(rs.getString("lastname"));
        user.setUserName(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setUserID(rs.getInt("ID"));

        return user;
    }
}