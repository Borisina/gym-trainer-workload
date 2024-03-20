package com.kolya.gym;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/component-features",  // указываете путь к .feature файлам
        glue = "com.kolya.gym.component.steps"  // указываете пакет, где находятся определения шагов
)
public class CucumberComponentTest {
}
