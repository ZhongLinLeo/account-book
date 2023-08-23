package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.application.info.FundsRecordInfo;
import cn.zl.account.book.controller.application.FundsRecordAppService;
import cn.zl.account.book.controller.converter.FundsRecordConverter;
import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.controller.request.FundsRecordRequestDTO;
import cn.zl.account.book.controller.request.PaginationFundsRecordRequestDTO;
import cn.zl.account.book.controller.response.FundsRecordResponseDTO;
import cn.zl.account.book.controller.response.NormalResponse;
import cn.zl.account.book.controller.response.PageBaseResponse;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("funds_record")
public class FundsRecordController {

    @Resource
    private FundsRecordAppService fundsRecordAppService;


    @GetMapping()
    public PageBaseResponse<FundsRecordResponseDTO> paginationFundsRecord(PaginationFundsRecordRequestDTO paginationReq) {
        Page<FundsRecordInfo> fundsRecords = fundsRecordAppService.paginationFundsRecord(paginationReq);

        List<FundsRecordResponseDTO> content = fundsRecords.get()
                .map(FundsRecordConverter::info2Resp)
                .collect(Collectors.toList());

        return PageBaseResponse.wrapSuccessPageResponse(paginationReq.getPageSize(), paginationReq.getPageNumber(),
                fundsRecords.getTotalElements(), content);
    }

    @PostMapping()
    public NormalResponse<Boolean> recordFunds(FundsRecordRequestDTO fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.req2Info(fundsRecordReq);
        fundsRecordAppService.recordFunds(fundsRecordInfo);
        return NormalResponse.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PutMapping()
    public NormalResponse<Boolean> modifyFundsRecord(FundsRecordRequestDTO fundsRecordReq) {
        FundsRecordInfo fundsRecordInfo = FundsRecordConverter.req2Info(fundsRecordReq);
        fundsRecordAppService.modifyFundsRecord(fundsRecordInfo);

        return NormalResponse.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @DeleteMapping("{recordId}")
    public NormalResponse<Boolean> delFundsRecord(@PathVariable Long recordId) {
        fundsRecordAppService.delFundsRecord(recordId);
        return NormalResponse.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PostMapping("import")
    public NormalResponse<Boolean> importFundsRecord(@RequestParam("file")MultipartFile excelFile) throws IOException {
        fundsRecordAppService.importFundsRecord(excelFile);
        return NormalResponse.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }


}
