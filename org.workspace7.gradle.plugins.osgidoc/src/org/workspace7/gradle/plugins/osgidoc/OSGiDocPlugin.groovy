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

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 
 * @author Kamesh Sampath
 *
 */
public class OSGiDocPlugin implements Plugin<Project> {

	public static final String PLUGIN_ID = 'org.workspace7.gradle.plugins.osgidoc'
	
	def logger

	void apply(Project project) {

		logger = project.logger

		project.extensions.create 'osgidoc', OSGiDocPluginProperties

		project.configure(project) {

				
			/*if (!plugins.hasPlugin('biz.aQute.bnd.builder')) {
				plugins.apply 'biz.aQute.bnd.builder'
			}*/

			jar {

				logger.debug 'RadheKrishna!'

				doLast {
					convention.plugins.osgidoc = new OSGiDocTaskConvention(jar)
					generateOSGiDoc()
				}
			}
		}
	}
}