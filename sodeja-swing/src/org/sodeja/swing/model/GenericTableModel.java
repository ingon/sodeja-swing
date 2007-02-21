package org.sodeja.swing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.sodeja.collections.ListUtils;
import org.sodeja.functional.Function1;

public class GenericTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = -7947554586824053358L;
	
	private List<T> data;
	private List<Function1<Object, T>> columnFunctors;

	public GenericTableModel(Function1<Object, T>... functors) {
		data = new ArrayList<T>();
		columnFunctors = ListUtils.asList(functors);
	}
	
	public int getColumnCount() {
		return columnFunctors.size();
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		T value = data.get(rowIndex);
		return columnFunctors.get(columnIndex).execute(value);
	}
	
	public void copyTo(Collection<T> target) {
		target.clear();
		target.addAll(data);
	}
	
	public void copyFrom(Collection<T> target) {
		data.clear();
		data.addAll(target);
	}
	
	public void addObject(T object) {
		data.add(object);
		fireTableDataChanged();
	}
	
	public void removeObject(T object) {
		data.remove(object);
		fireTableDataChanged();
	}
	
	public T getObject(int index) {
		return data.get(index);
	}
	
	public void removeObject(int index) {
		data.remove(index);
		fireTableDataChanged();
	}
}
