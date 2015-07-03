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

import java.util.regex.Matcher
import java.util.List
import java.util.ArrayList

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.gradle.api.GradleException
import org.gradle.api.logging.Logger

import aQute.bnd.header.Parameters
import aQute.bnd.osgi.Builder
import aQute.bnd.osgi.Constants
import aQute.bnd.osgi.Domain
import aQute.bnd.osgi.Jar
import aQute.bnd.osgi.Resource
import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import freemarker.template.Template

/**
 * 
 * @author Kamesh Sampath
 *
 */
class OSGiDocTaskConvention {

	private final org.gradle.api.tasks.bundling.Jar task
	private final Logger logger
	private File bndFile
	private final Configuration ftlConfig = new Configuration(Configuration.VERSION_2_3_22)
	private final ClassTemplateLoader classpathTemplateLoader = new ClassTemplateLoader(getClass(), OSGiDocPlugin.FTL_TEMPLATES_FOLDER)

	def PLUGIN_DEP_REGX=/org.workspace7.gradle.plugins.osgidoc-([0-9]*.[0-9]*.[0-9]*)-(SNAPSHOT|latest).jar$/

	private File docsDir

	private org.gradle.api.artifacts.Configuration configuration

	OSGiDocTaskConvention(org.gradle.api.tasks.bundling.Jar task) {
		this.task = task
		ftlConfig.setTemplateLoader(classpathTemplateLoader);
		ftlConfig.setDefaultEncoding("UTF-8")
		ftlConfig.setTemplateUpdateDelay(0)
		logger = task.project.logger
		bndFile = task.project.file('bnd.bnd')
	}
	
	public org.gradle.api.artifacts.Configuration getConfiguration() {
	  if (configuration == null) {
		setConfiguration(task.project.configurations.compile)
	  }
	  return configuration
	}

	public void setConfiguration(org.gradle.api.artifacts.Configuration configuration) {
	  this.configuration = configuration
	}

	private void generateOSGiDoc() {

		task.configure {

			def projectDocsDir = task.project.osgidoc.dir

			logger.debug "Project Dir :${task.project.projectDir}"

			if (projectDocsDir) {
				docsDir = new File("${task.project.projectDir}")
			} else {
				docsDir = new File("${task.project.buildDir}/osgidoc")
			}

			project.copy {
				from archivePath
				into temporaryDir
			}

			def jarFile = new File(temporaryDir, archiveName)

			def bndJar = new Jar(jarFile)

			if (bndJar != null) {

				makeDocDirs(project,temporaryDir)

				generateDocs(bndJar)

				//add docsDir folder to jar

				Builder builder = new Builder()

				if (bndFile) {
					builder.setProperties(bndFile, project.projectDir)
				} else {
					builder.setBase(project.projectDir)
				}

				def Set<File> cpArtifacts = new LinkedHashSet<>(
						configuration.resolvedConfiguration.resolvedArtifacts*.file)
				cpArtifacts.add(jarFile)
				builder.setClasspath(cpArtifacts.toArray(new File[cpArtifacts.size()]))

				//include as is bundled jar
				def String jarInclude = "\"@${jarFile.absolutePath.replaceAll(/"/, /\\"/)}\""

				def String includes = builder.getIncluded()

				def String osgiDocsInclude = "\"OSGI-DOC=${docsDir}\""
				
				if (includes) {
					includes = "${jarInclude},${includes},${osgiDocsInclude}"
				} else {
					includes = "${jarInclude},${osgiDocsInclude}"
				}
				
				logger.debug "OSGI Includes: ${includes}"

				builder.setProperty(Constants.INCLUDERESOURCE, includes)

				Jar bundleJar = builder.build()
				bundleJar.updateModified(System.currentTimeMillis(), 'Added OSGI-DOC to the bundle')

				//TODO Log errors

				try {
					bundleJar.write(archivePath)
				} catch (Exception) {
					task.archivePath.delete()
					throw new GradleException("Bundle ${archiveName} failed to build: ${e.getMessage()}", e)
				} finally {
					bundleJar.close()
				}
			}

		}

	}

	private void makeDocDirs(project,temporaryDir) {

		logger.debug "Project Docs Dir :${docsDir}"

		assert docsDir != null

		if (!docsDir.exists()) {

			docsDir.mkdirs()
		}


		File pluginJarFile =  project.buildscript.configurations.classpath.find{ it.name =~ PLUGIN_DEP_REGX}

		if(pluginJarFile){

			Jar thisPluginJar = new Jar(pluginJarFile)

			File pluginTmpDir = new File(temporaryDir,OSGiDocPlugin.PLUGIN_ID)

			thisPluginJar.expand(pluginTmpDir)

			File srcStylesheet = new File("${pluginTmpDir}/${OSGiDocPlugin.OSGi_DOC_STYLESHEET}")

			File destStylesheet = new File("${docsDir}/${OSGiDocPlugin.OSGi_DOC_STYLESHEET}")

			//copy stylesheet
			FileUtils.copyFile(srcStylesheet,destStylesheet)

			//copy resources directory
			def srcResourcesDir = new File(pluginTmpDir,'/resources')
			def destResourcesDir = new File("${docsDir}/resources")
			FileUtils.copyDirectory(srcResourcesDir, destResourcesDir)
			FileUtils.deleteDirectory(pluginTmpDir)
		}

	}

	private void generateDocs(Jar jar) {

		logger.debug "Project Bundle Jar :${jar}"

		def manifest = jar.getManifest()

		if (manifest != null) {

			def mfMainAttributes = manifest.getMainAttributes()

			def bsn = mfMainAttributes.getValue('Bundle-SymbolicName')

			def templates = ["${bsn}_index.html":
				ftlConfig.getTemplate("manifest_doc.ftl"),
				"${bsn}_exports.html":
				ftlConfig.getTemplate("exports_doc.ftl"),
				"${bsn}_imports.html":
				ftlConfig.getTemplate("imports_doc.ftl"),
				"${bsn}_scrs_index.html":
				ftlConfig.getTemplate("scrs_index_doc.ftl")
			]

			def templateModel = ['bundleName'           : mfMainAttributes.getValue('Bundle-Name'),
				'bundleSymbolicName'   : mfMainAttributes.getValue('Bundle-SymbolicName'),
				'bundleVersion'        : mfMainAttributes.getValue('Bundle-Version'),
				'bundleDescription'    : mfMainAttributes.getValue('Bundle-Description'),
				'bundleDocURL'         : mfMainAttributes.getValue('Bundle-DocURL'),
				'bndLastModified'      : mfMainAttributes.getValue('Bnd-LastModified'),
				'tool'                 : mfMainAttributes.getValue('Tool'),
				'bundleManifestVersion': mfMainAttributes.getValue('Bundle-ManifestVersion'),
				'bundleCategory'       : mfMainAttributes.getValue('Bundle-Category'),
				'bundleVendor'         : mfMainAttributes.getValue('Bundle-Vendor'),
				'bundleLicense'        : mfMainAttributes.getValue('Bundle-License'),
				'bundleActivator'      : mfMainAttributes.getValue('Bundle-Activator'),
				'manifestVersion'      : mfMainAttributes.getValue('Manifest-Version')
			]

			if (mfMainAttributes.getValue('Build-Jdk')) {
				templateModel.put('buildJdk', mfMainAttributes.getValue('Build-Jdk'))
			} else {
				templateModel.put('buildJdk', mfMainAttributes.getValue('Created-By'))
			}

			Domain domain = Domain.domain(manifest);
			//Imports
			Parameters imports = domain.getImportPackage()

			def importMap = buildTemplateModel(imports)

			templateModel.put("imports", importMap)

			//Exports

			Parameters exports = domain.getExportPackage()

			def exportMap = buildTemplateModel(exports)

			templateModel.put("exports", exportMap)

			//Service Components
			Parameters parameters =
					domain.getParameters(Constants.SERVICE_COMPONENT);

			def scrList = []
			
			List<SCR> scrs = new ArrayList<SCR>()

			parameters.keySet().each { serviceCompXml ->

				Resource resource = jar.getResource(serviceCompXml);

				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				resource.write(bout)
				bout.flush();
				bout.close();

				String xmlString = bout.toString()

				def component = new XmlSlurper().parseText(xmlString)

				if(component){

					def name = component['@name'].toString()
					scrList << name

					def scrProps = []

					component.'property'.find { scrPropNode ->
						ScrProperty scrProp = new ScrProperty(name:scrPropNode.@name,
						type:scrPropNode.@type,value:scrPropNode.@value)
						logger.debug "Service Properties:${scrProp}"
						scrProps << scrProp
					}


					def serviceImpl

					component.'implementation'.find { serviceImplNode ->
						logger.debug "Service Impl :${serviceImplNode.@class}"
						serviceImpl = serviceImplNode.@class
					}

					def serviceInterfaces = []

					component.'service'.provide.find { serviceNode ->
						logger.debug "Service :${serviceNode.@interface}"
						serviceInterfaces << serviceNode.@interface
					}

					boolean isServiceFactory = component.service.@serviceFactory.toBoolean()

					//<service>
					ScrService scrService = new ScrService(factory:isServiceFactory,
					implementation:serviceImpl,
					interfaces:serviceInterfaces)

					def references = []
					//<reference>
					component.'reference'.find { refNode ->

						println "Reference :${refNode.@name},"+
								"${refNode.@interface}," +
								"${refNode.@bind}"

						ScrReference scrRef = new ScrReference(name:refNode.@name,
						refinterface:refNode.@interface,bind:refNode.@bind)
						references << scrRef

					}

					SCR scr = new SCR(name:name,activate:component.@activate,
					deactivate:component.@deactivate,enabled:component.@enabled.toBoolean(),
					immediate:component.@immediate,
					configurationPolicy:component.@'configuration-policy',
					configurationPid:component.@'configuration-pid',
					references:references,
					service:scrService,
					xmlns:component.@xmlns,properties:scrProps)
					
					scrs.add(scr)
				
				}
			}

			templateModel.put("scrList",scrList)
			
			//Generate the SCR file
			scrs.each { scr ->
				
				def name = scr.name
				
				templateModel.put("scr",scr)
			
				def scrTpl = ftlConfig.getTemplate("scr_doc.ftl")
				
				File outFile = new File(docsDir,"scr_${name}.html")
	
				FileWriter outFileWriter = new FileWriter(outFile)
	
				scrTpl.process(templateModel,outFileWriter)
			}

			templates.each { outfileName,template ->

				File outFile = new File(docsDir,outfileName)

				FileWriter outFileWriter = new FileWriter(outFile)

				template.process(templateModel,outFileWriter)
			}
		}
	}

	private Map buildTemplateModel(Parameters parameters) {

		def paramMap = [:]

		parameters.each { param ->
			//println "${param.key}:${param.value}"

			def paramAttrs = []

			//TODO handle this properly
			param.value.each { paramAttrKey, paramAttrValue ->
				paramAttrs << paramAttrValue
			}

			paramMap.put(param.key, paramAttrs)
		}

		logger.debug "${paramMap}"
		return paramMap
	}
}