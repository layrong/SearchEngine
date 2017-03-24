package cn.ict.util;

public class MathUtil {
	
	/*
	 * param value: 需要运算的值
	 * param base: 对数的底
	 * 
	 * return dobule: 取对数后的结果
	 * 
	 * */
	static public double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}
}
