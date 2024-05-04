@file:JvmName("PluginsLoader")

package org.wdt.wdtc.core.openapi.plugins.loader

@field:JvmField
var currentClassLoader: ClassLoader = ClassLoader.getSystemClassLoader()