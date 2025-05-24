package myself.programing.coding.controllers;

import myself.programing.coding.dto.ChallengeDto;
import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.enums.API_RESPONSE_STATUS;
import myself.programing.coding.exception.ChallengeInfoException;
import myself.programing.coding.services.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Challenges")
public class ChallengeController {

    @Autowired
    ChallengeService challengeService;

    @GetMapping("/{type}")
    public HttpResponseApi<List<ChallengeDto>> getByType(@PathVariable int type) {
        try {
            return HttpResponseApi.<List<ChallengeDto>>builder()
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .data(challengeService.getByType(type))
                    .build();
        } catch (ChallengeInfoException e) {
            return HttpResponseApi.<List<ChallengeDto>>builder()
                    .code(API_RESPONSE_STATUS.BAD_REQUEST.getCode())
                    .message(API_RESPONSE_STATUS.BAD_REQUEST.getMessage() + "[ " + e.getMessage() + " ]")
                    .data(null)
                    .build();
        }
    }
}
