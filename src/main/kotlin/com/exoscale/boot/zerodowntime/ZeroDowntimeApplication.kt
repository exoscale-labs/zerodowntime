package com.exoscale.boot.zerodowntime

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@SpringBootApplication
class ZeroDowntimeApplication {

    @Bean
    fun displayInfo(context: Context) = CommandLineRunner {
        println("START: ${context.hostname} (running v${context.version})")
    }

    @Bean
    fun context() = Context()
}

fun main(args: Array<String>) {
    runApplication<ZeroDowntimeApplication>(*args)
}

@Controller
class PersonController(private val repository: PersonRepository, private val context: Context) {

    @GetMapping("/")
    fun getAll(model: Model): String {
        println("GET : ${context.hostname} (running v${context.version})")
        model["person"] = Person()
        model["persons"] = repository.findAll()
        model["title"] = "Person ${context.hostname} (v${context.version})"
        return "/persons"
    }

    @PostMapping("/")
    fun create(@Valid person: Person, result: BindingResult): String {
        println("POST: ${context.hostname} (running v${context.version})")
        repository.save(person)
        return "redirect:/"
    }
}

interface PersonRepository : JpaRepository<Person, Long>

