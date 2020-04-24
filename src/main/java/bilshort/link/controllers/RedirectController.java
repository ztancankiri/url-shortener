package bilshort.link.controllers;

import bilshort.link.models.Link;
import bilshort.link.services.LinkService;
import eu.bitwalker.useragentutils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.rasel.throttling.Throttling;
import ro.rasel.throttling.ThrottlingType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@RequestMapping("r")
@RestController
public class RedirectController {

    @Autowired
    private LinkService linkService;

    @Throttling(type = ThrottlingType.RemoteAddr, limit = 100, timeUnit = TimeUnit.MINUTES)
    @GetMapping("{code}")
    public ResponseEntity<?> redirectUrl(@PathVariable("code") String code, @RequestHeader(value = "User-Agent") String userAgent) {
        HttpHeaders responseHeaders = new HttpHeaders();

        Link link = linkService.getLinkByCode(code);

        if (link == null) {
            return ResponseEntity.notFound().build();
        }

        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        String browser = ua.getBrowser().getName();
        String os = ua.getBrowser().getName();

        if (browser.contains("Chrome")) {
            link.setVisitCountFromChrome(link.getVisitCountFromChrome() + 1);
        }
        else if (browser.contains("Firefox")) {
            link.setVisitCountFromFirefox(link.getVisitCountFromFirefox() + 1);
        }
        else if (browser.contains("Safari")) {
            link.setVisitCountFromSafari(link.getVisitCountFromSafari() + 1);
        }
        else if (browser.contains("Internet Explorer")) {
            link.setVisitCountFromIE(link.getVisitCountFromIE() + 1);
        }
        else {
            link.setVisitCountFromOtherBrowser(link.getVisitCountFromOtherBrowser() + 1);
        }


        if (os.contains("Windows")) {
            link.setVisitCountFromWindows(link.getVisitCountFromWindows() + 1);
        }
        else if (os.contains("Linux")) {
            link.setVisitCountFromLinux(link.getVisitCountFromLinux() + 1);
        }
        else if (os.contains("Mac")) {
            link.setVisitCountFromOsx(link.getVisitCountFromOsx() + 1);
        }
        else if (os.contains("Android")) {
            link.setVisitCountFromAndroid(link.getVisitCountFromAndroid() + 1);
        }
        else if (os.contains("iOS")) {
            link.setVisitCountFromIOS(link.getVisitCountFromIOS() + 1);
        }
        else {
            link.setVisitCountFromOtherOs(link.getVisitCountFromOtherOs() + 1);
        }

        linkService.updateLink(link);

        try {
            responseHeaders.setLocation(new URI(link.getUrl()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(responseHeaders).build();
    }
}