package smartspace.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

	private String name;
	private String description;
	private int importance;
	private String dateToCompleteTask;
	private String dateAddedTask;
	private int durationOfTask;
	
	
	public Task(String name, String description, int importance, String dateToCompleteTask,
			int durationOfTask) {
		super();
		this.name = name;
		this.description = description;
		this.importance = importance;
		this.dateToCompleteTask = dateToCompleteTask;
		this.dateAddedTask = getCurrentDate();
		this.durationOfTask = durationOfTask;
		
	}


	public Task()
	{	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}

	public String getDateToCompleteTask() {
		return dateToCompleteTask;
	}

	public void setDateToCompleteTask(String dateToCompleteTask) {
		this.dateToCompleteTask = dateToCompleteTask;
	}

	public String getDateAddedTask() {
		return dateAddedTask;
	}

	public void setDateAddedTask(String dateAddedTask) {
		this.dateAddedTask = dateAddedTask;
	}

	public int getDurationOfTask() {
		return durationOfTask;
	}

	public void setDurationOfTask(int durationOfTask) {
		this.durationOfTask = durationOfTask;
	}
	
	private String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}


	@Override
	public String toString() {
		return "Task [name=" + name + ", description=" + description + ", importance=" + importance
				+ ", dateToCompleteTask=" + dateToCompleteTask + ", dateAddedTask=" + dateAddedTask
				+ ", durationOfTask=" + durationOfTask + "]";
	}
	
	
}
