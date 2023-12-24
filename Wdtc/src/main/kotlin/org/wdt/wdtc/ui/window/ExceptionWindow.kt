package org.wdt.wdtc.ui.window

import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.logmaker
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea

fun setErrorWin(e: Throwable) {
  logmaker.error("Error", e)
  setWin(e.getExceptionMessage(), "发生错误!")
}

fun setWin(e: String, title: String) {
  val window = JFrame(title)
  val textArea = JTextArea()
  textArea.text = e
  textArea.setCaretPosition(0)
  textArea.setCaretColor(Color.RED)
  textArea.setBackground(Color(0xEEEEEE))
  textArea.setFont(Font("JetBrains Mono", Font.ITALIC, 14))
  textArea.setSelectionColor(Color.BLACK)
  textArea.setSelectedTextColor(Color.WHITE)
  textArea.setLineWrap(true)
  textArea.setWrapStyleWord(true)
  val jsp = JScrollPane(textArea)

  window.layout = BorderLayout()
  window.add(jsp, BorderLayout.CENTER)
  window.setTitle("文本编辑器")
  window.isVisible = true
  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  window.setSize(800, 500)
  window.setResizable(true)

}
