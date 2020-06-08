package org.fisco.bcos.solidity

import org.apache.commons.io.FileUtils
import org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator
import org.fisco.bcos.web3j.solidity.compiler.CompilationResult
import org.fisco.bcos.web3j.solidity.compiler.SolidityCompiler
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.io.IOException
import java.util.*

class SolidityFunctionWrapperGeneratorTest {
    protected var tempDirPath = File("src/test/java/").absolutePath
    protected var packageName = "org.fisco.bcos.solidity"

    @Test
    @Throws(Exception::class)
    fun generateClassFromABIForHelloWorld() {
        val binFile1 = ClassPathResource("solidity/HelloWorld.bin").file.absolutePath
        val abiFile1 = ClassPathResource("solidity/HelloWorld.abi").file.absolutePath
        SolidityFunctionWrapperGenerator.main(Arrays.asList(
                "-b", binFile1,
                "-a", abiFile1,
                "-p", packageName,
                "-o", tempDirPath
        ).toTypedArray())
    }

    @Test
    @Throws(IOException::class)
    fun compileSolFilesToJavaTest() {
        val solFileList = File("src/test/resources/contract")
        val solFiles = solFileList.listFiles()
        for (solFile in solFiles) {
            val res = SolidityCompiler.compile(solFile, true, SolidityCompiler.Options.ABI, SolidityCompiler.Options.BIN, SolidityCompiler.Options.INTERFACE, SolidityCompiler.Options.METADATA)
            log.info("Out: '{}'", res.output)
            log.info("Err: '{}'", res.errors)
            val result = CompilationResult.parse(res.output)
            log.info("contractname  {}", solFile.name)
            val contractname = solFile.name.split("\\.").toTypedArray()[0]
            val a = result.getContract(solFile.name.split("\\.").toTypedArray()[0])
            log.info("abi   {}", a.abi)
            log.info("bin   {}", a.bin)
            FileUtils.writeStringToFile(File("src/test/resources/solidity/$contractname.abi"), a.abi)
            FileUtils.writeStringToFile(File("src/test/resources/solidity/$contractname.bin"), a.bin)
            var binFile: String
            var abiFile: String
            val tempDirPath = File("src/test/java/").absolutePath
            val packageName = "org.fisco.bcos.temp"
            abiFile = "src/test/resources/solidity/$contractname.abi"
            binFile = "src/test/resources/solidity/$contractname.bin"
            SolidityFunctionWrapperGenerator.main(Arrays.asList(
                    "-a", abiFile,
                    "-b", binFile,
                    "-p", packageName,
                    "-o", tempDirPath
            ).toTypedArray())
        }
        println("generate successfully")
    }

    companion object {
        private val log = LoggerFactory.getLogger(SolidityFunctionWrapperGeneratorTest::class.java)
    }
}