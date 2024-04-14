package unizar.labis.g03.backendweb

import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.util.MimeTypeUtils



@SpringBootApplication
class BackendWebApplication

fun main(args: Array<String>) {
	runApplication<BackendWebApplication>(*args)
}

// Not sure if needed

@Bean
fun jsonConverter(): MessageConverter {
	val messageConverter = object : Jackson2JsonMessageConverter(){
		override fun fromMessage(message: Message): Any {
			message.messageProperties.contentType = "application/json"
			return super.fromMessage(message)
		}
	}
	val utf16 = "application/json; charset=utf-16"

	messageConverter.setSupportedContentType(MimeTypeUtils.parseMimeType(utf16))
	return messageConverter
}

@Bean
fun jsonRabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
	val template = RabbitTemplate(connectionFactory)
	template.messageConverter = jsonConverter()
	return template
}

/*
@Bean
fun jsonConverter(): MessageConverter {
	return Jackson2JsonMessageConverter()
}
 */
