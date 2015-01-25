package com.pankrator.raytracer;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

public class VectorTest {
	
	public static final double A = 1;
	public static final double B = 2;
	public static final double C = 3;
	
	
	@Test
	public void test_vector_length_with_valid_values() {
		Vector v = new Vector(A, B, C);
		assertTrue(v.length() == Math.sqrt(A*A + B*B + C*C));
	}
	
	@Test
	public void test_vector_length_squered() {
		Vector v = new Vector(A, B, C);
		assertThat(v.lengthSqr(), is(A*A + B*B + C*C));
	}
	
	@Test
	public void test_vector_make_zero() {
		Vector v = new Vector(A, B, C);
		v.makeZero();
		assertTrue(v.x == 0 && v.y == 0 && v.z == 0);
	}
	
	@Test
	public void test_vector_initialization() {
		Vector v = new Vector(A, B, C);
		assertTrue(v.x == A && v.y == B && v.z == C);
	}
	
	@Test
	public void test_vector_scaling() {
		Vector v = new Vector(A, B, C);
		v.scale(2);
		assertTrue(v.x == 2 * A);
		assertTrue(v.y == 2 * B);
		assertTrue(v.z == 2 * C);
	}
	
	@Test
	public void test_vector_addition() {
		Vector v = new Vector(A, B, C);
		Vector v2 = new Vector();
		v.add(v2);
		
		assertThat(v, is(new Vector(A, B, C)));
	}
	
	@Test
	public void test_vector_subtract() {
		Vector v = new Vector(A, B, C);
		Vector v2 = new Vector(1, 1, 1);
		v.subtract(v2);
		
		assertThat(v, is(new Vector(A - 1, B - 1, C - 1)));
	}
	
	@Test
	public void test_vector_normalization() {
		Vector v = new Vector(A, B, C);
		
		assertThat(v.length(), not(1d));
		
		v.normalize();
		assertThat(v.length(), is(1d));
	}
	
	@Test
	public void test_vector_dot_product() {
		Vector actual = new Vector(A, B, C);
		Vector v = new Vector(3, 5, 8);
		
		double expected = A * 3 + B * 5 + C * 8;
		assertThat(actual.dot(v), is(expected));
	}	
	
}
