
package org.workspace7.osgidoc.webconsole;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

public class OSGiDocResourceProvider implements HttpContext {

	private Hashtable<String, Bundle> osgiDocBundles =
		new Hashtable<String, Bundle>();

	private OSGiDocWebConsolePlugin plugin;

	public OSGiDocResourceProvider(
		OSGiDocWebConsolePlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleSecurity(
		HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		return true;
	}

	@Override
	public URL getResource(String name) {

		System.out.println("Resource name:" + name);

		if ("/osgidoc".equals(name)) {
			try {
				plugin.getServletContext().getResource(name);
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (name.endsWith(".css")) {

			String cssFileName = FilenameUtils.getName(name);

			System.out.println("CSS Filename:" + cssFileName);

			for (Entry<String, Bundle> entry : osgiDocBundles.entrySet()) {
				Bundle bundle = entry.getValue();
				URL bundleResource = bundle.getEntry(
					"/OSGI-DOC/" + cssFileName);
				if (bundleResource != null) {
					return bundleResource;
				}
			}

		}
		else if (name.endsWith(".png") || name.endsWith(".gif") ||
			name.endsWith(".jpg")) {

			String imageFileName = FilenameUtils.getName(name);

			System.out.println("Image file:" + imageFileName);

			for (Entry<String, Bundle> entry : osgiDocBundles.entrySet()) {
				Bundle bundle = entry.getValue();
				URL bundleResource = bundle.getEntry(
					"/OSGI-DOC/resources/" + imageFileName);
				if (bundleResource != null) {
					return bundleResource;
				}
			}

		}
		else {

			String bundleDocFileName = FilenameUtils.getName(name);

			System.out.println("bundleDocFileName:" + bundleDocFileName);

			if (bundleDocFileName != null) {
				for (Entry<String, Bundle> entry : osgiDocBundles.entrySet()) {
					Bundle bundle = entry.getValue();
					URL bundleResource = bundle.getEntry(
						"/OSGI-DOC/" + bundleDocFileName);
					if (bundleResource != null) {
						return bundleResource;
					}
				}
			}
		}

		return null;

	}

	@Override
	public String getMimeType(String name) {
		System.out.println(name);
		// Fix it
		return "text/html";
	}

	public void addBundle(Bundle bundle) {
		osgiDocBundles.put(bundle.getSymbolicName(), bundle);

	}

}
