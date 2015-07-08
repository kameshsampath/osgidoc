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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.HttpContext;
import org.osgi.service.log.LogService;

/**
 * The ResourceProvider class that helps in computing the resource URLS when the
 * {@link OSGiDocWebConsolePlugin} requests for the resource
 * 
 * @author Kamesh Sampath
 */
@Component(
				service = HttpContext.class,
				property = "provider.id=osgidocresourceprovider")
public class OSGiDocResourceProvider implements HttpContext {

	private Hashtable<String, Bundle> osgiDocBundles =
		new Hashtable<String, Bundle>();

	private OSGiDocWebConsolePlugin plugin;

	private static final Pattern NATIVE_RES_PATTERN =
		Pattern.compile("^/osgidoc(?<nativePath>/(css|js)/.*)");

	@Override
	public boolean handleSecurity(
		HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// Nothing for now
		return true;
	}

	public OSGiDocWebConsolePlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(OSGiDocWebConsolePlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public URL getResource(String name) {

		this.plugin.log(LogService.LOG_DEBUG, "Resource name:" + name);

		Matcher matcher = NATIVE_RES_PATTERN.matcher(name);

		try {
			if ("/osgidoc".equals(name)) {
				this.plugin.log("Plugin native path:" + name);
				return plugin.getServletContext().getResource(name);
			}
			else if (matcher.matches()) {

				this.plugin.log("Plugin native resource:" + name);

				String nativeResourcePath = matcher.group("nativePath");

				this.plugin.log(
					"Plugin native resource CSS Path:" + nativeResourcePath);

				URL nativeResourceURL = null;

				try {
					nativeResourceURL =
						getClass().getResource(nativeResourcePath);
				}
				catch (Exception e) {
					this.plugin.log(LogService.LOG_ERROR,
						"Plugin resource:" + name + " not found", e);
				}

				return nativeResourceURL;

			}
			else if (name.endsWith(".css")) {

				String cssFileName = FilenameUtils.getName(name);

				System.out.printf("CSS Filename:%s", cssFileName);

				return getResourceFromBundle(cssFileName);

			}
			else if (name.endsWith(".png") || name.endsWith(".gif") ||
				name.endsWith(".jpg")) {

				String imageFileName = FilenameUtils.getName(name);

				System.out.printf("Image Filename:%s",
					imageFileName);

				return getResourceFromBundle("resources/" + imageFileName);

			}
			else {

				String bundleResourceFileName = FilenameUtils.getName(name);

				System.out.printf("Resource :%s",
					bundleResourceFileName);

				if (bundleResourceFileName != null) {
					return getResourceFromBundle(bundleResourceFileName);
				}
			}
		}
		catch (MalformedURLException e) {
			this.plugin.log(LogService.LOG_ERROR,
				"Plugin resource:" + name + " not found", e);
		}

		return null;

	}

	@Override
	public String getMimeType(String name) {

		System.out.printf("Getting Mime of %s}", name);

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
				System.out.printf(
					"Resource:%s fetched from bundle:%s",
					resourceFileName, bundle.getSymbolicName());
				return bundleResource;
			}
		}

		return null;
	}

}
