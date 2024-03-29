package org.sodeja.swing.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import org.sodeja.functional.Pair;
import org.sodeja.swing.context.ApplicationContext;

public abstract class MultyLineTableCellRenderer<T extends ApplicationContext, R> extends JTextArea implements TableCellRenderer {

	protected T ctx;
	private Pair<Color, Color> scheme;
	
	public MultyLineTableCellRenderer(T ctx) {
		this.ctx = ctx;
		scheme = RendererUtils.makeColors(ctx, this);
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	}
	
	@SuppressWarnings("unchecked")
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		RendererUtils.updateView(this, scheme, row);
		
		String text = getTextDelegate((R) value);
		setText(text);
		
		RendererUtils.setProperBorder(this, isSelected, hasFocus);
		
		return this;
	}

	public abstract String getTextDelegate(R t);
}
