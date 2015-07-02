
package org.workspace7.impl;

import org.osgi.service.component.annotations.Component;
import org.workspace7.api.Printer;

@Component(
				name = "org.workspace7.simpleprinter",
				immediate = true,
				property = {
					"printer.type=simple",
},
				servicefactory=true,
				service = Printer.class)
public class SimplePrinter implements Printer {

	public void print(String message) {
		System.out.println(message);
	}

}
