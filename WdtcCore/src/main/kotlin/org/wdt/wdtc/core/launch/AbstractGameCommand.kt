package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.utils.STRING_SPACE
import java.io.File

abstract class AbstractGameCommand {
  protected val commandBuilder: StringBuilder = StringBuilder()

  abstract fun getCommand(): StringBuilder
  protected fun nonBreakingSpace(o: Any) {
    commandBuilder.append(o).append(STRING_SPACE)
  }

  protected fun insertclasspathSeparator(file: File) {
    insertclasspathSeparator(file.absolutePath)
  }

  private fun insertclasspathSeparator(str: String) {
    commandBuilder.append(str).append(";")
  }

  protected fun insertSpace(str: String?) {
    commandBuilder.append(str).append(STRING_SPACE)
  }

  protected fun nonBreakingSpace(str: String, string: Any) {
    nonBreakingSpace(str + string)
  }
}
