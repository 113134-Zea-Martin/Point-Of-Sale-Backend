package com.scaffold.template;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		// espera un poco más para asegurar que el servidor esté listo
		try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
		openBrowser("http://localhost:8080");
	}

	private void openBrowser(String url) {
		try {
			if (!GraphicsEnvironment.isHeadless() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(url));
				return;
			}

			String os = System.getProperty("os.name").toLowerCase();
			Runtime rt = Runtime.getRuntime();
			if (os.contains("win")) {
				rt.exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", url});
			} else if (os.contains("mac")) {
				rt.exec(new String[] {"open", url});
			} else {
				rt.exec(new String[] {"xdg-open", url});
			}
		} catch (Exception e) {
			System.err.println("No se pudo abrir el navegador: " + e.getMessage());
		}
	}
}