package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.utils.StringUtils
import java.io.File
import java.io.IOException

abstract class AbstractGameCommand {
  protected val commandBuilder: StringBuilder = StringBuilder()

  @Throws(IOException::class)
  abstract fun getCommand(): StringBuilder
  protected fun NonBreakingSpace(o: Any?) {
    commandBuilder.append(o).append(StringUtils.STRING_SPACE)
  }

  protected fun InsertclasspathSeparator(file: File) {
    InsertclasspathSeparator(file.absolutePath)
  }

  protected fun InsertclasspathSeparator(str: String?) {
    commandBuilder.append(str).append(";")
  }

  protected fun InsertSpace(str: String?) {
    commandBuilder.append(str).append(StringUtils.STRING_SPACE)
  }

  protected fun NonBreakingSpace(str: String, string: Any) {
    NonBreakingSpace(str + string)
  }
}
