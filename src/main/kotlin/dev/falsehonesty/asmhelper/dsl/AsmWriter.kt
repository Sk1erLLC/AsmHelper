package dev.falsehonesty.asmhelper.dsl

import dev.falsehonesty.asmhelper.dsl.code.InjectCodeBuilder
import dev.falsehonesty.asmhelper.dsl.instructions.InsnListBuilder
import dev.falsehonesty.asmhelper.printing.logger
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

abstract class AsmWriter(val className: String) {
    abstract fun transform(classNode: ClassNode)

    protected fun transformToInstructions(
        insnListBuilder: (InsnListBuilder.() -> Unit)?,
        codeBlockClassName: String?,
        method: MethodNode,
        classNode: ClassNode
    ): InsnList? {
        return when {
            insnListBuilder != null && codeBlockClassName != null -> {
                logger.error("$this specifies both an insnList and a codeBlock, please pick one or the other.")
                null
            }
            insnListBuilder != null -> {
                val builder = InsnListBuilder(method)
                insnListBuilder.let { builder.it() }
                builder.build()
            }
            codeBlockClassName != null -> {
                val clazzPath = codeBlockClassName.replace('.', '/') + ".class"
                val clazzInputStream = javaClass.classLoader.getResourceAsStream(clazzPath)

                val clazzReader = ClassReader(clazzInputStream)
                val codeClassNode = ClassNode()
                clazzReader.accept(codeClassNode, ClassReader.SKIP_FRAMES)

                val codeBuilder = InjectCodeBuilder(codeClassNode, classNode, method)

                codeBuilder.transformToInstructions()
            }
            else -> {
                logger.error("$this does not have instructions to inject. You must specify an insnList or codeBlock.")
                null
            }
        }
    }

    abstract class AsmWriterBuilder {
        val fullMcVersion =
            //#if MC==10809
            10809
        //#elseif MC==11202
        //$$ 11202
        //#elseif MC==11502
        //$$ 11502
        //#else
        //$$ 11604
        //#endif

        val mcVersion =
            //#if MC==10809
            8
        //#elseif MC==11202
        //$$ 12
        //#elseif MC==11502
        //$$ 15
        //#else
        //$$ 16
        //#endif
    }
}
