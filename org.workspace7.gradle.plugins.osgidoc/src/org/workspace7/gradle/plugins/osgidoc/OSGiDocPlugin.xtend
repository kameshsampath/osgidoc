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

/**
 * @author Kamesh Sampath
 * 
 */
class OSGiDocPlugin implements Plugin<Project> {

	public static final String PLUGIN_ID = 'org.workspace7.gradle.plugins.osgidoc'
	public static final String TEMPLATES_FOLDER = '/templates'
	public static final String OSGi_DOC_STYLESHEET = 'osgidoc.css'
	public static final String TASK_NAME = "osgidoc"
	public static final String BND_PLUGIN_ID = "aQute.bnd.gradle.BndPlugin"
	public static final String HTTL_CONFIG = "osgidoc-httl.properties"

	override void apply(extension Project p) {

		logger.debug("RadheKrishna!")

		// val extension Engine templateEngine = Engine.getEngine("/osgidoc-httl.properties")
		// val extension Context templateContext = Context.context
		extensions.create(TASK_NAME, OSGiDocExtension)

		/* Apply bnd gradle plugin if its not done already */
		if (!p.plugins.hasPlugin(BND_PLUGIN_ID)) {
			try {
				p.plugins.apply(BND_PLUGIN_ID)
			} catch (Exception e) {
				logger.error("Error applying Bnd Gradle Plugin", e)
				throw e
			}

		}

		TASK_NAME.task => [
			/* make the jar build first */
			val jarTask = tasks.getByName("jar")
			jarTask.dependsOn
		]

	}

}