package sudo.managers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import sudo.Client
import sudo.module.Mod
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class ConfigManager {
    private val path = File("Sudo")

    init {
        initModules()
    }

    private fun initModules() {
        if (!path.exists()) {
            path.mkdirs()
        }
        for (module in Client.moduleManager!!.modules) {
            val modulePath = getModulePath(module)
            if (!modulePath.exists()) {
                saveModuleConfig(module)
            } else {
                loadModule(module)
            }
        }
    }


    private fun saveModuleConfig(module: Mod) {
        val modulePath = getModulePath(module)
        if (!modulePath.exists()) {
            modulePath.parentFile.mkdirs()
            modulePath.createNewFile()
        }
        modulePath.parentFile.mkdirs()
        modulePath.createNewFile()
        val moduleJson = JsonObject()
        moduleJson.addProperty("Name", module.name)
        moduleJson.addProperty("Toggle", module.isEnabled)
        Files.write(
            modulePath.toPath(), GsonBuilder().setPrettyPrinting().create().toJson(moduleJson).toByteArray(
                StandardCharsets.UTF_8
            )
        )
    }

    private fun loadModule(module: Mod) {
        val modulePath = getModulePath(module)
        if (!modulePath.exists()) return
        val moduleJson = Gson().fromJson(
            String(Files.readAllBytes(modulePath.toPath()), StandardCharsets.UTF_8),
            JsonObject::class.java
        ) ?: return
        module.name = moduleJson.get("Name").asString
        val toggle = moduleJson.get("Toggle").asBoolean
        if (module.isEnabled && !toggle) {
            module.toggle()
        }
        if (!module.isEnabled && toggle) {
            module.toggle()
        }
        val element = moduleJson.get("Settings") ?: return
    }

    private fun saveAllModules() {
        for (module in Client.moduleManager!!.modules) {
            saveModuleConfig(module)
        }
    }


    private fun loadAllModules() {
        for (module in Client.moduleManager!!.modules) {
            loadModule(module)
        }
    }
    //ToDo add save setting thingy

    private fun getModulePath(module: Mod): File {
        return File("$path/modules/${module.category.name}/${module.name}.json")
    }

    fun saveAll() {
        saveAllModules()
    }

    fun loadAll() {
        loadAllModules()
    }
}