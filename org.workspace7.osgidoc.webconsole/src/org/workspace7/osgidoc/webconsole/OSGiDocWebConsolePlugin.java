/**
 * 
 */

package org.workspace7.osgidoc.webconsole;

import static org.apache.felix.webconsole.WebConsoleConstants.PLUGIN_LABEL;
import static org.apache.felix.webconsole.WebConsoleConstants.PLUGIN_TITLE;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.webconsole.AbstractWebConsolePlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * @author Kamesh Sampath
 */
@SuppressWarnings("serial")
@Component(
				name = "org.workspace7.osgidoc.webconsole",
				property = {
					PLUGIN_LABEL + "=osgidoc", PLUGIN_TITLE + "=OSGi Doc"
}, service = {
	Servlet.class
})
public class OSGiDocWebConsolePlugin extends AbstractWebConsolePlugin {

	final String PLUGIN_TITLE = "OSGi Doc";
	final String PLUGIN_LABEL = "osgidoc";
	final String OSGI_DOCS_FOLDER = "/OSGI-DOC";

	final Hashtable<String, Bundle> osgidocBundles =
		new Hashtable<String, Bundle>();
	private BundleTracker<Bundle> bundleTracker;
	private OSGiDocResourceProvider resourceProvider;

	@Activate
	void activated(BundleContext bundleContext) {
		bundleTracker = new BundleTracker<Bundle>(
			bundleContext, Bundle.INSTALLED | Bundle.UNINSTALLED,
			new OSGiDocBundleCustomizer());
		resourceProvider =
			new OSGiDocResourceProvider(this);
		bundleTracker.open();
		handleExistingDocBundles(bundleContext);
	}

	@Deactivate
	void deactivated(BundleContext bundleContext) {
		if (bundleTracker != null) {
			bundleTracker.close();
		}
	}

	@Override
	public String getLabel() {
		return PLUGIN_TITLE;
	}

	@Override
	public String getTitle() {
		return PLUGIN_LABEL;
	}

	@Override
	protected void renderContent(
		HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log("RadheKrishna!");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<h1>RadheKrishna!</h1>");
		printWriter.println("<ul>");
		for (Entry<String, Bundle> entry : osgidocBundles.entrySet()) {
			printWriter.println(
				"<li><a href='/system/console/osgidoc/" + entry.getKey() +
					"_index.html'>" +
					entry.getKey() +
					"</li>");
		}
		printWriter.println("</ul>");

	}

	protected String bundleEventAsString(int bundleEventType) {
		switch (bundleEventType) {
			case BundleEvent.INSTALLED: {
				return "INSTALLED";
			}
			case BundleEvent.LAZY_ACTIVATION: {
				return "LAZY_ACTIVATION";
			}
			case BundleEvent.RESOLVED: {
				return "RESOLVED";
			}
			case BundleEvent.STARTED: {
				return "STARTED";
			}
			case BundleEvent.STARTING: {
				return "STARTING";
			}
			case BundleEvent.STOPPED: {
				return "STOPPED";
			}
			case BundleEvent.STOPPING: {
				return "STOPPING";
			}
			case BundleEvent.UNRESOLVED: {
				return "UNRESOLVED";
			}
			case BundleEvent.UPDATED: {
				return "UPDATED";
			}
			case BundleEvent.UNINSTALLED: {
				return "UNINSTALLED";
			}
			default: {
				return "UNKNOWN";
			}
		}
	}

	protected void handleExistingDocBundles(BundleContext bundleContext) {
		Bundle[] bundles = bundleContext.getBundles();

		for (Bundle bundle : bundles) {
			if (hasDocs(bundle) && bundle != null) {
				System.out.println("adding existing bundles");
				osgidocBundles.put(bundle.getSymbolicName(), bundle);
				if(resourceProvider!=null){
					resourceProvider.addBundle(bundle);
				}
			}
		}

	}

	@Override
	protected Object getResourceProvider() {

		return resourceProvider;
	}

	private boolean hasDocs(Bundle bundle) {

		URL docsFolderURL =
			bundle.getEntry(OSGI_DOCS_FOLDER);

		return (docsFolderURL != null);
	}

	private class OSGiDocBundleCustomizer
		implements BundleTrackerCustomizer<Bundle> {

		@Override
		public Bundle addingBundle(Bundle bundle, BundleEvent event) {

			if (bundle != null && (BundleEvent.RESOLVED == event.getType() ||
				BundleEvent.INSTALLED == event.getType())) {

				if (hasDocs(bundle)) {
					System.out.println("Adding bundle to be tracked:" +
						bundle.getSymbolicName());
					resourceProvider.addBundle(bundle);
					osgidocBundles.put(bundle.getSymbolicName(), bundle);
					return bundle;
				}

			}

			return null;
		}

		@Override
		public void modifiedBundle(
			Bundle bundle, BundleEvent event, Bundle object) {

			String trackedBsn = object.getSymbolicName();

			if (object != null && ((BundleEvent.UPDATED == event.getType()))) {
				if (hasDocs(object)) {
					System.out.println("Update and add :" + trackedBsn);
					osgidocBundles.put(trackedBsn, object);
				}
			}
		}

		@Override
		public void removedBundle(
			Bundle bundle, BundleEvent event, Bundle object) {

			String trackedBsn = object.getSymbolicName();

			if (object != null &&
				(BundleEvent.UNINSTALLED == event.getType()) &&
				(osgidocBundles.containsKey(trackedBsn))) {
				if (hasDocs(object)) {
					System.out.println("Untrack:" + trackedBsn);
					osgidocBundles.remove(trackedBsn);
				}
			}
		}
	}

}
