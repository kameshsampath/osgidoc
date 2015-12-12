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

import static org.gradle.testkit.runner.TaskOutcome.*
import static org.junit.Assert.*

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification


/**
 * @author Kamesh Sampath
 *
 */
class OsgiDocPluginTest extends Specification {

	@Rule TemporaryFolder testProjectDir = new TemporaryFolder()
	File buildFile

	def setup() {
		buildFile = testProjectDir.newFile("build.file")
	}

	
	def "osgidoc task generates HTML documentation for bundle"(){
		given:
		buildFile << """
		task osgidoc {
			doLast {
				print 'HariBol'
		    }
		}
		"""

		when:
		def result = GradleRunner.create()
				.withProjectDir(testProjectDir.root)
				.withArguments('osgidoc')
				.build()

		then:
		result.output.contains('HariBol')
		result.task(':osgidoc').outcome == SUCCESS
	}
}
