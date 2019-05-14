package smartspace.data.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;
@Component
public class FakeActionGenerator implements ActionGenerator{

	private EntityFactory factory;
	private String smartspace;

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	

	@Override
	public ActionEntity getAction() {
		String generatedString = RandomString.make();
		String actionId= "565"+generatedString;
		String elementSmartspcae = "element" + generatedString;
		String type = "type" + generatedString;
		String creatorSmartspace = smartspace;
		String creatorEmail = "email" + generatedString;
		Date creationTimeStamp = new Date();
		
		Map<String,Object> moreAttributes = new HashMap<>();
		ActionEntity fakeAction =this.factory.createNewAction(actionId, elementSmartspcae, type, creationTimeStamp, creatorEmail,
				creatorSmartspace, moreAttributes);
		return fakeAction;
	}

}
