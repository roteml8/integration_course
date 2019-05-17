package smartspace.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.data.ActionEntity;

@Component
public class EchoPlugin implements Plugin {
	
	
	@Override
	public ActionEntity process(ActionEntity action) {
		action.getMoreAttributes().put("echo", "echo");
		return action;
	}

}
