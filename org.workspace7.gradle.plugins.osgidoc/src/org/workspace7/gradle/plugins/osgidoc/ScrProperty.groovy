package org.workspace7.gradle.plugins.osgidoc


class ScrProperty {
	String name
	String value
	String type

	String toString(){
		"Property[Name:${name}, Type:${type}, Value:${value}]"
	}
}
