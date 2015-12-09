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

import aQute.bnd.osgi.Builder
import aQute.bnd.osgi.Jar
import httl.Engine
import java.io.File
import org.eclipse.xtend.lib.annotations.Accessors
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import static org.apache.commons.io.FileUtils.*

/**
 * 
 * @author Kamesh Sampath
 * 
 */
class OSGiDocTaskConvention {

	val org.gradle.api.tasks.bundling.Jar task

	val File bndFile

	val PLUGIN_DEP_REGX = "/org.workspace7.gradle.plugins.osgidoc-([0-9]*.[0-9]*.[0-9]*)-
	(SNAPSHOT|latest). jar$/"

	var File docsDir

	@Accessors(PUBLIC_SETTER)
	var extension Configuration configuration

	val extension Engine engine = Engine.getEngine()

	val extension Project project

	val extension Builder = new Builder()

	new(org.gradle.api.tasks.bundling.Jar task) {
		this.task = task;
		project = task.project;
		// ftlConfig.setTemplateLoader(classpathTemplateLoader);
		// ftlConfig.setDefaultEncoding("UTF-8")
		// ftlConfig.setTemplateUpdateDelay(0)	
		bndFile = file('bnd.bnd')
	}

	/**
	 * 
	 */
	def generateOsgiDoc() {

		task => [

			val projectDocsDir = property("osgidoc.dir") as String

			logger.debug('''Project Dir:«projectDir»''')

			docsDir = if (projectDocsDir != null)
				new File(projectDocsDir)
			else
				new File(buildDir, "osgidoc")

			from(archivePath).into(temporaryDir)

			val jarFile = new File(temporaryDir, archiveName)
			val bndJar = new Jar(jarFile)

			if (bndJar != null) {

				makeDocDirs(project, jarFile)

				generateHTMLDocs(bndJar)

				if (bndFile != null) {
					setProperties(bndFile, projectDir)
				} else {
					base = projectDir
				}

				var cpArtifacts = resolvedConfiguration.resolvedArtifacts.files.toSet
				cpArtifacts.add(jarFile)
				classpath = cpArtifacts
			}

		]
	}

	private def makeDocDirs(Project project, File temporaryDir) {

		logger.debug('''Project Dir:«projectDir»''')

		if (!docsDir) {
			docsDir.mkdirs
		}

		var pluginJarFile = "classpath".findConfig().findFirst[t|t.name.matches(PLUGIN_DEP_REGX)]

		if (pluginJarFile != null) {

			val extension Jar thisPluginJar = new Jar(pluginJarFile)

			// explode into temporary directory
			val File pluginTmpDir = new File(temporaryDir, OSGiDocPlugin.PLUGIN_ID)
			expand(pluginTmpDir)

			// Copy stylesheets
			val srcStyleSheet = new File('''«pluginTmpDir»/«OSGiDocPlugin.OSGi_DOC_STYLESHEET»''')
			val destStyleSheet = new File('''«docsDir»/«OSGiDocPlugin.OSGi_DOC_STYLESHEET»''')
			copyFile(srcStyleSheet, destStyleSheet)

			// copy resources directory
			val srcResourcesDir = new File('''«pluginTmpDir»/resources''')
			val destResourcesDir = new File('''«docsDir»/resources''')
			copyDirectory(srcResourcesDir, destResourcesDir)
			deleteDirectory(pluginTmpDir)
		}
	}

	private def findConfig(String name) {
		buildscript.configurations.getByName(name)
	}

	private def generateHTMLDocs(extension Jar jar) {
		logger.debug('''Project Bundle Jar :«jar»''')
		val extension mf = manifest
		if (mf != null) {
			val extension mfAttributes = mainAttributes
			val bsn = "Bundle-SymbloicName".value
		}
	}

	private def operator_not(File file) {
		(file == null) || (!file.exists )
	}

}