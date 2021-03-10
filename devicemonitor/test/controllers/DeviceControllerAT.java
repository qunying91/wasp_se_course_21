package controllers;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.test.Helpers.*;

/**
 * Acceptance test for DeviceController
 */
public class DeviceControllerAT {

    /**
     * TC1: a basic acceptance test case to ensure welcome page is shown
     *
     * input: no test input required
     * output: a welcome page with proper heading
     * oracle: assert the page is received, and content has the intended information
     *
     */
    @Test
    public void testGettingWelcomePage() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333");
            assertThat(browser.pageSource(), containsString("Welcome to the device monitor platform"));
        });
    }

}
