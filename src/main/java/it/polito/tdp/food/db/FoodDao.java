package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Arco;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(Map<Integer, Food> idMap){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;

			List<Food> list = new ArrayList<>() ;

			ResultSet res = st.executeQuery() ;

			while(res.next()) {
				try {

					Food f = new Food(res.getInt("food_code"), res.getString("display_name"));
					list.add(f);
					idMap.put(f.getFood_code(), f);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;

			List<Condiment> list = new ArrayList<>() ;

			ResultSet res = st.executeQuery() ;

			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}

	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;

			List<Portion> list = new ArrayList<>() ;

			ResultSet res = st.executeQuery() ;

			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public List<Food> getVertici(int porzioni, Map<Integer, Food> idMap) {
		String sql = "select `food_code` as id, count(distinct `portion_id`) " + 
				"from `portion` " + 
				"group by`food_code` " + 
				"having count(distinct `portion_id`)=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			
			List<Food> list = new ArrayList<>() ;

			ResultSet res = st.executeQuery() ;

			while(res.next()) {
				try {
					list.add(idMap.get(res.getInt("food_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public List<Arco> getArchi(Map<Integer, Food> idMap) {
		String sql = "select fc1.`food_code` as uno, fc2.`food_code` as due, avg(c.`condiment_calories`) as peso " + 
				"from food_condiment as fc1, food_condiment as fc2, condiment as c " + 
				"where fc1.`condiment_code`=fc2.`condiment_code` " + 
				"and c.`condiment_code`= fc1.`condiment_code` " + 
				"and c.`condiment_code`= fc2.`condiment_code` " + 
				"and fc1.`food_code`<> fc2.food_code " + 
				"group by fc1.`food_code`, fc2.`food_code`" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Arco> list = new ArrayList<>() ;

			ResultSet res = st.executeQuery() ;

			while(res.next()) {
				try {
					list.add(new Arco(idMap.get(res.getInt("uno")), idMap.get(res.getInt("due")), res.getDouble("peso")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
