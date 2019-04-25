package smartspace.data.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

@Component

public class FakeElementGenerator implements ElementGenerator {
	
	private EntityFactory factory;
	private String smartspace;

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Value("${name.of.Smartspace:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}


	@Override
	public ElementEntity getElement() {
		
		String generatedString = RandomString.make();
		String name = "element" + generatedString;
		String type = "type" + generatedString;
		String creatorSmartspace = smartspace;
		String creatorEmail = "email" + generatedString;
		Date creationTimeStamp = new Date();
		Map<String,Object> moreAttributes = new HashMap<>();
		double x = new Random().nextDouble();
		double y = new Random().nextDouble();
		Location location = new Location(x,y);
		boolean expired = new Random().nextBoolean();
		
		ElementEntity fakeElement = this.factory.createNewElement(name, type, location, creationTimeStamp, creatorEmail, creatorSmartspace, expired, moreAttributes);
		return fakeElement;
	}

}
