package translator.algebraTree.bgpTree;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.shared.PrefixMapping;
import statistics.DatabaseStatistics;

public class TriplePattern {
	private String subject;
	private String predicate;
	private String object;
	private ElementType subjectType;
	private ElementType objectType;
	private ElementType predicateType;
	//private boolean isComplex = false;

	// construct from Jena triple
	public TriplePattern(final Triple triple, final PrefixMapping prefixes) {
		// extract and set the subject
		if (triple.getSubject().isVariable()) {
			subjectType = ElementType.VARIABLE;
			subject = triple.getSubject().toString();
		} else {
			subjectType = ElementType.CONSTANT;
			//logger.info("Subject of the triple: " + triple.getSubject());
			if (triple.getSubject().isLiteral()) {
				subject = triple.getSubject().toString();
			} else {
				subject = "<" + triple.getSubject().getURI() + ">";
			}
		}

		// extract and set the predicate
		if (triple.getPredicate().isVariable()) {
			predicateType = ElementType.VARIABLE;
			predicate = triple.getPredicate().toString();
		} else {
			predicateType = ElementType.CONSTANT;
			if (triple.getPredicate().isLiteral()) {
				predicate = triple.getPredicate().toString();
			} else {
				predicate = "<" + triple.getPredicate().toString() + ">";
			}
		}

		// extract and set the object
		if (triple.getObject().isVariable()) {
			objectType = ElementType.VARIABLE;
			object = triple.getObject().toString(prefixes);
		} else {
			objectType = ElementType.CONSTANT;
			//logger.info("Object of the triple: " + triple.getObject());
			if (triple.getObject().isLiteral()) {
				object = triple.getObject().toString();
			} else {
				object = "<" + triple.getObject().getURI() + ">";
			}
		}
	}

	@Override
	public String toString() {
		return String.format("(%s) (%s) (%s)", subject, predicate, object);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public String getObject() {
		return object;
	}

	public ElementType getSubjectType() {
		return subjectType;
	}

	public ElementType getObjectType() {
		return objectType;
	}

	public ElementType getPredicateType() {
		return predicateType;
	}

	boolean isComplex(final DatabaseStatistics statistics, final String predicate) {
		return statistics.getProperties().get(predicate).isComplex();
	}

	boolean isInverseComplex(final DatabaseStatistics statistics, final String predicate) {
		return statistics.getProperties().get(predicate).isInverseComplex();
	}
}