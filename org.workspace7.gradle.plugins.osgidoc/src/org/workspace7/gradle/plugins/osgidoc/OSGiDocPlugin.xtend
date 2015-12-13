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

/**
 * @author Kamesh Sampath
 * 
 */
class OSGiDocPlugin implements Plugin<Project>
{

	public static val PLUGIN_ID = 'org.workspace7.gradle.plugins.osgidoc'
	public static val TEMPLATES_FOLDER = '/templates'
	public static val OSGi_DOC_STYLESHEET = 'osgidoc.css'
	public static val TASK_NAME = "osgidoc"
	public static val TASK_DESCRIPTION = "Task that will generate OSGi" +
		"Documentation"
	public static val String BND_PLUGIN_ID = "biz.aQute.bnd.builder"
	public static val String HTTL_CONFIG = "osgidoc-httl.properties"

	override void apply(extension Project target)
	{

		logger.debug("RadheKrishna!")

		/* Apply bnd gradle plugin if its not done already */
		if(!plugins.hasPlugin(BND_PLUGIN_ID))
		{
			try
			{
				plugins.apply(BND_PLUGIN_ID)
			}
			catch(Exception e)
			{
				logger.error('''Error applying «BND_PLUGIN_ID»''', e)
				throw e
			}

		}

		val taskOpts = newHashMap(
			Task.TASK_NAME -> TASK_NAME,
			Task.TASK_TYPE -> OSGiDocTask,
			Task.TASK_DESCRIPTION -> TASK_DESCRIPTION,
			Task.TASK_DEPENDS_ON -> #[':jar']
		)

		target.task(taskOpts, TASK_NAME)

	}

}