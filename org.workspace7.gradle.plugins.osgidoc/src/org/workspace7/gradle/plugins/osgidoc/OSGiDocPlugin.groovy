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