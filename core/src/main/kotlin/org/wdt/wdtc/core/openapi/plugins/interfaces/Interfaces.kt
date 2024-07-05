package org.wdt.wdtc.core.openapi.plugins.interfaces

interface Action {
	fun invoke()
}

// TODO More Action api

interface StartupAction : Action