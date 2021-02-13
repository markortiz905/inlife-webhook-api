package com.inlife.webhook;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * @author Mark Anthony Ortiz - ortizmark905@gmail.com
 */
@Slf4j
//@EnableRetry
@EnableJpaRepositories
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class InlifeWebhookApplication {

	public static void main(String[] args) {
		log.info("SPRING_DATASOURCE_URL: " + System.getenv("SPRING_DATASOURCE_URL"));
		log.info("SPRING_DATASOURCE_URL:" + System.getenv("SPRING_DATASOURCE_USERNAME"));
		SpringApplication.run(InlifeWebhookApplication.class, args);
	}

	@Bean
	@Profile("dev")
	public CommonsRequestLoggingFilter logFilter() {
		CommonsRequestLoggingFilter filter
				= new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(100);
		filter.setIncludeHeaders(true);
		filter.setAfterMessagePrefix("REQUEST DATA : ");
		return filter;
	}

	@Bean
	public OpenAPI apiDoc() {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("basicScheme",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
				.info(new Info().title("Inlife Webhool API").description(
						"This is a sample server Inlife Webhool API server.")
						.termsOfService("http://swagger.io/terms/")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}

	//@Bean
	//@Profile({"dev", "test"})
	public CommandLineRunner printSomeProps(@Value("${spring.datasource.url}") String dbUrl, OpenAPI openAPI) {
		return (arg) -> {
			log.info("user.timezone : " + System.getProperty("user.timezone"));
			log.info("spring.datasource.url = " + dbUrl);
		};
	}

	/*@Bean
	@Profile({"dev", "test"})
	public CommandLineRunner testElasticSearch(EventEsService eventEsService, ElasticsearchOperations operations) {
		return (arg) -> {
			log.info("trying out create in elastic search...");
			operations.indexOps(EventDto.class).refresh();
			EventDto event = EventDto.builder().id(100L).eventName("ElasticSearch Test 1").build();
			EventDto event2 = EventDto.builder().id(101L).eventName("ElasticSearch Test 2").build();
			event2 = event2.toBuilder().information(InformationDto.builder()
					.eventId(101L).country("Canada").city("some city")
					.startDate(LocalDate.of(2021, 01, 10))
					.endDate(LocalDate.of(2021, 03, 10)).build()).build();
			event = event.toBuilder().information(InformationDto.builder()
					.eventId(101L).country("Canada").city("some city")
					.startDate(LocalDate.of(2021, 01, 10))
					.endDate(LocalDate.of(2021, 03, 10)).build()).build();

			CompletableFuture<EventDto>[] list = new CompletableFuture[2];
			List.of(event, event2).stream()
					.map(eventEsService::saveEvent).collect(Collectors.toList()).toArray(list);
			CompletableFuture.allOf(list).join();

			log.info("---> " + list[0].get().getEventName());
			log.info("---> " + list[1].get().getEventName());
			//Thread.sleep(5000);
			eventEsService.findByEventName("ElasticSearch", PageRequest.of(0,10)).stream().forEach(e->log.info(e.getEventName()));

			//log.info("trying out update on elastic search...");
			//eventEsService.saveEvent(event2.toBuilder().eventName("updated document").build()).join();

			//eventEsService.findById(101L).ifPresent(e->log.info(e.getEventName()));

			Page<EventDto> dtos = eventEsService.searchByCriteria(Optional.of("Test"),
					Optional.of(SortOrder.UPCOMING), Optional.ofNullable(null), Optional.ofNullable(null),
					Optional.ofNullable(null),
					Optional.ofNullable(null),
					List.of("Marathon"),
					PageRequest.of(0, 10));

			//log.info("size: " + dtos.size());

			dtos.stream().forEach(e->log.info(e.getEventName() + ", " + e.getInformation().getCountry()));

			//eventEsService.findAll(PageRequest.of(0,10)).stream().forEach(e->log.info(e.getEventName()));

		};
	}*/

	/**@Bean
	@Profile({"dev", "test"})
	public CommandLineRunner executeQuery(EventService eventService,
										  CategoryService categoryService,
										  InformationService informationService,
										  EditionService editionService,
										  SerieService serieService, SerieMapper serieMapper) {
		return (args) -> {
			Event e1 = eventService.create("test1");
			Event e2 = eventService.create("test2");
			eventService.getAll().forEach(str -> log.info(str.getEventName()));
			eventService.getAll().forEach(str -> log.info(str.getEventName()));
			eventService.create("test3");
			eventService.getAll().forEach(str -> log.info(str.getEventName()));
			eventService.getAll().forEach(str -> log.info(str.getEventName()));


			Category cat = categoryService.create("cat1", "some category");
			Information info = informationService.create(Information.builder().city("Mati City").build());
			Edition edition = editionService.create("2021", System.currentTimeMillis(), System.currentTimeMillis(), true, e1);
			Serie serie1 = serieService.create("Serie1", Gender.Male, 100, 15, 100, e1);


			serie1 = serie1.toBuilder().edition(edition).build();
			serie1 = serieService.updateSerie(serieMapper.toDto(serie1), serieMapper::updateEntity);
			edition = editionService.getEditionAndRacesAndSeries(edition);

			e2 = e2.toBuilder().category(cat).build();

			e1 = e1.toBuilder().category(cat).information(info).build();

			eventService.update(e1);
			eventService.update(e2);
			List<Event> e3 = categoryService.getCategoryEvents(cat.getId());

			for (Event event : e3) {
				System.out.println(event.getEventName());
			}

			Event eInfo = eventService.getEvent(e1.getId());
			eInfo = eventService.getEventAndEditions(e1);
			System.out.println(eInfo.getInformation().getCity());

			eInfo = eventService.getEventAndSeries(eInfo);

			eInfo.getEditions().forEach(System.out::println);
			eInfo.getSeries().forEach(System.out::println);

			edition.getSeries().forEach(System.out::println);
			edition.getRaces().forEach(System.out::println);
		};
	}*/

}
