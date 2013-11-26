package org.mdbda.model;

public class FilteringPatternTemplateConstatns {
	public static String getBloomfiltering() {
		return BloomFiltering;
	}
	public static String getTopten() {
		return TopTen;
	}
	public static String getDistinct() {
		return Distinct;
	}
	public static String getSimpleMatcherFilter() {
		return SimpleMatcherFilter;
	}
	public static final String BloomFiltering  = "BloomFiltering";
	public static final String TopTen  = "TopTen";
	public static final String Distinct  = "Distinct";
	public static final String SimpleMatcherFilter  = "SimpleMatcherFilter";
}
