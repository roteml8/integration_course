package smartspace.infra;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
class ProgressReportForManagers {
	
	private SendEmailTLS mailSender;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;

	@Autowired
	public void mailSender(SendEmailTLS mailSender) {
		this.mailSender = mailSender;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	
	 private static final Logger logger = LoggerFactory.getLogger(ProgressReportForManagers.class); //for testing
	 private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); //for testing
	
	//@Scheduled(cron = "0 0 9 * * ?") //executed at 9:00 am everyday
	@Scheduled(cron = "0 * * * * ?") //executed every minute. for testing.
	public void informManagers()
	{
		
		

		int defaultSize = 10, defaultPage = 0;
		
		logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));//for testing
		
		List <UserEntity>  managers = userDao.readUserWithRole(UserRole.MANAGER, defaultSize, defaultPage);
		List <ElementEntity>  elements = elementDao.readAll();
				
		for(UserEntity manager : managers)
		{
			List <ElementEntity> managersElements = elementDao.readElementWithCreatorEmail(manager.getUserEmail(), defaultSize, defaultPage);
			if(managersElements.isEmpty())
			{
				mailSender.sendMail(
						manager.getUserEmail(),
						"This is an automated tesk progress report for: " + manager.getUsername(),
						"Is this a joke? You have no tesks! What am i paying you for? Go think about stuff to do.");
			}
			else
			{
				int expiredCounter = 0;
				for(ElementEntity element : managersElements)
					if(element.isExpired())
						expiredCounter++;
					
				if(expiredCounter == managersElements.size())
					mailSender.sendMail(
							manager.getUserEmail(),
							"This is an automated tesk progress report for: " + manager.getUsername(),
							"Well done! All tesks completed. What am i paying you for? Go think about more stuff to do.");
			
				else
					mailSender.sendMail(
						manager.getUserEmail(),
						"This is an automated tesk progress report for: " + manager.getUsername(),
						prepareElementsForEmail
						(elementDao.readElementWithCreatorEmail
								(manager.getUserEmail(), defaultSize, defaultPage)));
			}
		}
		
		/*
		for(ElementEntity element : elements)
		{
			
		}
		*/
	}
	
	private String prepareElementsForEmail (List <ElementEntity>  elements)
	{
		String mailReadyMessageText = "";
		for(ElementEntity element : elements)
		{
			mailReadyMessageText +=
			" Element name: " + element.getName() + "\n" +
			" Element type: " + element.getType() + "\n" +
			" Element attributes: " + element.getMoreAttributes() + "\n" +
			" Element location: " + element.getLocation().toString() + "\n" +
			" Element active status: " + element.isExpired() + "\n" +
			" Element key: " + element.getKey() + "\n\n\n";
		}
		
		return mailReadyMessageText;
	}

}
