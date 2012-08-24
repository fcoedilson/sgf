/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.job;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;


public class ListenerJobArena implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}


	@SuppressWarnings("static-access")
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = new JobDetail("JobArena", scheduler.DEFAULT_GROUP, JobArena.class);
			CronTrigger cronTrigger = new CronTrigger("TriggerTransmissoesArena", scheduler.DEFAULT_GROUP, "0 0/10 * * * ?");
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
