package smartspace.dao.rdb;

public class GeneratedId {
	private static long ElementId = 0;
	private static long ActionId = 0;
	public GeneratedId() {
	}

	public static long getNextElementValue() {
		ElementId++;
		return ElementId;
	}
	
	public static long getNextActionValue() {
		ActionId++;
		return ActionId;
	}
	
	public static void setElementId(long num) {
		ElementId = num;
	}
	
	public static void setActionId(long num) {
		ActionId = num;
	}
	
	public static void resetId() {
		ElementId = 0;
		ActionId = 0;
	}

}
