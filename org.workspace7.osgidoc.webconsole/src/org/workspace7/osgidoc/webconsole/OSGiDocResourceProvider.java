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

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ResourceProvider class that helps in computing the resource URLS when the
 * {@link OSGiDocWebConsolePlugin} requests for the resource
 * 
 * @author Kamesh Sampath
 */
public class OSGiDocResourceProvider implements HttpContext {

	private final Logger logger =
		LoggerFactory.getLogger(OSGiDocResourceProvider.class);

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
		// Nothing for now
		return true;
	}

	@Override
	public URL getResource(String name) {

		logger.debug("Resource name:" + name);

		try {
			if ("/osgidoc".equals(name)) {

				plugin.getServletContext().getResource(name);

			}
			else if (name.endsWith(".css")) {

				String cssFileName = FilenameUtils.getName(name);

				logger.debug("CSS Filename:{}", new Object[] {
					cssFileName
				});

				return getResourceFromBundle(cssFileName);

			}
			else if (name.endsWith(".png") || name.endsWith(".gif") ||
				name.endsWith(".jpg")) {

				String imageFileName = FilenameUtils.getName(name);

				logger.debug("Image Filename:{}", new Object[] {
					imageFileName
				});

				return getResourceFromBundle("resources/" + imageFileName);

			}
			else {

				String bundleResourceFileName = FilenameUtils.getName(name);

				logger.debug("Resource :{}", new Object[] {
					bundleResourceFileName
				});

				if (bundleResourceFileName != null) {
					return getResourceFromBundle(bundleResourceFileName);
				}
			}
		}
		catch (MalformedURLException e) {
			logger.error("Unable to build URL for :{}", new Object[] {
				name
			});
		}

		return null;

	}

	@Override
	public String getMimeType(String name) {

		logger.debug("Getting Mime of {}", new Object[] {
			name
		});

		if (name != null) {

			String ext = FilenameUtils.getExtension(name);

			if ("html".equals(ext)) {
				return "text/html";
			}
			else if ("png".equals(ext)) {
				return "image/png";
			}
			else if ("jpg".equals(ext)) {
				return "image/jpeg";
			}
			else if ("gif".equals(ext)) {
				return "image/fig";
			}
			else if ("jpeg".equals(ext)) {
				return "image/jpeg";
			}
			else if ("js".equals(ext)) {
				return "application/javascript";
			}
			else if ("css".equals(ext)) {
				return "text/stylesheet";
			}

		}
		return "text/plain";
	}

	/**
	 * An utility method to hold the collection of bundles from which the
	 * resource will be served. The key is usually the Bundle Symbolic name. If
	 * the bundle is already exists in the collection it will be removed and
	 * added i.e. updating
	 * 
	 * @param bundle
	 *            - the bundle which will serve the resource
	 */
	public void addBundle(Bundle bundle) {

		if (bundle != null) {

			String bsn = bundle.getSymbolicName();

			if (osgiDocBundles.contains(bsn)) {
				removeBundle(bsn);

			}
			osgiDocBundles.put(bsn, bundle);
		}
	}

	/**
	 * An utility method to remove the bundle from the collection of resource
	 * provider bundles
	 * 
	 * @param key
	 *            - the Bundle Symbolic Name
	 */
	public void removeBundle(String key) {
		if (osgiDocBundles.containsKey(key)) {
			osgiDocBundles.remove(key);
		}
	}

	/**
	 * An utility method to clear the bundle
	 */
	public void clear() {
		osgiDocBundles.clear();
	}

	/**
	 * A utility method to fetch the resource from the Bundle
	 * 
	 * @param resourceFileName
	 *            - the resource file name path that needs to be fetched
	 */
	private URL getResourceFromBundle(String resourceFileName) {

		for (Entry<String, Bundle> entry : osgiDocBundles.entrySet()) {

			Bundle bundle = entry.getValue();

			URL bundleResource = bundle.getEntry(StringUtils.join(
				OSGiDocPluginConstants.OSGI_DOCS_FOLDER, "/",
				resourceFileName));

			if (bundleResource != null) {
				logger.debug(
					"Resource:{} fetched from bundle:{}", new Object[] {
						resourceFileName, bundle.getSymbolicName()
				});
				return bundleResource;
			}
		}

		return null;
	}

}
