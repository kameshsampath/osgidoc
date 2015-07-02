
package org.workspace7.impl;

import aQute.bnd.annotation.metatype.*;

@Meta.OCD(id = "org.workspace7.dotmatrix.printer.configuration")
public interface DotmatrixPrinterConfiguration {

	@Meta.AD(id = "name", description = "Brand name of the printer")
	String name();

	@Meta.AD(id = "vendor", description = "Vendor name")
	String vendor();

	@Meta.AD(
					id = "yearOfManufacture",
					description = "When the Printer was manufactured")
	String yearOfManufacture();

	@Meta.AD(
					id = "dotsPerInch",
					description = "How many dots per inch will be printed")
	Long dotsPerInch();
}
