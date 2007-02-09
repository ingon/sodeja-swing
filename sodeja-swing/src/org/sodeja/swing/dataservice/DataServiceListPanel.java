package org.sodeja.swing.dataservice;

import java.awt.GridBagLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.sodeja.dataservice.DataService;
import org.sodeja.swing.ButtonBarFactory;
import org.sodeja.swing.ComponentUtils;
import org.sodeja.swing.GridBag;
import org.sodeja.swing.component.ApplicationPanel;
import org.sodeja.swing.component.action.CallLocalMethodAction;
import org.sodeja.swing.component.form.FormPanel;
import org.sodeja.swing.context.ApplicationContext;
import org.sodeja.swing.event.CallLocalMethodListSelectionListener;
import org.sodeja.swing.resource.ResourceConstants;

public abstract class DataServiceListPanel<T extends ApplicationContext, R> extends ApplicationPanel<T> {

	private JList dataList;
	private JPanel dataPanel;
	
	private DataService<R> dataService;
	private DataServiceListModel<R> dataServiceListModel;
	
	private FormPanel<T, R> addFormPanel;
	private FormPanel<T, R> editFormPanel;
	private FormPanel<T, R> viewFormPanel;
	
	public DataServiceListPanel(T ctx, DataService<R> dataService) {
		super(ctx);
		this.dataService = dataService;
		
		initComponents();
	}

	protected final void initComponents() {
		this.setLayout(new GridBagLayout());
		
		this.add(ButtonBarFactory.constructHorizontalButtonsPane(
				new CallLocalMethodAction<T>(ctx, ResourceConstants.BTN_SEARCH, this, "searchCallback"),
				new CallLocalMethodAction<T>(ctx, ResourceConstants.BTN_ADD, this, "addCallback"),
				new CallLocalMethodAction<T>(ctx, ResourceConstants.BTN_EDIT, this, "editCallback"),
				new CallLocalMethodAction<T>(ctx, ResourceConstants.BTN_DELETE, this, "deleteCallback")),
			GridBag.leftButtonLine(0));
		
		JSplitPane contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		contentPanel.setDividerSize(3);

		dataServiceListModel = new DataServiceListModel<R>(dataService);
		dataList = new JList(dataServiceListModel);
		dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataList.addListSelectionListener(new CallLocalMethodListSelectionListener(this, "viewCallback"));
		
		JScrollPane scrollExercisesList = new JScrollPane(dataList);
		contentPanel.setLeftComponent(scrollExercisesList);

		dataPanel = new JPanel();
		dataPanel.setLayout(new GridBagLayout());
		contentPanel.setRightComponent(dataPanel);

		contentPanel.setDividerLocation(200);
		
		this.add(contentPanel, GridBag.bigPanel(1, 1));
	}

	public void setListRenderer(ListCellRenderer renderer) {
		dataList.setCellRenderer(renderer);
	}
	
	protected abstract FormPanel<T, R> createAddForm();	

	protected abstract FormPanel<T, R> createEditForm();	

	protected abstract FormPanel<T, R> createViewForm();	

	@SuppressWarnings("unchecked")
	private R getSelectedValue() {
		int selectedIndex = dataList.getSelectedIndex();
		if(selectedIndex < 0) {
			return null;
		}
		return (R) dataServiceListModel.getElementAt(selectedIndex);
	}

	// WARN used through call local method
	@SuppressWarnings("unused")
	private void addCallback() {
		if(addFormPanel == null) {
			addFormPanel = createAddForm();
		}
		
		addFormPanel.showForm();
		ComponentUtils.swapInContainer(dataPanel, addFormPanel, GridBag.bigPanel());
	}
	
	// WARN used through call local method
	@SuppressWarnings("unused")
	private void editCallback() {
		R value = getSelectedValue();
		if (value == null) {
			ComponentUtils.clearContainer(dataPanel);
			return;
		}
		
		if(editFormPanel == null) {
			editFormPanel = createEditForm();
		}
		
		editFormPanel.showForm(value);
		ComponentUtils.swapInContainer(dataPanel, editFormPanel, GridBag.bigPanel());
	}
	
	// WARN used through call local method
	@SuppressWarnings("unused")
	private void viewCallback() {
		R value = getSelectedValue();
		if (value == null) {
			ComponentUtils.clearContainer(dataPanel);
			return;
		}

		if(viewFormPanel == null) {
			viewFormPanel = createViewForm();
		}
		
		viewFormPanel.showForm(value);
		ComponentUtils.swapInContainer(dataPanel, viewFormPanel, GridBag.bigPanel());
	}
	
	// WARN used through call local method
	@SuppressWarnings("unused")
	private void deleteCallback() {
		R value = getSelectedValue();
		if(value == null) {
			return;
		}
		
		dataService.delete(value);
		dataList.clearSelection();
		
		ComponentUtils.clearContainer(dataPanel);
	}
}