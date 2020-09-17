package eis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import eis.iilang.Percept;

public class PerceptUpdate implements Serializable, Cloneable {
	private static final long serialVersionUID = 5046532402504828412L;

	private final List<Percept> addlist;
	private final List<Percept> dellist;

	public PerceptUpdate() {
		this(new LinkedList<>(), new LinkedList<>());
	}

	public PerceptUpdate(final List<Percept> addlist, final List<Percept> dellist) {
		this.addlist = addlist;
		this.dellist = dellist;
	}

	public List<Percept> getAddList() {
		return Collections.unmodifiableList(this.addlist);
	}

	public List<Percept> getDeleteList() {
		return Collections.unmodifiableList(this.dellist);
	}

	public boolean isEmpty() {
		return (this.addlist.isEmpty() && this.dellist.isEmpty());
	}

	public void merge(final PerceptUpdate other) {
		this.addlist.addAll(other.addlist);
		this.dellist.addAll(other.dellist);
	}

	@Override
	public Object clone() {
		return new PerceptUpdate(new ArrayList<>(this.addlist), new ArrayList<>(this.dellist));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.addlist == null) ? 0 : this.addlist.hashCode());
		result = prime * result + ((this.dellist == null) ? 0 : this.dellist.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof PerceptUpdate)) {
			return false;
		}

		final PerceptUpdate other = (PerceptUpdate) obj;
		if (this.addlist == null) {
			if (other.addlist != null) {
				return false;
			}
		} else if (!this.addlist.equals(other.addlist)) {
			return false;
		}

		if (this.dellist == null) {
			if (other.dellist != null) {
				return false;
			}
		} else if (!this.dellist.equals(other.dellist)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("PerceptUpdate [add=").append(this.addlist).append(", del=").append(this.dellist).append("]");
		return builder.toString();
	}
}
