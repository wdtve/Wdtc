package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.utils.StringUtils
import java.io.File
import java.io.IOException

abstract class AbstractGameCommand {
  protected val commandBuilder: StringBuilder = StringBuilder()

  @Throws(IOException::class)
  abstract fun getCommand(): StringBuilder
  protected fun nonBreakingSpace(o: Any) {
    commandBuilder.append(o).append(StringUtils.STRING_SPACE)
  }

  protected fun insertclasspathSeparator(file: File) {
    insertclasspathSeparator(file.absolutePath)
  }

  protected fun insertclasspathSeparator(str: String) {
    commandBuilder.append(str).append(";")
  }

  protected fun insertSpace(str: String?) {
    commandBuilder.append(str).append(StringUtils.STRING_SPACE)
  }

  protected fun nonBreakingSpace(str: String, string: Any) {
    nonBreakingSpace(str + string)
  }
}
