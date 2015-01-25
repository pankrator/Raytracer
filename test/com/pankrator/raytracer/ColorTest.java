package com.pankrator.raytracer;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

public class ColorTest {
	
	@Test
	public void test_add_two_colors() {
		Color c1 = new Color(0.5f, 0.5f, 0.5f);
		Color c2 = new Color(0.2f, 0.3f, 0.5f);
		
		c1.add(c2);
		assertThat(c1, is(new Color(0.7f, 0.8f, 1f)));
	}
	
	@Test
	public void test_multiply_two_colors() {
		Color c1 = new Color(0.5f, 0.5f, 0.5f);
		Color c2 = new Color(0.2f, 0.3f, 0.5f);
		
		c1.multiply(c2);
		assertThat(c1, is(new Color(0.1f, 0.15f, 0.25f)));
	}
	
	@Test
	public void test_multiply_color_with_number() {
		Color c1 = new Color(0.5f, 0.5f, 0.2f);
		
		c1.multiply(2);
		assertThat(c1, is(new Color(1f, 1f, 0.4f)));
	}
}
