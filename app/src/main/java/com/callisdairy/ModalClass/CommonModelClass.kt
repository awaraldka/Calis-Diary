package com.callisdairy.ModalClass

import java.io.File

class CommonModelClass {
}

data class FileParsingClass  (var imageFile : File, var mimeType : String): java.io.Serializable




data class dateImport(
    var Date: Long,
    var EventType:String,
    var userName:String,
    var petname:String,
        )