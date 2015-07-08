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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.webconsole.AbstractWebConsolePlugin;
import org.apache.felix.webconsole.SimpleWebConsolePlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * The Felix {@link AbstractWebConsolePlugin} that is used to display the
 * bundles wth OSGi documentation
 * 
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
public class OSGiDocWebConsolePlugin extends SimpleWebConsolePlugin {

	final Hashtable<String, Bundle> osgidocBundles =
		new Hashtable<String, Bundle>();
	private BundleTracker<Bundle> bundleTracker;
	private OSGiDocResourceProvider resourceProvider;
	private static final String[] CSS_REFS = {
		"/osgidoc/css/osgidoc-plugin.css"
	};

	public OSGiDocWebConsolePlugin() {
		super(OSGiDocPluginConstants.PLUGIN_LABEL, OSGiDocPluginConstants.PLUGIN_TITLE, null, CSS_REFS);
	}

	@Activate
	void activated(BundleContext bundleContext) {
		bundleTracker = new BundleTracker<Bundle>(
			bundleContext,
			(Bundle.INSTALLED | Bundle.RESOLVED | Bundle.UNINSTALLED),
			new OSGiDocBundleCustomizer());
		bundleTracker.open();
		handleExistingDocBundles(bundleContext);
	}

	@Deactivate
	void deactivated(BundleContext bundleContext) {
		this.resourceProvider = null;
		if (bundleTracker != null) {
			bundleTracker.close();
		}
	}

	@Reference(
					service = HttpContext.class,
					target = "(provider.id=osgidocresourceprovider)")
	void setResourceProvider(HttpContext httpContext) {
		this.resourceProvider = (OSGiDocResourceProvider)httpContext;
		this.resourceProvider.setPlugin(this);
	}

	@Override
	protected void renderContent(
		HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter printWriter = response.getWriter();

		List<String> docBundleNames = new ArrayList<String>();
		docBundleNames.addAll(osgidocBundles.keySet());
		MustacheFactory mf = new DefaultMustacheFactory();

		Mustache mustache = mf.compile("home.mustache");
		mustache.execute(
			printWriter,
			new OSGiDocMustacheContext(docBundleNames)).flush();

		printWriter.flush();
		printWriter.close();

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
				log(LogService.LOG_DEBUG,"Adding existing bundles");
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

		log(LogService.LOG_DEBUG,"Clear existing bundles");

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

			if ((bundle != null && event != null) &&
				(BundleEvent.RESOLVED == event.getType() ||
					BundleEvent.INSTALLED == event.getType())) {

				if (hasDocs(bundle)) {
					log(LogService.LOG_DEBUG,"Adding bundle to be tracked:" +
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

			if ((object != null && event != null) &&
				((BundleEvent.UPDATED == event.getType()))) {
				if (hasDocs(object)) {
					log(LogService.LOG_DEBUG,"Update and add :" + trackedBsn);
					osgidocBundles.put(trackedBsn, object);
				}
			}
		}

		@Override
		public void removedBundle(
			Bundle bundle, BundleEvent event, Bundle object) {

			String trackedBsn = object.getSymbolicName();

			if ((object != null && event != null) &&
				(BundleEvent.UNINSTALLED == event.getType()) &&
				(osgidocBundles.containsKey(trackedBsn))) {
				if (hasDocs(object)) {
					log(LogService.LOG_DEBUG,"Untrack:" + trackedBsn);
					osgidocBundles.remove(trackedBsn);
					resourceProvider.removeBundle(trackedBsn);
				}
			}
		}
	}

}
