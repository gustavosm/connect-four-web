package com.deviget.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class PackageOrganizing {

    @Test
    public void domainMustNotSeeIncoming() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.deviget");
        ArchRule rule = noClasses().that().resideInAPackage("..domain..").and()
                .haveSimpleNameNotEndingWith("TestIT").should().accessClassesThat()
                .resideInAPackage("..application.incoming..")
                .because("Classes are not supposed to be aware of who is calling them");

        rule.check(classes);
    }
}
