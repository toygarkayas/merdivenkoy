package webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
public class ReactiveWebSocketConfiguration {
	
    @Autowired
    @Qualifier("ReactiveWebSocketHandler")
    private WebSocketHandler webSocketHandler;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public HandlerMapping webSocketHandlerMapping() {

        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/event-emitter", webSocketHandler);
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
    
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
    	System.out.println("springBeanJobFactory is working");
    	AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public Scheduler jobScheduler() throws SchedulerException, IOException {
    	System.out.println("jobScheduler is working");
    	SchedulerFactoryBean factory = schedulerFactoryBean();
    	Scheduler scheduler = factory.getScheduler();
        scheduler.scheduleJob(jobDetail(),jobTrigger());
        scheduler.start();
        return scheduler;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
    	System.out.println("schedulerFactoryBean is working");
    	SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(springBeanJobFactory());
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    public Properties quartzProperties() throws IOException {
    	System.out.println("quartzProperties is working");
    	PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    
    @Bean
    public JobDetail jobDetail() {
    	System.out.println("jobDetail is working");
    	return JobBuilder.newJob(JobService.class).withIdentity("getJenkinsContentJob")
                .withDescription("jenkinsContent").storeDurably().build();
    }

    @Bean
    public Trigger jobTrigger() {
        System.out.println("jobTrigger is working");
    	return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("getJenkinsContentTrigger")
                .withDescription("gettingJenkinsContent")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .build();               
//.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))
    }
}