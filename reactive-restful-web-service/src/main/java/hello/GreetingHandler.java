package hello;

import java.util.Random;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {
	public Mono<ServerResponse> randomInt(ServerRequest request){
		Random rand = new Random();
		String tmp = Integer.toString(rand.nextInt()%100);
		return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromValue(tmp));
	}
}
