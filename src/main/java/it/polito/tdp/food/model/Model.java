package it.polito.tdp.food.model;

import java.lang.ProcessHandle.Info;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private List<Food> cibi;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	private FoodDao dao;

	public Model() {
		dao = new FoodDao();
		idMap = new HashMap<Integer, Food>();
		dao.listAllFoods(idMap);
	}

	public void creaGrafo(int porzioni) {

		grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		Graphs.addAllVertices(this.grafo, dao.getVertici(porzioni, idMap));

		for(Arco a:dao.getArchi(idMap)) {
			DefaultWeightedEdge e = this.grafo.getEdge(a.getF1(), a.getF2());

			if(e==null) {
				//TODO Sono lo stesso oggetto anche se arriva dalla stessa idMap ma passa per una lista?
				if(this.grafo.vertexSet().contains(a.getF1())&&this.grafo.vertexSet().contains(a.getF2()))
					Graphs.addEdge(this.grafo, a.getF1(), a.getF2(), a.getPeso());
			}
		}

		System.out.format("#Vertici: %d\n#Archi: %d", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
		
	}

	public List<Food> getCibi() {

		cibi = new ArrayList<>(this.grafo.vertexSet());

		Collections.sort(cibi, new Comparator<Food>() {

			@Override
			public int compare(Food o1, Food o2) {
				// TODO Auto-generated method stub
				return o1.getDisplay_name().compareTo(o2.getDisplay_name());
			}

		});


		return cibi;
	}

	public List<InfoFood> getCalorie(Food cibo) {
		
		List<InfoFood> output = new ArrayList<>();
		
		for(Food f: Graphs.neighborListOf(this.grafo, cibo)) {
			output.add(new InfoFood(f, this.grafo.getEdgeWeight(this.grafo.getEdge(cibo, f))));
		}
		
		Collections.sort(output, new Comparator<InfoFood>() {

			@Override
			public int compare(InfoFood o1, InfoFood o2) {
				// TODO Auto-generated method stub
				return -Double.compare(o1.getCalorie(), o2.getCalorie());
			}
			
		});
		
		return output;
	}

}
