/**
 * Copyright 2015-present Kamesh Sampath<kamesh.sampath@hotmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kamesh Sampath
 */
@SuppressWarnings("serial")
@Component(
				name = "org.workspace7.osgidoc.webconsole",
				property = {
					PLUGIN_LABEL + "=" + OSGiDocPluginConstants.PLUGIN_LABEL,
					PLUGIN_TITLE + "=" + OSGiDocPluginConstants.PLUGIN_TITLE
}, service = {
	Servlet.class
})
public class OSGiDocWebConsolePlugin extends AbstractWebConsolePlugin {

	private final Logger logger =
		LoggerFactory.getLogger(OSGiDocWebConsolePlugin.class);

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
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter printWriter = response.getWriter();
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

	/**
	 * The utility method to add OSGi documentation bundles tracked bundle list
	 * 
	 * @param bundleContext
	 *            - the {@link BundleContext} form where we can fetch the
	 *            bundles
	 */
	protected void handleExistingDocBundles(BundleContext bundleContext) {
		Bundle[] bundles = bundleContext.getBundles();

		for (Bundle bundle : bundles) {
			if (hasDocs(bundle) && bundle != null) {
				logger.trace("Adding existing bundles");
				osgidocBundles.put(bundle.getSymbolicName(), bundle);
				if (resourceProvider != null) {
					resourceProvider.addBundle(bundle);
				}
			}
		}

	}

	/**
	 * The utility method to clear the existing OSGi documentation bundles
	 */
	protected void clearExistingDocBundles() {

		logger.trace("Clear existing bundles");

		if (resourceProvider != null) {
			for (String bsn : osgidocBundles.keySet()) {
				resourceProvider.removeBundle(bsn);
			}
		}

		osgidocBundles.clear();
	}

	@Override
	protected Object getResourceProvider() {

		return resourceProvider;
	}

	private boolean hasDocs(Bundle bundle) {

		URL docsFolderURL =
			bundle.getEntry(OSGiDocPluginConstants.OSGI_DOCS_FOLDER);

		return (docsFolderURL != null);
	}

	private class OSGiDocBundleCustomizer
		implements BundleTrackerCustomizer<Bundle> {

		@Override
		public Bundle addingBundle(Bundle bundle, BundleEvent event) {

			if (bundle != null && (BundleEvent.RESOLVED == event.getType() ||
				BundleEvent.INSTALLED == event.getType())) {

				if (hasDocs(bundle)) {
					logger.debug("Adding bundle to be tracked:" +
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
					logger.debug("Update and add :" + trackedBsn);
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
					logger.debug("Untrack:" + trackedBsn);
					osgidocBundles.remove(trackedBsn);
					resourceProvider.removeBundle(trackedBsn);
				}
			}
		}
	}

}
