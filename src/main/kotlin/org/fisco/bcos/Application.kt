package org.fisco.bcos

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties
open class Application : CommandLineRunner {
    /**
     * Callback used to run the bean.
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    override fun run(vararg args: String?) {
        lgr.info("Meter Chain Starter launching...")
    }
}

private val lgr = LoggerFactory.getLogger(Application::class.java)
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)

    Runtime.getRuntime().addShutdownHook(Thread {
        lgr.info("Meter Chain Starter exited.")
    })
}
