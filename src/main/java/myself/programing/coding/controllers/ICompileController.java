package myself.programing.coding.controllers;

import java.util.List;
import myself.programing.coding.dto.CompileRequestDto;
import myself.programing.coding.dto.CompileResponse;
import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.dto.RunWithTestCasesDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ICompileController {

    @PostMapping("/")
    HttpResponseApi<CompileResponse> compile(@RequestBody CompileRequestDto request);

    @PostMapping("/run")
    HttpResponseApi<CompileResponse> run(@RequestBody CompileRequestDto request);

    @PostMapping("/runWithTestcases")
    HttpResponseApi<List<RunWithTestCasesDto>> runWithTests(@RequestBody CompileRequestDto request);
}
