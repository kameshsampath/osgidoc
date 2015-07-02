
package org.workspace7.impl;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.workspace7.api.Printer;

@Component(
				name = "org.workspace7.dotprinter",
				configurationPid = "org.workspace7.dotmatrix.printer.configuration",
				configurationPolicy = ConfigurationPolicy.OPTIONAL,
				property = {
					"printer.type=dotmatrix"
},
				service = Printer.class,
				immediate = true)
public class DotmatrixPrinter implements Printer {

	private BundleContext bundleContext;
	private LogService logservice;

	public void print(String message) {
		logservice.log(LogService.LOG_INFO, message);
	}

	@Activate
	void activate(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Deactivate
	void deactivate() {
		this.bundleContext = null;
	}

	@Reference
	public void setLog(LogService logservice) {
		this.logservice = logservice;
	}

}
