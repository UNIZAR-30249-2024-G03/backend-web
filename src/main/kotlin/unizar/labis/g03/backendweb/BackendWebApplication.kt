package unizar.labis.g03.backendweb

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class BackendWebApplication

fun main(args: Array<String>) {
	runApplication<BackendWebApplication>(*args)
}

@Bean
fun jsonConverter(): MessageConverter {
	return Jackson2JsonMessageConverter()
}
