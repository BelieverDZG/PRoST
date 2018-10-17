package extVp;

/**
 * Class containing containing combinations of literals. Used when an ExtVP does not contain all possible entries, but
 * each VP table is filtered by a literal before creating the ExtVP table
 *
 */
public class LiteralsTuple {
	String outerLiteral;
	String innerLiteral;

	public LiteralsTuple(final String outerLiteral, final String innerLiteral) {
		super();
		this.outerLiteral = outerLiteral;
		this.innerLiteral = innerLiteral;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((innerLiteral == null) ? 0 : innerLiteral.hashCode());
		result = prime * result + ((outerLiteral == null) ? 0 : outerLiteral.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LiteralsTuple other = (LiteralsTuple) obj;
		if (innerLiteral == null) {
			if (other.innerLiteral != null) {
				return false;
			}
		} else if (!innerLiteral.equals(other.innerLiteral)) {
			return false;
		}
		if (outerLiteral == null) {
			if (other.outerLiteral != null) {
				return false;
			}
		} else if (!outerLiteral.equals(other.outerLiteral)) {
			return false;
		}
		return true;
	}
}