package smartspace.data.util;

import java.util.Random;


import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@Component
public class FakeUserGenerator implements UserGenerator {

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
	public UserEntity getUser() {

		String generatedString = RandomString.make();

		String userSmartspace = smartspace;
		String userEmail = generatedString + "missroteml@gmail.com";
		String userName = generatedString + "rotemlevi";
		String userAvatar = generatedString + "cat";

		UserRole userRole = UserRole.values()[new Random().nextInt(UserRole.values().length)];

		long userPoints = new Random().nextLong();
		UserEntity fakeUser = this.factory.createNewUser(userEmail, userSmartspace, userName, userAvatar, userRole,
				userPoints);

		return fakeUser;
	}

}
