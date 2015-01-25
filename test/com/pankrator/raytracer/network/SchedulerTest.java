package com.pankrator.raytracer.network;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.awt.Dimension;

import org.junit.Test;

public class SchedulerTest {

	public static final Dimension dimension = new Dimension(1080, 960);

	@Test
	public void test_get_new_task() {
		Scheduler s = new Scheduler(dimension);
		Task task = s.getNextTask(0);

		assertThat(task, notNullValue());
		assertThat(s.getNumberOfWaitingTasks(), is(1));
	}

	@Test
	public void test_generate_all_tasks() {
		Scheduler s = new Scheduler(dimension);
		Task t = null;
		while(( t = s.getNextTask(0)) != null) {
			assertThat(t, notNullValue());
		}
		
		assertThat(s.getNumberOfWaitingTasks(), is(72));
	}
}
