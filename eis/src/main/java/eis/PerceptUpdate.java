package eis;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import eis.iilang.Percept;

public class PerceptUpdate {
	private final List<Percept> addlist;
	private final List<Percept> dellist;

	public PerceptUpdate() {
		this(new LinkedList<>(), new LinkedList<>());
	}

	public PerceptUpdate(List<Percept> addlist, List<Percept> dellist) {
		this.addlist = addlist;
		this.dellist = dellist;
	}

	public List<Percept> getAddList() {
		return Collections.unmodifiableList(this.addlist);
	}

	public List<Percept> getDeleteList() {
		return Collections.unmodifiableList(this.dellist);
	}

	public void merge(PerceptUpdate other) {
		this.addlist.addAll(other.addlist);
		this.dellist.addAll(other.dellist);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addlist == null) ? 0 : addlist.hashCode());
		result = prime * result + ((dellist == null) ? 0 : dellist.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof PerceptUpdate)) {
			return false;
		}
		PerceptUpdate other = (PerceptUpdate) obj;
		if (addlist == null) {
			if (other.addlist != null) {
				return false;
			}
		} else if (!addlist.equals(other.addlist)) {
			return false;
		}
		if (dellist == null) {
			if (other.dellist != null) {
				return false;
			}
		} else if (!dellist.equals(other.dellist)) {
			return false;
		}
		return true;
	}
}
