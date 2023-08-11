package com.kafkahandson.kafkadocker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkahandson.kafkadocker.config.KafkaConfigProps;
import com.kafkahandson.kafkadocker.domain.ChatMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class KafkaDockerApplication {

	private final ObjectMapper objectMapper;

	@Autowired
	public KafkaDockerApplication(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(KafkaDockerApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(final KafkaTemplate<String, String> kafkaTemplate, final KafkaConfigProps kafkaConfigProps) throws JsonProcessingException {

        final ChatMessageEvent event = ChatMessageEvent.builder()
				.clientId(UUID.randomUUID().toString())
				.dateTime(LocalDateTime.now())
				.textMessage(getRandomSentence(4))
				.build();

		final String payload = objectMapper.writeValueAsString(event);
        return args -> {
			kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
		};
    }

	@KafkaListener(topics = "client-chats")
	public String listens(final String in){
		System.out.println(in);
		return in;
	}


	/**
	 * list of words.
	 */
	private static final String[] words = new String[] { "Lorem", //$NON-NLS-1$
			"ipsum", //$NON-NLS-1$
			"dolor", //$NON-NLS-1$
			"sit", //$NON-NLS-1$
			"amet", //$NON-NLS-1$
			"consetetur", //$NON-NLS-1$
			"sadipscing", //$NON-NLS-1$
			"elitr", //$NON-NLS-1$
			"sed", //$NON-NLS-1$
			"diam", //$NON-NLS-1$
			"nonumy", //$NON-NLS-1$
			"eirmod", //$NON-NLS-1$
			"tempor", //$NON-NLS-1$
			"invidunt", //$NON-NLS-1$
			"ut", //$NON-NLS-1$
			"labore", //$NON-NLS-1$
			"et", //$NON-NLS-1$
			"dolore", //$NON-NLS-1$
			"magna", //$NON-NLS-1$
			"aliquyam", //$NON-NLS-1$
			"erat", //$NON-NLS-1$
			"sed", //$NON-NLS-1$
			"diam", //$NON-NLS-1$
			"voluptua", //$NON-NLS-1$
			"At", //$NON-NLS-1$
			"vero", //$NON-NLS-1$
			"eos", //$NON-NLS-1$
			"et", //$NON-NLS-1$
			"accusam", //$NON-NLS-1$
			"et", //$NON-NLS-1$
			"justo", //$NON-NLS-1$
			"duo", //$NON-NLS-1$
			"dolores", //$NON-NLS-1$
			"et", //$NON-NLS-1$
			"ea", //$NON-NLS-1$
			"rebum", //$NON-NLS-1$
			"Stet", //$NON-NLS-1$
			"clita", //$NON-NLS-1$
			"kasd", //$NON-NLS-1$
			"gubergren", //$NON-NLS-1$
			"no", //$NON-NLS-1$
			"sea", //$NON-NLS-1$
			"takimata", //$NON-NLS-1$
			"sanctus", //$NON-NLS-1$
			"est" };
	/**
	 * random number producer.
	 */
	private static final Random random = new Random();

	/**
	 * returns a random sentence.
	 * @param wordNumber number of word in the sentence
	 * @return random sentence made of <code>wordNumber</code> words
	 */
	public static String getRandomSentence(int wordNumber) {
		StringBuilder buffer = new StringBuilder(wordNumber * 12);

		int j = 0;
		while (j < wordNumber) {
			buffer.append(getRandomWord());
			buffer.append(" "); //$NON-NLS-1$
			j++;
		}
		return buffer.toString();
	}

	/**
	 * returns a random word.
	 * @return random word
	 */
	public static String getRandomWord() {
		return words[random.nextInt(words.length)];
	}
}
