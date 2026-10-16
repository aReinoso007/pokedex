package dev.pokedex;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/** Enforces the Clean Architecture dependency rule at build time. */
@AnalyzeClasses(packages = "dev.pokedex", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule domainDependsOnNothingOutsideItself = noClasses()
            .that().resideInAPackage("dev.pokedex.domain..")
            .should().dependOnClassesThat().resideInAnyPackage("dev.pokedex.application..", "dev.pokedex.infrastructure..", "dev.pokedex.web..");

    @ArchTest
    static final ArchRule domainIsFrameworkFree = noClasses()
            .that().resideInAPackage("dev.pokedex.domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..", "jakarta..", "com.fasterxml..", "io.jsonwebtoken..");

    @ArchTest
    static final ArchRule applicationOnlyKnowsDomain = noClasses()
            .that().resideInAPackage("dev.pokedex.application..")
            .should().dependOnClassesThat().resideInAnyPackage("dev.pokedex.infrastructure..", "dev.pokedex.web..");

    @ArchTest
    static final ArchRule applicationIsFrameworkFree = noClasses()
            .that().resideInAPackage("dev.pokedex.application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..", "jakarta..", "com.fasterxml..");

    @ArchTest
    static final ArchRule infrastructureDoesNotReachIntoWeb = noClasses()
            .that().resideInAPackage("dev.pokedex.infrastructure..")
            .should().dependOnClassesThat().resideInAPackage("dev.pokedex.web..");

    @ArchTest
    static final ArchRule webDoesNotTouchInfrastructure = noClasses()
            .that().resideInAPackage("dev.pokedex.web..")
            .should().dependOnClassesThat().resideInAPackage("dev.pokedex.infrastructure..");
}
