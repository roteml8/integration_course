package smartspace.data.util;

	
	public class ImportFromLocalException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3097405950370000074L;
		int indx;

		public ImportFromLocalException(int indx) {
			super();
			this.indx = indx;
		}

		public String toString() {
			return this.getStackTrace() + "/n Can't import from local smartspace! check your array at location " + indx;
		}

	}

