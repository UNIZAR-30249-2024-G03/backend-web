package unizar.labis.g03.backendweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendWebApplication

fun main(args: Array<String>) {
	runApplication<BackendWebApplication>(*args)
}
