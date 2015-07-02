package org.workspace7.gradle.plugins.osgidoc


class SCR {



	String activate
	String deactivate
	String configurationPid
	String configurationPolicy
	boolean enabled
	String factory
	String name
	boolean immediate
	String implementation
	List properties
	String[] propertyResources	
	ScrReference[] references
	ScrService service
	boolean serviceFactory
	String xmlns

	String toString() {
		"SCR [ name:${name}, configurationPolicy:${configurationPolicy}," +
				" immediate: ${immediate}, activate:${activate}, deactivate:${deactivate},"+
				" configurationPid:${configurationPid}, configurationPolicy:${configurationPolicy} "
	}
}
