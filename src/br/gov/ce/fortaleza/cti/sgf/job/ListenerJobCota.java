package br.gov.ce.fortaleza.cti.sgf.job;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
/**
 * Listener para o job da cota mensal
 * 
 * @author Lafitte
 * @since 30/07/2010
 *
 */
public class ListenerJobCota implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Agendamento da reinicializa��o das cotas dispon�veis no m�s
	 * para os ve�culos
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = new JobDetail("JobCotaMensal", scheduler.DEFAULT_GROUP, JobCotaMensal.class);
			CronTrigger cronTrigger = new CronTrigger("TriggerCotaMensal", scheduler.DEFAULT_GROUP, "0 0 0 1 * ?");
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
