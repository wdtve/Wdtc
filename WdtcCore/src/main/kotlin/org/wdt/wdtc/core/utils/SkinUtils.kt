package org.wdt.wdtc.core.utils

import org.wdt.utils.io.*
import org.wdt.wdtc.core.manger.FileManger
import org.wdt.wdtc.core.utils.StringUtils.appendForString
import org.wdt.wdtc.core.utils.StringUtils.cleanStrInString
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO

class SkinUtils {
  @JvmField
  var skinFile: File? = null

  @JvmField
  var userName: String? = null

  var userSkinInput: InputStream? = null

  constructor(skinFile: File?) {
    this.skinFile = skinFile
  }

  constructor(userName: String?) {
    this.userName = userName
  }

  @Throws(IOException::class)
  fun writeSkinHead(): File {
    val image = ImageIO.read(userSkinInput ?: skinFile?.newInputStream())
    val extension = skinFile?.extension
    val image1 = image.getSubimage(8, 8, 8, 8)
    val file = File(
      FileManger.userAsste,
      skinFile!!.getName().cleanStrInString(".$extension").appendForString("-head.", extension)
    )
    file.touch()
    ImageIO.write(image1, extension, file.newOutputStream())
    return file
  }

  fun getSkinFile(): File {
    return File(FileManger.userAsste, "$userName.png")
  }

  @Throws(IOException::class)
  fun copySkinFile() {
    IOUtils.copy(userSkinInput, FileUtils.newOutputStream(getSkinFile()))
  }
}
