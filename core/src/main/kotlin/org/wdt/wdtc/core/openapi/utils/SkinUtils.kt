package org.wdt.wdtc.core.openapi.utils

import org.wdt.wdtc.core.openapi.manager.userAsste
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO

class SkinUtils(
	private var skinFile: File
) {
	
	private var userName: String? = null
	
	var userSkinInput: InputStream? = null
	
	suspend fun writeSkinHead(): File = runOnIO {
		val newImage = ImageIO.read(userSkinInput ?: skinFile.inputStream()).getSubimage(8, 8, 8, 8)
		skinFile.extension.let {
			File(userAsste, skinFile.name.cleanStrInString(".$it").appendForString("-head.", it)).apply {
				createNewFile()
				ImageIO.write(newImage, extension, outputStream())
			}
		}
	}
	
	companion object {
		fun getUserSkinFile(userName: String?): File {
			return File(userAsste, "$userName.png")
		}
	}
}
