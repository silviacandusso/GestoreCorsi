package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;

public class CorsoDAO {
	
	public List<Corso> getCorsiByPeriodo(Integer periodo){
		String sql="SELECT * FROM corso WHERE pd=?";
		List<Corso> risultato= new ArrayList<Corso>();
		
		try {
			Connection conn= DBConnect.getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			st.setInt(1,periodo);
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				Corso c= new Corso(res.getString("Codins"), res.getInt("crediti"), res.getString("nome"), res.getInt("pd"));
				risultato.add(c);
			}
			res.close();
			st.close();
			conn.close();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		return risultato;
	}
	
	public Map<Corso,Integer> getIscrittiByPeriodo(Integer pd){
	
		Map<Corso,Integer> risultato= new HashMap<Corso,Integer>();
		String sql="SELECT  c.codins, c.nome, c.crediti, c.pd, COUNT(*) AS tot "
				+ "FROM corso c, iscrizione i "
				+ "WHERE c.codins=i.codins AND c.pd=? "
				+ "GROUP BY c.codins, c.nome, c.crediti, c.pd "
				+ "ORDER BY tot DESC";
		try {
			Connection conn= DBConnect.getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			st.setInt(1,pd);
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				int tot;
				Corso c= new Corso(res.getString("Codins"), res.getInt("crediti"), res.getString("nome"), res.getInt("pd"));
				tot=res.getInt("tot");
				risultato.put(c,tot);
			}
			res.close();
			st.close();
			conn.close();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		return risultato;
	}


}
