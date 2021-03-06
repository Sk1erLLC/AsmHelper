package dev.falsehonesty.asmhelper.remapping

import dev.falsehonesty.asmhelper.dsl.instructions.Descriptor

/**
 * The MCP Remapper is used in an environment that targets vanilla (Notch Mappings).
 * This is primarily meant for use in an environment that still uses ForgeGradle however.
 */
class NotchRemapper : Remapper {
    private val classMappings = mutableMapOf<String, String>()
    private val fieldMappings = mutableMapOf<Descriptor, Descriptor>()
    private val methodMappings = mutableMapOf<Descriptor, Descriptor>()

    init {
        val mappings = javaClass.getResource("/mcp-notch.srg").readText()

        mappings.split("\n").forEach {
            when {
                it.startsWith("CL: ") -> {
                    val (deobf, obf) = it.substring(4).split(" ")

                    classMappings[deobf] = obf
                }
                it.startsWith("FD: ") -> {
                    val (deobf, obf) = it.substring(4).split(" ")

                    val deobfDescriptor = Descriptor(
                        deobf.substring(0, deobf.lastIndexOf("/")),
                        deobf.substring(deobf.lastIndexOf("/") + 1),
                        ""
                    )

                    val obfDescriptor = Descriptor(
                        obf.substring(0, obf.lastIndexOf("/")),
                        obf.substring(obf.lastIndexOf("/") + 1),
                        ""
                    )

                    fieldMappings[deobfDescriptor] = obfDescriptor
                }
                it.startsWith("MD: ") -> {
                    val (deobf, deobfDesc, obf, obfDesc) = it.substring(4).split(" ")

                    val deobfDescriptor = Descriptor(
                        deobf.substring(0, deobf.lastIndexOf("/")),
                        deobf.substring(deobf.lastIndexOf("/") + 1),
                        deobfDesc
                    )

                    val obfDescriptor = Descriptor(
                        obf.substring(0, obf.lastIndexOf("/")),
                        obf.substring(obf.lastIndexOf("/") + 1),
                        obfDesc
                    )

                    methodMappings[deobfDescriptor] = obfDescriptor
                }
            }
        }
    }

    override fun remapClassName(className: String) = classMappings[className] ?: className

    override fun remapMethodName(methodDescriptor: Descriptor) = methodMappings[methodDescriptor]?.name ?: methodDescriptor.name

    override fun remapFieldName(fieldDescriptor: Descriptor) = fieldMappings[fieldDescriptor.copy(desc = "")]?.name ?: fieldDescriptor.name

    override fun remapDesc(desc: String): String {
        TODO("Not yet implemented")
    }
}
