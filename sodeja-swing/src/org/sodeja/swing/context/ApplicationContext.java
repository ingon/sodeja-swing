package org.sodeja.swing.context;

import org.sodeja.swing.component.ApplicationFrame;
import org.sodeja.swing.config.GUIConfiguration;
import org.sodeja.swing.locale.LocaleProvider;
import org.sodeja.swing.resource.LocalizationFactory;
import org.sodeja.swing.resource.ResourceProvider;
import org.sodeja.swing.validation.ValidationController;

public interface ApplicationContext {
	public void startup();
	public void shutdown();
	
	public void addApplicationContextListener(ApplicationContextListener listener);
	public void removeApplicationContextListener(ApplicationContextListener listener);
	
	public LocaleProvider getLocaleProvider();
	public ResourceProvider getResourceProvider();
	public LocalizationFactory getLocalizationFactory();
	
	public ApplicationFrame getRootFrame();
	public ValidationController getValidationController();
	
	public GUIConfiguration getGuiConfiguration();
}
