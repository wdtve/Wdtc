package org.wdt.wdtc.core.openapi.utils

import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.newInputStream
import org.wdt.utils.io.newOutputStream
import org.wdt.utils.io.touch
import org.wdt.wdtc.core.openapi.manger.userAsste
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO

class SkinUtils(
	private var skinFile: File
) {
	
	private var userName: String? = null
	
	var userSkinInput: InputStream? = null
	
	suspend fun writeSkinHead(): File = runOnIO {
		val newImage = ImageIO.read(userSkinInput ?: skinFile.newInputStream()).getSubimage(8, 8, 8, 8)
		skinFile.extension.let {
			File(userAsste, skinFile.name.cleanStrInString(".$it").appendForString("-head.", it)).apply {
				launch("Write skin file") {
					touch()
					ImageIO.write(newImage, extension, outputStream())
				}
			}
		}
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
