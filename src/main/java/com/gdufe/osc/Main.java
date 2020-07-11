package com.gdufe.osc;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * @author changwenbo
 * @date 2020/7/2 17:35
 */
public class Main {
	public static void main(String[] args) {
		Flux.create((Consumer<FluxSink<Integer>>) (sink) -> {
			for (int i = 1; i <= 10; i++) {
				sink.next(i);
			}
			sink.complete();
		}).filter((x) -> (x / 0) == 1).reduce(0, (x, y) -> x + y)
				.onErrorResume(e -> {
					if (e instanceof IllegalStateException) {
						return Mono.just(10);
					}
					if (e instanceof ArithmeticException) {
						return Mono.just(20);
					}
					return Mono.just(66);
				})
				.doOnSubscribe()



		Mono.create((Consumer<MonoSink<Integer>>) (sink) -> {
			sink.success(10);
		}).map((x) -> x * x).take(Duration.ofMillis(100)).subscribe((x) -> System.out.println("x = " + x));

	}
}
