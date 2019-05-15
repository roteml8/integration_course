package smartspace.infra;

	
	public class ImportFromLocalException extends RuntimeException {

		private static final long serialVersionUID = 3097405950370000074L;
		int indx;
		
		public ImportFromLocalException() {
		}

		public ImportFromLocalException(String message) {
			super(message);
		}

		public ImportFromLocalException(Throwable cause) {
			super(cause);
		}

		public ImportFromLocalException(String message, Throwable cause) {
			super(message, cause);
		}

		public String toString() {
			return this.getStackTrace() + "/n Can't import from local smartspace! check your array at location " + indx;
		}

	}


