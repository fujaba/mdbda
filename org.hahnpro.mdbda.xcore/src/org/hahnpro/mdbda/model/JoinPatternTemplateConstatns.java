package org.hahnpro.mdbda.model;

public class JoinPatternTemplateConstatns {
	public static String getCartesianproduct() {
		return CartesianProduct;
	}
	public static String getCompositejoin() {
		return CompositeJoin;
	}
	public static String getReducesidejoin() {
		return ReduceSideJoin;
	}
	public static String getReplicatedjoin() {
		return ReplicatedJoin;
	}
	public static final String CartesianProduct = "CartesianProduct";
	public static final String CompositeJoin = "CompositeJoin";
	public static final String ReduceSideJoin = "ReduceSideJoin";
	public static final String ReplicatedJoin = "ReplicatedJoin";
}
