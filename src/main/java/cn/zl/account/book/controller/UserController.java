package cn.zl.account.book.controller;

import cn.zl.account.book.info.UserInfo;
import cn.zl.account.book.application.UserAppService;
import cn.zl.account.book.converter.UserConverter;
import cn.zl.account.book.view.request.UserRequest;
import cn.zl.account.book.view.response.NormalResponse;
import cn.zl.account.book.view.response.UserResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lin.zl
 */
@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Resource
    private UserAppService userAppService;

    @GetMapping("list")
    public NormalResponse<List<UserResponse>> listUser() {
        List<UserInfo> userInfos = userAppService.listUser();
        List<UserResponse> userRespList = userInfos.stream().map(UserConverter::info2Resp)
                .collect(Collectors.toList());
        return NormalResponse.wrapSuccessResponse(userRespList);
    }

    @PostMapping
    public NormalResponse<Boolean> createUser(UserRequest userReq) {
        UserInfo userInfo = UserConverter.req2Info(userReq);

        userAppService.createUser(userInfo);

        return NormalResponse.wrapSuccessResponse(Boolean.TRUE);
    }

    @PutMapping
    public NormalResponse<Boolean> modifyUser(UserRequest userReq) {
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
