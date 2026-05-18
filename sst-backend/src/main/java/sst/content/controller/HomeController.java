package sst.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sst.content.dto.PlaceCardDto;
import sst.content.service.CodeMasterService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final CodeMasterService codeMasterService;

    @GetMapping("/api/home/places")
    public List<PlaceCardDto> getPlaceCards(@RequestParam("regionCode") int regionCode) {
        return codeMasterService.getPlaceCardsByRegionCode(regionCode);
    }
}