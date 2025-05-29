package myself.programing.coding.controllers;

import myself.programing.coding.dto.ChallengeDto;
import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.enums.API_RESPONSE_STATUS;
import myself.programing.coding.enums.CHALLENGE_ERROR_TYPE;
import myself.programing.coding.exception.ChallengeInfoException;
import myself.programing.coding.services.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    @Autowired
    ChallengeService challengeService;

    @GetMapping("/type/{type}")
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

    @GetMapping("/{id}")
    public HttpResponseApi<ChallengeDto> getChallenge(@PathVariable Long id) {
        try {
            ChallengeDto challenge = challengeService.getById(id);
            return HttpResponseApi.<ChallengeDto>builder()
                    .data(challenge)
                    .message("OK")
                    .code(CHALLENGE_ERROR_TYPE.NORMAL_RESULT.getCode())
                    .build();
        } catch (ChallengeInfoException e) {
            return HttpResponseApi.<ChallengeDto>builder()
                    .data(null)
                    .message(e.getInfoMessage())
                    .code(e.getStatus())
                    .build();
        }
    }
}
