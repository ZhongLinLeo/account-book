package cn.zl.account.book.domain.impl;

import cn.zl.account.book.constant.FundsRecordConstants;
import cn.zl.account.book.domain.FundsRecordDomainService;
import cn.zl.account.book.info.FundsRecordInfo;
import cn.zl.account.book.domain.converter.FundsRecordEntityConverter;
import cn.zl.account.book.infrastructure.repository.FundsRecordClassifyRepository;
import cn.zl.account.book.infrastructure.repository.FundsRecordRepository;
import cn.zl.account.book.infrastructure.repository.UserRepository;
import cn.zl.account.book.infrastructure.entity.FundsRecordClassifyEntity;
import cn.zl.account.book.infrastructure.entity.FundsRecordEntity;
import cn.zl.account.book.infrastructure.entity.UserEntity;
import cn.zl.account.book.util.BeanCopyUtils;
import cn.zl.account.book.util.SnowIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.zl.account.book.constant.FundsRecordConstants.*;

/**
 * @author lin.zl
 */
@Service
@Slf4j
public class FundsRecordDomainServiceImpl implements FundsRecordDomainService {

    @Resource
    private FundsRecordRepository fundsRecordRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private FundsRecordClassifyRepository fundsRecordClassifyRepository;

    @Override
    public void recordFunds(FundsRecordInfo fundsRecordInfo) {
        FundsRecordEntity entity = FundsRecordEntityConverter.info2Entity(fundsRecordInfo);

        // generate account id
        final long fundsRecordId = SnowIdUtils.nextId();
        entity.setFundsRecordId(fundsRecordId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setModifyTime(LocalDateTime.now());

        fundsRecordRepository.save(entity);
    }

    @Override
    public void modifyFundRecord(FundsRecordEntity classifyEntity, FundsRecordInfo fundsRecordInfo) {
        BeanUtils.copyProperties(fundsRecordInfo,classifyEntity, BeanCopyUtils.getNullPropertyNames(fundsRecordInfo));
        classifyEntity.setModifyTime(LocalDateTime.now());

        fundsRecordRepository.save(classifyEntity);
    }

    @Override
    public void delFundRecord(Long recordId) {
        fundsRecordRepository.deleteLogical(recordId);
    }

    @Override
    public void importFundsRecord(MultipartFile excelFile) {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(excelFile.getInputStream());
        } catch (IOException e) {
            log.error("{}", e.getMessage());
            return;
        }

        List<FundsRecordEntity> entities = parseExcel(workbook);

        fundsRecordRepository.saveAll(entities);
    }

    private List<FundsRecordEntity> parseExcel(XSSFWorkbook workbook) {
        XSSFSheet worksheet = workbook.getSheetAt(0);

        initExcelField(worksheet);

        Map<String, Long> classifyMap = findClassifyMap();
        Map<String, Long> userMap = findUserMap();

        List<FundsRecordEntity> entities = new ArrayList<>();
        int number = worksheet.getPhysicalNumberOfRows();
        for (int index = 1; index < number; index++) {
            FundsRecordEntity entity = new FundsRecordEntity();

            XSSFRow row = worksheet.getRow(index);

            Integer balance = FundsRecordConstants.getValue(BALANCE_FIELD);
            entity.setFundsRecordBalance(Long.parseLong(row.getCell(balance).getStringCellValue()));

            Integer time = FundsRecordConstants.getValue(RECORD_TIME_FIELD);
            entity.setFundsRecordTime(row.getCell(time).getLocalDateTimeCellValue());

            Integer describe = FundsRecordConstants.getValue(DESCRIBE_FIELD);
            entity.setFundsRecordDescribe(row.getCell(describe).getStringCellValue());

            Integer remark = FundsRecordConstants.getValue(REMARK_FIELD);
            entity.setFundsRecordRemark(row.getCell(remark).getStringCellValue());

            Integer classifyNameIndex = FundsRecordConstants.getValue(CLASSIFY_NAME_FIELD);
            String classifyName = row.getCell(classifyNameIndex).getStringCellValue();
            Long classifyId = classifyMap.get(classifyName);
            entity.setFundsRecordClassifyId(classifyId);

            Integer userIndex = FundsRecordConstants.getValue(USER_NAME_FIELD);
            Long userId = userMap.get(row.getCell(userIndex).getStringCellValue());
            entity.setFundsUserId(userId);

            entities.add(entity);
        }
        return entities;
    }

    private Map<String, Long> findUserMap() {
        return userRepository.findAll()
                .stream()
                .collect(Collectors.toMap(UserEntity::getUserName, UserEntity::getUserId, (o, n) -> n));
    }

    private Map<String, Long> findClassifyMap() {
        return fundsRecordClassifyRepository.findAll()
                .stream()
                .collect(Collectors.toMap(FundsRecordClassifyEntity::getClassifyName,
                        FundsRecordClassifyEntity::getClassifyId, (o, n) -> n));

    }

    private void initExcelField(XSSFSheet worksheet) {
        XSSFRow fieldRow = worksheet.getRow(0);
        int cellNum = fieldRow.getPhysicalNumberOfCells();
        for (int i = 0; i < cellNum; i++) {
            XSSFCell cell = fieldRow.getCell(i);
            String cellName = cell.getStringCellValue();
            FundsRecordConstants.putValue(cellName, i);
        }
    }


}
