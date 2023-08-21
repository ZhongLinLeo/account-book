package cn.zl.account.book.controller.biz.funds;

import cn.zl.account.book.controller.enums.ResponseStatusEnum;
import cn.zl.account.book.controller.response.FundsRecordResponseDTO;
import cn.zl.account.book.controller.response.PageResponseDTO;
import cn.zl.account.book.controller.response.ResponseDTO;
import org.springframework.web.bind.annotation.*;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("fund_record")
public class FundRecordController {



    @GetMapping()
    public PageResponseDTO<FundsRecordResponseDTO> paginationFundsRecords(){





        return PageResponseDTO.wrapPageResponse(ResponseStatusEnum.SUCCESS,null);
    }

    @PostMapping()
    public ResponseDTO<Boolean> record(){


        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS,Boolean.TRUE);
    }

    @PutMapping()
    public ResponseDTO<Boolean> modifyFundRecord(){



        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS,Boolean.TRUE);
    }

    @DeleteMapping("{recordId}")
    public ResponseDTO<Boolean> delFundRecord(@PathVariable Long recordId){



        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS,Boolean.TRUE);
    }

    @PostMapping("import")
    public ResponseDTO<Boolean> importFundRecords(){

        return ResponseDTO.wrapResponse(ResponseStatusEnum.SUCCESS,Boolean.TRUE);
    }


}
