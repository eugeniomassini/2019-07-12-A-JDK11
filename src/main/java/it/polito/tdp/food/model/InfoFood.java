package it.polito.tdp.food.model;

public class InfoFood {
	
	private Food food;
	private Double calorie;
	public InfoFood(Food food, Double calorie) {
		super();
		this.food = food;
		this.calorie = calorie;
	}
	@Override
	public String toString() {
		return food.getDisplay_name() + ", calorie=" + calorie;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	public Double getCalorie() {
		return calorie;
	}
	public void setCalorie(Double calorie) {
		this.calorie = calorie;
	}
	
	
	
	

}
