package org.wdt.wdtc.core.impl.launch

import org.wdt.wdtc.core.openapi.utils.STRING_SPACE


abstract class AbstractGameCommand {
  protected val commandBuilder: StringBuilder = StringBuilder()
  private val regex = ".*\\\$\\{(.*)}".toRegex()

  abstract fun getCommand(): StringBuilder

  abstract val dataMap: Map<String, String>
	
	protected fun nonBreakingSpace(o: Any) {
    commandBuilder.append(o).append(STRING_SPACE)
  }

  protected fun nonBreakingSpace(str: String, o: Any) {
    commandBuilder.append(str).append(o).append(STRING_SPACE)
  }

  protected fun String.replaceData(dataMap: Map<String, String>): String {
    if (this.matches(regex)) {
      return regex.find(this)?.groups?.get(1)?.value.let { str ->
        dataMap[str].let {
          if (it == null) this
          else replace("\${$str}", it)
        }
      }
    }
    return this
  }
}
