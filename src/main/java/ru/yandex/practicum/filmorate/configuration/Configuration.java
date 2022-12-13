package ru.yandex.practicum.filmorate.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.aspect", "ru.yandex.practicum.filmorate.db.dao"})
public class Configuration {
}
