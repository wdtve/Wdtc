package org.wdt.wdtc.core.openapi.plugins.interfaces

interface Action {
	fun invoke()
}

interface ActionImpls : Action