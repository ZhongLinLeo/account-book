package cn.zl.account.book.controller.biz.user;

import cn.zl.account.book.application.info.UserInfo;
import cn.zl.account.book.controller.application.UserAppService;
import cn.zl.account.book.controller.converter.UserConverter;
import cn.zl.account.book.controller.request.UserRequestDTO;
import cn.zl.account.book.controller.response.NormalResponse;
import cn.zl.account.book.controller.response.UserResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserAppService userAppService;

    @GetMapping("list")
    public NormalResponse<List<UserResponseDTO>> listUser() {
        List<UserInfo> userInfos = userAppService.listUser();
        List<UserResponseDTO> userRespList = userInfos.stream().map(UserConverter::info2Resp)
                .collect(Collectors.toList());
        return NormalResponse.wrapSuccessResponse(userRespList);
    }

    @PostMapping
    public NormalResponse<Boolean> createUser(UserRequestDTO userReq) {
        UserInfo userInfo = UserConverter.req2Info(userReq);

        userAppService.createUser(userInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @PutMapping
    public NormalResponse<Boolean> modifyUser(UserRequestDTO userReq) {
        UserInfo userInfo = UserConverter.req2Info(userReq);

        userAppService.modifyUser(userInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @DeleteMapping("{userId}")
    public NormalResponse<Boolean> delUser(@PathVariable Long userId) {
        userAppService.delUser(userId);
        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }
}
