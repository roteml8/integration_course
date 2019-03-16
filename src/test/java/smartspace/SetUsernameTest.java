package smartspace;

import smartspace.data.UserEntity;

public class SetUsernameTest {
	public static void main(String[] args) throws Exception{
		// Given nothing
				
		
		// When the user sets the UserEntity username to "test"
		UserEntity user = new UserEntity();
		user.setUsername("test");
		
		
		// Then the UserEntity username is changed to "test"
		String expected = "test";
		if(!user.getUsername().equals(expected)) {
			throw new Exception("Error while changing username. expected: " + expected + ", while actual: " + user.getUsername());
		}
		else 
			System.out.println("SetUsernameTest successful");
	}
}
