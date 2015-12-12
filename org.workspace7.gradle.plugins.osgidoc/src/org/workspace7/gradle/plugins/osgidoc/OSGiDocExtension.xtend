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

import aQute.bnd.header.Parameters
import aQute.bnd.osgi.Domain
import aQute.bnd.osgi.Jar
import httl.Context
import httl.Engine
import java.io.File
import java.io.FileWriter
import java.util.LinkedHashMap
import org.eclipse.xtend.lib.annotations.Accessors
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

import static org.apache.commons.io.FileUtils.*
import static org.workspace7.gradle.plugins.osgidoc.OSGiDocPlugin.*

/**
 * 
 * @author Kamesh Sampath
 * 
 */
class OSGiDocExtension extends DefaultTask {

	val PLUGIN_DEP_REGX = "/org.workspace7.gradle.plugins.osgidoc-([0-9]*.[0-9]*.[0-9]*)-
	(SNAPSHOT|latest). jar$/"

	@Accessors
	String docDir = "osgidoc"

	var File docsDir

	val extension Project project

	var extension Engine templateEngine

	var extension Context templateContext

	new() {
		templateContext = Context.getContext()
		this.project = super.project
	}

	@TaskAction
	def generateOsgiDoc() {

		logger.debug('''Project Dir:«projectDir»''')

		if (!docsDir) {
			docsDir = '''«buildDir»/«docDir»'''.mkdir
		}

		var pluginJarFile = "classpath".findConfig().findFirst [ t |
			t.name.matches(PLUGIN_DEP_REGX)
		]

		if (pluginJarFile != null) {

			val extension Jar thisPluginJar = new Jar(pluginJarFile)

			// explode into temporary directory
			val File pluginTmpDir = new File(temporaryDir, PLUGIN_ID)
			expand(pluginTmpDir)

			val httlConfig = new File('''«pluginTmpDir»/«HTTL_CONFIG»''').absolutePath

			this.templateEngine = Engine.getEngine(httlConfig)

			// Copy stylesheets
			val srcStyleSheet = new File('''«pluginTmpDir»/«OSGi_DOC_STYLESHEET»''')
			val destStyleSheet = new File('''«docsDir»/«OSGi_DOC_STYLESHEET»''')
			copyFile(srcStyleSheet, destStyleSheet)

			// copy resources directory
			val srcResourcesDir = new File('''«pluginTmpDir»/resources''')
			val destResourcesDir = new File('''«docsDir»/resources''')
			copyDirectory(srcResourcesDir, destResourcesDir)
			deleteDirectory(pluginTmpDir)
		}
		
		val bndJarFile = tasks.getByName("jar").outputs.file
		
		val extension bndJar = new Jar(bndJarFile)

		bndJar.generateHTMLDocs
	}

	/**
	 * 
	 */
	private def findConfig(String name) {
		buildscript.configurations.getByName(name)
	}

	/**
	 * 
	 */
	private def generateHTMLDocs(extension Jar bndJar) {

		logger.debug('''Project Bundle Jar :«bndJar»''')

		val extension mf = manifest

		if (mf != null) {
			val extension mfAttributes = mainAttributes

			val bsn = "Bundle-SymbloicName".value

			val templates = newLinkedHashMap(
				'''«bsn»_index.html''' -> '''/manifest_doc.httl''',
				'''«bsn»_exports.html''' -> '''/exports_doc.httl''',
				'''«bsn»_imports.html''' -> '''/imports_doc.httl''',
				'''«bsn»_scrs_index.html''' -> '''/scrs_index_doc.httl'''
			)

			val mfInfo = newLinkedHashMap(
				'bundleName' -> 'Bundle-Name'.value,
				'bundleSymbolicName' -> bsn,
				'bundleVersion' -> 'Bundle-Version'.value,
				'bundleDescription' -> 'Bundle-Description'.value,
				'bundleDocURL' -> 'Bundle-DocURL'.value,
				'tool' -> 'Bnd-LastModified'.value,
				'bundleVersion' -> 'Tool'.value,
				'bndLastModified' -> 'Bnd-LastModified'.value,
				'bundleManifestVersion' -> 'Bundle-ManifestVersion'.value,
				'bundleCategory' -> 'Bundle-Category'.value,
				'bundleVendor' -> 'Bundle-Vendor'.value,
				'bundleLicense' -> 'Bundle-License'.value,
				'bundleActivator' -> 'Bundle-Activator'.value,
				'manifestVersion' -> 'Manifest-Version'.value
			)

			if ("Build-Jdk".value.isNullOrEmpty)
				mfInfo.put('buildJdk', "Build-Jdk".value)
			else
				mfInfo.put('buildJdk', "Created-By".value)

			templateContext.put("manifestInfo", mfInfo)

			val extension domain = Domain.domain(mf)

			// Imports
			val imports = importPackage
			val importMap = imports.map
			templateContext.put('imports', importMap)

			// Exports
			val exports = exportPackage
			val exportsMap = exports.map
			templateContext.put('exports', exportsMap)

			// Render the Manifest
			templates.renderTemplate

		// Service Components
		}
	}

	/**
	 * 
	 */
	private def map(extension Parameters parameters) {
		val paramMap = newLinkedHashMap()

		parameters.forEach [ paramKey, paramAttrs |
			val paramAttrValues = paramAttrs.values.toList
			paramMap.put(paramKey, paramAttrValues)
		]

		paramMap
	}

	/**
	 * 
	 */
	private def operator_not(File file) {
		(file == null) || (!file.exists )
	}

	/**
	 * 
	 */
	private def renderTemplate(LinkedHashMap<String, String> templateMap) {
		templateMap.forEach [ htmlTemplate, httlTemplate |
			try {

				val htmlOut = new FileWriter('''«docsDir»/«htmlTemplate»''')

				val extension template = httlTemplate.template

				htmlOut.render

			} catch (Exception e) {
				logger.error('''Error rendering template «httlTemplate»''', e)
			}
		]
	}

}