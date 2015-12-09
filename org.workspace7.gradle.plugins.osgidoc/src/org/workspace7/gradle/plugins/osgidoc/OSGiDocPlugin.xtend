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
package org.workspace7.gradle.plugins.osgidoc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Jar

/**
 * @author Kamesh Sampath
 * 
 */
class OSGiDocPlugin implements Plugin<Project> {

	public static final String PLUGIN_ID = 'org.workspace7.gradle.plugins.osgidoc'
	public static final String FTL_TEMPLATES_FOLDER = '/templates'
	public static final String OSGi_DOC_STYLESHEET = 'osgidoc.css'

	override void apply(extension Project target) {

		logger.debug("RadheKrishna!")

		extensions.create('osgidoc', OSGiDocPluginProperties)

		target => [
			configureJar(task("jar"))
		]
	}

	def configureJar(Task task) {
		val extension Jar jarTask = task as Jar
		val extension OSGiDocTaskConvention osgiDocPluginConvention = new OSGiDocTaskConvention(jarTask)
		doLast([ s |
			s.convention.plugins.put("osgidoc", osgiDocPluginConvention)
			generateOsgiDoc()
		])
	}

}