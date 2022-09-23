package com.jean.postapp

import java.io.InputStreamReader

class JsonFileLoader {

    private var jsonStr: String? = null

    fun loadJsonString(file: String): String? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return jsonStr
    }
}