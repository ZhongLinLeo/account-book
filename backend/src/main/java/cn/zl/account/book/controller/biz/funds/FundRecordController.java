package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.controller.converter.FundsRecordConverter;
import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.controller.request.PaginationFundsRecordRequestDTO;
import cn.zl.account.book.controller.response.FundsRecordResponseDTO;
import cn.zl.account.book.controller.response.PageResponseDTO;
import cn.zl.account.book.controller.response.ResponseDTO;
import cn.zl.account.book.infrastructure.biz.funds.FundsRecordRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("fund_record")
public class FundRecordController {

    @Resource
    private FundsRecordRepository fundsRecordRepository;


    @GetMapping()
    public PageResponseDTO<FundsRecordResponseDTO> paginationFundsRecords(PaginationFundsRecordRequestDTO paginationReq) {
        PageRequest pageRequest = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize());

        Page<FundsRecordEntity> fundsRecords = fundsRecordRepository.findAll(pageRequest);

        List<FundsRecordResponseDTO> content = fundsRecords.get()
                .map(FundsRecordConverter::fundsRecordEntity2FundsRecord)
                .collect(Collectors.toList());

        return PageResponseDTO.wrapPageResponse(ResponseStatusEnum.SUCCESS, fundsRecords,content);
    }

    @PostMapping()
    public ResponseDTO<Boolean> record() {


        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PutMapping()
    public ResponseDTO<Boolean> modifyFundRecord() {


        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @DeleteMapping("{recordId}")
    public ResponseDTO<Boolean> delFundRecord(@PathVariable Long recordId) {


        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }

    @PostMapping("import")
    public ResponseDTO<Boolean> importFundRecords() {

        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS, Boolean.TRUE);
    }


}
