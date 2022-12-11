package ru.yandex.practicum.filmorate;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.yandex.practicum.filmorate.web.converter.SearchByTypeConverter;
import ru.yandex.practicum.filmorate.web.converter.SortTypeDirectorsConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortTypeDirectorsConverter());
        registry.addConverter(new SearchByTypeConverter());
    }
}
