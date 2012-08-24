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

/**
 * @author Deivid
 * @since 29/01/2010
 */
public class ListenerJob implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	/**
	 * Agendamento do fechamento autom�tico de bombas para as 18:00
	 */
	@SuppressWarnings("static-access")
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = new JobDetail("JobDiarioBomba", scheduler.DEFAULT_GROUP, JobDiarioBomba.class);
			CronTrigger cronTrigger = new CronTrigger("TriggerDiarioBomba", scheduler.DEFAULT_GROUP, "0 0 18 ? * * ");
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
