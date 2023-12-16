package org.wdt.wdtc.core.utils

import org.wdt.utils.io.*
import org.wdt.wdtc.core.manger.userAsste
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

  constructor(skinFile: File) {
    this.skinFile = skinFile
  }

  constructor(userName: String) {
    this.userName = userName
  }

  @Throws(IOException::class)
  fun writeSkinHead(): File {
    val image = ImageIO.read(userSkinInput ?: skinFile?.newInputStream())
    val extension = skinFile?.extension
    val newImage = image.getSubimage(8, 8, 8, 8)
    val headPhoto = File(
      userAsste, skinFile?.name?.cleanStrInString(".$extension")?.appendForString("-head.", extension)!!
    )
    headPhoto.touch()
    ImageIO.write(newImage, extension, headPhoto.newOutputStream())
    return headPhoto
  }

  fun copySkinFile() {
    IOUtils.copy(userSkinInput, getUserSkinFile(userName).newOutputStream())
  }

  companion object {
    fun getUserSkinFile(userName: String?): File {
      return File(userAsste, "$userName.png")
    }
  }
}
